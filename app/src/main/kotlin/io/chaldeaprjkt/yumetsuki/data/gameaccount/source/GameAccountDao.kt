package io.chaldeaprjkt.yumetsuki.data.gameaccount.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface GameAccountDao {
    @Query("SELECT * FROM in_game_account")
    fun all(): Flow<List<GameAccount>>

    @Query("SELECT * FROM in_game_account WHERE active = 1")
    fun actives(): Flow<List<GameAccount>>

    @Query("SELECT * FROM in_game_account WHERE active = 1 and game = (:game)")
    fun activeOn(game: HoYoGame): Flow<GameAccount?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg gameAccounts: GameAccount)

    @Query("DELETE FROM in_game_account WHERE hoyolab_uid = (:hoyoUID)")
    suspend fun deleteBy(hoyoUID: Int)

    @Update
    suspend fun update(account: GameAccount)
}
