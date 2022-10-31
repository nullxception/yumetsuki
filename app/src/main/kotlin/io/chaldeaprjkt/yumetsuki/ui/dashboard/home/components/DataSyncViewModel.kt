package io.chaldeaprjkt.yumetsuki.ui.dashboard.home.components

import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.common.HoYoApiCode
import io.chaldeaprjkt.yumetsuki.data.common.HoYoData
import io.chaldeaprjkt.yumetsuki.data.common.HoYoError
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.data.gameaccount.server
import io.chaldeaprjkt.yumetsuki.data.session.entity.Session
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import io.chaldeaprjkt.yumetsuki.domain.repository.DataSwitchRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.GameAccountRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.RealtimeNoteRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SessionRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SettingsRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.UserRepo
import io.chaldeaprjkt.yumetsuki.ui.common.BaseViewModel
import io.chaldeaprjkt.yumetsuki.ui.events.LocalEventContainer
import io.chaldeaprjkt.yumetsuki.ui.widget.WidgetEventDispatcher
import io.chaldeaprjkt.yumetsuki.worker.WorkerEventDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class DataSyncViewModel @Inject constructor(
    localEventContainer: LocalEventContainer,
    private val settingsRepo: SettingsRepo,
    private val userRepo: UserRepo,
    private val realtimeNoteRepo: RealtimeNoteRepo,
    private val sessionRepo: SessionRepo,
    private val gameAccountRepo: GameAccountRepo,
    private val dataSwitchRepo: DataSwitchRepo,
    private val widgetEventDispatcher: WidgetEventDispatcher,
    private val workerEventDispatcher: WorkerEventDispatcher,
) : BaseViewModel(localEventContainer) {
    private var _dataSyncState = MutableStateFlow<DataSyncState>(DataSyncState.Success)
    private var _privateNoteState = MutableStateFlow<PrivateNoteState>(PrivateNoteState.Success)
    private val _isNotePrivate = MutableStateFlow(false)
    private val _privateNoteError = MutableStateFlow(false)

    val settings = settingsRepo.data.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val session = sessionRepo.data.stateIn(viewModelScope, SharingStarted.Eagerly, Session.Empty)
    val dataSyncState = _dataSyncState.asStateFlow()
    val privateNoteState = _privateNoteState.asStateFlow()

    init {
        syncOnGenshinUserChanged()
    }

    private fun syncOnGenshinUserChanged() {
        viewModelScope.launch {
            userRepo.activeUserOf(HoYoGame.Genshin).filterNotNull()
                .distinctUntilChangedBy { it.uid }.collect {
                    sync(it)
                }
        }
    }

    fun updatePeriodicTime(value: Long) {
        viewModelScope.launch {
            settingsRepo.update {
                it.copy(syncPeriod = value)
            }
        }
    }

    fun enablePublicNote(user: User) {
        viewModelScope.launch {
            _privateNoteState.emit(PrivateNoteState.Loading)
            dataSwitchRepo.get(
                gameId = HoYoGame.Genshin.id,
                switchId = 3,
                isPublic = true,
                cookie = user.cookie,
            ).collect {
                when (it) {
                    is HoYoData -> {
                        _privateNoteState.emit(PrivateNoteState.Success)
                        sync(user)
                    }
                    is HoYoError.Network -> {
                        _privateNoteState.emit(PrivateNoteState.Error(R.string.fail_connect_hoyolab))
                    }
                    is HoYoError.Code, is HoYoError.Api -> {
                        _privateNoteState.emit(PrivateNoteState.Error(R.string.makepublicnote_error))
                    }
                    is HoYoError.Empty -> {
                        _privateNoteState.emit(PrivateNoteState.Error(R.string.err_noresponse))
                    }

                }
            }
        }
    }

    fun sync(user: User) {
        val start = System.currentTimeMillis()
        viewModelScope.launch {
            val acc = gameAccountRepo.getActive(HoYoGame.Genshin).firstOrNull()
            if (acc == null) {
                _dataSyncState.emit(DataSyncState.Error(R.string.realtimenote_error_noacc))
                return@launch
            }

            _dataSyncState.emit(DataSyncState.Loading)
            realtimeNoteRepo.sync(
                uid = acc.uid,
                server = acc.server,
                cookie = user.cookie,
            ).collect { result ->
                delay(max(1000 - (System.currentTimeMillis() - start), 100))
                when (result) {
                    is HoYoError.Api, is HoYoError.Code -> {
                        _dataSyncState.emit(DataSyncState.Error(R.string.realtimenote_sync_failed))
                    }
                    is HoYoError.Empty -> {
                        _dataSyncState.emit(DataSyncState.Error(R.string.err_noresponse))
                    }
                    is HoYoError.Network -> {
                        _dataSyncState.emit(DataSyncState.Error(R.string.fail_connect_hoyolab))
                    }
                    is HoYoData -> {
                        when (result.code) {
                            HoYoApiCode.Success -> {
                                sessionRepo.update { session ->
                                    session.copy(
                                        lastGameDataSync = System.currentTimeMillis(),
                                        expeditionTime = result.data.expeditionSettledTime
                                    )
                                }
                                widgetEventDispatcher.refreshAll()
                                workerEventDispatcher.updateRefreshWorker()
                                _dataSyncState.emit(DataSyncState.Success)
                            }
                            HoYoApiCode.InternalDB -> {
                                _dataSyncState.emit(DataSyncState.Error(R.string.err_hoyointernal))
                            }
                            HoYoApiCode.TooManyRequest -> {
                                _dataSyncState.emit(DataSyncState.Error(R.string.err_overrequest))
                            }
                            HoYoApiCode.PrivateData -> {
                                _isNotePrivate.value = true
                                _dataSyncState.emit(DataSyncState.Error(R.string.realtimenote_error_private))
                                _privateNoteState.emit(PrivateNoteState.Private(user))
                            }
                            else -> _dataSyncState.emit(DataSyncState.Error(R.string.realtimenote_sync_failed))
                        }
                    }
                }
            }
        }
    }

    fun ignorePrivateNote() {
        _isNotePrivate.value = false
    }

    fun ignorePrivateNoteWarning() {
        _privateNoteError.value = false
    }
}

sealed interface DataSyncState {
    object Loading : DataSyncState
    object Success : DataSyncState
    data class Error(@StringRes val messageId: Int) : DataSyncState
}

sealed interface PrivateNoteState {
    object Loading : PrivateNoteState
    object Success : PrivateNoteState
    data class Private(val user: User) : PrivateNoteState
    data class Error(@StringRes val messageId: Int) : PrivateNoteState
}
