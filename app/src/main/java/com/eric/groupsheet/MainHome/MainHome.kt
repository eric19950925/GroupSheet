package com.eric.groupsheet.MainHome

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.eric.groupsheet.NameList.NameListViewModel
import com.eric.groupsheet.R
import com.eric.groupsheet.base.BaseFragment
import com.eric.groupsheet.base.observe
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.fragment_mainhome.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MainHome : BaseFragment(),SheetListController{
    private val viewModel by viewModel<MainHomeViewModel>()
    private val nameListviewModel by sharedViewModel<NameListViewModel>()
    private val accountViewModel by sharedViewModel<SharedAccountViewModel>()
    lateinit var mSheet : SheetClass
    override fun getLayoutRes(): Int =
        R.layout.fragment_mainhome

    override fun initData() {
        }

    override fun initObserver() {
        observe(nameListviewModel.NameList){
            //create empty sheet data for new sheet
            viewModel.emptySheet(nameListviewModel.NameList,accountViewModel.userAccount.value?.userID.toString())
        }
        observe(viewModel.SheetList){
            val sheetListAdapter = SheetListAdapter(it,this)
            rv_sheetList.adapter = sheetListAdapter
        }
    }

    override fun onFragmentShow() {
        super.onFragmentShow()
        checkInternet({})

        MobileAds.initialize(context) {}
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
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
    override fun initView() {
        viewModel.UserId.value = accountViewModel.userAccount.value?.userID.toString()
        nameListviewModel.reloadNameList(accountViewModel.userAccount.value?.userID.toString())



        tv_nameList.setOnClickListener { checkInternet({viewModel.toNameList()} )}
        tv_setting.setOnClickListener { viewModel.toSetting() }
        tv_about.setOnClickListener {  checkInternet({viewModel.toAbout()} ) }
        tv_newList.setOnClickListener {  checkInternet({createNewList()} )}
        tv_life.setOnClickListener { viewModel.toLife() }

        viewModel.reloadSheetList(accountViewModel.userAccount.value?.userID.toString())
        rv_sheetList.setHasFixedSize(true)
        rv_sheetList.layoutManager = LinearLayoutManager(context)
        val sheetListAdapter = viewModel.SheetList.value?.let { SheetListAdapter(it,this) }
        rv_sheetList.adapter = sheetListAdapter

        tv_account.setOnClickListener {
            checkInternet( { viewModel.toAccount() } )
        }

    }

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
        val currentDateTime= LocalDateTime.now()
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
        mSheet = SheetClass(
            id = currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
            listName = "未命名表單",
            rule = items,
            sheetData = viewModel.mEmptySheet.value!!,
            editDate = "empty",
            history = "" )
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
}