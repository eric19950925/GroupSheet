package com.eric.groupsheet.MainHome

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eric.groupsheet.NameList.MemberClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList
import java.util.HashMap

class MainHomeViewModel(
    private var toNameListPage:()->Unit,
    private var toAboutPage:()->Unit,
    private var toSettingPage:()->Unit,
    private var toNewSheetPage:()->Unit,
    private var toEditSheetPage:(id:String)->Unit,
    private var toAccountPage:()->Unit,
    private var toLifePage:()->Unit
    ):ViewModel() {
    var Answer = MutableLiveData<Int>()
    var Tutorial_toNameList = MutableLiveData<Int>()
    var Tutorial_addSheet = MutableLiveData<Int>()
    var UserId : MutableLiveData<String> = MutableLiveData<String>()
    var SheetListRef = FirebaseDatabase.getInstance().getReference("userData")
    var SheetList = MutableLiveData<List<SheetClass>>()
    var mEmptySheet = MutableLiveData<List<SheetItemClass>>()

    fun getRightRef(userId:String){
        SheetListRef = FirebaseDatabase.getInstance().getReference("userData").child(userId).child("sheetList")
    }

    fun toNameList(){
        toNameListPage()
    }
    fun toAbout(){
        toAboutPage()
    }
    fun toSetting(){
        toSettingPage()
    }
    fun toNewSheet(){
        toNewSheetPage()
    }
    fun toEditSheet(id:String){
        toEditSheetPage(id)
    }
    fun toAccount(){
        toAccountPage()
    }
    fun toLife(){
        toLifePage()
    }
    fun addList(mSheet: SheetClass,userId:String) {
        getRightRef(userId)
        val pushSheetRef = SheetListRef.push()
        pushSheetRef.setValue(mSheet)
        reloadSheetList(userId)
    }
    fun reloadSheetList(userId:String) {
        getRightRef(userId)
        SheetListRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val NewSheetList : ArrayList<SheetClass> = ArrayList<SheetClass>()
//                Log.d("TAG",p0.toString())
                for(p in p0.children){
                    val SheetListData = p.getValue(SheetClass::class.java)
                    SheetListData?.let { NewSheetList.add(it) }
                }
                SheetList.value = NewSheetList
            }


        })
    }
    fun DeletMember(id: String,userId:String) {
        getRightRef(userId)
        val mQuery = SheetListRef.orderByChild("memberId").equalTo(id)
        mQuery.addListenerForSingleValueEvent(
            object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (memberSnapshot in p0.getChildren()) {
                        memberSnapshot.ref.removeValue()
                    }
                }

            }
        )
        reloadSheetList(userId)
    }

    fun emptySheet(nameList: MutableLiveData<List<MemberClass>>,userId:String) {
        val NewEmptySheet : ArrayList<SheetItemClass> = ArrayList<SheetItemClass>()
        for(p in nameList.value!!){
            val EmptySheetData = SheetItemClass(
                id = p.memberId,
                who = p.mName,
                type = p.mType,
                c1 = 0,
                c2 = 0,
                c3 = 0,
                c4 = 0,
                c5 = 0
            )
            NewEmptySheet.add(EmptySheetData)
        }
        mEmptySheet.value = NewEmptySheet.toList()
    }
}