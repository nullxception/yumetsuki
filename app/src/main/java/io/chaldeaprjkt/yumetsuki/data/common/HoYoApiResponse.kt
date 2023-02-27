package io.chaldeaprjkt.yumetsuki.data.common

import com.skydoves.sandwich.ApiResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HoYoApiResponse<T>(
    @Json(name = "retcode") val retcode: HoYoApiCode,
    @Json(name = "message") val message: String,
    @Json(name = "data") val data: T? = null
)

typealias HoYoResponse<T> = ApiResponse<HoYoApiResponse<T>>
