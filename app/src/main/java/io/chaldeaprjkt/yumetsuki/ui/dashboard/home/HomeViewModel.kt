package io.chaldeaprjkt.yumetsuki.ui.dashboard.home

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.common.HoYoData
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameAccount
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import io.chaldeaprjkt.yumetsuki.data.user.entity.UserInfo
import io.chaldeaprjkt.yumetsuki.domain.repository.CheckInRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.GameAccountRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SessionRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SettingsRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.UserRepo
import io.chaldeaprjkt.yumetsuki.domain.usecase.SyncCheckInStatusUseCase
import io.chaldeaprjkt.yumetsuki.domain.usecase.SyncGameAccUseCase
import io.chaldeaprjkt.yumetsuki.ui.common.BaseViewModel
import io.chaldeaprjkt.yumetsuki.ui.events.LocalEventContainer
import io.chaldeaprjkt.yumetsuki.util.extension.copyToClipboard
import io.chaldeaprjkt.yumetsuki.worker.WorkerEventDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class HomeViewModel @Inject constructor(
    localEventContainer: LocalEventContainer,
    checkInRepo: CheckInRepo,
    private val userRepo: UserRepo,
    private val sessionRepo: SessionRepo,
    private val settingsRepo: SettingsRepo,
    private val gameAccountRepo: GameAccountRepo,
    private val workerEventDispatcher: WorkerEventDispatcher,
    private val syncGameAccUsecase: SyncGameAccUseCase,
    private val syncCheckInStatusUseCase: SyncCheckInStatusUseCase,
) : BaseViewModel(localEventContainer) {
    private var _gameAccSyncState = MutableStateFlow<GameAccSyncState>(GameAccSyncState.Success)
    val gameAccountsSyncState = _gameAccSyncState.asStateFlow()
    val gameAccounts = gameAccountRepo.accounts
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val checkInStatus = checkInRepo.data
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val users = userRepo.users
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val settings = settingsRepo.data
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val genshinUser = flow {
        userRepo.activeUserOf(HoYoGame.Genshin)
            .collect(this)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val starRailUser = flow {
        userRepo.activeUserOf(HoYoGame.StarRail)
            .collect(this)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        syncCheckInStatus()
    }

    private fun syncCheckInStatus() {
        viewModelScope.launch {
            syncCheckInStatusUseCase(HoYoGame.Genshin).collect()
            syncCheckInStatusUseCase(HoYoGame.Houkai).collect()
            syncCheckInStatusUseCase(HoYoGame.StarRail).collect()
        }
    }

    fun syncUser(user: User) {
        val start = System.currentTimeMillis()
        viewModelScope.launch {
            var userInfo = user
            _gameAccSyncState.emit(GameAccSyncState.Loading)
            userRepo.fetch(userInfo.cookie).collect { userResult ->
                if (userResult is HoYoData && userResult.data.info != UserInfo.Empty) {
                    userInfo = User.fromNetworkSource(user.cookie, userResult.data.info)
                    userRepo.update(userInfo)
                    _gameAccSyncState.emit(GameAccSyncState.Success)
                } else {
                    _gameAccSyncState.emit(GameAccSyncState.Error(R.string.fail_connect_hoyolab))
                }
            }

            _gameAccSyncState.emit(GameAccSyncState.Loading)
            delay(max(2000 - (System.currentTimeMillis() - start), 500))
            syncGameAccUsecase(userInfo).collect {
                if (it is HoYoData) {
                    _gameAccSyncState.emit(GameAccSyncState.Success)
                } else {
                    _gameAccSyncState.emit(GameAccSyncState.Error(R.string.fail_get_ingame_data))
                }
            }
        }
    }

    fun markLaunched() {
        viewModelScope.launch {
            settingsRepo.update {
                it.copy(isFirstLaunch = false)
            }
        }
    }

    fun logout(user: User) {
        viewModelScope.launch {
            gameAccountRepo.clear(user)
            sessionRepo.clear()
            userRepo.clear(user)
        }
    }

    fun activateGameAccount(account: GameAccount) {
        viewModelScope.launch {
            gameAccounts.firstOrNull()?.forEach {
                if (it.game == account.game) {
                    gameAccountRepo.update(it.copy(active = false))
                }
            }
            gameAccountRepo.update(account.copy(active = true))
            syncCheckInStatusUseCase(account.game).collect()
        }
    }

    fun copyCookieString(context: Context, user: User) = with(context) {
        viewModelScope.launch {
            sessionRepo.data.firstOrNull()?.let {
                copyToClipboard(user.cookie, getString(R.string.copied_to_clipboard))
            }
        }
    }

    fun updateCheckInSettings(value: Boolean, game: HoYoGame) {
        viewModelScope.launch {
            if (value) {
                syncCheckInStatusUseCase(game).collect()
            }

            settingsRepo.updateCheckIn {
                when (game) {
                    HoYoGame.Houkai -> it.copy(houkai = value)
                    HoYoGame.StarRail -> it.copy(starRail = value)
                    else -> it.copy(genshin = value)
                }
            }
            workerEventDispatcher.updateCheckInWorkers()
        }
    }

    fun checkInNow() {
        viewModelScope.launch {
            workerEventDispatcher.checkInNow()
        }
    }
}

sealed interface GameAccSyncState {
    object Loading : GameAccSyncState
    object Success : GameAccSyncState
    data class Error(@StringRes val messageId: Int) : GameAccSyncState
}
