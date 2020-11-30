package com.eric.groupsheet

import android.animation.ObjectAnimator
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.eric.groupsheet.MVVM.Navigator
import com.eric.groupsheet.MVVM.toLifePage
import com.eric.groupsheet.MVVM.toLoginPage
import com.eric.groupsheet.MainHome.SharedAccountViewModel
import com.eric.groupsheet.Widget.GSVerse.*
import com.eric.groupsheet.Widget.GSVerse.updateAppWidget_regular
import com.eric.groupsheet.Widget.SheetOV.SheetOverViewWidget
import com.eric.groupsheet.Widget.SheetOV.updateAppWidget
import com.eric.groupsheet.base.BaseFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.concurrent.schedule


class MainActivity : AppCompatActivity() {
    lateinit var versionReference : DatabaseReference
    lateinit var mGoogleSignInClient : GoogleSignInClient
    private val RC_SIGN_IN = 7
    val myCfg = ConfigClass()
    var userName = "user"
    var userEmail = "userEmail"
    var userPhotoUrl = "userPhotoUrl"
    var userID = "userID"
    var NewsVersion = 0
    var versionInfo = ""
    private lateinit var auth: FirebaseAuth
    private val navigator by inject<Navigator>()
    private val sharedAccountInfoViewModel by viewModel<SharedAccountViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        navigator.activity=this
        auth = FirebaseAuth.getInstance()
        // Configure Google Sign In
        SignInConfigure()
        signIn()
        SaveConfig()
        btn_sign.setOnClickListener {
            if(!isNetworkAvailable(this)){
                showInternetDialog(this)
            }else{
                SignInConfigure()
                signIn()
            }
        }
        btn_life.setOnClickListener {
            navigator.toLifePage()
        }
        btn_sign_out.setOnClickListener {
            updateUI(false)
            auth.signOut()
            mGoogleSignInClient.signOut()
            tv_version.text = "無法辨識版本"
            btn_sign_out.visibility = View.GONE
            btn_sign.visibility = View.VISIBLE
            btn_life.visibility = View.VISIBLE

            //update widget status to log out
            val prefs_edit = this.getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE).edit()
            prefs_edit.putBoolean("LogInStatus", false)
            prefs_edit.apply()
            //Manual update all widgets with widgetIds
            val appWidgetManager = AppWidgetManager.getInstance(this)
            val mSheetOverViewWidget = ComponentName(
                applicationContext,
                SheetOverViewWidget::class.java
            )
            val SheetOverViewWidget_IDs = appWidgetManager.getAppWidgetIds(mSheetOverViewWidget)
            for(id in SheetOverViewWidget_IDs){
                updateAppWidget(this, appWidgetManager, id)
            }

        }
        img_Account.setOnClickListener {
            if(versionInfo.equals(versionInfo)){
                navigator.toLoginPage(
                    userName,
                    userEmail,
                    userPhotoUrl,
                    userID,
                    NewsVersion
                )
            }else UpdateDialog()
        }
        var pinfo: PackageInfo? = null
        try {
            pinfo = this.getPackageManager()?.getPackageInfo(this.getPackageName(), 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return
        }
        versionInfo = pinfo?.versionName.toString()
    }

    private fun SaveConfig() {
        val sharedPreference =  this.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val editor = sharedPreference?.edit()
        editor?.putString("Verse",myCfg.mVerse)
        editor?.putString("VerseUrl",myCfg.mVerseUrl)
        editor?.putString("VerseUrl",myCfg.mVerseUrl)
        editor?.apply()
        //manual update widgets
        val appWidgetManager = AppWidgetManager.getInstance(this)

        val mGSVerseWidget = ComponentName(
            applicationContext,
            GSVerseWidget::class.java
        )

        val GSVerseWidget_IDs = appWidgetManager.getAppWidgetIds(mGSVerseWidget)
        for(GSclockId in GSVerseWidget_IDs){
            updateAppWidget_regular(this, appWidgetManager, GSclockId)
        }
    }

    private fun showInternetDialog(context: Context) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("請連接網路")
            builder.setPositiveButton("確定", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            }).setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->
                finish()
                dialog.dismiss()
            })
            builder.show()

    }

    private fun isNetworkAvailable(context: Context?): Boolean {
            if (context == null) return false
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            return true
                        }
                    }
                }
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    return true
                }
            }
            return false

    }

    fun AccessFirebase(){
        versionReference = FirebaseDatabase.getInstance().getReference("config")
        val versionListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val dsData = dataSnapshot.getValue()
                myCfg.mVersion = dataSnapshot.children.find {
                    it.key == "version"
                }?.value.toString()
                myCfg.mVerse = dataSnapshot.children.find {
                    it.key == "verse"
                }?.value.toString()
                myCfg.mVerseUrl = dataSnapshot.children.find {
                    it.key == "verseUrl"
                }?.value.toString()
                myCfg.mUrl = dataSnapshot.children.find {
                    it.key == "url"
                }?.value.toString()
                myCfg.mUpdateVersion = dataSnapshot.children.find {
                    it.key == "updateVersion"
                }?.value.toString().toInt()//long can not change to int so...
                SaveConfig()
                getNewsVersion()
                tv_version.text = "現行版本 : ${myCfg.mVersion}"
                Log.d("TAG", "${myCfg.mVersion}  $versionInfo")


                if(myCfg.mVersion.equals(versionInfo)){
                    Toast.makeText(this@MainActivity, "現為最新版本", Toast.LENGTH_SHORT).show()
                }
                else UpdateDialog()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        versionReference.addValueEventListener(versionListener)

    }

    private fun getNewsVersion() {
        NewsVersion = myCfg.mUpdateVersion
    }

    private fun UpdateDialog() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("新版本上架囉!請至APP商店更新。")
        builder.setPositiveButton("確定", DialogInterface.OnClickListener { dialog, which ->
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(myCfg.mUrl)
            startActivity(openURL)
            dialog.dismiss()
        }).setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })
        builder.show()
    }

    private fun SignInConfigure() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }
    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
//                Toast.makeText(this,"Google sign in good : ${account.id}",Toast.LENGTH_LONG).show()
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
//               Toast.makeText(this,"Google sign in failed : $e",Toast.LENGTH_LONG).show()
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Log.d("TAG", user?.getDisplayName()+ user?.getEmail()+user?.uid+"  "+user?.photoUrl)
                    userName = user?.getDisplayName().toString()
                    userEmail = user?.getEmail().toString()
                    userPhotoUrl = user?.photoUrl.toString()
                    userID = user?.uid.toString()

                    updateUI(true)
                    AccessFirebase()
                    if(versionInfo.equals(versionInfo)){
                        Timer().schedule(1600) {
                            navigator.toLoginPage(
                                userName,
                                userEmail,
                                userPhotoUrl,
                                userID,
                                NewsVersion
                            )
                        }
                    }

                } else {
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    tv_version.text = task.exception.toString()
                }
            }
    }

    private fun updateUI(isSignIn:Boolean) {
        btn_sign.visibility =if(!isSignIn)View.VISIBLE else View.GONE
        btn_life.visibility =if(!isSignIn)View.VISIBLE else View.GONE
        tv_welcome.visibility = if(isSignIn)View.VISIBLE else View.GONE
        img_Account.visibility = if(isSignIn)View.VISIBLE else View.GONE
        tv_welcome.text = "歡迎 $userName \n\n 您正在進入 總務小幫手APP"
        btn_sign_out.visibility = View.VISIBLE
        Picasso.get()
            .load(userPhotoUrl)
            .placeholder(R.drawable.common_google_signin_btn_icon_dark)
            .fit()
            .into(img_Account)
        val anim = ObjectAnimator.ofFloat(tv_welcome, "alpha",0f,1f)
        anim.setDuration(2000)
//        anim.repeatCount = Animation.INFINITE
        anim.start()
        val animLogout = ObjectAnimator.ofFloat(btn_sign_out, "alpha",0f,1f)
        animLogout.setDuration(4000)
//        anim.repeatCount = Animation.INFINITE
        animLogout.start()
    }

    override fun onBackPressed() {
        val fragmentList: List<*> = supportFragmentManager.fragments
//        if(fragmentList.isEmpty()){
//            val builder = AlertDialog.Builder(this)
//            builder.setTitle("確定要退出嗎?")
//            builder.setPositiveButton("確定", DialogInterface.OnClickListener { dialog, which ->
//                dialog.dismiss()
//                super.onBackPressed()
//
//            }).setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->
//            dialog.dismiss()
//            })
//            builder.show()
//        }
        var handled = false
        for (f in fragmentList) {
            if (f is BaseFragment) {
                handled = f.onBackPressed()
                if (handled) {
                    break
                }
            }
        }

        if (!handled) {
            super.onBackPressed()
        }
    }
    fun removeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().remove(fragment).commit()
        //commitNow will error due to sync issue
    }

}

