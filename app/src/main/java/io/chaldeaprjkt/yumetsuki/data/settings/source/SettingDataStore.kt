package io.chaldeaprjkt.yumetsuki.data.settings.source

import io.chaldeaprjkt.yumetsuki.data.settings.entity.Settings
import kotlinx.coroutines.flow.Flow

interface SettingDataStore {

    val data: Flow<Settings>
    suspend fun update(transform: suspend (Settings) -> Settings): Settings
}
