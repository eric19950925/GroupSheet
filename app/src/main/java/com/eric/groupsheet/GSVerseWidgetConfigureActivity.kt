package com.eric.groupsheet

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.google.firebase.database.*

/**
 * The configuration screen for the [GSVerseWidget] AppWidget.
 */
class GSVerseWidgetConfigureActivity : Activity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var appWidgetText: EditText
    lateinit var BibleVerseReference : DatabaseReference
    var widgetText = ""
    var verseText = ""
    private var onClickListener = View.OnClickListener {
        val context = this
        widgetText = appWidgetText.text.toString()
        saveTitlePref(context, appWidgetId, widgetText)
        val appWidgetManager = AppWidgetManager.getInstance(this)
        updateAppWidget(context,
            appWidgetManager,
            appWidgetId)
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()

    }

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setResult(RESULT_CANCELED)
        setContentView(R.layout.g_s_verse_widget_configure)
        appWidgetText = findViewById<View>(R.id.appwidget_text) as EditText
        findViewById<View>(R.id.add_button).setOnClickListener(onClickListener)
//        val appWidgetManager = AppWidgetManager.getInstance(this)
//        updateAppWidget(this,
//            appWidgetManager,
//            appWidgetId)
        // Find the widget id from the intent.
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }
        val action = intent.action
        val man = AppWidgetManager.getInstance(this)
        val ids = man.getAppWidgetIds(ComponentName(this,GSVerseWidget::class.java))
        for (appWidgetId in ids) {
//                updateTimeAppWidget(context, appWidgetManager, appWidgetId)
            if (action == Intent.ACTION_TIME_TICK ||
                action == Intent.ACTION_TIME_CHANGED ||
                action == Intent.ACTION_TIMEZONE_CHANGED ||
                action == Intent.ACTION_POWER_CONNECTED
            ) {
                updateAppWidget(
                    this,
                    AppWidgetManager.getInstance(this),
                    appWidgetId
                )
            }
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        appWidgetText.setText(loadTitlePref(this@GSVerseWidgetConfigureActivity, appWidgetId))
    }

}


private const val PREFS_NAME = "com.eric.groupsheet.GSVerseWidget"
private const val PREF_PREFIX_KEY = "appwidget_"

// Write the prefix to the SharedPreferences object for this widget
internal fun saveTitlePref(context: Context, appWidgetId: Int, text: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putString(PREF_PREFIX_KEY + appWidgetId, text)
    prefs.apply()
}

// Read the prefix from the SharedPreferences object for this widget.
// If there is no preference saved, get the default from a resource
internal fun loadTitlePref(context: Context, appWidgetId: Int): String {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    val titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null)
    return titleValue ?: context.getString(R.string.appwidget_text)
}

internal fun deleteTitlePref(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.remove(PREF_PREFIX_KEY + appWidgetId)
    prefs.apply()
}