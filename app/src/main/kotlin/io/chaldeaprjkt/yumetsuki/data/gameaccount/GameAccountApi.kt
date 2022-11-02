package io.chaldeaprjkt.yumetsuki.data.gameaccount

import io.chaldeaprjkt.yumetsuki.data.common.HoYoResponse
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.RecordCardApiResult
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GameAccountApi {
    @GET("/game_record/card/wapi/getGameRecordCard")
    suspend fun get(
        @Query("uid") hoyolabUid: Int,
        @Header("Cookie") cookie: String,
        @Header("DS") ds: String
    ): HoYoResponse<RecordCardApiResult>
}
