package io.chaldeaprjkt.yumetsuki.data.widgetsetting.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WidgetSettings(
    @Json(name = "resin")
    val resin: ResinWidgetSettings = ResinWidgetSettings.Empty,
    @Json(name = "detail")
    val detail: DetailWidgetSettings = DetailWidgetSettings.Empty,
    @Json(name = "simple")
    val simple: SimpleWidgetSettings = SimpleWidgetSettings.Empty,
) {
    companion object {
        val Empty = WidgetSettings()
        const val key = "widget_settings"
    }
}
