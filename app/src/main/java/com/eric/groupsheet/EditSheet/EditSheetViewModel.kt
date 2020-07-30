package com.eric.groupsheet.EditSheet

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eric.groupsheet.MainHome.SheetClass
import com.eric.groupsheet.MainHome.SheetItemClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditSheetViewModel(
    private var toEditRulePage:(id:String)->Unit,
    private var toAnalysisPage:(id:String)->Unit
):ViewModel() {
    var UserId = "user001"
    var SheetListRef = FirebaseDatabase.getInstance().getReference("userData").child(UserId).child("sheetList")
    var Sheet = MutableLiveData<SheetClass>()
    var RuleTitle = MutableLiveData<ArrayList<String>>()
    var dataPos = MutableLiveData<Int>()
    var dataPosY = MutableLiveData<Int>()
    var SheetData = MutableLiveData<List<SheetItemClass>>()
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
    fun getOneSheetItemData(id:String):SheetItemClass{
        val mSheetItem =
            SheetData.value?.find {sheetItemClass: SheetItemClass -> id.equals(sheetItemClass.memberId) }
        return mSheetItem!!
    }
    fun toEditRule(id:String){
        toEditRulePage(id)
    }

    fun toAnalysis(id:String){
        toAnalysisPage(id)
    }

    fun getRuleTitle(sheetData:SheetClass){
        val mRuleTitle : ArrayList<String> = ArrayList<String>()
        val sortedMap: MutableMap<String, java.util.ArrayList<String>> = LinkedHashMap()
        sheetData.mRule.entries.sortedBy { it.key }.forEach { sortedMap[it.key] = it.value }

        for(i in sortedMap){
            mRuleTitle.add(i.value.elementAt(10).toString())
        }
        RuleTitle.value = mRuleTitle
    }

    fun updateRuleCon(
        sheetId: String,
        id: String,
        newCon: MutableList<Int>,
        text: String
    ) {
        val mQuery = SheetListRef.orderByChild("memberId").equalTo(sheetId)

        mQuery.addListenerForSingleValueEvent(
            object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }
                override fun onDataChange(p0: DataSnapshot) {
                    for (mRuleSnapshot in p0.getChildren()) {
                        val memberCon = mRuleSnapshot.ref.child("msheetData").orderByChild("memberId").equalTo(id)
                        memberCon.addListenerForSingleValueEvent(
                            object : ValueEventListener{
                                override fun onCancelled(error: DatabaseError) {
                                }
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (mRuleItem in snapshot.getChildren()) {
                                        mRuleItem.ref.child("mc1").setValue(newCon[0])
                                        mRuleItem.ref.child("mc2").setValue(newCon[1])
                                        mRuleItem.ref.child("mc3").setValue(newCon[2])
                                        mRuleItem.ref.child("mc4").setValue(newCon[3])
                                        mRuleItem.ref.child("mc5").setValue(newCon[4])
                                        mRuleItem.ref.child("submitted").setValue(text.toInt())
                                    }
                                }
                            })

                    }
                }
            }
        )
        SheetData.value?.filter { it.memberId == id }?.first()?.mC1 = newCon[0]
        SheetData.value?.filter { it.memberId == id }?.first()?.mC2 = newCon[1]
        SheetData.value?.filter { it.memberId == id }?.first()?.mC3 = newCon[2]
        SheetData.value?.filter { it.memberId == id }?.first()?.mC4 = newCon[3]
        SheetData.value?.filter { it.memberId == id }?.first()?.mC5 = newCon[4]
        SheetData.value?.filter { it.memberId == id }?.first()?.submitted = text.toInt()
        //先改暫存資料，因為雲端資料更新太慢，無法即時由雲端抓資料來更新
    }

    fun updateSheetName(sheetId: String, newName: String) {
        val mQuery = SheetListRef.orderByChild("memberId").equalTo(sheetId)
        mQuery.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (memberSnapshot in p0.getChildren()) {
                        memberSnapshot.ref.child("mname").setValue(newName)
                    }
                }

            }
        )
        Sheet.value?.mName = newName
    }
}