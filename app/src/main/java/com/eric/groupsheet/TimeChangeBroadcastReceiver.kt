package com.eric.groupsheet

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
class Synchronize_Time : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val extras = intent.extras//useless
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val man = AppWidgetManager.getInstance(context)
        val ids = man.getAppWidgetIds(ComponentName(context,GSVerseWidget::class.java))
        for (appWidgetId in ids) {
                updateTimeAppWidget(context, appWidgetManager, appWidgetId)
            if (action == Intent.ACTION_TIME_TICK ||
                action == Intent.ACTION_TIME_CHANGED ||
                action == Intent.ACTION_TIMEZONE_CHANGED ||
                action == Intent.ACTION_POWER_CONNECTED
            ) {
                updateTimeAppWidget(
                    context,
                    AppWidgetManager.getInstance(context),
                    appWidgetId
                )
            }
        }

    }
}
