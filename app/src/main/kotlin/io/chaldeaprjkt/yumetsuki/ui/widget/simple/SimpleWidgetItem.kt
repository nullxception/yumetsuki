package io.chaldeaprjkt.yumetsuki.ui.widget.simple

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class SimpleWidgetItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val status: String = ""
)
