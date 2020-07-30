package com.eric.groupsheet.EditSheet

import android.os.Bundle
import android.view.animation.ScaleAnimation
import com.eric.groupsheet.MainHome.SharedAccountViewModel
import com.eric.groupsheet.R
import com.eric.groupsheet.base.BaseFragment
import com.eric.groupsheet.base.observe
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.fragment_sheet_analysis.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class SheetAnalysis:BaseFragment() {
    override fun getLayoutRes(): Int = R.layout.fragment_sheet_analysis
    private val accountViewModel by sharedViewModel<SharedAccountViewModel>()
    private val viewModel by viewModel<SheetAnalysisViewModel>()
    override fun initData() {
        viewModel.getRightRef(accountViewModel.userAccount.value?.userID.toString())
    }

    override fun initObserver() {
        observe(viewModel.Sheet){
            tv_title.text = it.mName
            viewModel.toRuleList(it)
        }
        observe(viewModel.RuleClassList){
            updateRuleUi(it)

        }
        observe(viewModel.AmountList){
            updateAmountUi(it)
            viewModel.countTotalMoney(it,viewModel.RuleClassList)
            animaAmountUi()
        }
        observe(viewModel.TotalMoney){
            tv_total_member_num.text = viewModel.MemberAmount.toString()
            tv_total_money_num.text = it.toString()
            tv_total_submoney_num.text = viewModel.SubMoney.toString()
            tv_total_left_money_num.text = (it - viewModel.SubMoney).toString()
        }
    }

    private fun updateAmountUi(amountList: List<List<Int>>) {
        amountList.let {
            tv_rule1_op1_num.text = it.get(0).get(0).toString()
            tv_rule1_op2_num.text = it.get(0).get(1).toString()
            tv_rule1_op3_num.text = it.get(0).get(2).toString()
            tv_rule1_op4_num.text = it.get(0).get(3).toString()
            tv_rule1_op5_num.text = it.get(0).get(4).toString()
            tv_rule2_op1_num.text = it.get(1).get(0).toString()
            tv_rule2_op2_num.text = it.get(1).get(1).toString()
            tv_rule2_op3_num.text = it.get(1).get(2).toString()
            tv_rule2_op4_num.text = it.get(1).get(3).toString()
            tv_rule2_op5_num.text = it.get(1).get(4).toString()
            tv_rule3_op1_num.text = it.get(2).get(0).toString()
            tv_rule3_op2_num.text = it.get(2).get(1).toString()
            tv_rule3_op3_num.text = it.get(2).get(2).toString()
            tv_rule3_op4_num.text = it.get(2).get(3).toString()
            tv_rule3_op5_num.text = it.get(2).get(4).toString()
            tv_rule4_op1_num.text = it.get(3).get(0).toString()
            tv_rule4_op2_num.text = it.get(3).get(1).toString()
            tv_rule4_op3_num.text = it.get(3).get(2).toString()
            tv_rule4_op4_num.text = it.get(3).get(3).toString()
            tv_rule4_op5_num.text = it.get(3).get(4).toString()
            tv_rule5_op1_num.text = it.get(4).get(0).toString()
            tv_rule5_op2_num.text = it.get(4).get(1).toString()
            tv_rule5_op3_num.text = it.get(4).get(2).toString()
            tv_rule5_op4_num.text = it.get(4).get(3).toString()
            tv_rule5_op5_num.text = it.get(4).get(4).toString()

        }
    }

    private fun updateRuleUi(it: List<RuleClass>) {
        tv_rule1_name.text = it.get(0).mRuleName
        tv_rule1_op1.text = it.get(0).mOption1Name
        tv_rule1_op2.text = it.get(0).mOption2Name
        tv_rule1_op3.text = it.get(0).mOption3Name
        tv_rule1_op4.text = it.get(0).mOption4Name
        tv_rule1_op5.text = it.get(0).mOption5Name

        tv_rule2_name.text = it.get(1).mRuleName
        tv_rule2_op1.text = it.get(1).mOption1Name
        tv_rule2_op2.text = it.get(1).mOption2Name
        tv_rule2_op3.text = it.get(1).mOption3Name
        tv_rule2_op4.text = it.get(1).mOption4Name
        tv_rule2_op5.text = it.get(1).mOption5Name

        tv_rule3_name.text = it.get(2).mRuleName
        tv_rule3_op1.text = it.get(2).mOption1Name
        tv_rule3_op2.text = it.get(2).mOption2Name
        tv_rule3_op3.text = it.get(2).mOption3Name
        tv_rule3_op4.text = it.get(2).mOption4Name
        tv_rule3_op5.text = it.get(2).mOption5Name

        tv_rule4_name.text = it.get(3).mRuleName
        tv_rule4_op1.text = it.get(3).mOption1Name
        tv_rule4_op2.text = it.get(3).mOption2Name
        tv_rule4_op3.text = it.get(3).mOption3Name
        tv_rule4_op4.text = it.get(3).mOption4Name
        tv_rule4_op5.text = it.get(3).mOption5Name

        tv_rule5_name.text = it.get(4).mRuleName
        tv_rule5_op1.text = it.get(4).mOption1Name
        tv_rule5_op2.text = it.get(4).mOption2Name
        tv_rule5_op3.text = it.get(4).mOption3Name
        tv_rule5_op4.text = it.get(4).mOption4Name
        tv_rule5_op5.text = it.get(4).mOption5Name
    }

    override fun initView() {
        arguments?.getString(SHEET_ID)?.let {
            viewModel.getSheetData(it)
        }
        MobileAds.initialize(context) {}
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

    }

    private fun animaAmountUi() {
        val scaleAnimation11 = ScaleAnimation(0.5f, getOptionAmount(0,0)?:2f, 1f, 1f)
        scaleAnimation11.setDuration(1500)
        scaleAnimation11.fillAfter = true
        card11.startAnimation(scaleAnimation11)
        val scaleAnimation12 = ScaleAnimation(0.5f, getOptionAmount(0,1)?:2f, 1f, 1f)
        scaleAnimation12.setDuration(1500)
        scaleAnimation12.fillAfter = true
        card12.startAnimation(scaleAnimation12)
        val scaleAnimation13 = ScaleAnimation(0.5f, getOptionAmount(0,2)?:2f, 1f, 1f)
        scaleAnimation13.setDuration(1500)
        scaleAnimation13.fillAfter = true
        card13.startAnimation(scaleAnimation13)
        val scaleAnimation14 = ScaleAnimation(0.5f, getOptionAmount(0,3)?:2f, 1f, 1f)
        scaleAnimation14.setDuration(1500)
        scaleAnimation14.fillAfter = true
        card14.startAnimation(scaleAnimation14)
        val scaleAnimation15 = ScaleAnimation(0.5f, getOptionAmount(0,4)?:2f, 1f, 1f)
        scaleAnimation15.setDuration(1500)
        scaleAnimation15.fillAfter = true
        card15.startAnimation(scaleAnimation15)
        val scaleAnimation21 = ScaleAnimation(0.5f, getOptionAmount(1,0)?:2f, 1f, 1f)
        scaleAnimation21.setDuration(1500)
        scaleAnimation21.fillAfter = true
        card21.startAnimation(scaleAnimation21)
        val scaleAnimation22 = ScaleAnimation(0.5f, getOptionAmount(1,1)?:2f, 1f, 1f)
        scaleAnimation22.setDuration(1500)
        scaleAnimation22.fillAfter = true
        card22.startAnimation(scaleAnimation22)
        val scaleAnimation23 = ScaleAnimation(0.5f, getOptionAmount(1,2)?:2f, 1f, 1f)
        scaleAnimation23.setDuration(1500)
        scaleAnimation23.fillAfter = true
        card23.startAnimation(scaleAnimation23)
        val scaleAnimation24 = ScaleAnimation(0.5f, getOptionAmount(1,3)?:2f, 1f, 1f)
        scaleAnimation24.setDuration(1500)
        scaleAnimation24.fillAfter = true
        card24.startAnimation(scaleAnimation24)
        val scaleAnimation25 = ScaleAnimation(0.5f, getOptionAmount(1,4)?:2f, 1f, 1f)
        scaleAnimation25.setDuration(1500)
        scaleAnimation25.fillAfter = true
        card25.startAnimation(scaleAnimation25)
        val scaleAnimation31 = ScaleAnimation(0.5f, getOptionAmount(2,0)?:2f, 1f, 1f)
        scaleAnimation31.setDuration(1500)
        scaleAnimation31.fillAfter = true
        card31.startAnimation(scaleAnimation31)
        val scaleAnimation32 = ScaleAnimation(0.5f, getOptionAmount(2,1)?:2f, 1f, 1f)
        scaleAnimation32.setDuration(1500)
        scaleAnimation32.fillAfter = true
        card32.startAnimation(scaleAnimation32)
        val scaleAnimation33 = ScaleAnimation(0.5f, getOptionAmount(2,2)?:2f, 1f, 1f)
        scaleAnimation33.setDuration(1500)
        scaleAnimation33.fillAfter = true
        card33.startAnimation(scaleAnimation33)
        val scaleAnimation34 = ScaleAnimation(0.5f, getOptionAmount(2,3)?:2f, 1f, 1f)
        scaleAnimation34.setDuration(1500)
        scaleAnimation34.fillAfter = true
        card34.startAnimation(scaleAnimation34)
        val scaleAnimation35 = ScaleAnimation(0.5f, getOptionAmount(2,4)?:2f, 1f, 1f)
        scaleAnimation35.setDuration(1500)
        scaleAnimation35.fillAfter = true
        card35.startAnimation(scaleAnimation35)
        val scaleAnimation41 = ScaleAnimation(0.5f, getOptionAmount(3,0)?:2f, 1f, 1f)
        scaleAnimation41.setDuration(1500)
        scaleAnimation41.fillAfter = true
        card41.startAnimation(scaleAnimation41)
        val scaleAnimation42 = ScaleAnimation(0.5f, getOptionAmount(3,1)?:2f, 1f, 1f)
        scaleAnimation42.setDuration(1500)
        scaleAnimation42.fillAfter = true
        card42.startAnimation(scaleAnimation42)
        val scaleAnimation43 = ScaleAnimation(0.5f, getOptionAmount(3,2)?:2f, 1f, 1f)
        scaleAnimation43.setDuration(1500)
        scaleAnimation43.fillAfter = true
        card43.startAnimation(scaleAnimation43)
        val scaleAnimation44 = ScaleAnimation(0.5f, getOptionAmount(3,3)?:2f, 1f, 1f)
        scaleAnimation44.setDuration(1500)
        scaleAnimation44.fillAfter = true
        card44.startAnimation(scaleAnimation44)
        val scaleAnimation45 = ScaleAnimation(0.5f, getOptionAmount(3,4)?:2f, 1f, 1f)
        scaleAnimation45.setDuration(1500)
        scaleAnimation45.fillAfter = true
        card45.startAnimation(scaleAnimation45)
        val scaleAnimation51 = ScaleAnimation(0.5f, getOptionAmount(4,0)?:2f, 1f, 1f)
        scaleAnimation51.setDuration(1500)
        scaleAnimation51.fillAfter = true
        card51.startAnimation(scaleAnimation51)
        val scaleAnimation52 = ScaleAnimation(0.5f, getOptionAmount(4,1)?:2f, 1f, 1f)
        scaleAnimation52.setDuration(1500)
        scaleAnimation52.fillAfter = true
        card52.startAnimation(scaleAnimation52)
        val scaleAnimation53 = ScaleAnimation(0.5f, getOptionAmount(4,2)?:2f, 1f, 1f)
        scaleAnimation53.setDuration(1500)
        scaleAnimation53.fillAfter = true
        card53.startAnimation(scaleAnimation53)
        val scaleAnimation54 = ScaleAnimation(0.5f, getOptionAmount(4,3)?:2f, 1f, 1f)
        scaleAnimation54.setDuration(1500)
        scaleAnimation54.fillAfter = true
        card54.startAnimation(scaleAnimation54)
        val scaleAnimation55 = ScaleAnimation(0.5f, getOptionAmount(4,4)?:2f, 1f, 1f)
        scaleAnimation55.setDuration(1500)
        scaleAnimation55.fillAfter = true
        card55.startAnimation(scaleAnimation55)

    }

    private fun getOptionAmount(rule: Int, option: Int): Float? {
        val son = viewModel.AmountList.value?.get(rule)?.get(option)
        val mom = viewModel.MemberAmount
        val per = son?.toFloat()?.div(mom)
//        Log.d("TAG",per.toString()+" "+son)
        return per
    }

    companion object{
        private const val SHEET_ID = "sheetId"

        fun newInstance(sheetId: String) = SheetAnalysis().apply {
            arguments = Bundle().apply {
                putString(SHEET_ID, sheetId)
            }
        }
    }
}