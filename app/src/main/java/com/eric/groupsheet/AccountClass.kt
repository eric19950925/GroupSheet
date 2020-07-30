package com.eric.groupsheet

class AccountClass (name: String = "", email: String = "", url: String = "", id: String = "") {//如何建立類別kotlin
    var userName : String = ""
    var userEmail : String = ""
    var userPhotoUrl : String = ""
    var userID : String = ""

    init {
        this.userName = name
        this.userEmail = email
        this.userPhotoUrl = url
        this.userID = id
    }

    fun isItemSame(other: AccountClass): Boolean {
        return userID == other.userID
    }

    fun isContentSameTo(other: AccountClass): Boolean {
        return userName == other.userName
    }
}