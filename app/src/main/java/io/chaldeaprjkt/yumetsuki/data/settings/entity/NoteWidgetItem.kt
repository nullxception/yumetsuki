package io.chaldeaprjkt.yumetsuki.data.settings.entity

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

@JsonClass(generateAdapter = false)
enum class NoteWidgetItem(val value: Int) {
    GenshinResin(100),
    GenshinDailyCommission(101),
    GenshinWeeklyBoss(102),
    GenshinRealmCurrency(103),
    GenshinExpedition(103),
    GenshinParaTransformer(104),
    StarRailPower(201);

    object Adapter {
        @ToJson
        fun toJson(enum: NoteWidgetItem): Int {
            return enum.value
        }

        @FromJson
        fun fromJson(value: Int): NoteWidgetItem {
            return values().associateBy(NoteWidgetItem::value)[value] ?: GenshinResin
        }
    }
}
