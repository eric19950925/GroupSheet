package com.eric.groupsheet.Widget.SheetOV

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.eric.groupsheet.EditSheet.EditRule.RuleClass
import com.eric.groupsheet.MainHome.SheetClass
import com.eric.groupsheet.R
import com.eric.groupsheet.base.listenClick
import com.eric.groupsheet.base.observe
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.sheetover_widget_configure.*
import java.util.ArrayList

class SheetOverConfigureActivity :  AppCompatActivity() , SheetOVController{
    private val sheetOVAdapter = SheetOverViewAdapter(this)
    val SheetList = MutableLiveData<List<SheetClass>>()
    lateinit var targetSheetItem : SheetClass
    var RuleClassList = MutableLiveData<List<RuleClass>>()
    var AmountList = MutableLiveData<List<List<Int>>>()
    var MemberAmount = 0
    var TotalMoney = MutableLiveData<Int>()
    var SubMoney = 0
    var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(Activity.RESULT_CANCELED)
        setContentView(R.layout.sheetover_widget_configure)


        val appWidgetManager: AppWidgetManager = AppWidgetManager.getInstance(this)
        val prefxxxs = this.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val userId = prefxxxs?.getString("userId", "000").toString()
        val mSheetIdList = MutableLiveData<List<String>>()
        val SheetListRef = FirebaseDatabase.getInstance().getReference("userData").child(userId).child("sheetList")
        SheetListRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                val NewSheetList : ArrayList<SheetClass> = ArrayList<SheetClass>()
                val SheetIdList : ArrayList<String> = ArrayList<String>()
                for(p in p0.children){
                    val SheetListData = p.getValue(SheetClass::class.java)
                    SheetListData?.let {
                        NewSheetList.add(it)
                        SheetIdList.add(it.memberId)
                    }
                }
                SheetList.value = NewSheetList
                mSheetIdList.value = SheetIdList
            }
        })



        rv_sov.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_sov.layoutManager = mLayoutManager
        rv_sov.adapter = sheetOVAdapter

        SheetList.observeForever {
            if(!it.isNullOrEmpty()){
                tv_empty_sheetList.visibility = View.GONE
                sheetOVAdapter.submitList(it.toMutableList())
            }
        }
        observe(mSheetIdList){
            //should not add here , should add when log in
            if(!it.isNullOrEmpty()){
                it.forEachIndexed { index, s ->
                    Log.d("TAG",s)
                    val prefs = this.getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE).edit()
                    prefs.putString("sheetId${index}", s)
                    prefs.apply()
                }
            }
        }


        btn_create.listenClick {
            if(targetSheetItem == null){
                return@listenClick
            }
            val intent = intent
            val extras = intent.extras
            if (extras != null) {
                appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
                )
            }
            val action = intent.action

            //store widget in Sharepreferences with widgetId as key.
            val sov_id_file = this.getSharedPreferences("SOV_ID_FILE_${targetSheetItem.memberId}", Context.MODE_PRIVATE).edit()
            sov_id_file.putInt(appWidgetId.toString(), appWidgetId)
            sov_id_file.putString("${appWidgetId}_sov_name", targetSheetItem.mName)
            sov_id_file.putString("${appWidgetId}_sov_total_money", "總額: ${TotalMoney.value}元")
//        prefs.putString("device_sceneId", it.first().sceneId)
//        prefs.putBoolean("device_active", it.first().active)
            sov_id_file.apply()
            updateAppWidget(this, appWidgetManager, appWidgetId)
            // If this activity was started with an intent without an app widget ID, finish with an error.
            if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish()
                return@listenClick
            }

            val resultValue = Intent()
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            setResult(Activity.RESULT_OK, resultValue)
            finish()
        }
    }

    override fun storeTargetSheet(targetSheet: SheetClass) {
        SheetList.value?.forEach {
            //change check status
            if(it.equals(targetSheet)){
                it.mCheck = true
                targetSheetItem = it
                Log.d("TAG",targetSheetItem.mName)
            }
            else it.mCheck = false
        }
        sheetOVAdapter.notifyDataSetChanged()

        //reset target data
        RuleClassList.value = null
        AmountList.value = null
        TotalMoney.value = null
        MemberAmount = 0
        SubMoney = 0

        toRuleList(targetSheetItem)

    }
    private fun toRuleList(list: SheetClass) {
        val mRuleList : ArrayList<RuleClass> = ArrayList<RuleClass>()
        val sortedMap: MutableMap<String, ArrayList<String>> = LinkedHashMap()
        list.mRule.entries.sortedBy { it.key }.forEach { sortedMap[it.key] = it.value }

        for(i in sortedMap){
            val mRule = RuleClass()
            mRule.mRuleId = i.key
            mRule.mRuleName = i.value.elementAt(10).toString()
            mRule.mOption1Name  = i.value.elementAt(0).toString()
            mRule.mOption1Value = i.value.elementAt(1).toString()
            mRule.mOption2Name  = i.value.elementAt(2).toString()
            mRule.mOption2Value = i.value.elementAt(3).toString()
            mRule.mOption3Name  = i.value.elementAt(4).toString()
            mRule.mOption3Value = i.value.elementAt(5).toString()
            mRule.mOption4Name  = i.value.elementAt(6).toString()
            mRule.mOption4Value = i.value.elementAt(7).toString()
            mRule.mOption5Name  = i.value.elementAt(8).toString()
            mRule.mOption5Value = i.value.elementAt(9).toString()
            mRuleList.add(mRule)
        }
        RuleClassList.value = mRuleList
//        Toast.makeText(this,"RuleList OK",Toast.LENGTH_LONG).show()
        CountInfo(targetSheetItem)
    }

    private fun CountInfo(list: SheetClass) {
        var amount_11 = 0
        var amount_12 = 0
        var amount_13 = 0
        var amount_14 = 0
        var amount_15 = 0
        var amount_21 = 0
        var amount_22 = 0
        var amount_23 = 0
        var amount_24 = 0
        var amount_25 = 0
        var amount_31 = 0
        var amount_32 = 0
        var amount_33 = 0
        var amount_34 = 0
        var amount_35 = 0
        var amount_41 = 0
        var amount_42 = 0
        var amount_43 = 0
        var amount_44 = 0
        var amount_45 = 0
        var amount_51 = 0
        var amount_52 = 0
        var amount_53 = 0
        var amount_54 = 0
        var amount_55 = 0

        list.mSheetData.forEach {
            MemberAmount++
            SubMoney+=it.submitted
            when(it.mC1){
                0->amount_11++
                2->amount_12++
                4->amount_13++
                6->amount_14++
                8->amount_15++
            }
            when(it.mC2){
                0->amount_21++
                2->amount_22++
                4->amount_23++
                6->amount_24++
                8->amount_25++
            }
            when(it.mC3){
                0->amount_31++
                2->amount_32++
                4->amount_33++
                6->amount_34++
                8->amount_35++
            }
            when(it.mC4){
                0->amount_41++
                2->amount_42++
                4->amount_43++
                6->amount_44++
                8->amount_45++
            }
            when(it.mC5){
                0->amount_51++
                2->amount_52++
                4->amount_53++
                6->amount_54++
                8->amount_55++
            }

        }

        val amount1 = arrayListOf(amount_11,amount_12,amount_13,amount_14,amount_15)
        val amount2 = arrayListOf(amount_21,amount_22,amount_23,amount_24,amount_25)
        val amount3 = arrayListOf(amount_31,amount_32,amount_33,amount_34,amount_35)
        val amount4 = arrayListOf(amount_41,amount_42,amount_43,amount_44,amount_45)
        val amount5 = arrayListOf(amount_51,amount_52,amount_53,amount_54,amount_55)
        AmountList.value = arrayListOf(amount1,amount2,amount3,amount4,amount5)
//        Toast.makeText(this,"AmountList OK",Toast.LENGTH_LONG).show()
        countTotalMoney(AmountList.value,RuleClassList.value?:return)

    }


    private fun countTotalMoney(
        list: List<List<Int>>?,
        ruleClassList: List<RuleClass>
    ) {
        var RuleValueList =  mutableListOf(0,0,0,0,0)
        if (list != null){
            for(i in 0..4){
                val v1 = ruleClassList.get(i).mOption1Value.toInt()?:0
                val v2 = ruleClassList.get(i).mOption2Value.toInt()?:0
                val v3 = ruleClassList.get(i).mOption3Value.toInt()?:0
                val v4 = ruleClassList.get(i).mOption4Value.toInt()?:0
                val v5 = ruleClassList.get(i).mOption5Value.toInt()?:0
                val rv1 = list.get(i).get(0).times(v1)
                val rv2 = list.get(i).get(1).times(v2)
                val rv3 = list.get(i).get(2).times(v3)
                val rv4 = list.get(i).get(3).times(v4)
                val rv5 = list.get(i).get(4).times(v5)
                RuleValueList[i] = rv1 + rv2 + rv3 + rv4 + rv5
                Log.d("TAG","RuleValueList[${i}] = "+RuleValueList[i])

            }

        }

        TotalMoney.value = RuleValueList[0]+RuleValueList[1]+RuleValueList[2]+RuleValueList[3]+RuleValueList[4]
        Toast.makeText(this,"總額: ${TotalMoney.value}",Toast.LENGTH_SHORT).show()
        Log.d("TAG",TotalMoney.value.toString())

    }
}