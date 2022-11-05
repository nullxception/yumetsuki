package io.chaldeaprjkt.yumetsuki.ui.widget.simple

import android.content.Context
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.widgetsetting.entity.SimpleWidgetSettings
import io.chaldeaprjkt.yumetsuki.util.extension.setTextViewSize

class SimpleWidgetFactory(
    private val context: Context,
    private val items: List<SimpleWidgetItem>,
    private val settings: SimpleWidgetSettings,
) :
    RemoteViewsService.RemoteViewsFactory {
    private val layout = RemoteViews(context.packageName, R.layout.item_simple_widget)

    override fun onCreate() {}

    override fun onDataSetChanged() {}

    override fun onDestroy() {}

    override fun getCount(): Int = items.count()

    override fun getViewAt(position: Int) = layout.apply {
        if (items[position].icon > 0) {
            setImageViewResource(R.id.icon, items[position].icon)
        } else {
            setImageViewBitmap(R.id.icon, null)
        }

        setTextViewText(R.id.status, items[position].status)
        setTextViewSize(R.id.status, settings.fontSize)
        if (settings.showDescription) {
            setViewVisibility(R.id.desc, View.VISIBLE)
            setTextViewText(R.id.desc, context.getString(items[position].title))
            setTextViewSize(R.id.desc, settings.fontSize)
        } else {
            setViewVisibility(R.id.desc, View.INVISIBLE)
        }
    }

    override fun getLoadingView() = layout

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true
}
