package io.chaldeaprjkt.yumetsuki.data.widgetsetting.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ResinWidgetSettings(
    @Json(name = "show_time") val showTime: Boolean = true,
    @Json(name = "show_resinimage") val showResinImage: Boolean = true,
    @Json(name = "font_size") val fontSize: Float = DefaultFontSize,
    @Json(name = "background_alpha") val backgroundAlpha: Float = DefaultBackgroundAlpha,
) : BaseWidgetSettings {
    companion object {
        val Empty = ResinWidgetSettings()
        const val DefaultFontSize = 30f
        const val DefaultBackgroundAlpha = 1.0f
    }
}
