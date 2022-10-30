package io.chaldeaprjkt.yumetsuki.data.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.chaldeaprjkt.yumetsuki.BuildConfig
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameAccount
import io.chaldeaprjkt.yumetsuki.data.gameaccount.source.GameAccountDao
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import io.chaldeaprjkt.yumetsuki.data.user.source.UserDao

@Database(entities = [GameAccount::class, User::class], version = 1, exportSchema = true)
@TypeConverters(YumeConverters::class)
abstract class YumeDatabase : RoomDatabase() {
    abstract fun gameAccountDao(): GameAccountDao
    abstract fun userDao(): UserDao

    companion object {
        const val Name: String = "${BuildConfig.APPLICATION_ID}.db"
    }
}
