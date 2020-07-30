package com.eric.groupsheet.MainHome

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eric.groupsheet.AccountClass

class SharedAccountViewModel:ViewModel() {
    var userAccount = MutableLiveData<AccountClass>()
}