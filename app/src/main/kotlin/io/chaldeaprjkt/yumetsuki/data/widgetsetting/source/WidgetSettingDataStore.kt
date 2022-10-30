package io.chaldeaprjkt.yumetsuki.data.widgetsetting.source

import io.chaldeaprjkt.yumetsuki.data.widgetsetting.entity.WidgetSettings
import kotlinx.coroutines.flow.Flow

interface WidgetSettingDataStore {

    val data: Flow<WidgetSettings>
    suspend fun update(transform: suspend (WidgetSettings) -> WidgetSettings): WidgetSettings
}
