package com.eric.groupsheet

import android.os.Bundle
import android.util.Log
import com.eric.groupsheet.MainHome.SharedAccountViewModel
import com.eric.groupsheet.base.BaseFragment
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
    lateinit var mUserAccount : AccountClass
    override fun initData() {
    }
    companion object {

        fun newInstance(
            userName: String,
            userEmail: String,
            userPhotoUrl: String,
            userID: String
        ) = Login().apply {
            arguments = Bundle().apply {
                putString(USER_NAME, userName)
                putString(USER_EMAIL, userEmail)
                putString(USER_PHOTO, userPhotoUrl)
                putString(USER_ID, userID)

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
            arguments?.getString(USER_ID,"").toString()
        )
        accountViewModel.userAccount.value = mUserAccount
        Log.d("TAG",accountViewModel.userAccount.value?.userID.toString())
        Timer().schedule(1000) {
            viewModel.toMainHome()
        }
    }

}