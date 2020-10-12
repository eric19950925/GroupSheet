package com.eric.groupsheet.EditSheet.EditSheet

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eric.groupsheet.MainHome.SharedAccountViewModel
import com.eric.groupsheet.R
import com.eric.groupsheet.base.BaseFragment
import com.eric.groupsheet.base.listenClick
import com.eric.groupsheet.base.observe
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.fragment_edit_sheet.*
import kotlinx.android.synthetic.main.fragment_edit_sheet.adView
import kotlinx.android.synthetic.main.fragment_edit_sheet.got_it
import kotlinx.android.synthetic.main.fragment_edit_sheet.tv_title
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap


class EditSheet:BaseFragment() ,
    SheetDataController,
    SheetDataNameController,
    View.OnScrollChangeListener {
    private val viewModel by viewModel<EditSheetViewModel>()
    lateinit var sheetDataAdapter : SheetDataAdapter
    lateinit var sheetDataNameAdapter : SheetDataNameController
    private val accountViewModel by sharedViewModel<SharedAccountViewModel>()
    override fun initData() {
        viewModel.UserId = accountViewModel.userAccount.value?.userPhotoUrl.toString()
    }
    override fun getLayoutRes(): Int =
        R.layout.fragment_edit_sheet


    override fun initObserver() {
        observe(viewModel.Sheet){
            tv_title.text = it.mName
            viewModel.SheetData.value = it.mSheetData
            viewModel.getRuleTitle(it)
        }
        observe(viewModel.SheetData){
            Log.d("TAG","refresh")

            sheetDataAdapter = viewModel.SheetData.value?.let {
                SheetDataAdapter(
                    it,
                    this
                )
            }!!
            rv_sheetData.adapter = sheetDataAdapter
            sheetDataAdapter.notifyDataSetChanged()
            sheetDataNameAdapter = viewModel.SheetData.value?.let {
                SheetDataNameAdapter(
                    it,
                    this
                )
            }!!
            rv_name.adapter = sheetDataNameAdapter as SheetDataNameAdapter
        }
        observe(viewModel.dataPos){
            hs_data_title.scrollX = it
            hs_data.scrollX = it
        }
        observe(viewModel.dataPosY){
            rv_name.smoothScrollToPosition(it)
        }
        observe(viewModel.RuleTitle){
            item_c1.text = it.get(0)
            item_c2.text = it.get(1)
            item_c3.text = it.get(2)
            item_c4.text = it.get(3)
            item_c5.text = it.get(4)
        }
        observe(viewModel.Tutorial_editSheet){
            if(it == 0){
                cl_tutorial_editSheet.visibility = View.VISIBLE
            }else cl_tutorial_editSheet.visibility = View.GONE
        }

    }

    override fun onFragmentShow() {
        arguments?.getString(SHEET_ID)?.let {
            viewModel.getSheetData(it)
        }
        MobileAds.initialize(context) {}
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        checkAnswer()
    }
    private fun checkAnswer() {
        val sharedPreference =  context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        viewModel.Tutorial_editSheet.value = sharedPreference?.getInt("Tutorial_editSheet",0)
    }
    override fun initView() {
        viewModel.getRightRef(accountViewModel.userAccount.value?.userID.toString())
        arguments?.getString(SHEET_ID)?.let {
            viewModel.getSheetData(it)
        }
        rv_sheetData.setHasFixedSize(true)
        rv_sheetData.layoutManager = LinearLayoutManager(context)
        val sheetDataAdapter = viewModel.SheetData.value?.let {
            SheetDataAdapter(
                it,
                this
            )
        }
        rv_sheetData.adapter = sheetDataAdapter

        rv_name.setHasFixedSize(true)
        rv_name.layoutManager = LinearLayoutManager(context)
        val sheetDataNameAdapter = viewModel.SheetData.value?.let {
            SheetDataNameAdapter(
                it,
                this
            )
        }
        rv_name.adapter = sheetDataNameAdapter

        hs_data.setOnScrollChangeListener(this)
        hs_data_title.setOnScrollChangeListener(this)

        tv_edit_Sheet_rule.setOnClickListener {
            arguments?.getString(SHEET_ID)?.let { viewModel.toEditRule(it)}
        }

        tv_List_analysis.setOnClickListener {
            arguments?.getString(SHEET_ID)?.let { viewModel.toAnalysis(it)}
        }

        tv_edit_Sheet_name.setOnClickListener {
            viewModel.Sheet.value?.mName
            val EditSheetNameView : View = getLayoutInflater().inflate(R.layout.dialog_edit_sheet_name,null)
            val ed_name : EditText = EditSheetNameView.findViewById(R.id.ed_mName)
            ed_name.setText(viewModel.Sheet.value?.mName)

            val builder = activity?.let { it1 -> AlertDialog.Builder(it1) }
            builder?.setTitle("修改表單名稱")
            builder?.setView(EditSheetNameView)
            builder?.setPositiveButton("確定", DialogInterface.OnClickListener { dialog, which ->

                arguments?.getString(SHEET_ID)?.let {
                    viewModel.updateSheetName(it, ed_name.text.toString())
                }
                tv_title.text = ed_name.text.toString()
                dialog.dismiss()
            })?.setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            builder?.show()
        }

        var isProgrammaticallyScrolling = false
        rv_sheetData.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!isProgrammaticallyScrolling) {
                    isProgrammaticallyScrolling = true
                    rv_name.scrollBy(dx, dy)
                    isProgrammaticallyScrolling = false
                }
            }
        })
        got_it.listenClick {
            val sharedPreference =  context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            val editor = sharedPreference?.edit()
            editor?.putInt("Tutorial_editSheet",1)
            editor?.apply()
            checkAnswer()
            viewModel.Tutorial_editSheet.value = 1
        }

    }
    companion object{
        private const val SHEET_ID = "sheetId"

        fun newInstance(sheetId: String) = EditSheet()
            .apply {
            arguments = Bundle().apply {
                putString(SHEET_ID, sheetId)
            }
        }
    }

    override fun updateRvAfterDel(id: String) {

    }

    override fun editMember(id: String) {
        val mSheetItem = viewModel.getOneSheetItemData(id)
        val AddSheetItemView : View = getLayoutInflater().inflate(R.layout.dialog_edit_sheet_item,null)
        val tvsheetItemName: TextView =AddSheetItemView.findViewById(R.id.tv_mName)
        val tv_c1 : TextView = AddSheetItemView.findViewById(R.id.tv_c1)
        val tv_c2 : TextView = AddSheetItemView.findViewById(R.id.tv_c2)
        val tv_c3 : TextView = AddSheetItemView.findViewById(R.id.tv_c3)
        val tv_c4 : TextView = AddSheetItemView.findViewById(R.id.tv_c4)
        val tv_c5 : TextView = AddSheetItemView.findViewById(R.id.tv_c5)
        val ed_sub : EditText = AddSheetItemView.findViewById(R.id.ed_mSub)
        val spinner_c1 : Spinner = AddSheetItemView.findViewById(R.id.sp_c1)
        val spinner_c2 : Spinner = AddSheetItemView.findViewById(R.id.sp_c2)
        val spinner_c3 : Spinner = AddSheetItemView.findViewById(R.id.sp_c3)
        val spinner_c4 : Spinner = AddSheetItemView.findViewById(R.id.sp_c4)
        val spinner_c5 : Spinner = AddSheetItemView.findViewById(R.id.sp_c5)
        val mRule = viewModel.Sheet.value?.mRule
        val sortedMap: MutableMap<String, ArrayList<String>> = LinkedHashMap()
        mRule?.entries?.sortedBy { it.key }?.forEach { sortedMap[it.key] = it.value }
        tv_c1.text = sortedMap.values.elementAt(0).get(10)
        tv_c2.text = sortedMap.values.elementAt(1).get(10)
        tv_c3.text = sortedMap.values.elementAt(2).get(10)
        tv_c4.text = sortedMap.values.elementAt(3).get(10)
        tv_c5.text = sortedMap.values.elementAt(4).get(10)
        ed_sub.setText(mSheetItem.submitted.toString())

        val OptionA =  mutableListOf<String>()
        val OptionB =  mutableListOf<String>()
        val OptionC =  mutableListOf<String>()
        val OptionD =  mutableListOf<String>()
        val OptionE =  mutableListOf<String>()
        for(i in 0 until 9){
            if( i % 2 == 0 ){
                OptionA.add(sortedMap.values.elementAt(0).get(i)+" = "+sortedMap.values.elementAt(0).get(i+1))
                OptionB.add(sortedMap.values.elementAt(1).get(i)+" = "+sortedMap.values.elementAt(1).get(i+1))
                OptionC.add(sortedMap.values.elementAt(2).get(i)+" = "+sortedMap.values.elementAt(2).get(i+1))
                OptionD.add(sortedMap.values.elementAt(3).get(i)+" = "+sortedMap.values.elementAt(3).get(i+1))
                OptionE.add(sortedMap.values.elementAt(4).get(i)+" = "+sortedMap.values.elementAt(4).get(i+1))
            }
        }
        val adapterA = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, OptionA )
        spinner_c1.adapter = adapterA
        spinner_c1.setSelection(mSheetItem.mC1/2)
        val adapterB = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, OptionB )
        spinner_c2.adapter = adapterB
        spinner_c2.setSelection(mSheetItem.mC2/2)
        val adapterC = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, OptionC )
        spinner_c3.adapter = adapterC
        spinner_c3.setSelection(mSheetItem.mC3/2)
        val adapterD = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, OptionD )
        spinner_c4.adapter = adapterD
        spinner_c4.setSelection(mSheetItem.mC4/2)
        val adapterE = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, OptionE )
        spinner_c5.adapter = adapterE
        spinner_c5.setSelection(mSheetItem.mC5/2)
        tvsheetItemName.text = mSheetItem.mName
        val newCon = mutableListOf<Int>(0,0,0,0,0)

        spinner_c1.onItemSelectedListener =
            object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>) {}
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    newCon[0] = p2 * 2
                }
            }
        spinner_c2.onItemSelectedListener =
            object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>) {}
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    newCon[1] = p2 * 2
                }
            }
        spinner_c3.onItemSelectedListener =
            object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>) {}
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    newCon[2] = p2 * 2
                }
            }
        spinner_c4.onItemSelectedListener =
            object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>) {}
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    newCon[3] = p2 * 2
                }
            }
        spinner_c5.onItemSelectedListener =
            object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>) {}
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    newCon[4] = p2 * 2
                }
            }
        val builder = activity?.let { it1 -> AlertDialog.Builder(it1) }
        builder?.setTitle("修改表單資料")
        builder?.setView(AddSheetItemView)
        builder?.setPositiveButton("確定", DialogInterface.OnClickListener { dialog, which ->

            arguments?.getString(SHEET_ID)?.let {
                viewModel.updateRuleCon(it,id,newCon, ed_sub.text.toString())
            }
            sheetDataAdapter.notifyDataSetChanged()
            dialog.dismiss()
        })?.setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })
        builder?.show()
    }

    override fun getRuleValue(title: Int, con: Int) : String{
        val sortedMap: MutableMap<String, ArrayList<String>> = LinkedHashMap()
        viewModel.Sheet.value?.mRule?.entries?.sortedBy { it.key }?.forEach { sortedMap[it.key] = it.value }
        return sortedMap.values.elementAt(title).get(con)
    }

    override fun getMoney(mC1: Int, mC2: Int, mC3: Int, mC4: Int, mC5: Int): String {
        val sortedMap: MutableMap<String, ArrayList<String>> = LinkedHashMap()
        viewModel.Sheet.value?.mRule?.entries?.sortedBy { it.key }?.forEach { sortedMap[it.key] = it.value }
        return (sortedMap.values.elementAt(0).get(mC1+1).toInt()+
                sortedMap.values.elementAt(1).get(mC2+1).toInt()+
                sortedMap.values.elementAt(2).get(mC3+1).toInt()+
                sortedMap.values.elementAt(3).get(mC4+1).toInt()+
                sortedMap.values.elementAt(4).get(mC5+1).toInt()).toString()
    }

    override fun onScrollChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int) {
        viewModel.dataPos.value = p1
    }
}