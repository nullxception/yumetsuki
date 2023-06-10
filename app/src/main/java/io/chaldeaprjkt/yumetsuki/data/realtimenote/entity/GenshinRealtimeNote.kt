package io.chaldeaprjkt.yumetsuki.data.realtimenote.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class GenshinRealtimeNote(
    @Json(name = "current_resin") val currentResin: Int = -1,
    @Json(name = "max_resin") val totalResin: Int = 160,
    @Json(name = "resin_recovery_time") val resinRecoveryTime: Int = -1,
    @Json(name = "finished_task_num") val completedTask: Int = -1,
    @Json(name = "total_task_num") val totalTask: Int = -1,
    @Json(name = "is_extra_task_reward_received") val receivedExtraTaskReward: Boolean = false,
    @Json(name = "remain_resin_discount_num") val remainingWeeklyBoss: Int = -1,
    @Json(name = "resin_discount_num_limit") val totalWeeklyBoss: Int = -1,
    @Json(name = "current_home_coin") val currentRealmCurrency: Int = -1,
    @Json(name = "max_home_coin") val totalRealmCurrency: Int = -1,
    @Json(name = "home_coin_recovery_time") val realmCurrencyRecoveryTime: Int = -1,
    @Json(name = "current_expedition_num") val currentExpedition: Int = -1,
    @Json(name = "max_expedition_num") val totalExpedition: Int = -1,
    @Json(name = "expeditions") val expeditions: List<ExpeditionStatus> = listOf(),
    @Json(name = "transformer") val paraTransformerStatus: ParaTransformerStatus? = ParaTransformerStatus.Empty
) : Parcelable {

    val expeditionSettledTime: Int
        get() = try {
            expeditions.maxOf { it.remainingTime }
        } catch (e: NoSuchElementException) {
            0
        }

    companion object {
        const val key = "in_game_data.genshin"
        val Empty = GenshinRealtimeNote()
        val Sample = GenshinRealtimeNote(
            currentResin = 159,
            resinRecoveryTime = 50000,
            completedTask = 3,
            totalTask = 4,
            receivedExtraTaskReward = true,
            remainingWeeklyBoss = 2,
            totalWeeklyBoss = 3,
            currentRealmCurrency = 1000,
            totalRealmCurrency = 2400,
            realmCurrencyRecoveryTime = 60000,
            currentExpedition = 2,
            totalExpedition = 5,
            expeditions = listOf(
                ExpeditionStatus.Sample,
                ExpeditionStatus.Sample,
            ),
            paraTransformerStatus = ParaTransformerStatus.Sample
        )
    }
}
