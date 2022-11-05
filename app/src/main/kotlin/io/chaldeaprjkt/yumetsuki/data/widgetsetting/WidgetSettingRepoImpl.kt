package io.chaldeaprjkt.yumetsuki.data.widgetsetting

import io.chaldeaprjkt.yumetsuki.data.widgetsetting.entity.DetailWidgetSettings
import io.chaldeaprjkt.yumetsuki.data.widgetsetting.entity.SimpleWidgetSettings
import io.chaldeaprjkt.yumetsuki.data.widgetsetting.entity.WidgetSettings
import io.chaldeaprjkt.yumetsuki.data.widgetsetting.source.WidgetSettingDataStore
import io.chaldeaprjkt.yumetsuki.domain.repository.WidgetSettingsRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WidgetSettingRepoImpl @Inject constructor(
    private val widgetSettingDataStore: WidgetSettingDataStore
) : WidgetSettingsRepo {

    override val data = widgetSettingDataStore.data

    private suspend fun update(transform: suspend (WidgetSettings) -> WidgetSettings) =
        widgetSettingDataStore.update(transform)

    override suspend fun updateDetail(transform: suspend (DetailWidgetSettings) -> DetailWidgetSettings) =
        update { it.copy(detail = transform(it.detail)) }

    override suspend fun updateSimple(transform: suspend (SimpleWidgetSettings) -> SimpleWidgetSettings) =
        update { it.copy(simple = transform(it.simple)) }
}
