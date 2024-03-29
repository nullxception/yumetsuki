package io.chaldeaprjkt.yumetsuki.data.settings.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class NoteWidgetSetting(
    @Json(name = "font_size") val fontSize: Float = DefaultFontSize,
    @Json(name = "background_alpha") val backgroundAlpha: Float = DefaultBackgroundAlpha,
    @Json(name = "show_title") val showDescription: Boolean = false,
    @Json(name = "show_remain_time") val showRemainTime: Boolean = false,
    @Json(name = "display_items") val items: List<NoteWidgetItem> = DefaultItems,
    @Json(name = "show_icons") val showIcons: Boolean = true,
) : Parcelable {
    companion object {
        val DefaultItems = NoteWidgetType.values().map(::NoteWidgetItem)
        val Empty = NoteWidgetSetting()
        const val DefaultFontSize = 12f
        const val DefaultBackgroundAlpha = 1.0f
        const val key = "note_widget_setting"
    }
}
