package io.chaldeaprjkt.yumetsuki.domain.repository

import io.chaldeaprjkt.yumetsuki.data.settings.entity.CheckInSettings
import io.chaldeaprjkt.yumetsuki.data.settings.entity.NoteWidgetOption
import io.chaldeaprjkt.yumetsuki.data.settings.entity.NotifierSettings
import io.chaldeaprjkt.yumetsuki.data.settings.entity.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepo {
    val data: Flow<Settings>
    suspend fun update(transform: suspend (Settings) -> Settings): Settings
    suspend fun updateCheckIn(transform: suspend (CheckInSettings) -> CheckInSettings): Settings
    suspend fun updateNotifier(transform: suspend (NotifierSettings) -> NotifierSettings): Settings
    suspend fun updateNoteWidget(transform: suspend (NoteWidgetOption) -> NoteWidgetOption): Settings
}
