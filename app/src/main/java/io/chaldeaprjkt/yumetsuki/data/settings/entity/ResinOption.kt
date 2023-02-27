package io.chaldeaprjkt.yumetsuki.data.settings.entity

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

@JsonClass(generateAdapter = false)
enum class ResinOption(val value: Int) {
    None(0),
    OnEach40(40),
    OnEach80(80),
    On120(120),
    OnFull(160);

    object Adapter {
        @ToJson
        fun toJson(enum: ResinOption): Int {
            return enum.value
        }

        @FromJson
        fun fromJson(value: Int): ResinOption {
            return values().associateBy(ResinOption::value)[value] ?: None
        }
    }
}
