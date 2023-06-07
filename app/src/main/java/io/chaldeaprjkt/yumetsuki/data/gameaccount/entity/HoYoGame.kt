package io.chaldeaprjkt.yumetsuki.data.gameaccount.entity

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

@JsonClass(generateAdapter = false)
enum class HoYoGame(val id: Int) {
    Unknown(0),
    Houkai(1),
    Genshin(2),
    StarRail(6);

    object Adapter {
        @ToJson
        fun toJson(enum: HoYoGame): Int {
            return enum.id
        }

        @FromJson
        fun fromJson(value: Int): HoYoGame {
            return values().associateBy(HoYoGame::id)[value] ?: Unknown
        }
    }
}
