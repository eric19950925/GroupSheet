package com.eric.groupsheet.NameList

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class NameListViewModel(/*private var toMainHomePage:()->Unit*/):ViewModel() {
    var NameListRef = FirebaseDatabase.getInstance().getReference("userData")
    var NameList = MutableLiveData<List<MemberClass>>()
    var Tutorial_addNameList = MutableLiveData<Int>()
    fun getOneNameData(id : String):MemberClass{
        val mMember =
            NameList.value?.find {memberClass: MemberClass -> id.equals(memberClass.memberId) }
        return mMember!!
    }
    fun getRightRef(userId:String){
        NameListRef = FirebaseDatabase.getInstance().getReference("userData").child(userId).child("nameList").child("List1")
    }
    fun addMember(mMember: MemberClass,userId:String) {
        getRightRef(userId)
        val pushMemberRef = NameListRef.push()
        pushMemberRef.setValue(mMember)
    }

    fun reloadNameList(userId:String) {
        getRightRef(userId)
        NameListRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val NewNameList : ArrayList<MemberClass> = ArrayList<MemberClass>()
                for(p in p0.children){
                    val NameListData = p.getValue(MemberClass::class.java)
                    NameListData?.let { NewNameList.add(it) }
                }
                NameList.value = NewNameList
            }


        })
    }

    fun DeletMember(id: String,userId:String) {
        getRightRef(userId)
        val mQuery = NameListRef.orderByChild("memberId").equalTo(id)
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
        reloadNameList(userId)
    }

    fun updateMember(id: String, mMember: MemberClass,userId:String) {
        getRightRef(userId)
        val mQuery = NameListRef.orderByChild("memberId").equalTo(id)
        mQuery.addListenerForSingleValueEvent(
            object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (memberSnapshot in p0.getChildren()) {
                        memberSnapshot.ref.setValue(mMember)
                    }
                }

            }
        )
        reloadNameList(userId)
    }
}