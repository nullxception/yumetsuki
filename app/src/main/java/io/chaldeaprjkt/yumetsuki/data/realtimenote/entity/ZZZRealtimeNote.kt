package io.chaldeaprjkt.yumetsuki.data.realtimenote.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ZZZRealtimeNote(
    @Json(name = "energy") val energy: ZZZEnergy = ZZZEnergy(),
) : Parcelable {

    companion object {
        const val key = "in_game_data.zzz"
        val Empty = ZZZRealtimeNote()
        val Sample = ZZZRealtimeNote(ZZZEnergy(ZZZEnergyProgress.Sample))
    }
}

@JsonClass(generateAdapter = true)
@Parcelize
data class ZZZEnergy(
    @Json(name = "progress") val progress: ZZZEnergyProgress = ZZZEnergyProgress.Empty,
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class ZZZEnergyProgress(
    @Json(name = "max") val max: Int = 240,
    @Json(name = "current") val current: Int = 0,
) : Parcelable {
    companion object {
        val Empty = ZZZEnergyProgress()
        val Sample = ZZZEnergyProgress(current = 100)
    }
}
