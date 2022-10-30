package io.chaldeaprjkt.yumetsuki.data.settings

import io.chaldeaprjkt.yumetsuki.data.settings.entity.CheckInSettings
import io.chaldeaprjkt.yumetsuki.data.settings.entity.NotifierSettings
import io.chaldeaprjkt.yumetsuki.data.settings.entity.Settings
import io.chaldeaprjkt.yumetsuki.data.settings.source.SettingDataStore
import io.chaldeaprjkt.yumetsuki.domain.repository.SettingsRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepoImpl @Inject constructor(
    private val settingDataStore: SettingDataStore,
) : SettingsRepo {

    override val data = settingDataStore.data

    override suspend fun update(transform: suspend (Settings) -> Settings) =
        settingDataStore.update(transform)

    override suspend fun updateCheckIn(transform: suspend (CheckInSettings) -> CheckInSettings) =
        settingDataStore.update { it.copy(checkIn = transform(it.checkIn)) }

    override suspend fun updateNotifier(transform: suspend (NotifierSettings) -> NotifierSettings) =
        settingDataStore.update { it.copy(notifier = transform(it.notifier)) }
}
