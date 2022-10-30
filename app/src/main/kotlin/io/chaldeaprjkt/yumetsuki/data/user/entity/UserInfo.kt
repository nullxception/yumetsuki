package io.chaldeaprjkt.yumetsuki.data.user.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserInfo(
    @Json(name = "uid") val id: Int = -1,
    @Json(name = "nickname") val nickname: String = "",
    @Json(name = "introduce") val status: String = "",
    @Json(name = "avatar") val avatar: Int = -1,
    @Json(name = "gender") val gender: Int = -1,
    @Json(name = "avatar_url") val avatarUrl: String = "",
    @Json(name = "pendant") val frameUrl: String = "",
    @Json(name = "bg_url") val bannerUrl: String = "",
) {

    companion object {

        val Empty = UserInfo()
        const val key = "hoyolab-user-info"
    }
}
