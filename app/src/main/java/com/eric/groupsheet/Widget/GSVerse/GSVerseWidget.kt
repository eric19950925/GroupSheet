package com.eric.groupsheet.Widget.GSVerse

import android.app.PendingIntent
import android.app.Service
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.*
import android.net.Uri
import android.os.*
import android.widget.RemoteViews
import android.widget.Toast
import com.eric.groupsheet.MainActivity
import com.eric.groupsheet.R
import com.eric.groupsheet.Widget.SheetOV.ShortCutOnClick
import com.eric.groupsheet.Widget.SmallSov.SmallOff
import com.eric.groupsheet.Widget.SmallSov.SmallOn
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.fixedRateTimer


/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [GSVerseWidgetConfigureActivity]
 */
class GSVerseWidget : AppWidgetProvider() {
//    var m_timeChangedReceiver: BroadcastReceiver = Synchronize_Time()
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget_regular(context, appWidgetManager, appWidgetId)
        }
}

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
    }

    override fun onEnabled(context: Context) {

    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (MyOnClick.equals(intent?.getAction())){
//            val intent_main = Intent(context, MainActivity::class.java)
//            intent_main.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            context?.startActivity(intent_main)

            val sharedPreference =  context?.getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
            val versionUrl = sharedPreference?.getString("VerseUrl","http://philabnb.com/").toString()
            val openUrl = Intent(Intent.ACTION_VIEW)
            openUrl.data = Uri.parse(versionUrl)
            openUrl.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(openUrl)

        }
        if (ShortCutOnClick.equals(intent?.getAction())){
            Toast.makeText(context, ShortCutOnClick, Toast.LENGTH_SHORT).show()
            val intent_main = Intent(context, MainActivity::class.java)
            intent_main.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(intent_main)
        }
        if (SmallOn.equals(intent?.getAction())){
            Toast.makeText(context, "Click On", Toast.LENGTH_SHORT).show()

        }
        if (SmallOff.equals(intent?.getAction())){
            Toast.makeText(context, "Click Off", Toast.LENGTH_SHORT).show()

        }
    }
}

const val MyOnClick = "myOnClickTag"

internal fun updateAppWidget_regular(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {

    // Construct the RemoteViews object
    val sharedPreference =  context.getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
    val version = sharedPreference?.getString("Verse","Ooops,something wrong...").toString()

    for(i in 1..7){
        val styleList = context.getSharedPreferences("GSV_style${i}_FILE", Context.MODE_PRIVATE)
        val widgetIdList = styleList.all.values
        if(widgetIdList.contains(appWidgetId)){
            var views : RemoteViews
            when(i){
                 1 -> views = RemoteViews(context.packageName, R.layout.g_s_verse_widget)
                 2 -> views = RemoteViews(context.packageName, R.layout.g_s_verse_widget_trans)
                 3 -> views = RemoteViews(context.packageName, R.layout.g_s_verse_widget_jf)
                 4 -> views = RemoteViews(context.packageName, R.layout.g_s_verse_widget_sample)
                 5 -> views = RemoteViews(context.packageName, R.layout.g_s_verse_widget_2049)
                 6 -> views = RemoteViews(context.packageName, R.layout.g_s_verse_widget_gatsby)
                 else -> views = RemoteViews(context.packageName, R.layout.g_s_verse_widget_sun)
            }

            views.setTextViewText(R.id.appwidget_text, version)

            views.setOnClickPendingIntent(
                R.id.appwidget_text,
                getPendingSelfIntent(
                    context,
                    MyOnClick
                )
            )
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    // Instruct the widget manager to update the widget
}

fun getPendingSelfIntent(
    context: Context?,
    action: String?
): PendingIntent? {
    val intent = Intent(context, GSVerseWidget::class.java)
    //All intents are controled by GSVerseWidget's onReceive
    intent.action = action
    return PendingIntent.getBroadcast(context, 0, intent, 0)
}