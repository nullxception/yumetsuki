package io.chaldeaprjkt.yumetsuki.data.user.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM hoyolab_user")
    fun all(): Flow<List<User>>

    @Query("SELECT * FROM hoyolab_user where uid = (:uid) LIMIT 1")
    fun ofUID(uid: Int): Flow<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg users: User)

    @Delete
    suspend fun delete(user: User)

    @Update
    suspend fun update(user: User)

    @Query(
        "SELECT hoyolab_user.* FROM hoyolab_user, in_game_account " +
                "WHERE hoyolab_user.uid = in_game_account.hoyolab_uid " +
                "and in_game_account.game = (:game) " +
                "and in_game_account.active = 1"
    )
    fun activeUserOf(game: HoYoGame): Flow<User?>
}
