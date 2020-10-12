package com.eric.groupsheet

import android.util.Log
import com.eric.groupsheet.MainHome.SharedAccountViewModel
import com.eric.groupsheet.base.BaseFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.UserEmail
import kotlinx.android.synthetic.main.fragment_account.UserName
import kotlinx.android.synthetic.main.fragment_account.adView
import kotlinx.android.synthetic.main.fragment_account.img_Account
import org.koin.android.viewmodel.ext.android.sharedViewModel

class UserAccount : BaseFragment() {

    private val accountViewModel by sharedViewModel<SharedAccountViewModel>()
    override fun getLayoutRes(): Int = R.layout.fragment_account
    lateinit var UrlReference : DatabaseReference

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
        UrlReference = FirebaseDatabase.getInstance().getReference("LifeData").child("verse")
        val UrlListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                verse.text = dataSnapshot.getValue().toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        UrlReference.addValueEventListener(UrlListener)

        MobileAds.initialize(context) {}
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
    companion object{
        fun newInstance() = UserAccount()
    }
}