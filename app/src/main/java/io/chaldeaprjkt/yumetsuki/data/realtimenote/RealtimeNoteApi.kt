package io.chaldeaprjkt.yumetsuki.data.realtimenote

import io.chaldeaprjkt.yumetsuki.data.common.HoYoResponse
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.GenshinRealtimeNote
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.StarRailRealtimeNote
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RealtimeNoteApi {
    @GET("/game_record/genshin/api/dailyNote")
    suspend fun genshinNote(
        @Query("role_id") uid: Int,
        @Query("server") server: String,
        @Header("Cookie") cookie: String,
        @Header("DS") ds: String
    ): HoYoResponse<GenshinRealtimeNote>

    @GET("/game_record/hkrpg/api/note")
    suspend fun starRailNote(
        @Query("role_id") uid: Int,
        @Query("server") server: String,
        @Header("Cookie") cookie: String,
        @Header("DS") ds: String
    ): HoYoResponse<StarRailRealtimeNote>
}
