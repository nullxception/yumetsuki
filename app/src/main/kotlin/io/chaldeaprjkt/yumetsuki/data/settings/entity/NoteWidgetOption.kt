package io.chaldeaprjkt.yumetsuki.data.settings.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class NoteWidgetOption(
    @Json(name = "show_resin") val showResinData: Boolean = true,
    @Json(name = "show_dailycommission") val showDailyCommissionData: Boolean = true,
    @Json(name = "show_weeklyboss") val showWeeklyBossData: Boolean = true,
    @Json(name = "show_realmcurrency") val showRealmCurrencyData: Boolean = true,
    @Json(name = "show_expedition") val showExpeditionData: Boolean = true,
    @Json(name = "show_paratransformer") val showParaTransformerData: Boolean = true,
    @Json(name = "font_size") val fontSize: Float = DefaultFontSize,
    @Json(name = "background_alpha") val backgroundAlpha: Float = DefaultBackgroundAlpha,
    @Json(name = "show_title") val showDescription: Boolean = false,
    @Json(name = "show_remain_time") val showRemainTime: Boolean = false
) : Parcelable {
    companion object {
        val Empty = NoteWidgetOption()
        const val DefaultFontSize = 12f
        const val DefaultBackgroundAlpha = 1.0f
        const val key = "note_widget"
    }
}