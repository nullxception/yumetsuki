package io.chaldeaprjkt.yumetsuki.domain.repository

import io.chaldeaprjkt.yumetsuki.data.widgetsetting.entity.DetailWidgetSettings
import io.chaldeaprjkt.yumetsuki.data.widgetsetting.entity.SimpleWidgetSettings
import io.chaldeaprjkt.yumetsuki.data.widgetsetting.entity.WidgetSettings
import kotlinx.coroutines.flow.Flow

interface WidgetSettingsRepo {
    val data: Flow<WidgetSettings>
    suspend fun updateDetail(transform: suspend (DetailWidgetSettings) -> DetailWidgetSettings): WidgetSettings
    suspend fun updateSimple(transform: suspend (SimpleWidgetSettings) -> SimpleWidgetSettings): WidgetSettings
}
