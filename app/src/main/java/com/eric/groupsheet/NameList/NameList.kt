package com.eric.groupsheet.NameList

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.eric.groupsheet.MainHome.SharedAccountViewModel
import com.eric.groupsheet.R
import com.eric.groupsheet.base.BaseFragment
import com.eric.groupsheet.base.listenClick
import com.eric.groupsheet.base.observe
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.fragment_name_list.*
import kotlinx.android.synthetic.main.fragment_name_list.adView
import kotlinx.android.synthetic.main.fragment_name_list.got_it
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class NameList:BaseFragment() ,NameListController{
    private val viewModel by viewModel<NameListViewModel>()
    lateinit var mMember : MemberClass
    override fun getLayoutRes(): Int =
        R.layout.fragment_name_list
    private val accountViewModel by sharedViewModel<SharedAccountViewModel>()
    override fun initData() {
        Log.d("TAG",accountViewModel.userAccount.value?.userID.toString())
        checkAnswer()
    }

    override fun initObserver () {
        observe(viewModel.NameList){
            val nameListAdapter = NameListAdapter(it,this)
            rv_nameList.adapter = nameListAdapter
        }
        observe(viewModel.Tutorial_addNameList){
            if(it == 0){
                cl_tutorial_to_add_nameList.visibility = View.VISIBLE
            }
            else cl_tutorial_to_add_nameList.visibility = View.GONE
        }
    }
    private fun checkAnswer() {
        val sharedPreference =  context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        viewModel.Tutorial_addNameList.value = sharedPreference?.getInt("Tutorial_addNameList",0)
    }
    @RequiresApi(Build.VERSION_CODES.O)
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
                    val currentDateTimeOV= Calendar.getInstance().time

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        mMember = MemberClass(
                            id = currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
                            who = etMemberName.text.toString(),
                            type = ToTypeNum( userSex , userAge )
                        )
                    }else{
                        val df = SimpleDateFormat("yyyyMMddHHmmss")
                        mMember = MemberClass(
                            id = df.format(currentDateTimeOV),
                            who = etMemberName.text.toString(),
                            type = ToTypeNum( userSex , userAge )
                        )
                    }

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

        got_it.listenClick {
            val sharedPreference =  context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            val editor = sharedPreference?.edit()
            editor?.putInt("Tutorial_addNameList",1)
            editor?.apply()
            checkAnswer()
            viewModel.Tutorial_addNameList.value = 1
        }
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

    @RequiresApi(Build.VERSION_CODES.O)
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
            val currentDateTimeOV= Calendar.getInstance().time

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mMember = MemberClass(
                    id = currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
                    who = etMemberName.text.toString(),
                    type = ToTypeNum( userSex , userAge )
                )
            }else{
                val df = SimpleDateFormat("yyyyMMddHHmmss")
                mMember = MemberClass(
                    id = df.format(currentDateTimeOV),
                    who = etMemberName.text.toString(),
                    type = ToTypeNum( userSex , userAge )
                )
            }
            viewModel.updateMember(id,mMember,accountViewModel.userAccount.value?.userID.toString())
            dialog.dismiss()
        })?.setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })
        builder?.show()
    }
}