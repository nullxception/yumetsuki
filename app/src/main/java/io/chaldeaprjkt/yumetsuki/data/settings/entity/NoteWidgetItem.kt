package io.chaldeaprjkt.yumetsuki.data.settings.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class NoteWidgetItem(
    @Json(name = "type") val type: NoteWidgetType = NoteWidgetType.GenshinResin,
    @Json(name = "show") val show: Boolean = true,
) : Parcelable {
    companion object {
        val Empty = NoteWidgetItem()
        const val key = "note_widget_item"
    }
}
