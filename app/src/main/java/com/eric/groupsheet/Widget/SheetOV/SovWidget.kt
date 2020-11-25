package com.eric.groupsheet.Widget.SheetOV

import android.appwidget.AppWidgetManager
import android.content.Context
import android.util.Log

open class SovWidget {
    fun updateWidgetBySheetFile(context : Context , sheetId : String){
        val sov_file = context.getSharedPreferences("SOV_ID_FILE_${sheetId}", Context.MODE_PRIVATE)
        val appWidgetManager: AppWidgetManager = AppWidgetManager.getInstance(context)
        val sov_wdigetIds = sov_file.all.values
        sov_wdigetIds.forEach {
            if(it is Int){
                Log.d("TAG","widgetId = ${it}")
                updateAppWidget(context, appWidgetManager, it)
            }
        }
    }
    fun showSheetFileValues(context : Context , sheetId : String){
        val sov_file = context.getSharedPreferences("SOV_ID_FILE_${sheetId}", Context.MODE_PRIVATE)
        val sov_wdigetIds = sov_file.all.values
        sov_wdigetIds.forEach {
//            val widgetId = sov_file?.getInt(it, 0)

            if(it is Int){
                Log.d("TAG","widgetId = ${it}")
            }else Log.d("TAG","widget data = ${it}")
        }
    }
    fun updateWidgetDataBySheetFile(context : Context , sheetId : String , newData : String){
        val appWidgetManager: AppWidgetManager = AppWidgetManager.getInstance(context)
        val sov_file = context.getSharedPreferences("SOV_ID_FILE_${sheetId}", Context.MODE_PRIVATE)
        val sov_file_edit = context.getSharedPreferences("SOV_ID_FILE_${sheetId}", Context.MODE_PRIVATE).edit()
        val sov_wdigetIds = sov_file.all.values
        val mWidgetIdList : ArrayList<Int> = ArrayList<Int>()
        sov_wdigetIds.forEach {
            if(it is Int){
                mWidgetIdList.add(it)
            }
        }
        mWidgetIdList.forEach {
            sov_file_edit?.putString("${it}_sov_name",newData)
            sov_file_edit?.apply()
        }
        sov_wdigetIds.forEach {
            if(it is Int){
                Log.d("TAG","widgetId = ${it}")
                updateAppWidget(context, appWidgetManager, it)
            }
        }
    }
}