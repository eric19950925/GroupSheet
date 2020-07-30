package com.eric.groupsheet.NameList

import android.content.DialogInterface
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.eric.groupsheet.MainHome.SharedAccountViewModel
import com.eric.groupsheet.R
import com.eric.groupsheet.base.BaseFragment
import com.eric.groupsheet.base.observe
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.fragment_name_list.*
import kotlinx.android.synthetic.main.fragment_name_list.adView
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NameList:BaseFragment() ,NameListController{
    private val viewModel by viewModel<NameListViewModel>()
    lateinit var mMember : MemberClass
    override fun getLayoutRes(): Int =
        R.layout.fragment_name_list
    private val accountViewModel by sharedViewModel<SharedAccountViewModel>()
    override fun initData() {
        Log.d("TAG",accountViewModel.userAccount.value?.userID.toString())
    }

    override fun initObserver () {
        observe(viewModel.NameList){
            val nameListAdapter = NameListAdapter(it,this)
            rv_nameList.adapter = nameListAdapter
        }
    }

    override fun initView() {
        tv_newName.setOnClickListener {
            if(viewModel.NameList.value?.size!! > 19){
                val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
                builder?.setTitle("最多只能加入 20 位成員")
                builder?.setPositiveButton("知道了", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                builder?.show()
                return@setOnClickListener
            }
            val AddMemberView : View = getLayoutInflater().inflate(R.layout.dialog_add_member,null)
            val builder = activity?.let { it1 -> AlertDialog.Builder(it1) }
                builder?.setView(AddMemberView)
                builder?.setTitle("新增成員")
                builder?.setPositiveButton("確定", DialogInterface.OnClickListener { dialog, which ->
                    val etMemberName: EditText =AddMemberView.findViewById(R.id.et_mName)
                    val rbSexMale: RadioButton =AddMemberView.findViewById(R.id.rb_male)
                    val rbSexFemale: RadioButton =AddMemberView.findViewById(R.id.rb_female)
                    val rbAgeElders: RadioButton =AddMemberView.findViewById(R.id.rb_elders)
                    val rbAgeAdult: RadioButton =AddMemberView.findViewById(R.id.rb_adult)
                    val rbAgeStudent: RadioButton =AddMemberView.findViewById(R.id.rb_student)
                    val rbAgeChild: RadioButton =AddMemberView.findViewById(R.id.rb_child)
                    var userSex = "None"
                    var userAge = "None"
                    rbSexMale.let{
                        if(it.isChecked)userSex = "Male"
                    }
                    rbSexFemale.let{
                        if(it.isChecked)userSex = "Female"
                    }
                    rbAgeElders.let{
                        if(it.isChecked)userAge = "Elders"
                    }
                    rbAgeAdult.let {
                        if(it.isChecked)userAge = "Adult"
                    }
                    rbAgeStudent.let{
                        if(it.isChecked)userAge = "Student"
                    }
                    rbAgeChild.let {
                        if(it.isChecked)userAge = "Child"
                    }
                    val currentDateTime= LocalDateTime.now()

                    mMember = MemberClass(
                        id = currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
                        who = etMemberName.text.toString(),
                        type = ToTypeNum( userSex , userAge )
                    )

                    viewModel.addMember(mMember,accountViewModel.userAccount.value?.userID.toString())
                    viewModel.reloadNameList(accountViewModel.userAccount.value?.userID.toString())
                    dialog.dismiss()
            })?.setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            builder?.show()

        }
        viewModel.reloadNameList(accountViewModel.userAccount.value?.userID.toString())
        rv_nameList.setHasFixedSize(true)
        rv_nameList.layoutManager = LinearLayoutManager(context)
        val nameListAdapter = viewModel.NameList.value?.let { NameListAdapter(it,this) }
        rv_nameList.adapter = nameListAdapter
        MobileAds.initialize(context) {}
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun ToTypeNum(userSex: String, userAge: String): Int =
        when (userSex) {
            "Male" -> {
                when (userAge) {
                    "Elders" -> 1
                    "Adult" -> 2
                    "Student" -> 3
                    "Child" -> 4
                    else -> 0
                }
            }
            "Female" -> {
                when (userAge) {
                    "Elders" -> 5
                    "Adult" -> 6
                    "Student" -> 7
                    "Child" -> 8
                    else -> 0
                }
            }
            else -> 0
        }

    companion object {
        fun newInstance() = NameList()
    }

    override fun updateRvAfterDel(id: String) {
        viewModel.DeletMember(id,accountViewModel.userAccount.value?.userID.toString())
    }

    override fun editMember(id: String) {
        var mMember = viewModel.getOneNameData(id)
        val AddMemberView : View = getLayoutInflater().inflate(R.layout.dialog_add_member,null)
        val etMemberName: EditText =AddMemberView.findViewById(R.id.et_mName)
        val rbSexMale: RadioButton =AddMemberView.findViewById(R.id.rb_male)
        val rbSexFemale: RadioButton =AddMemberView.findViewById(R.id.rb_female)
        val rbAgeElders: RadioButton =AddMemberView.findViewById(R.id.rb_elders)
        val rbAgeAdult: RadioButton =AddMemberView.findViewById(R.id.rb_adult)
        val rbAgeStudent: RadioButton =AddMemberView.findViewById(R.id.rb_student)
        val rbAgeChild: RadioButton =AddMemberView.findViewById(R.id.rb_child)
        etMemberName.setText(mMember.mName)
        var userSex = "None"
        var userAge = "None"
        rbSexMale.isChecked = mMember.mType < 5 && mMember.mType > 0
        rbSexFemale.isChecked = mMember.mType > 5
        rbAgeElders.isChecked = mMember.mType == 1 || mMember.mType == 5
        rbAgeAdult.isChecked = mMember.mType == 2 || mMember.mType == 6
        rbAgeStudent.isChecked = mMember.mType == 3 || mMember.mType == 7
        rbAgeChild.isChecked = mMember.mType == 4 || mMember.mType == 8
        val builder = activity?.let { it1 -> AlertDialog.Builder(it1) }
        builder?.setTitle("修改成員資料")
        builder?.setView(AddMemberView)
        builder?.setPositiveButton("確定", DialogInterface.OnClickListener { dialog, which ->
            rbSexMale.let{
                if(it.isChecked)userSex = "Male"
            }
            rbSexFemale.let{
                if(it.isChecked)userSex = "Female"
            }
            rbAgeElders.let{
                if(it.isChecked)userAge = "Elders"
            }
            rbAgeAdult.let {
                if(it.isChecked)userAge = "Adult"
            }
            rbAgeStudent.let{
                if(it.isChecked)userAge = "Student"
            }
            rbAgeChild.let {
                if(it.isChecked)userAge = "Child"
            }
            val currentDateTime= LocalDateTime.now()
            mMember = MemberClass(
                id = currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
                who = etMemberName.text.toString(),
                type = ToTypeNum( userSex , userAge )
            )
            viewModel.updateMember(id,mMember,accountViewModel.userAccount.value?.userID.toString())
            dialog.dismiss()
        })?.setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })
        builder?.show()
    }
}