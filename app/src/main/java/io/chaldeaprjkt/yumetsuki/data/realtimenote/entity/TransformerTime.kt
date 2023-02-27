package io.chaldeaprjkt.yumetsuki.data.realtimenote.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class TransformerTime(
    @Json(name = "Day") val days: Int = 0,
    @Json(name = "Hour") val hours: Int = 0,
    @Json(name = "Minute") val minutes: Int = 0,
    @Json(name = "Second") val secs: Int = 0,
    @Json(name = "reached") val isReached: Boolean = false
) : Parcelable {
    val totalInSecs: Int get() = days * 86400 + hours * 3600 + minutes * 60 + secs

    companion object {
        val Empty = TransformerTime()
        val Sample = TransformerTime(3, 0, 0, 0, false)
    }
}
