package io.chaldeaprjkt.yumetsuki.data.common

import com.squareup.moshi.JsonClass

// Should only be used when expecting for an empty object
@JsonClass(generateAdapter = true)
data class HoYoEmptyData(val stub: String? = null)
