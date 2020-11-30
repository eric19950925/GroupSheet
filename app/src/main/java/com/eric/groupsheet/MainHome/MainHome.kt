package com.eric.groupsheet.MainHome

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.*
import com.eric.groupsheet.NameList.NameListViewModel
import com.eric.groupsheet.R
import com.eric.groupsheet.base.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_mainhome.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.*
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainHome() : BaseFragment(),SheetListController {
    private val viewModel by viewModel<MainHomeViewModel>()
    private val nameListviewModel by sharedViewModel<NameListViewModel>()
    lateinit var DVReference : DatabaseReference
    private val accountViewModel by sharedViewModel<SharedAccountViewModel>()
    lateinit var mSheet : SheetClass
    var mVerseUrl = ""
    lateinit var BibleVerseReference : DatabaseReference
    lateinit var VerseUrlReference : DatabaseReference
    val funTest = "002"
    override fun getLayoutRes(): Int =
        R.layout.fragment_mainhome

    override fun initData() {
        viewModel.sheetPage.value = 0
        }

    override fun initObserver() {
        observe(nameListviewModel.NameList){
            //create empty sheet data for new sheet
            viewModel.emptySheet(nameListviewModel.NameList,accountViewModel.userAccount.value?.userID.toString())
        }
        observe(viewModel.SheetList){
            val sheetListAdapter = SheetListAdapter(it,this)
            rv_sheetList.adapter = sheetListAdapter
            when(it.size){
                0 -> {
                    ll_pager.visibility = View.INVISIBLE
                    tv_empty.visibility = View.VISIBLE
                }
                1 -> {
                    ll_pager.visibility = View.VISIBLE
                    p1.visibility = View.VISIBLE
                    p2.visibility = View.GONE
                    p3.visibility = View.GONE
                    p4.visibility = View.GONE
                    p5.visibility = View.GONE
                    tv_empty.visibility = View.GONE
                }
                2 -> {
                    p1.visibility = View.VISIBLE
                    p2.visibility = View.VISIBLE
                    p3.visibility = View.GONE
                    p4.visibility = View.GONE
                    p5.visibility = View.GONE
                    tv_empty.visibility = View.GONE
                }
                3 -> {
                    p1.visibility = View.VISIBLE
                    p2.visibility = View.VISIBLE
                    p3.visibility = View.VISIBLE
                    p4.visibility = View.GONE
                    p5.visibility = View.GONE
                    tv_empty.visibility = View.GONE
                }
                4 -> {
                    p1.visibility = View.VISIBLE
                    p2.visibility = View.VISIBLE
                    p3.visibility = View.VISIBLE
                    p4.visibility = View.VISIBLE
                    p5.visibility = View.GONE
                    tv_empty.visibility = View.GONE
                }
                5 -> {
                    p1.visibility = View.VISIBLE
                    p2.visibility = View.VISIBLE
                    p3.visibility = View.VISIBLE
                    p4.visibility = View.VISIBLE
                    p5.visibility = View.VISIBLE
                    tv_empty.visibility = View.GONE
                }
            }
        }
        observe(viewModel.Answer){
            if(it == 1){
                buy.visibility = View.GONE
            }
        }
        observe(viewModel.Tutorial_addSheet){
            if(it == 0 && nameListviewModel.NameList.value?.size != 0 && viewModel.Tutorial_toNameList.value != 0){
                cl_tutorial_Addsheet.visibility = View.VISIBLE
            }else cl_tutorial_Addsheet.visibility = View.GONE
        }
        observe(viewModel.Tutorial_toNameList){
            if(it == 0){
                cl_tutorial_welcome.visibility = View.VISIBLE
            }else cl_tutorial_welcome.visibility = View.GONE
        }
        observe(viewModel.sheetPage){
            Log.d("TAG Position", "" + it)
            setFade(rv_sheetList,it)
            p1.background = resources.getDrawable(R.drawable.btn_style_feature3)
            p2.background = resources.getDrawable(R.drawable.btn_style_feature3)
            p3.background = resources.getDrawable(R.drawable.btn_style_feature3)
            p4.background = resources.getDrawable(R.drawable.btn_style_feature3)
            p5.background = resources.getDrawable(R.drawable.btn_style_feature3)
            rv_sheetList.scrollToPosition(it)
            when(it){
                0 -> {
                    p1.background = resources.getDrawable(R.drawable.btn_style_feature1)
                }
                1 -> {
                    p2.background = resources.getDrawable(R.drawable.btn_style_feature1)
                }
                2 -> {
                    p3.background = resources.getDrawable(R.drawable.btn_style_feature1)
                }
                3 -> {
                    p4.background = resources.getDrawable(R.drawable.btn_style_feature1)
                }
                4 -> {
                    p5.background = resources.getDrawable(R.drawable.btn_style_feature1)
                }
            }
        }
    }

    override fun onFragmentShow() {
        super.onFragmentShow()
        checkInternet({})
        checkNews()
        checkAnswer()
        MobileAds.initialize(context) {}
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

    }

    private fun checkAnswer() {
        val sharedPreference =  context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
//        val editor = sharedPreference?.edit()
//        editor?.putInt("AnswerSheet",0)
//        editor?.apply()
        viewModel.Answer.value = sharedPreference?.getInt("AnswerSheet",0)
        viewModel.Tutorial_toNameList.value = sharedPreference?.getInt("Tutorial_toNameList",0)
        viewModel.Tutorial_addSheet.value = sharedPreference?.getInt("Tutorial_addSheet",0)
    }

    private fun checkInternet(toGo:()->Unit) {
        if(!isNetworkAvailable(context)){
            context?.let { showInternetDialog(it) }
        }else{
            cl_check_internet.visibility = View.GONE
            toGo.invoke()
        }
    }

    private fun showInternetDialog(context: Context) {
        cl_check_internet.visibility = View.VISIBLE
        val builder = AlertDialog.Builder(context)
            builder.setTitle("請連接網路")
            builder.setPositiveButton("確定", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            }).setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->
                activity?.finish()
                dialog.dismiss()
            })
            builder.show()
    }

    @SuppressLint("ServiceCast")
    fun isNetworkAvailable(context: Context?): Boolean {
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        viewModel.UserId.value = accountViewModel.userAccount.value?.userID.toString()
        nameListviewModel.reloadNameList(accountViewModel.userAccount.value?.userID.toString())

        settingRecyclerView()
        icon_menuBar.listenClick {
            ll_menu.show()
            val anim = ObjectAnimator.ofFloat(ll_menu, "alpha",0f,1f)
            anim.setDuration(200)
            anim.start()
        }
        icon_close.listenClick {
            val anim = ObjectAnimator.ofFloat(ll_menu, "alpha",1f,0f)
            anim.setDuration(200)
            anim.doOnEnd {
                ll_menu.hide()
            }
            anim.start()
        }
        tv_nameList.listenClick { checkInternet({viewModel.toNameList()} )}
//        tv_setting.listenClick { viewModel.toSetting() }
        icon_info.listenClick {  checkInternet({viewModel.toAbout()} ) }
        tv_newList.listenClick {
            val anim = ObjectAnimator.ofFloat(ll_menu, "alpha",1f,0f)
            anim.setDuration(200)
            anim.doOnEnd {
                ll_menu.hide()
            }
            anim.start()
            checkInternet({createNewList()} )
        }
        tv_life.listenClick { viewModel.toLife() }
        img_more.listenClick {

            val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
            builder?.setTitle("要看更多文章介紹嗎?")
            builder?.setPositiveButton("確定", DialogInterface.OnClickListener { dialog, which ->
                val openUrl = Intent(Intent.ACTION_VIEW)
                openUrl.data = Uri.parse(mVerseUrl)
                startActivity(openUrl)
                dialog.dismiss()
            })?.setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            builder?.show()

        }
        viewModel.reloadSheetList(accountViewModel.userAccount.value?.userID.toString())

        tv_account.listenClick {
            checkInternet( { viewModel.toAccount() } )
        }
        buy.listenClick {
            when(funTest){
                "001"->checkToDownload()//未來下載檔案的方法預寫
                "002"->takeAns()//資料統計尚未實作
            }
        }
        next_step.listenClick {

            cl_tutorial_nameList.visibility = View.VISIBLE
            cl_tutorial_welcome.visibility = View.GONE
        }
        got_it.listenClick {
            ll_menu_for_t1.visibility = View.VISIBLE
            cl_tutorial_nameList.visibility = View.GONE
        }
        tv_nameList_for_t1s.listenClick {
            val sharedPreference =  context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            val editor = sharedPreference?.edit()
            editor?.putInt("Tutorial_toNameList",1)
            editor?.apply()
            checkAnswer()
            viewModel.Tutorial_toNameList.value = 1
            ll_menu_for_t1.visibility = View.GONE
            //to nameList page
            checkInternet({viewModel.toNameList()} )
        }
        got_it_addSheet.listenClick {
            val sharedPreference =  context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            val editor = sharedPreference?.edit()
            editor?.putInt("Tutorial_addSheet",1)
            editor?.apply()
            checkAnswer()
            viewModel.Tutorial_addSheet.value = 1
        }


        BibleVerseReference = FirebaseDatabase.getInstance().getReference("LifeData").child("verse")
        val bibleVerseListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                textView19.text = dataSnapshot.getValue().toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        BibleVerseReference.addValueEventListener(bibleVerseListener)
        VerseUrlReference = FirebaseDatabase.getInstance().getReference("LifeData").child("verseUrl")
        val verseUrlListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mVerseUrl = dataSnapshot.getValue().toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        VerseUrlReference.addValueEventListener(verseUrlListener)
    }

    private fun settingRecyclerView() {
        rv_sheetList.setHasFixedSize(true)

        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rv_sheetList.layoutManager = mLayoutManager


        val sheetListAdapter = viewModel.SheetList.value?.let { SheetListAdapter(it, this) }
        rv_sheetList.adapter = sheetListAdapter

        val marginDecoration = DefaultDecoration()
        rv_sheetList.addItemDecoration(marginDecoration)

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rv_sheetList)
        rv_sheetList.isNestedScrollingEnabled = false

        rv_sheetList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                recyclerView.let { super.onScrollStateChanged(it, newState) }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val centerView = snapHelper.findSnapView(mLayoutManager)
                    val pos = mLayoutManager.getPosition(centerView!!)
//                    Log.d("TAG Position", "" + pos)
//                    setFade(rv_sheetList?:return,pos)
                    viewModel.sheetPage.value = pos
                }
            }
        })


    }

    private fun setFade(recyclerView: RecyclerView, position: Int) {
        // 中間頁面
        val mCurView = recyclerView.layoutManager!!.findViewByPosition(position)
        // 右邊頁面
        val mRightView = recyclerView.layoutManager!!.findViewByPosition(position + 1)
        // 左邊頁面
        val mLeftView = recyclerView.layoutManager!!.findViewByPosition(position - 1)
        // 右右邊頁面，再向右滑的時候會出現
        val mRRView = recyclerView.layoutManager!!.findViewByPosition(position + 2)

        if (mCurView != null) {
            val anim = ObjectAnimator.ofFloat(mCurView, "alpha",0.5f,1f)
            anim.setDuration(200)
            anim.start()
            mCurView.isClickable = true
        }else Log.d("TAG","!mCurView")
        if (mRightView != null) {
//            val anim = ObjectAnimator.ofFloat(mRightView, "alpha",1f,0.5f)
//            anim.setDuration(400)
//            anim.start()
            mRightView.alpha = 0.5f
            mRightView.isClickable = false
        }else Log.d("TAG","!mRightView")
        if (mLeftView != null) {
//            val anim = ObjectAnimator.ofFloat(mLeftView, "alpha",1f,0.5f)
//            anim.setDuration(400)
//            anim.start()
            mLeftView.alpha = 0.5f
            mLeftView.isClickable = false
        }else Log.d("TAG","!mLeftView")
        if (mRRView != null) {
            val anim = ObjectAnimator.ofFloat(mRRView, "alpha",0.5f,1f)
            anim.setDuration(200)
            anim.start()
            mRRView.isClickable = true
        }else Log.d("TAG","!mRRView")


    }


    private fun takeAns() {
        val MoreSheetsView : View = getLayoutInflater().inflate(R.layout.dialog_more_sheets,null)
            val builder = activity?.let { it1 -> AlertDialog.Builder(it1) }
            builder?.setView(MoreSheetsView)
            builder?.setTitle("需求調查")
            builder?.setPositiveButton("確定", DialogInterface.OnClickListener { dialog, which ->
                val rbEnough: RadioButton =MoreSheetsView.findViewById(R.id.rb_enough)
                val rb5more: RadioButton =MoreSheetsView.findViewById(R.id.rb_5more)
                val rb10more: RadioButton =MoreSheetsView.findViewById(R.id.rb_10more)
                val rbSub: RadioButton =MoreSheetsView.findViewById(R.id.rb_sub)
                val rbNotSub: RadioButton =MoreSheetsView.findViewById(R.id.rb_not_sub)
                val rbBuy: RadioButton =MoreSheetsView.findViewById(R.id.rb_buy)
                val rbNotBuy: RadioButton =MoreSheetsView.findViewById(R.id.rb_not_buy)
                val rbAd: RadioButton =MoreSheetsView.findViewById(R.id.rb_ad)
                val rbNotAd: RadioButton =MoreSheetsView.findViewById(R.id.rb_not_ad)
                var SheetAmount = "None"
                var SheetSub = "None"
                var SheetBuy = "None"
                var SheetAd = "None"
                rbEnough.let{
                    if(it.isChecked)SheetAmount = "0"
                }
                rb5more.let{
                    if(it.isChecked)SheetAmount = "5"
                }
                rb10more.let{
                    if(it.isChecked)SheetAmount = "10"
                }
                rbSub.let {
                    if(it.isChecked)SheetSub = "Sub"
                }
                rbNotSub.let{
                    if(it.isChecked)SheetSub = "NotSub"
                }
                rbBuy.let {
                    if(it.isChecked)SheetBuy = "Buy"
                }
                rbNotBuy.let{
                    if(it.isChecked)SheetBuy = "NotBuy"
                }
                rbAd.let {
                    if(it.isChecked)SheetAd = "Ad"
                }
                rbNotAd.let{
                    if(it.isChecked)SheetAd = "NotAd"
                }

//                val currentDateTime= LocalDateTime.now()

//                mMember = MemberClass(
//                    id = currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
//                    who = etMemberName.text.toString(),
//                    type = ToTypeNum( userSex , userAge )
//                )
//
//                viewModel.addMember(mMember,accountViewModel.userAccount.value?.userID.toString())
//                viewModel.reloadNameList(accountViewModel.userAccount.value?.userID.toString())
                if(SheetAmount .equals("None")|| SheetSub .equals("None")||
                    SheetBuy .equals("None")|| SheetAd .equals("None")){
                    Toast.makeText(context,"遞交失敗，每題都需選擇!",Toast.LENGTH_LONG).show()
                }else{
                    viewModel.Answer.value = 1
                    val sharedPreference =  context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                    val editor = sharedPreference?.edit()
                    editor?.putInt("AnswerSheet",viewModel.Answer.value?:0)
                    editor?.apply()
                    checkAnswer()
                    Toast.makeText(context,"遞交成功!",Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                }

            })?.setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            builder?.show()
    }

    private fun checkToDownload() {
        val permission_write = activity?.let { it1 ->
            ContextCompat.checkSelfPermission(
                it1,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permission_write != PackageManager.PERMISSION_GRANTED) {
            setupPermissions()
            return
        } else downloadMp4()
    }

    private fun downloadMp4() {

        class ProgressTask: AsyncTask<Void, Void, String>() {
            val progressDialog :Dialog = ProgressDialog(context)
            private val TIMEOUT_CONNECTION = 5000 //5sec
            private val TIMEOUT_SOCKET = 30000 //30sec

            override fun onPreExecute() {
                progressDialog.setTitle("Downloading...")
                progressDialog.show()
            }


            override fun onPostExecute(result: String?) {
                progressDialog.dismiss()
            }

            override fun doInBackground(vararg p0: Void?): String {
                try {
                    val url = URL("http://vjs.zencdn.net/v/oceans.mp4")
                    val startTime = System.currentTimeMillis()
                    Log.i("TAG", "video download beginning: $url")

                    //Open a connection to that URL.
                    val ucon: URLConnection = url.openConnection()

                    //this timeout affects how long it takes for the app to realize there's a connection problem
                    ucon.setReadTimeout(TIMEOUT_CONNECTION)
                    ucon.setConnectTimeout(TIMEOUT_SOCKET)


                    //Define InputStreams to read from the URLConnection.
                    // uses 3KB download buffer
                    val `is`: InputStream = ucon.getInputStream()
                    val inStream = BufferedInputStream(`is`, 1024 * 5)
                    val outStream = FileOutputStream(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            .toString().toString() + "/new.mp4"
                    )
                    val buff = ByteArray(5 * 1024)

                    //Read bytes (and store them) until there is nothing more to read(-1)
                    var len: Int = 0
                    while (inStream.read(buff).also({ len = it }) != -1) {
                        outStream.write(buff, 0, len)
                    }
                    //clean up
                    outStream.flush()
                    outStream.close()
                    inStream.close()

                    Log.i(
                        "TAG", "download completed in "
                                + (System.currentTimeMillis() - startTime) / 1000
                                + " sec"
                    )
                }catch (e: MalformedURLException){
                    Log.e("TAG", "MalformedURLException", e)
                }catch (e: FileNotFoundException) {
                    Log.e("TAG", "FileNotFoundException", e)
                } catch (e: IOException) {
                    Log.e("TAG", "IOException", e)
                }
                return "Executed"
            }
        }

        val progressTask = ProgressTask()
        progressTask.execute()
    }

    private fun checkNews() {
        val sharedPreference =  context?.getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
        DVReference = FirebaseDatabase.getInstance().getReference("LifeData").child("updateVersion")
        val DVListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val version = dataSnapshot.getValue().toString()
                Log.d("TAG",version+" "+sharedPreference?.getInt("NewsVersion",0)+" "+accountViewModel.userAccount.value?.newsVersion)
                if(version.equals(sharedPreference?.getInt("NewsVersion",0).toString())){
                    img_news.visibility = View.GONE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        DVReference.addValueEventListener(DVListener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNewList(){
        if(nameListviewModel.NameList.value?.size == 0){
            val builder2 = context?.let { it1 -> AlertDialog.Builder(it1) }
            builder2?.setTitle("建立名單後才能新增表單")
            builder2?.setPositiveButton("知道了", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            builder2?.show()
            return
        }
        if(viewModel.SheetList.value?.size!! > 4){
            val builder2 = context?.let { it1 -> AlertDialog.Builder(it1) }
            builder2?.setTitle("最多只能建立 5 份表單")
            builder2?.setPositiveButton("知道了", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            builder2?.show()
            return
        }

//            val items_detail = 0
        val items_detailA = arrayListOf("空白選項","0","空白選項","0","空白選項","0","空白選項","0","空白選項","0","條件A")
        val items_detailB = arrayListOf("空白選項","0","空白選項","0","空白選項","0","空白選項","0","空白選項","0","條件B")
        val items_detailC = arrayListOf("空白選項","0","空白選項","0","空白選項","0","空白選項","0","空白選項","0","條件C")
        val items_detailD = arrayListOf("空白選項","0","空白選項","0","空白選項","0","空白選項","0","空白選項","0","條件D")
        val items_detailE = arrayListOf("空白選項","0","空白選項","0","空白選項","0","空白選項","0","空白選項","0","條件E")

        val items = HashMap<String, ArrayList<String>>()
        items.put("條件A", items_detailA)
        items.put("條件B", items_detailB)
        items.put("條件C", items_detailC)
        items.put("條件D", items_detailD)
        items.put("條件E", items_detailE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentDateTime= LocalDateTime.now()
            mSheet = SheetClass(
                id = currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
                listName = "未命名表單",
                rule = items,
                sheetData = viewModel.mEmptySheet.value!!,
                editDate = "empty",
                history = "" )
        }else{
            val currentDateTimeOV= Calendar.getInstance().time
            val df = SimpleDateFormat("yyyyMMddHHmmss")
            mSheet = SheetClass(
                id = df.format(currentDateTimeOV),
                listName = "未命名表單",
                rule = items,
                sheetData = viewModel.mEmptySheet.value!!,
                editDate = "empty",
                history = "" )
        }

        viewModel.addList(mSheet,accountViewModel.userAccount.value?.userID.toString())

    }

    companion object {
        fun newInstance() = MainHome()
    }

    override fun updateRvAfterDel(id: String) {

    }

    override fun editMember(id: String) {
        val EditSheetView : View = getLayoutInflater().inflate(R.layout.dialog_edit_sheet,null)
        val img_delete: ImageView = EditSheetView.findViewById(R.id.imgDel)
        val img_edit: ImageView = EditSheetView.findViewById(R.id.imgEdit)
        val builder = activity?.let { it1 -> AlertDialog.Builder(it1) }
        builder?.setView(EditSheetView)
        val ad = builder?.show()
        img_delete.setOnClickListener {
            val builder2 = context?.let { it1 -> AlertDialog.Builder(it1) }
            builder2?.setTitle("確定要刪除嗎?")
            builder2?.setPositiveButton("確定", DialogInterface.OnClickListener { dialog, which ->
                viewModel.DeletMember(id,accountViewModel.userAccount.value?.userID.toString())
                dialog.dismiss()
                ad?.dismiss()
            })?.setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            builder2?.show()
        }
        img_edit.setOnClickListener {
            viewModel.toEditSheet(id)
            ad?.dismiss()
        }
    }

    override fun onItemChanged(p: Int) {
    }

    interface OnSnapPositionChangeListener {

        fun onSnapPositionChange(position: Int)
    }
}