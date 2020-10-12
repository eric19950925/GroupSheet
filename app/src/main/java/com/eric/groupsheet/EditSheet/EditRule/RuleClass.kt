package com.eric.groupsheet.EditSheet.EditRule

class RuleClass (
    RuleName: String = "",
    RuleId: String = "",
    Option1Name: String = "",
    Option1Value: String = "",
    Option2Name: String = "",
    Option2Value: String = "",
    Option3Name: String = "",
    Option3Value: String = "",
    Option4Name: String = "",
    Option4Value: String = "",
    Option5Name: String = "",
    Option5Value: String = "")
{//如何建立類別kotlin
    var mRuleName: String = ""
    var mRuleId: String = ""
    var mOption1Name: String = ""
    var mOption1Value: String = ""
    var mOption2Name: String = ""
    var mOption2Value: String = ""
    var mOption3Name: String = ""
    var mOption3Value: String = ""
    var mOption4Name: String = ""
    var mOption4Value: String = ""
    var mOption5Name: String = ""
    var mOption5Value: String = ""

    init {
        this.mRuleName = RuleName
        this.mRuleId = RuleId
        this.mOption1Name = Option1Name
        this.mOption1Value = Option1Value
        this.mOption2Name = Option2Name
        this.mOption2Value = Option2Value
        this.mOption3Name = Option3Name
        this.mOption3Value = Option3Value
        this.mOption4Name = Option4Name
        this.mOption4Value = Option4Value
        this.mOption5Name = Option5Name
        this.mOption5Value = Option5Value
    }

    fun isItemSame(other: RuleClass): Boolean {
        return mRuleName == other.mRuleName
    }

    fun isContentSameTo(other: RuleClass): Boolean {
        return mRuleName == other.mRuleName
    }
}