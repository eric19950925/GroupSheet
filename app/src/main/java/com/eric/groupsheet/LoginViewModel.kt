package com.eric.groupsheet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eric.groupsheet.MainHome.SheetClass

class LoginViewModel(private var toMainHomePage:()->Unit):ViewModel() {
    var mLogInStatue = MutableLiveData<Boolean>()
    val SheetList = MutableLiveData<List<SheetClass>>()
    var mSheetIdList = MutableLiveData<List<String>>()
    fun toMainHome(){
        toMainHomePage()
    }
}