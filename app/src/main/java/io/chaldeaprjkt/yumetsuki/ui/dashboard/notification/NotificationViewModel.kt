package io.chaldeaprjkt.yumetsuki.ui.dashboard.notification

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.chaldeaprjkt.yumetsuki.data.settings.entity.ResinOption
import io.chaldeaprjkt.yumetsuki.domain.repository.SettingsRepo
import io.chaldeaprjkt.yumetsuki.data.settings.entity.NotifierSettings
import io.chaldeaprjkt.yumetsuki.ui.common.BaseViewModel
import io.chaldeaprjkt.yumetsuki.ui.events.LocalEventContainer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val settingsRepo: SettingsRepo,
    localEventContainer: LocalEventContainer,
) : BaseViewModel(localEventContainer) {
    val settings = settingsRepo.data.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private fun updateNotifier(transform: suspend (NotifierSettings) -> NotifierSettings) {
        viewModelScope.launch {
            settingsRepo.updateNotifier(transform)
        }
    }

    fun notifyOnCheckInSuccess(enabled: Boolean) =
        updateNotifier { it.copy(onCheckInSuccess = enabled) }

    fun notifyOnCheckInFailed(enabled: Boolean) =
        updateNotifier { it.copy(onCheckInFailed = enabled) }

    fun notifyOnResin(option: ResinOption) =
        updateNotifier { it.copy(onResin = option) }

    fun notifyOnRealmCurrencyFull(enabled: Boolean) =
        updateNotifier { it.copy(onRealmCurrencyFull = enabled) }

    fun notifyOnExpeditionCompleted(enabled: Boolean) =
        updateNotifier { it.copy(onExpeditionCompleted = enabled) }
}
