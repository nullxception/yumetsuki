package io.chaldeaprjkt.yumetsuki.ui.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.chaldeaprjkt.yumetsuki.ui.widget.simple.SimpleWidget
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WidgetEventDispatcherImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : WidgetEventDispatcher {

    private val widgets = listOf(
        ResinWidget::class.java,
        DetailWidget::class.java,
        SimpleWidget::class.java,
    )

    override fun refresh(widget: Class<out AppWidgetProvider>) {
        val ids = AppWidgetManager.getInstance(context)
            .getAppWidgetIds(ComponentName(context, widget))

        context.sendBroadcast(
            Intent(context, widget).setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        )
    }

    override fun refreshAll() {
        widgets.forEach {
            refresh(it)
        }
    }
}
