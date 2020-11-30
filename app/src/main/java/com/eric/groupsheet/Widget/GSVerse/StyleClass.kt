package com.eric.groupsheet.Widget.GSVerse

class StyleClass (
        id: String = "",
        name: String = "",
        hint: String = "",
        imgId: Int = 0,
        select: Boolean = false
    ) {//如何建立類別kotlin
    var styleId : String = ""
    var styleName : String = ""
    var styleHint : String = ""
    var styleImgId : Int = 0
    var styleSelect : Boolean = false

    init {
        this.styleId = id
        this.styleName = name
        this.styleHint = hint
        this.styleImgId = imgId
        this.styleSelect = select

    }

    fun isItemSame(other: StyleClass): Boolean {
        return styleName == other.styleName
    }

    fun isContentSameTo(other: StyleClass): Boolean {
        return styleName == other.styleName
    }

}