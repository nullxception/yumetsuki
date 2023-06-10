package io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.chaldeaprjkt.yumetsuki.data.settings.entity.NoteWidgetSetting
import io.chaldeaprjkt.yumetsuki.domain.repository.SettingsRepo
import io.chaldeaprjkt.yumetsuki.ui.common.BaseViewModel
import io.chaldeaprjkt.yumetsuki.ui.events.LocalEventContainer
import io.chaldeaprjkt.yumetsuki.ui.widget.NoteWidgetProvider
import io.chaldeaprjkt.yumetsuki.ui.widget.WidgetEventDispatcher
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class CustomWidgetViewModel
@Inject
constructor(
    localEventContainer: LocalEventContainer,
    val settingsRepo: SettingsRepo,
    val widgetEventDispatcher: WidgetEventDispatcher,
) : BaseViewModel(localEventContainer) {
    val settings = settingsRepo.data.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun updateNoteWidget(transform: suspend (NoteWidgetSetting) -> NoteWidgetSetting) {
        viewModelScope.launch {
            settingsRepo.updateNoteWidget(transform)
            widgetEventDispatcher.refresh(NoteWidgetProvider::class.java)
        }
    }
}
