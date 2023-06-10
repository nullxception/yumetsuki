package io.chaldeaprjkt.yumetsuki.data.gameaccount.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecordCardApiResult(@Json(name = "list") val list: List<RecordCard> = emptyList()) {
    companion object {
        val Empty = RecordCardApiResult()
    }
}
