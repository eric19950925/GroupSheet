package com.eric.groupsheet.Widget.SheetOV

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.eric.groupsheet.MainActivity
import com.eric.groupsheet.MainHome.SheetClass
import com.eric.groupsheet.R
import com.eric.groupsheet.Widget.GSVerse.getPendingSelfIntent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


/**
 * Implementation of App Widget functionality.
 */
class SheetOverViewWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them (Auto)
        // Update other account's widgets (Not logged in)(x)
        // Update all widgets (o)
        // Add widget by activity still walk through here.
        for(id in appWidgetIds){
            Log.d("TAG_W",id.toString())
            updateAppWidget(context, appWidgetManager, id)

        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        // Clear SharePreferences data
        super.onDeleted(context, appWidgetIds)
        val prefxxxs = (context?:return).getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val userId = prefxxxs?.getString("userId", "000").toString()
        val mSheetIdList = MutableLiveData<List<String>>()
        val SheetListRef = FirebaseDatabase.getInstance().getReference("userData").child(userId).child("sheetList")
        SheetListRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                val SheetIdList : ArrayList<String> = ArrayList<String>()
                for(p in p0.children){
                    val SheetListData = p.getValue(SheetClass::class.java)
                    SheetListData?.let {
                        SheetIdList.add(it.memberId)
                    }
                }
                mSheetIdList.value = SheetIdList
            }
        })

        var widFromWhichSheet = "000"

        mSheetIdList.observeForever {
            if(it != null ) {
                it.forEach {
                    val sov_id_file =
                        context.getSharedPreferences("SOV_ID_FILE_${it}", Context.MODE_PRIVATE)
                    val sov_wdigetIds = sov_id_file.all.values
                    if (sov_wdigetIds.contains(appWidgetIds?.get(0))) {
                        widFromWhichSheet = it
                    }
                }

                val sov_id_file = context.getSharedPreferences(
                    "SOV_ID_FILE_${widFromWhichSheet}",
                    Context.MODE_PRIVATE
                )
                val sov_edit = sov_id_file?.edit()
                sov_edit?.remove("${appWidgetIds?.get(0)}")
                sov_edit?.remove("${appWidgetIds?.get(0)}_sov_name")
                sov_edit?.remove("${appWidgetIds?.get(0)}_sov_total_money")
                sov_edit?.apply()

            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Toast.makeText(context, "onReceive", Toast.LENGTH_SHORT).show()
        if (ShortCutOnClick.equals(intent?.getAction())){
            Toast.makeText(context, ShortCutOnClick, Toast.LENGTH_SHORT).show()
            val intent_main = Intent(context, MainActivity::class.java)
            intent_main.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(intent_main)
        }
    }
}
const val ShortCutOnClick = "myShortCut"
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
//    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.sheet_over_view_widget)


    val prefxxxs = context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
    val userId = prefxxxs?.getString("userId", "000").toString()


    val prefs = context.getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE)
    //need internet to get sheetList
    val mSheetIdList = MutableLiveData<List<String>>()
    val SheetListRef = FirebaseDatabase.getInstance().getReference("userData").child(userId).child("sheetList")
    SheetListRef.addValueEventListener(object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {}

        override fun onDataChange(p0: DataSnapshot) {
            val SheetIdList : ArrayList<String> = ArrayList<String>()
            for(p in p0.children){
                val SheetListData = p.getValue(SheetClass::class.java)
                SheetListData?.let {
                    SheetIdList.add(it.memberId)
                }
            }
            mSheetIdList.value = SheetIdList
        }
    })

    var widFromWhichSheet = "000"

    mSheetIdList.observeForever {
        if(it != null ){
            it.forEach {
                val sov_id_file = context.getSharedPreferences("SOV_ID_FILE_${it}", Context.MODE_PRIVATE)
                val sov_wdigetIds = sov_id_file.all.values
                Log.d("TAG_W","sp : ${sov_wdigetIds}")
                if(sov_wdigetIds.contains(appWidgetId)){
                    widFromWhichSheet = it
                }
            }

            val sov_id_file = context.getSharedPreferences("SOV_ID_FILE_${widFromWhichSheet}", Context.MODE_PRIVATE)
            val widgetText = sov_id_file?.getString("${appWidgetId}_sov_name", "000").toString()
            val TotalMoney = sov_id_file?.getString("${appWidgetId}_sov_total_money", "000").toString()

            //check log in account and update widget
            val LogInStatus =
                if(it.contains(widFromWhichSheet)
                    && prefs?.getBoolean("LogInStatus", false)?:false){true}
                else false

            views.setTextViewText(R.id.appwidget_text_sov, if(LogInStatus)widgetText+" " else "Not logged in")
            views.setTextViewText(R.id.appwidget_TotalMoney_text, if(LogInStatus)TotalMoney else "Not logged in")
            views.setOnClickPendingIntent(
                R.id.appwidget_text_sov,
                getPendingSelfIntent(
                    context,
                    ShortCutOnClick // Does not work...
                )
            )
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
        Log.d("TAG_W","==============================================================")
    }

}

