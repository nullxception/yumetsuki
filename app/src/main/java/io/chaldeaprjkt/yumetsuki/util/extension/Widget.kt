package io.chaldeaprjkt.yumetsuki.util.extension

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews

fun RemoteViews.setTextViewSize(id: Int, value: Float) =
    setTextViewTextSize(id, TypedValue.COMPLEX_UNIT_SP, value)

fun View.replaceWith(newView: View) {
    (parent as ViewGroup).apply {
        val thisIndex = indexOfChild(this@replaceWith)
        removeView(this)
        addView(newView, thisIndex)
    }
}
