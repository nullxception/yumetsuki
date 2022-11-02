package io.chaldeaprjkt.yumetsuki.data.dataswitch

import io.chaldeaprjkt.yumetsuki.data.common.HoYoEmptyData
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResponse
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface DataSwitchApi {
    @POST("/game_record/card/wapi/changeDataSwitch")
    suspend fun changeDataSwitch(
        @Query("game_id") gameId: Int,
        @Query("switch_id") switchId: Int,
        @Query("is_public") isPublic: Boolean,
        @Header("Cookie") cookie: String,
        @Header("DS") ds: String
    ): HoYoResponse<HoYoEmptyData>
}
