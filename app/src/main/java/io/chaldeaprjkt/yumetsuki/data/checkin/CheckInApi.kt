package io.chaldeaprjkt.yumetsuki.data.checkin

import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInNoteResult
import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInResult
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface CheckInApi {
    @POST("https://sg-hk4e-api.hoyolab.com/event/sol/sign")
    suspend fun genshin(
        @Query("lang") lang: String,
        @Query("act_id") actId: String,
        @Header("Cookie") cookie: String,
        @Header("X-Rpc-Signgame") signGame: String = ""
    ): HoYoResponse<CheckInResult>

    @POST("https://sg-public-api.hoyolab.com/event/mani/sign")
    suspend fun houkai(
        @Query("lang") lang: String,
        @Query("act_id") actId: String,
        @Header("Cookie") cookie: String,
        @Header("X-Rpc-Signgame") signGame: String = ""
    ): HoYoResponse<CheckInResult>

    @POST("https://sg-public-api.hoyolab.com/event/luna/os/sign")
    suspend fun starRail(
        @Query("lang") lang: String,
        @Query("act_id") actId: String,
        @Header("Cookie") cookie: String,
        @Header("X-Rpc-Signgame") signGame: String = "hkrpg"
    ): HoYoResponse<CheckInResult>

    @POST("https://sg-public-api.hoyolab.com/event/luna/zzz/os/sign")
    suspend fun zzz(
        @Query("lang") lang: String,
        @Query("act_id") actId: String,
        @Header("Cookie") cookie: String,
        @Header("X-Rpc-Signgame") signGame: String = "zzz"
    ): HoYoResponse<CheckInResult>

    @GET("https://sg-hk4e-api.hoyolab.com/event/sol/info")
    suspend fun genshinStatus(
        @Query("lang") lang: String,
        @Query("act_id") actId: String,
        @Header("Cookie") cookie: String,
        @Header("X-Rpc-Signgame") signGame: String = ""
    ): HoYoResponse<CheckInNoteResult>

    @GET("https://sg-public-api.hoyolab.com/event/mani/info")
    suspend fun houkaiStatus(
        @Query("lang") lang: String,
        @Query("act_id") actId: String,
        @Header("Cookie") cookie: String,
        @Header("X-Rpc-Signgame") signGame: String = ""
    ): HoYoResponse<CheckInNoteResult>

    @GET("https://sg-public-api.hoyolab.com/event/luna/os/info")
    suspend fun starRailStatus(
        @Query("lang") lang: String,
        @Query("act_id") actId: String,
        @Header("Cookie") cookie: String,
        @Header("X-Rpc-Signgame") signGame: String = "hkrpg"
    ): HoYoResponse<CheckInNoteResult>

    @GET("https://sg-public-api.hoyolab.com/event/luna/zzz/os/info")
    suspend fun zzzStatus(
        @Query("lang") lang: String,
        @Query("act_id") actId: String,
        @Header("Cookie") cookie: String,
        @Header("X-Rpc-Signgame") signGame: String = "zzz"
    ): HoYoResponse<CheckInNoteResult>
}
