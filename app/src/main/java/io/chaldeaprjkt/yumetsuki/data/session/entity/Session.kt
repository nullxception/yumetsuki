package io.chaldeaprjkt.yumetsuki.data.session.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Session(
    @Json(name = "lastGameDataSync") val lastGameDataSync: Long = 0L,
    @Json(name = "expeditionTime") val expeditionTime: Int = 0,
) : Parcelable {
    companion object {
        val Empty = Session()
        const val key = "session"
    }
}
