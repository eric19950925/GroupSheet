package com.eric.groupsheet

class AccountClass(
    name: String = "",
    email: String = "",
    url: String = "",
    id: String = "",
    newsVersion: Int
) {//如何建立類別kotlin
    var userName : String = ""
    var userEmail : String = ""
    var userPhotoUrl : String = ""
    var userID : String = ""
    var newsVersion : Int = 0

    init {
        this.userName = name
        this.userEmail = email
        this.userPhotoUrl = url
        this.userID = id
        this.newsVersion = newsVersion
    }

    fun isItemSame(other: AccountClass): Boolean {
        return userID == other.userID
    }

    fun isContentSameTo(other: AccountClass): Boolean {
        return userName == other.userName
    }
}