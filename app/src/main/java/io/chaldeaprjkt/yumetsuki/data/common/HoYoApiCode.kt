package io.chaldeaprjkt.yumetsuki.data.common

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

@JsonClass(generateAdapter = false)
enum class HoYoApiCode(val id: Int) {
    AccountNotFound(-10002),
    ClaimedDailyReward(-5003),
    InvalidInputFormat(-502),
    InvalidLanguage(-108),
    NotLoggedIn(-100),
    Unknown(-10),
    InternalDB(-1),
    Success(0),
    CheckedIntoHoyolab(2001),
    TooManyRequest(10101),
    NotLoggedInV2(10001),
    PrivateData(10102),
    NotLoggedInV3(10103),
    WrongAccount(10104);

    object Adapter {
        @ToJson
        fun toJson(enum: HoYoApiCode): Int {
            return enum.id
        }

        @FromJson
        fun fromJson(value: Int): HoYoApiCode {
            return values().associateBy(HoYoApiCode::id)[value] ?: Unknown
        }
    }
}
