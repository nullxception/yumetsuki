package io.chaldeaprjkt.yumetsuki.data.realtimenote.source

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.GenshinRealtimeNote
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.StarRailRealtimeNote
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.ZZZRealtimeNote
import io.chaldeaprjkt.yumetsuki.data.store.yumeDataStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealtimeNoteDataStoreImpl
@Inject
constructor(
    moshi: Moshi,
    @ApplicationContext private val context: Context,
) : RealtimeNoteDataStore {

    private val Context.dataStoreGenshin by
    yumeDataStore(moshi, GenshinRealtimeNote.key, GenshinRealtimeNote.Empty)
    private val Context.dataStoreStarRail by
    yumeDataStore(moshi, StarRailRealtimeNote.key, StarRailRealtimeNote.Empty)
    private val Context.dataStoreZZZ by
    yumeDataStore(moshi, ZZZRealtimeNote.key, ZZZRealtimeNote.Empty)

    override val dataGenshin = context.dataStoreGenshin.data
    override val dataStarRail = context.dataStoreStarRail.data
    override val dataZZZ = context.dataStoreZZZ.data

    override suspend fun updateGenshin(
        transform: suspend (GenshinRealtimeNote) -> GenshinRealtimeNote
    ) = context.dataStoreGenshin.updateData(transform)

    override suspend fun updateStarRail(
        transform: suspend (StarRailRealtimeNote) -> StarRailRealtimeNote
    ) = context.dataStoreStarRail.updateData(transform)

    override suspend fun updateZZZ(
        transform: suspend (ZZZRealtimeNote) -> ZZZRealtimeNote
    ) = context.dataStoreZZZ.updateData(transform)
}
