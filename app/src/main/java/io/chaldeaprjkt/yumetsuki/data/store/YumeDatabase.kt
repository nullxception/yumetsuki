package io.chaldeaprjkt.yumetsuki.data.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import io.chaldeaprjkt.yumetsuki.BuildConfig
import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInNote
import io.chaldeaprjkt.yumetsuki.data.checkin.source.CheckInNoteDao
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameAccount
import io.chaldeaprjkt.yumetsuki.data.gameaccount.source.GameAccountDao
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import io.chaldeaprjkt.yumetsuki.data.user.source.UserDao

@Database(
    entities = [GameAccount::class, User::class, CheckInNote::class],
    version = 2,
    exportSchema = true
)

@TypeConverters(YumeConverters::class)
abstract class YumeDatabase : RoomDatabase() {
    abstract fun gameAccountDao(): GameAccountDao
    abstract fun userDao(): UserDao
    abstract fun checkInNoteDao(): CheckInNoteDao

    companion object {
        const val Name: String = "${BuildConfig.APPLICATION_ID}.db"
    }

    object Migrator {
        val Version_1_to_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `check_in_status` (" +
                            "`uid` INTEGER NOT NULL, `hoyolab_uid` INTEGER NOT NULL, " +
                            "`game` INTEGER NOT NULL, `checkin_date` TEXT NOT NULL, PRIMARY KEY(`uid`))"
                )
            }
        }
    }
}
