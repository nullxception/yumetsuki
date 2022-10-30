package io.chaldeaprjkt.yumetsuki.data.widgetsetting.source

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import io.chaldeaprjkt.yumetsuki.data.store.yumeDataStore
import io.chaldeaprjkt.yumetsuki.data.widgetsetting.entity.WidgetSettings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WidgetSettingDataStoreImpl @Inject constructor(
    moshi: Moshi,
    @ApplicationContext private val context: Context,
) : WidgetSettingDataStore {

    private val Context.dataStore by yumeDataStore(moshi, WidgetSettings.key, WidgetSettings.Empty)

    override val data = context.dataStore.data

    override suspend fun update(transform: suspend (WidgetSettings) -> WidgetSettings) =
        context.dataStore.updateData(transform)
}
