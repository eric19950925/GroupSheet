package com.eric.groupsheet

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.eric.groupsheet.MainHome.SharedAccountViewModel
import com.eric.groupsheet.MainHome.SheetClass
import com.eric.groupsheet.Widget.SheetOV.SovWidget
import com.eric.groupsheet.base.BaseFragment
import com.eric.groupsheet.base.observe
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.concurrent.schedule

class Login :BaseFragment(){
    override fun getLayoutRes(): Int = R.layout.fragment_login
    private val accountViewModel by sharedViewModel<SharedAccountViewModel>()
    private val viewModel by viewModel<LoginViewModel>()
    private val USER_NAME = "UserName"
    private val USER_EMAIL = "UserEmail"
    private val USER_PHOTO = "UserPhotoUrl"
    private val USER_ID = "UserID"
    private val NEWS_VERSION = "NewsVersion"
    lateinit var mUserAccount : AccountClass
    override fun initData() {
    }
    companion object {

        fun newInstance(
            userName: String,
            userEmail: String,
            userPhotoUrl: String,
            userID: String,
            newsVersion: Int
        ) = Login().apply {
            arguments = Bundle().apply {
                putString(USER_NAME, userName)
                putString(USER_EMAIL, userEmail)
                putString(USER_PHOTO, userPhotoUrl)
                putString(USER_ID, userID)
                putInt(NEWS_VERSION,newsVersion)
            }

        }
    }
    override fun initObserver() {
    }

    override fun initView() {
        mUserAccount = AccountClass(
            arguments?.getString(USER_NAME,"").toString(),
            arguments?.getString(USER_EMAIL,"").toString(),
            arguments?.getString(USER_PHOTO,"").toString(),
            arguments?.getString(USER_ID,"").toString(),
            arguments?.getInt(NEWS_VERSION)?:0
        )
        accountViewModel.userAccount.value = mUserAccount
        Log.d("TAG","Log in account's id = ${accountViewModel.userAccount.value?.userID}")
        val sharedPreference =  context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val editor = sharedPreference?.edit()
        editor?.putString("userId",accountViewModel.userAccount.value?.userID.toString())
        editor?.apply()

        val prefs = context?.getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE)
        val prefs_edit = prefs?.edit()
        prefs_edit?.putBoolean("LogInStatus", true)
        prefs_edit?.apply()
        //reload SheetIdList
        getSheetData(accountViewModel.userAccount.value?.userID.toString())
        observe(viewModel.mSheetIdList){

            it.forEach{
                (SovWidget::class.java)
                    .newInstance()
                    .updateWidgetBySheetFile(context?:return@observe,it)
            }
            viewModel.mLogInStatue.value = true
        }

        Timer().schedule(1000) {
            viewModel.toMainHome()
        }
    }

    fun getSheetData(id:String){

        val SheetListRef =
            FirebaseDatabase.getInstance().getReference("userData").child(id).child("sheetList")
        SheetListRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                val NewSheetList: ArrayList<SheetClass> = ArrayList<SheetClass>()
                val SheetIdList: ArrayList<String> = ArrayList<String>()
                for (p in p0.children) {
                    val SheetListData = p.getValue(SheetClass::class.java)
                    SheetListData?.let {
                        NewSheetList.add(it)
                        SheetIdList.add(it.memberId)
                    }
                }
                viewModel.SheetList.value = NewSheetList
                viewModel.mSheetIdList.value = SheetIdList
                Log.d("TAG",viewModel.mSheetIdList.value.toString())
            }
        })
    }
}