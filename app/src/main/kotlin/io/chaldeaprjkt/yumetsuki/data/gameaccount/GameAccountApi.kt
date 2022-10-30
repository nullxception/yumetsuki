package io.chaldeaprjkt.yumetsuki.data.gameaccount

import io.chaldeaprjkt.yumetsuki.data.common.HoYoResponse
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.RecordCardApiResult
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface GameAccountApi {
    @Headers(
        "Accept: application/json, text/plain, */*",
        "Content-Type: application/json;charset=UTF-8",
        "Referer: https://webstatic-sea.hoyolab.com/",
        "sec-ch-ua-mobile: ?1",
        "x-rpc-client_type: 4",
        "x-rpc-app_version: 1.5.0",
        "x-rpc-language: en-us",
    )
    @GET("/game_record/card/wapi/getGameRecordCard")
    suspend fun get(
        @Query("uid") hoyolabUid: Int,
        @Header("Cookie") cookie: String,
        @Header("DS") ds: String
    ): HoYoResponse<RecordCardApiResult>
}
