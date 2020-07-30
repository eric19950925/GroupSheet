package com.eric.groupsheet.EditSheet

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.eric.groupsheet.MainHome.SharedAccountViewModel
import com.eric.groupsheet.R
import com.eric.groupsheet.base.BaseFragment
import com.eric.groupsheet.base.observe
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.fragment_edit_rule.*
import kotlinx.android.synthetic.main.fragment_edit_rule.adView
import kotlinx.android.synthetic.main.fragment_edit_rule.tv_title
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class EditRule : BaseFragment(),RuleListController{
    override fun getLayoutRes(): Int = R.layout.fragment_edit_rule
    private val editSheetviewModel by sharedViewModel<EditSheetViewModel>()
    private val accountViewModel by sharedViewModel<SharedAccountViewModel>()
    private val viewModel by viewModel<EditRuleViewModel>()
    override fun initData() {
        viewModel.getRightRef(accountViewModel.userAccount.value?.userID.toString())
    }

    override fun initObserver() {
        observe(viewModel.Sheet){
            tv_title.text = it.mName
            viewModel.toRuleList(it)
        }
        observe(viewModel.RuleClassList){
            val rulesAdapter = it.let { RulesAdapter(it,this) }
            rv_ruleList.adapter = rulesAdapter
        }
    }


    override fun initView() {
        arguments?.getString(SHEET_ID)?.let {
            viewModel.getSheetData(it)
        }
        rv_ruleList.setHasFixedSize(true)
        rv_ruleList.layoutManager = LinearLayoutManager(context)
        MobileAds.initialize(context) {}
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
    companion object{
        private const val SHEET_ID = "sheetId"

        fun newInstance(sheetId: String) = EditRule().apply {
            arguments = Bundle().apply {
                putString(SHEET_ID, sheetId)
            }
        }
    }

    override fun updateRvAfterDel(id: String) {

    }

    override fun editRule(ruleId: String) {
        val EditRuleView : View = getLayoutInflater().inflate(R.layout.dialog_edit_rule,null)
        val ed_mName : EditText = EditRuleView.findViewById(R.id.ed_c1)
        val ed_n1 : EditText = EditRuleView.findViewById(R.id.ed_option1_name)
        val ed_v1 : EditText = EditRuleView.findViewById(R.id.ed_option1_value)
        val ed_n2 : EditText = EditRuleView.findViewById(R.id.ed_option2_name)
        val ed_v2 : EditText = EditRuleView.findViewById(R.id.ed_option2_value)
        val ed_n3 : EditText = EditRuleView.findViewById(R.id.ed_option3_name)
        val ed_v3 : EditText = EditRuleView.findViewById(R.id.ed_option3_value)
        val ed_n4 : EditText = EditRuleView.findViewById(R.id.ed_option4_name)
        val ed_v4 : EditText = EditRuleView.findViewById(R.id.ed_option4_value)
        val ed_n5 : EditText = EditRuleView.findViewById(R.id.ed_option5_name)
        val ed_v5 : EditText = EditRuleView.findViewById(R.id.ed_option5_value)
        val mRule = viewModel.getOneRuleItemData(ruleId)
        ed_mName.setText(mRule.mRuleName)
        ed_n1.setText(mRule.mOption1Name)
        ed_v1.setText(mRule.mOption1Value)
        ed_n2.setText(mRule.mOption2Name)
        ed_v2.setText(mRule.mOption2Value)
        ed_n3.setText(mRule.mOption3Name)
        ed_v3.setText(mRule.mOption3Value)
        ed_n4.setText(mRule.mOption4Name)
        ed_v4.setText(mRule.mOption4Value)
        ed_n5.setText(mRule.mOption5Name)
        ed_v5.setText(mRule.mOption5Value)

        val builder = activity?.let { it1 -> AlertDialog.Builder(it1) }
        builder?.setTitle("修改表單規則")
        builder?.setView(EditRuleView)
        builder?.setPositiveButton("確定", DialogInterface.OnClickListener { dialog, which ->
            val mNewRule = arrayListOf(
                ed_n1.text.toString() ,
                ed_v1.text.toString(),
                ed_n2.text.toString(),
                ed_v2.text.toString(),
                ed_n3.text.toString(),
                ed_v3.text.toString(),
                ed_n4.text.toString(),
                ed_v4.text.toString(),
                ed_n5.text.toString(),
                ed_v5.text.toString(),
                ed_mName.text.toString())
            //            val currentDateTime= LocalDateTime.now()
            //            mMember = MemberClass(
            //                id = currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
            //                who = etMemberName.text.toString(),
            //                type = ToTypeNum( userSex , userAge )
            //            )
            //            viewModel.updateMember(id,mMember)
            arguments?.getString(SHEET_ID)?.let {
                viewModel.updateRuleListAfterEdit(it, ruleId, mNewRule)
            }
                dialog.dismiss()
        })?.setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })
        builder?.show()
    }
}