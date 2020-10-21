package com.eric.groupsheet

import android.animation.ObjectAnimator
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.eric.groupsheet.MVVM.Navigator
import com.eric.groupsheet.MVVM.toLifePage
import com.eric.groupsheet.MVVM.toLoginPage
import com.eric.groupsheet.MainHome.SharedAccountViewModel
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
    lateinit var NVReference : DatabaseReference
    lateinit var UrlReference : DatabaseReference
    lateinit var mGoogleSignInClient : GoogleSignInClient
    private val RC_SIGN_IN = 7
    var userName = "user"
    var userEmail = "userEmail"
    var userPhotoUrl = "userPhotoUrl"
    var userID = "userID"
    var NewsVersion = 0
    var versionInfo = ""
    var AppUrlInfo = ""
    private lateinit var auth: FirebaseAuth
    private val navigator by inject<Navigator>()
    private val sharedAccountInfoViewModel by viewModel<SharedAccountViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigator.activity=this
        auth = FirebaseAuth.getInstance()
        // Configure Google Sign In
        SignInConfigure()
        signIn()

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
        versionReference = FirebaseDatabase.getInstance().getReference("config").child("version")
        val versionListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val version = dataSnapshot.getValue().toString()
                tv_version.text = "現行版本 : $version"
                Log.d("TAG", "$version  $versionInfo")
                if(version.equals(versionInfo)){
                    Toast.makeText(this@MainActivity, "現為最新版本", Toast.LENGTH_SHORT).show()
                }
                else UpdateDialog()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        versionReference.addValueEventListener(versionListener)
        getNewsVersion()
    }

    private fun getNewsVersion() {
        NVReference = FirebaseDatabase.getInstance().getReference("LifeData").child("updateVersion")
        val versionListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                NewsVersion = dataSnapshot.getValue().toString().toInt()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        NVReference.addValueEventListener(versionListener)
    }

    private fun UpdateDialog() {
        UrlReference = FirebaseDatabase.getInstance().getReference("config").child("url")
        val UrlListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                AppUrlInfo = dataSnapshot.getValue().toString()
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("新版本上架囉!請至APP商店更新。")
                builder.setPositiveButton("確定", DialogInterface.OnClickListener { dialog, which ->
                    val openURL = Intent(Intent.ACTION_VIEW)
                    openURL.data = Uri.parse(AppUrlInfo)
                    startActivity(openURL)
                    dialog.dismiss()
                }).setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                })
                builder.show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        UrlReference.addValueEventListener(UrlListener)

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

