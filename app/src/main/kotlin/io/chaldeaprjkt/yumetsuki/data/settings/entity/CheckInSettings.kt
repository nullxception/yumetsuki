package io.chaldeaprjkt.yumetsuki.data.settings.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CheckInSettings(
    @Json(name = "genshinImpact") val genshin: Boolean = false,
    @Json(name = "honkaiImpact3rd") val houkai: Boolean = false,
) {
    companion object {
        val Empty = CheckInSettings()
        const val key = "checkIn"
    }
}
