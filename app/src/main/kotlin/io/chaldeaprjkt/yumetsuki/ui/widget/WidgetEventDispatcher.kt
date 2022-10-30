package io.chaldeaprjkt.yumetsuki.ui.widget

import android.appwidget.AppWidgetProvider

interface WidgetEventDispatcher {
    fun refresh(widget: Class<out AppWidgetProvider>)
    fun refreshAll()
}
