package io.chaldeaprjkt.yumetsuki.ui.dashboard.home.components

import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.common.HoYoApiCode
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.data.gameaccount.server
import io.chaldeaprjkt.yumetsuki.data.session.entity.Session
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import io.chaldeaprjkt.yumetsuki.domain.common.RepoResult
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
import kotlinx.coroutines.flow.flow
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
                .distinctUntilChangedBy { it.uid }
                .collect {
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

    private suspend fun makeNotePublic(user: User) = flow {
        dataSwitchRepo.get(
            gameId = HoYoGame.Genshin.id,
            switchId = 3,
            isPublic = true,
            cookie = user.cookie,
        ).collect {
            when (it) {
                is HoYoResult.Success -> {
                    if (it.code == HoYoApiCode.Success) {
                        emit(RepoResult.Success(R.string.makepublicnote_success))
                    } else {
                        emit(RepoResult.Error(R.string.makepublicnote_error_pluscode, it.code))
                    }
                }

                is HoYoResult.Failure -> {
                    emit(RepoResult.Error(R.string.fail_connect_hoyolab))
                }

                is HoYoResult.Error -> {
                    emit(RepoResult.Error(R.string.makepublicnote_error))
                }

                is HoYoResult.Null -> {
                    emit(RepoResult.Error(R.string.err_noresponse))
                }
            }
        }
    }

    private suspend fun noteSync(user: User) = flow {
        val acc = gameAccountRepo.activeGenshin.firstOrNull()
        if (acc == null) {
            emit(RepoResult.Error(R.string.realtimenote_error_noacc))
            return@flow
        }

        realtimeNoteRepo.sync(
            uid = acc.uid,
            server = acc.server,
            cookie = user.cookie,
        ).collect { result ->
            when (result) {
                is HoYoResult.Success -> {
                    when (result.code) {
                        HoYoApiCode.Success -> {
                            sessionRepo.update { session ->
                                session.copy(
                                    lastGameDataSync = System.currentTimeMillis(),
                                    expeditionTime = result.data.expeditionSettledTime
                                )
                            }
                            emit(RepoResult.Success(R.string.realtimenote_sync_success))
                        }

                        HoYoApiCode.InternalDB -> {
                            emit(RepoResult.Error(R.string.err_hoyointernal))
                        }

                        HoYoApiCode.TooManyRequest -> {
                            emit(RepoResult.Error(R.string.err_overrequest))
                        }

                        HoYoApiCode.PrivateData -> {
                            emit(
                                RepoResult.Error(
                                    R.string.realtimenote_error_private,
                                    HoYoApiCode.PrivateData
                                )
                            )
                        }

                        else ->
                            emit(
                                RepoResult.Error(
                                    R.string.realtimenote_sync_failed_pluscode,
                                    result.code
                                )
                            )
                    }
                }

                is HoYoResult.Failure -> {
                    emit(RepoResult.Error(R.string.fail_connect_hoyolab))
                }

                is HoYoResult.Error -> {
                    emit(RepoResult.Error(R.string.realtimenote_sync_failed))
                }

                is HoYoResult.Null -> {
                    emit(RepoResult.Error(R.string.err_noresponse))
                }
            }
        }
    }

    fun enablePublicNote(user: User) {
        viewModelScope.launch {
            _privateNoteState.emit(PrivateNoteState.Loading)
            makeNotePublic(user).collect {
                when (it) {
                    is RepoResult.Success -> {
                        _privateNoteState.emit(PrivateNoteState.Success)
                        sync(user)
                    }
                    is RepoResult.Error -> {
                        _privateNoteState.emit(PrivateNoteState.Error(it.messageId))
                        _privateNoteError.value = true
                    }
                    else -> {
                        _privateNoteState.emit(PrivateNoteState.Success)
                    }
                }
            }
        }
    }

    fun sync(user: User) {
        val start = System.currentTimeMillis()
        viewModelScope.launch {
            _dataSyncState.emit(DataSyncState.Loading)
            noteSync(user).collect {
                delay(max(1000 - (System.currentTimeMillis() - start), 100))
                if (it is RepoResult.Success) {
                    widgetEventDispatcher.refreshAll()
                    workerEventDispatcher.updateRefreshWorker()
                    _dataSyncState.emit(DataSyncState.Success)
                } else if (it is RepoResult.Error && it.errCode == HoYoApiCode.PrivateData) {
                    _isNotePrivate.value = true
                    _dataSyncState.emit(DataSyncState.Error(it.messageId))
                    _privateNoteState.emit(PrivateNoteState.Private(user))
                } else if (it is RepoResult.Error) {
                    _dataSyncState.emit(DataSyncState.Error(it.messageId))
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
