package io.chaldeaprjkt.yumetsuki.data.gameaccount.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "in_game_account")
data class GameAccount(
    @PrimaryKey @ColumnInfo(name = "uid") val uid: Int,
    @ColumnInfo(name = "hoyolab_uid") val hoyolabUid: Int = 0,
    @ColumnInfo(name = "game") val game: HoYoGame = HoYoGame.Unknown,
    @ColumnInfo(name = "nickname") val nickname: String = "",
    @ColumnInfo(name = "level") val level: Int = 0,
    @ColumnInfo(name = "region") val region: String = "",
    @ColumnInfo(name = "active") val active: Boolean = false,
) {

    companion object {
        val Empty: GameAccount = GameAccount(uid = 0)
        val Preview = GameAccount(
            hoyolabUid = 1,
            nickname = "Kanchou",
            level = 50,
            uid = 908671234,
            game = HoYoGame.Houkai,
            region = "os_asia"
        )

        val StarRailStub = GameAccount(
            hoyolabUid = 1,
            nickname = "Trailblazer",
            level = 0,
            uid = 404,
            game = HoYoGame.StarRail,
            region = StarRailServer.ASIA.regionId
        )

        fun fromNetworkSource(hoyolabUid: Int, acc: RecordCard) =
            GameAccount(
                uid = acc.uid,
                hoyolabUid = hoyolabUid,
                game = acc.game,
                nickname = acc.nickname,
                level = acc.level,
                region = acc.region,
            )
    }
}
