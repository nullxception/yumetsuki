package io.chaldeaprjkt.yumetsuki.data.checkin.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import java.time.LocalDate
import java.time.format.DateTimeParseException

@Entity(tableName = "check_in_status")
data class CheckInNote(
    @PrimaryKey @ColumnInfo(name = "uid") val uid: Int,
    @ColumnInfo(name = "hoyolab_uid") val hoyolabUid: Int = 0,
    @ColumnInfo(name = "game") val game: HoYoGame = HoYoGame.Unknown,
    @ColumnInfo(name = "checkin_date") val checkinDate: String = "",
) {

    fun checkedToday(): Boolean {
        val date = try {
            LocalDate.parse(checkinDate)
        } catch (e: DateTimeParseException) {
            LocalDate.ofEpochDay(0)
        }
        val current = LocalDate.now()
        return date.isEqual(current) || date.isAfter(current)
    }

    companion object {
        val Empty: CheckInNote = CheckInNote(uid = 0)
        val Preview = CheckInNote(
            uid = 12354354,
            hoyolabUid = 123,
            game = HoYoGame.Genshin,
            checkinDate = "2022-10-31",
        )
    }
}
