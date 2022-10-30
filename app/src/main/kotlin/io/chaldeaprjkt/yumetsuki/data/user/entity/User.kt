package io.chaldeaprjkt.yumetsuki.data.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hoyolab_user")
data class User(
    @PrimaryKey @ColumnInfo(name = "uid") val uid: Int,
    @ColumnInfo(name = "cookie") val cookie: String = "",
    @ColumnInfo(name = "nickname") val nickname: String = "",
    @ColumnInfo(name = "introduce") val status: String = "",
    @ColumnInfo(name = "avatar") val avatar: Int = -1,
    @ColumnInfo(name = "gender") val gender: Int = -1,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String = "",
    @ColumnInfo(name = "pendant") val frameUrl: String = "",
    @ColumnInfo(name = "bg_url") val bannerUrl: String = "",
    @ColumnInfo(name = "login_timestamp") val loginTimestamp: Long = 0L,
    @ColumnInfo(name = "gameaccsync_timestamp") val gameAccountsSyncTimestamp: Long = 0L,
) {

    companion object {
        val Empty: User = User(uid = 0)
        val Preview = User(
            nickname = "Kanchou",
            status = "Hi Mom",
            uid = 123456789,
        )

        fun fromNetworkSource(cookie: String, info: UserInfo) =
            User(
                uid = info.id,
                cookie = cookie,
                status = info.status,
                nickname = info.nickname,
                avatar = info.avatar,
                gender = info.avatar,
                avatarUrl = info.avatarUrl,
                frameUrl = info.frameUrl,
                bannerUrl = info.bannerUrl,
            )
    }
}
