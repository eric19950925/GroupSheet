package com.eric.groupsheet.Widget.SmallSov

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.eric.groupsheet.R
import com.eric.groupsheet.Widget.GSVerse.getPendingSelfIntent
import com.eric.groupsheet.Widget.SheetOV.ShortCutOnClick

/**
 * Implementation of App Widget functionality.
 */
class SmallSovWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
const val SmallOn = "mySmallOn"
const val SmallOff = "mySmallOff"

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.small_sov_widget)
    views.setTextViewText(R.id.appwidget_text, widgetText)
    views.setOnClickPendingIntent(
        R.id.small_sov_widget_on,
        getPendingSelfIntent(
            context,
            SmallOn
        )
    )
    views.setOnClickPendingIntent(
        R.id.small_sov_widget_off,
        getPendingSelfIntent(
            context,
            SmallOff
        )
    )
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}