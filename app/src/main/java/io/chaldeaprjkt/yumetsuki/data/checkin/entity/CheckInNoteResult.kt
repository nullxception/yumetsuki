package io.chaldeaprjkt.yumetsuki.data.checkin.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CheckInNoteResult(
    @Json(name = "today") val today: String = "",
    @Json(name = "is_sign") val signed: Boolean = false,
    @Json(name = "region") val region: String = "",
) {
    companion object {
        val Empty = CheckInNoteResult()
    }
}
