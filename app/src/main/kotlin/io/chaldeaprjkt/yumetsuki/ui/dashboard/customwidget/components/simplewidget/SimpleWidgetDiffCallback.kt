package io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.components.simplewidget

import androidx.recyclerview.widget.DiffUtil
import io.chaldeaprjkt.yumetsuki.ui.widget.simple.SimpleWidgetItem

class SimpleWidgetDiffCallback(
    private val prevList: List<SimpleWidgetItem>,
    private val nextList: List<SimpleWidgetItem>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = prevList.size

    override fun getNewListSize(): Int = nextList.size

    override fun areItemsTheSame(prevPos: Int, nextPos: Int) =
        prevList[prevPos].status == nextList[nextPos].status

    override fun areContentsTheSame(prevPos: Int, nextPos: Int) =
        prevList[prevPos].icon == nextList[nextPos].icon && prevList[prevPos].status == nextList[nextPos].status
}
