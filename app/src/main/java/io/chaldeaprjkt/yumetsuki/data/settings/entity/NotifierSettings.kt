package io.chaldeaprjkt.yumetsuki.data.settings.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotifierSettings(
    @Json(name = "resin") val onResin: ResinOption = ResinOption.None,
    @Json(name = "expeditionCompleted") val onExpeditionCompleted: Boolean = false,
    @Json(name = "realmCurrencyFull") val onRealmCurrencyFull: Boolean = false,
    @Json(name = "checkinSuccess") val onCheckInSuccess: Boolean = true,
    @Json(name = "checkinFailed") val onCheckInFailed: Boolean = false,
) {
    companion object {
        val Empty = NotifierSettings()
        const val key = "notifier"
    }
}
