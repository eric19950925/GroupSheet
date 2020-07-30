package com.eric.groupsheet.NameList

class MemberClass(id: String = "", who: String = "", type: Int = 0) {//如何建立類別kotlin
    var memberId : String = ""
    var mName : String = ""
    var mType : Int = 0

    init {
        this.mName = who
        this.mType = type
        this.memberId = id
    }

    fun isItemSame(other: MemberClass): Boolean {
        return memberId == other.memberId
    }

    fun isContentSameTo(other: MemberClass): Boolean {
        return mName == other.mName
    }
}
