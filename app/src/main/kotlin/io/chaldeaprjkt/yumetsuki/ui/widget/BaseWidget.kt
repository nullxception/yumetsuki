package io.chaldeaprjkt.yumetsuki.ui.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.text.format.DateFormat
import android.widget.RemoteViews
import androidx.annotation.LayoutRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

abstract class BaseWidget(@LayoutRes private val layout: Int) : AppWidgetProvider() {

    val Context.remoteViews get() = RemoteViews(packageName, layout)
    val coroutineScope by lazy { CoroutineScope(SupervisorJob() + Dispatchers.Main) }

    abstract suspend fun onCreateViews(context: Context, view: RemoteViews, id: Int)

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        context ?: return
        appWidgetIds?.forEach {
            coroutineScope.launch {
                context.remoteViews.apply {
                    onCreateViews(context, this, it)
                    appWidgetManager?.updateAppWidget(it, this)
                }
            }
        }
    }

    fun Long.formatSyncTime(): CharSequence =
        DateFormat.format("MM/dd hh:mm", this)
}
