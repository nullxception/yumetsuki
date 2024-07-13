package io.chaldeaprjkt.yumetsuki.data.settings.entity

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

@JsonClass(generateAdapter = false)
enum class NoteWidgetType(val value: Int) {
    GenshinResin(100),
    GenshinDailyCommission(101),
    GenshinWeeklyBoss(102),
    GenshinRealmCurrency(103),
    GenshinExpedition(103),
    GenshinParaTransformer(104),
    StarRailPower(201),
    ZZZBatteryCharge(301);

    object Adapter {
        @ToJson
        fun toJson(enum: NoteWidgetType): Int {
            return enum.value
        }

        @FromJson
        fun fromJson(value: Int): NoteWidgetType {
            return values().associateBy(NoteWidgetType::value)[value] ?: GenshinResin
        }
    }
}
