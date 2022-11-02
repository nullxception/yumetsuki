package io.chaldeaprjkt.yumetsuki.data.user

import io.chaldeaprjkt.yumetsuki.data.common.HoYoResponse
import io.chaldeaprjkt.yumetsuki.data.user.entity.UserFullInfo
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface UserFullInfoApi {
    @GET("https://bbs-api-os.hoyolab.com/community/user/wapi/getUserFullInfo")
    suspend fun fetch(
        @Query("lang") lang: String,
        @Header("Cookie") cookie: String
    ): HoYoResponse<UserFullInfo>
}
