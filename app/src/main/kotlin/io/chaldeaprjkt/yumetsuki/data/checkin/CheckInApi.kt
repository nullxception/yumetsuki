package io.chaldeaprjkt.yumetsuki.data.checkin

import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInResult
import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInStatus
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResponse
import retrofit2.http.GET
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
    @POST("https://hk4e-api-os.hoyoverse.com/event/sol/sign")
    suspend fun genshin(
        @Query("lang") lang: String,
        @Query("act_id") actId: String,
        @Header("Cookie") cookie: String
    ): HoYoResponse<CheckInResult>

    @Headers(
        "Accept: application/json, text/plain, */*",
        "Content-Type: application/json;charset=UTF-8",
        "Referer: https://webstatic-sea.hoyolab.com/",
        "sec-ch-ua-mobile: ?1"
    )
    @POST("https://sg-public-api.hoyolab.com/event/mani/sign")
    suspend fun houkai(
        @Query("lang") lang: String,
        @Query("act_id") actId: String,
        @Header("Cookie") cookie: String,
    ): HoYoResponse<CheckInResult>

    @Headers(
        "Accept: application/json, text/plain, */*",
        "Content-Type: application/json;charset=UTF-8",
        "Referer: https://webstatic-sea.hoyolab.com/",
        "sec-ch-ua-mobile: ?1",
        "x-rpc-client_type: 4",
        "x-rpc-app_version: 1.5.0",
        "x-rpc-language: en-us",
    )
    @GET("https://sg-hk4e-api.hoyolab.com/event/sol/info")
    suspend fun genshinStatus(
        @Query("lang") lang: String,
        @Query("act_id") actId: String,
        @Header("Cookie") cookie: String,
    ): HoYoResponse<CheckInStatus>

    @Headers(
        "Accept: application/json, text/plain, */*",
        "Content-Type: application/json;charset=UTF-8",
        "Referer: https://webstatic-sea.hoyolab.com/",
        "sec-ch-ua-mobile: ?1",
        "x-rpc-client_type: 4",
        "x-rpc-app_version: 1.5.0",
        "x-rpc-language: en-us",
    )
    @GET("https://sg-public-api.hoyolab.com/event/mani/info")
    suspend fun houkaiStatus(
        @Query("lang") lang: String,
        @Query("act_id") actId: String,
        @Header("Cookie") cookie: String,
    ): HoYoResponse<CheckInStatus>
}
