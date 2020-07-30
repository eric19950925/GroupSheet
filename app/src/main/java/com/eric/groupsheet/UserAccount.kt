package com.eric.groupsheet

import com.eric.groupsheet.MainHome.SharedAccountViewModel
import com.eric.groupsheet.base.BaseFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_account.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class UserAccount : BaseFragment() {

    private val accountViewModel by sharedViewModel<SharedAccountViewModel>()
    override fun getLayoutRes(): Int = R.layout.fragment_account

    override fun initData() {
    }

    override fun initObserver() {
    }

    override fun initView() {
        UserName.text = accountViewModel.userAccount.value?.userName
        UserEmail.text = accountViewModel.userAccount.value?.userEmail
        Picasso.get()
            .load(accountViewModel.userAccount.value?.userPhotoUrl)
            .placeholder(R.drawable.common_google_signin_btn_icon_dark)
            .fit()
            .into(img_Account)
        MobileAds.initialize(context) {}
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
    companion object{
        fun newInstance() = UserAccount()
    }
}