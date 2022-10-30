package io.chaldeaprjkt.yumetsuki.ui.widget.simple

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.util.extension.setTextViewSize

class SimpleWidgetFactory(
    context: Context,
    private val items: List<SimpleWidgetItem>,
    private val fontSize: Float,
) :
    RemoteViewsService.RemoteViewsFactory {
    private val layout = RemoteViews(context.packageName, R.layout.item_simple_widget)

    override fun onCreate() {}

    override fun onDataSetChanged() {}

    override fun onDestroy() {}

    override fun getCount(): Int = items.count()

    override fun getViewAt(position: Int) = layout.apply {
        setImageViewResource(R.id.icon, items[position].icon)
        setTextViewText(R.id.status, items[position].status)
        setTextViewSize(R.id.status, fontSize)
    }

    override fun getLoadingView() = layout

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true
}
