package io.chaldeaprjkt.yumetsuki.ui.widget

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class NoteListItem(
    @StringRes val desc: Int,
    @DrawableRes val icon: Int,
    val status: String = ""
)
