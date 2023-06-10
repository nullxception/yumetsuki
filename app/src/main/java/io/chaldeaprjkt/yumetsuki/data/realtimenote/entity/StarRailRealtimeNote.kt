package io.chaldeaprjkt.yumetsuki.data.realtimenote.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class StarRailRealtimeNote(
    @Json(name = "current_stamina") val currentStamina: Int = -1,
    @Json(name = "max_stamina") val totalStamina: Int = 180,
) : Parcelable {

    companion object {
        const val key = "in_game_data.starrail"
        val Empty = StarRailRealtimeNote()
        val Sample = StarRailRealtimeNote(currentStamina = 159)
    }
}
