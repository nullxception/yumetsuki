package io.chaldeaprjkt.yumetsuki.data.settings.source

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import io.chaldeaprjkt.yumetsuki.data.settings.entity.Settings
import io.chaldeaprjkt.yumetsuki.data.store.yumeDataStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingDataStoreImpl @Inject constructor(
    moshi: Moshi,
    @ApplicationContext private val context: Context,
) : SettingDataStore {

    private val Context.dataStore by yumeDataStore(moshi, Settings.key, Settings.Empty)

    override val data = context.dataStore.data

    override suspend fun update(transform: suspend (Settings) -> Settings) =
        context.dataStore.updateData(transform)
}
