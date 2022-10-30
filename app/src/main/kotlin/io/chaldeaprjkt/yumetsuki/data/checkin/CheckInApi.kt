package io.chaldeaprjkt.yumetsuki.data.checkin

import io.chaldeaprjkt.yumetsuki.constant.Source
import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckIn
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResponse
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface CheckInApi {
    @Headers(
        "Accept: application/json, text/plain, */*",
        "Content-Type: application/json;charset=UTF-8",
        "Referer: https://webstatic-sea.hoyolab.com/",
        "sec-ch-ua-mobile: ?1"
    )
    @POST(Source.Genshin.CheckInURL)
    suspend fun genshin(
        @Query("lang") lang: String,
        @Query("act_id") actId: String,
        @Header("Cookie") cookie: String
    ): HoYoResponse<CheckIn>

    @Headers(
        "Accept: application/json, text/plain, */*",
        "Content-Type: application/json;charset=UTF-8",
        "Referer: https://webstatic-sea.hoyolab.com/",
        "sec-ch-ua-mobile: ?1"
    )
    @POST(Source.Houkai.CheckInURL)
    suspend fun houkai(
        @Query("lang") lang: String,
        @Query("act_id") actId: String,
        @Header("Cookie") cookie: String,
    ): HoYoResponse<CheckIn>
}
