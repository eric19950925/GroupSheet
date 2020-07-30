package com.eric.groupsheet

import com.eric.groupsheet.base.BaseFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.fragment_about.*

class About:BaseFragment() {
    override fun getLayoutRes(): Int = R.layout.fragment_about
    override fun initData() {
    }

    override fun initObserver() {
    }

    override fun initView() {
        MobileAds.initialize(context) {}
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
    companion object{
        fun newInstance() = About()
    }
}