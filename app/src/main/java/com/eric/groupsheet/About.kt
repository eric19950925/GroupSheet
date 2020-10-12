package com.eric.groupsheet

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.eric.groupsheet.MainHome.SharedAccountViewModel
import com.eric.groupsheet.base.BaseFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.fragment_about.img_Account
import org.koin.android.viewmodel.ext.android.sharedViewModel

class About:BaseFragment() {
    override fun getLayoutRes(): Int = R.layout.fragment_about
    private val accountViewModel by sharedViewModel<SharedAccountViewModel>()
    lateinit var UrlReference : DatabaseReference
    override fun initData() {
    }

    override fun initObserver() {
    }

    override fun initView() {
        val sharedPreference =  context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val editor = sharedPreference?.edit()
        editor?.putInt("NewsVersion",accountViewModel.userAccount.value?.newsVersion?:0)
        editor?.apply()
        getPicUrl()
        MobileAds.initialize(context) {}
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun getPicUrl() {
        var Url = ""
        UrlReference = FirebaseDatabase.getInstance().getReference("LifeData").child("PicInfo")
        val UrlListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                Url = dataSnapshot.child("PicUrl").value.toString()
                Picasso.get()
                    .load(Url)
                    .placeholder(R.drawable.common_google_signin_btn_icon_dark)
//                    .fit()
                    .resize(dataSnapshot.child("X").value.toString().toInt(), dataSnapshot.child("Y").value.toString().toInt())
                    .onlyScaleDown()
                    .into(img_Account)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        UrlReference.addValueEventListener(UrlListener)
    }

    companion object{
        fun newInstance() = About()
    }
}