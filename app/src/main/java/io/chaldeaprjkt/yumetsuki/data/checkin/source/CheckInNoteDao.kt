package io.chaldeaprjkt.yumetsuki.data.checkin.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInNote
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckInNoteDao {
    @Query("SELECT * FROM check_in_status") fun all(): Flow<List<CheckInNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(vararg notes: CheckInNote)

    @Query("DELETE FROM check_in_status WHERE hoyolab_uid = (:hoyoUID)")
    suspend fun deleteBy(hoyoUID: Int)

    @Update suspend fun update(note: CheckInNote)
}
