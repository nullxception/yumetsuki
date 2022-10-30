package io.chaldeaprjkt.yumetsuki.data.gameaccount.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecordCard(
    @Json(name = "game_role_id") val uid: Int = -1,
    @Json(name = "game_id") val game: HoYoGame = HoYoGame.Unknown,
    @Json(name = "level") val level: Int = -1,
    @Json(name = "nickname") val nickname: String = "",
    @Json(name = "region") val region: String = "",
) {

    companion object {
        val Empty = RecordCard()
    }
}
