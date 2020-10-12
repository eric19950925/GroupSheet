package com.eric.groupsheet.EditSheet.SheetAnalysis

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eric.groupsheet.EditSheet.EditRule.RuleClass
import com.eric.groupsheet.MainHome.SheetClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class SheetAnalysisViewModel:ViewModel() {
    var SheetListRef = FirebaseDatabase.getInstance().getReference("userData")
    var Sheet = MutableLiveData<SheetClass>()
    var RuleClassList = MutableLiveData<List<RuleClass>>()
    var AmountList = MutableLiveData<List<List<Int>>>()
    var MemberAmount = 0
    var TotalMoney = MutableLiveData<Int>()
    var SubMoney = 0
    fun getRightRef(userId:String){
        SheetListRef = FirebaseDatabase.getInstance().getReference("userData").child(userId).child("sheetList")
    }
    fun getSheetData(id:String){
        val mQuery = SheetListRef.orderByChild("memberId").equalTo(id)
        mQuery.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (memberSnapshot in p0.getChildren()) {
                        Sheet.value = memberSnapshot.getValue(SheetClass::class.java)
                    }

                }

            }
        )
    }
    fun toRuleList(sheetClass: SheetClass?) {
        var mRuleList : ArrayList<RuleClass> = ArrayList<RuleClass>()
        val sortedMap: MutableMap<String, ArrayList<String>> = LinkedHashMap()
        sheetClass?.mRule?.entries?.sortedBy { it.key }?.forEach { sortedMap[it.key] = it.value }

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

        CountInfo(Sheet)


    }


    private fun CountInfo(list: MutableLiveData<SheetClass>) {
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

        list.value?.mSheetData?.forEach {
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



    }

    fun countTotalMoney(
        list: List<List<Int>>,
        ruleClassList: MutableLiveData<List<RuleClass>>
    ) {
        var RuleValueList =  mutableListOf(0,0,0,0,0)
        for(i in 0..4){
            val v1 = ruleClassList.value?.get(i)?.mOption1Value?.toInt()?:0
            val v2 = ruleClassList.value?.get(i)?.mOption2Value?.toInt()?:0
            val v3 = ruleClassList.value?.get(i)?.mOption3Value?.toInt()?:0
            val v4 = ruleClassList.value?.get(i)?.mOption4Value?.toInt()?:0
            val v5 = ruleClassList.value?.get(i)?.mOption5Value?.toInt()?:0
            val rv1 = list.get(i).get(0).times(v1)
            val rv2 = list.get(i).get(1).times(v2)
            val rv3 = list.get(i).get(2).times(v3)
            val rv4 = list.get(i).get(3).times(v4)
            val rv5 = list.get(i).get(4).times(v5)
            RuleValueList[i] = rv1 + rv2 + rv3 + rv4 + rv5
            Log.d("TAG","RuleValueList[i] = "+RuleValueList[i])

        }

        TotalMoney.value = RuleValueList[0]+RuleValueList[1]+RuleValueList[2]+RuleValueList[3]+RuleValueList[4]


    }
    var pbNum = MutableLiveData<Int>()
    var countdownDisposable: Disposable? = null
    fun startRunPb() {
        if (countdownDisposable != null) {
            return
        }
        pbNum.value = 0
        countdownDisposable = Flowable.interval(10, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                pbNum.value = pbNum.value!! + 1
            }, {

            })
    }

    fun StopRunPb() {
        if (!countdownDisposable?.isDisposed!!) {
            countdownDisposable?.dispose()
            countdownDisposable = null
        }
    }

}

