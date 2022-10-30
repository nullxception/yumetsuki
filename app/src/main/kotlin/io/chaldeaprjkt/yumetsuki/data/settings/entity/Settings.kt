package io.chaldeaprjkt.yumetsuki.data.settings.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Settings(
    @Json(name = "firstLaunch") val isFirstLaunch: Boolean = true,
    @Json(name = "syncPeriod") val syncPeriod: Long = DefaultSyncPeriod,
    @Json(name = NotifierSettings.key) val notifier: NotifierSettings = NotifierSettings.Empty,
    @Json(name = CheckInSettings.key) val checkIn: CheckInSettings = CheckInSettings.Empty,
) {
    companion object {
        val Empty = Settings()
        const val key = "settings"
        const val DefaultSyncPeriod = 15L
    }
}
