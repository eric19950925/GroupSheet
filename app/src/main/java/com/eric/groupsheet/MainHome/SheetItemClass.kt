package com.eric.groupsheet.MainHome

class SheetItemClass (
    id : String = "",
    who : String = "",
    type : Int = 0,
    c1 : Int = 0,
    c2 : Int = 0,
    c3 : Int = 0,
    c4 : Int = 0,
    c5 : Int = 0,
    submitMoney : Int = 0,
    history : String = ""
) {
    var memberId : String = ""
    var mName : String = ""
    var mType : Int = 0
    var mC1 : Int = 0
    var mC2 : Int = 0
    var mC3 : Int = 0
    var mC4 : Int = 0
    var mC5 : Int = 0
    var submitted : Int = 0
    var history : String = ""


    init {
        this.mName = who
        this.mType = type
        this.memberId = id
        this.mC1 = c1
        this.mC2 = c2
        this.mC3 = c3
        this.mC4 = c4
        this.mC5 = c5
        this.submitted = submitMoney
        this.history = history
    }

    fun isItemSame(other: SheetItemClass): Boolean {
        return memberId == other.memberId
    }

    fun isContentSameTo(other: SheetItemClass): Boolean {
        return mName == other.mName
    }
}
