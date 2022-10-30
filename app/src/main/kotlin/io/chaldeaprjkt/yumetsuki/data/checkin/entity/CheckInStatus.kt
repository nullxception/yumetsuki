package io.chaldeaprjkt.yumetsuki.data.checkin.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CheckInStatus(
    @Json(name = "today") val today: String = "",
    @Json(name = "is_sign") val signed: Boolean = false,
) {
    companion object {
        val Empty = CheckInStatus()
    }
}
