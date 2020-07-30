package com.eric.groupsheet.MainHome

class SheetListClass(
    id: String = "",
    which: String = "",
    type: Int = 0,
    rule1:RuleClass,
    rule2:RuleClass,
    rule3:RuleClass,
    rule4:RuleClass,
    rule5:RuleClass,
    total : Int = 0,
    submitted : Int = 0 ,
    leftToPay : Int = 0
) {//如何建立類別kotlin
    var SheetId : String = ""
    var mSheetName : String = ""
    var mType : Int = 0
    var editDate : String = ""
    lateinit var rule1 : RuleClass
    lateinit var rule2 : RuleClass
    lateinit var rule3 : RuleClass
    lateinit var rule4 : RuleClass
    lateinit var rule5 : RuleClass
    var total : Int = 0
    var submitted : Int = 0
    var leftToPay : Int = 0
    lateinit var mNameListBigDataList : List<NameListBigData>
    init {
        this.mSheetName = which
        this.mType = type
        this.SheetId = id
        this.editDate = id
        this.SheetId = id
        this.SheetId = id
        this.SheetId = id
        this.SheetId = id
        this.SheetId = id
    }

    fun isItemSame(other: SheetListClass): Boolean {
        return SheetId == other.SheetId
    }

    fun isContentSameTo(other: SheetListClass): Boolean {
        return mSheetName == other.mSheetName
    }
}

class NameListBigData {
    var memberId : String = ""
    var mName : String = ""
    var mType : Int = 0
    var rule1 : Int = 0
    var rule2 : Int = 0
    var rule3 : Int = 0
    var rule4 : Int = 0
    var rule5 : Int = 0
    var hadSubmit : Int = 0
    var dataEditDate : String = ""
}

class RuleClass {
    lateinit var option1:OptionClass
    lateinit var option2:OptionClass
    lateinit var option3:OptionClass
    lateinit var option4:OptionClass
    lateinit var option5:OptionClass
}

class OptionClass {
    var optionName:String = ""
    var optionValue:Int = 0
    var optionEditDate : String = ""
}
