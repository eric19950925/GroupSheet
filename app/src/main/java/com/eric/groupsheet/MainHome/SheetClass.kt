package com.eric.groupsheet.MainHome

import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class SheetClass(
    id: String = "",
    listName: String = "",
    rule: HashMap<String, ArrayList<String>> = HashMap<String, ArrayList<String>>(),
    sheetData: List<SheetItemClass> = emptyList<SheetItemClass>(),
    editDate: String = "",
    type: Int = 0,
    history: String = "")
{//如何建立類別kotlin
    var mEditDate : String = ""
    var memberId : String = ""
    var mHistory : String = ""
    var mName : String = ""
    var mType : Int = 0
    var mSheetData : List<SheetItemClass> = emptyList<SheetItemClass>()
    var mRule : HashMap<String, ArrayList<String>> = HashMap<String, ArrayList<String>>()

    init {
        this.memberId = id
        this.mName = listName
        this.mRule = rule
        this.mSheetData = sheetData
        this.mEditDate = editDate
        this.mHistory = history
        this.mType = type
    }

    fun isItemSame(other: SheetClass): Boolean {
        return memberId == other.memberId && mSheetData == other.mSheetData && mType == other.mType
    }

    fun isContentSameTo(other: SheetClass): Boolean {
        return mName == other.mName
    }
}