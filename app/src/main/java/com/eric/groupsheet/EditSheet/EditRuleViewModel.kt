package com.eric.groupsheet.EditSheet

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eric.groupsheet.MainHome.SheetClass
import com.eric.groupsheet.MainHome.SheetItemClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class EditRuleViewModel:ViewModel() {
    var SheetListRef = FirebaseDatabase.getInstance().getReference("userData")
    var Sheet = MutableLiveData<SheetClass>()
    var dataPos = MutableLiveData<Int>()
    var SheetData = MutableLiveData<List<SheetItemClass>>()
    var RuleClassList = MutableLiveData<List<RuleClass>>()
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
    fun getOneRuleItemData(mRuleName:String):RuleClass{
        val mRuleItem =
            RuleClassList.value?.find {RuleClass: RuleClass -> mRuleName.equals(RuleClass.mRuleId) }
        return mRuleItem!!
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
    }

    fun updateRuleListAfterEdit(
        sheetId: String,
        ruleId: String,
        mRule: ArrayList<String>
    ) {
        Log.d("TAG","to edit")
        val mQuery = SheetListRef.orderByChild("memberId").equalTo(sheetId)

        mQuery.addListenerForSingleValueEvent(
            object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (mRuleSnapshot in p0.getChildren()) {
//                        Log.d("TAG",mRuleSnapshot.toString())
                        mRuleSnapshot.ref.child("mrule").child(ruleId).setValue(mRule)
                    }
                    reLoadRule(sheetId)
                }

            }
        )

    }

    private fun reLoadRule(sheetId: String) {
        val mQuery = SheetListRef.orderByChild("memberId").equalTo(sheetId)
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

}