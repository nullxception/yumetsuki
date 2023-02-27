package io.chaldeaprjkt.yumetsuki.data.realtimenote.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class ParaTransformerStatus(
    @Json(name = "obtained") val obtained: Boolean = false,
    @Json(name = "recovery_time") val recoveryTime: TransformerTime = TransformerTime.Empty
) : Parcelable {
    companion object {
        val Empty = ParaTransformerStatus()
        val Sample = ParaTransformerStatus(
            obtained = true,
            recoveryTime = TransformerTime.Sample
        )
    }
}
