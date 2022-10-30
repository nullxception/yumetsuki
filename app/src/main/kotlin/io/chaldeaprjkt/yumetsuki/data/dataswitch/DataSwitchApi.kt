package io.chaldeaprjkt.yumetsuki.data.dataswitch

import io.chaldeaprjkt.yumetsuki.data.common.HoYoEmptyData
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResponse
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface DataSwitchApi {
    @Headers(
        "Accept: application/json, text/plain, */*",
        "Content-Type: application/json;charset=UTF-8",
        "Referer: https://webstatic-sea.hoyolab.com/",
        "sec-ch-ua-mobile: ?1",
        "x-rpc-client_type: 4",
        "x-rpc-app_version: 1.5.0",
        "x-rpc-language: en-us",
    )
    @POST("/game_record/card/wapi/changeDataSwitch")
    suspend fun changeDataSwitch(
        @Query("game_id") gameId: Int,
        @Query("switch_id") switchId: Int,
        @Query("is_public") isPublic: Boolean,
        @Header("Cookie") cookie: String,
        @Header("DS") ds: String
    ): HoYoResponse<HoYoEmptyData>
}
