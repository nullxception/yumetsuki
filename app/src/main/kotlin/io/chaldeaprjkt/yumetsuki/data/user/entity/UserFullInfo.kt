package io.chaldeaprjkt.yumetsuki.data.user.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserFullInfo(
    @Json(name = "user_info") val info: UserInfo = UserInfo.Empty,
) {
    companion object {
        val Empty = UserFullInfo()
    }
}
