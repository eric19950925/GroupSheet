package com.eric.groupsheet

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.*
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [GSVerseWidgetConfigureActivity]
 */
class GSVerseWidget : AppWidgetProvider() {
    var m_timeChangedReceiver: BroadcastReceiver = Synchronize_Time()
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
            updateTimeAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            deleteTitlePref(context, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created

        // Create an intent filter

        // Enter relevant functionality for when the first widget is created

        // Create an intent filter
        val nixie_intentFilter = IntentFilter()
        nixie_intentFilter.addAction(Intent.ACTION_POWER_CONNECTED)
        nixie_intentFilter.addAction(Intent.ACTION_TIME_TICK)
        nixie_intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED)
        nixie_intentFilter.addAction(Intent.ACTION_TIME_CHANGED)

//        context.applicationContext.registerReceiver(m_timeChangedReceiver, nixie_intentFilter)

    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (MyOnClick.equals(intent?.getAction())){
            Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show()
//            val intent_main = Intent(context,MainActivity::class.java)
//            intent_main.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            context?.startActivity(intent_main)

            val sharedPreference =  context?.getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
            val versionUrl = sharedPreference?.getString("VerseUrl","http://philabnb.com/").toString()
            val openUrl = Intent(Intent.ACTION_VIEW)
            openUrl.data = Uri.parse(versionUrl)
            openUrl.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(openUrl)

        }
    }
}

const val MyOnClick = "myOnClickTag"
const val MyOnClick_test = "myOnClickTESTTag"
internal fun updateTimeAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.g_s_verse_widget)
//    views.setTextViewText(R.id.widget_hour, LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
//    views.setTextViewText(R.id.widget_min, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd - ")) +LocalDateTime.now().dayOfWeek.name.toLowerCase())

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {

    // Construct the RemoteViews object
    val sharedPreference =  context.getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
    val version = sharedPreference?.getString("Verse","Ooops,something wrong...").toString()

    val views = RemoteViews(context.packageName, R.layout.g_s_verse_widget)
    views.setTextViewText(R.id.appwidget_text, version)

    views.setOnClickPendingIntent(R.id.img_more,
        getPendingSelfIntent(context, MyOnClick))
    val intent = Intent(context, GSVerseWidget::class.java)
    intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0, intent, PendingIntent.FLAG_UPDATE_CURRENT
    )
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

fun BuildTimeBitmap(timeData:String,textSize:Float,context: Context){
    var paint = Paint()
    paint.textSize = textSize
    paint.typeface = ResourcesCompat.getFont(context, R.font.iato_bold)
    paint.color = Color.WHITE

}
fun getPendingSelfIntent(
    context: Context?,
    action: String?
): PendingIntent? {
    val intent = Intent(context,GSVerseWidget::class.java)
    intent.action = action
    return PendingIntent.getBroadcast(context, 0, intent, 0)
}