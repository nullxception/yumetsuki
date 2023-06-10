package io.chaldeaprjkt.yumetsuki.data.checkin.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CheckInResult(@Json(name = "code") val code: String = "") {
    companion object {
        val Empty = CheckInResult()
    }
}
