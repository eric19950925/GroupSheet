package com.eric.groupsheet.Life

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eric.groupsheet.MainHome.SheetClass

class LifeViewModel (
    private var toKey1Page:()->Unit,
    private var toLifePage:()->Unit,
    private var toMainHome:()->Unit
): ViewModel() {
    var KeyNumber = MutableLiveData<Int>()
    fun toKey1(){
        toKey1Page()
    }
    fun toLife(){
        toLifePage()
    }
    fun toHome(){
        toMainHome()
    }
}