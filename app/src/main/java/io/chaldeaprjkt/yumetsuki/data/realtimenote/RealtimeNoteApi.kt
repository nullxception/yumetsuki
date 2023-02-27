package io.chaldeaprjkt.yumetsuki.data.realtimenote

import io.chaldeaprjkt.yumetsuki.data.common.HoYoResponse
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.RealtimeNote
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RealtimeNoteApi {
    @GET("/game_record/genshin/api/dailyNote")
    suspend fun dailyNote(
        @Query("role_id") uid: Int,
        @Query("server") server: String,
        @Header("Cookie") cookie: String,
        @Header("DS") ds: String
    ): HoYoResponse<RealtimeNote>
}
