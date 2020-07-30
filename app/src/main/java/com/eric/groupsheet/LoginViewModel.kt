package com.eric.groupsheet

import androidx.lifecycle.ViewModel

class LoginViewModel(private var toMainHomePage:()->Unit):ViewModel() {
    fun toMainHome(){
        toMainHomePage()
    }
}