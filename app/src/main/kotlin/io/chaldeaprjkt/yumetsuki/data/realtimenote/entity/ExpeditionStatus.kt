package io.chaldeaprjkt.yumetsuki.data.realtimenote.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class ExpeditionStatus(
    @Json(name = "data") val characterIcon: String = "",
    @Json(name = "status") val status: String = "",
    @Json(name = "remained_time") val remainingTime: Int = 0
) : Parcelable {
    companion object {
        val Sample = ExpeditionStatus(status = "Ongoing", remainingTime = 60000)
    }
}
