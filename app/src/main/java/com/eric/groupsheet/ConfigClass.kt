package com.eric.groupsheet

import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ConfigClass(
    version: String = "",
    url: String = "",
    updateVersion: Int = 0,
    verse: String = "",
    verseUrl: String = "",
    picUrl: String = "",
    picX: Int = 0,
    picY: Int = 0
    )
{//如何建立類別kotlin
    var mVersion: String = ""
    var mUrl: String = ""
    var mUpdateVersion: Int = 0
    var mVerse: String = ""
    var mVerseUrl: String = ""
    var mPicUrl: String = ""
    var mPicX: Int = 0
    var mPicY: Int = 0

    init {
        this.mVersion = version
        this.mUrl = url
        this.mUpdateVersion = updateVersion
        this.mVerse = verse
        this.mVerseUrl = verseUrl
        this.mPicUrl = picUrl
        this.mPicX = picX
        this.mPicY = picY
    }

//    fun isItemSame(other: ConfigClass): Boolean {
//        return memberId == other.memberId && mSheetData == other.mSheetData && mType == other.mType
//    }
//
//    fun isContentSameTo(other: ConfigClass): Boolean {
//        return mName == other.mName
//    }
}