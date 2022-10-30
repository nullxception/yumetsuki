package io.chaldeaprjkt.yumetsuki.ui.dashboard.home

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.common.HoYoResult
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameAccount
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import io.chaldeaprjkt.yumetsuki.data.user.entity.UserInfo
import io.chaldeaprjkt.yumetsuki.domain.common.RepoResult
import io.chaldeaprjkt.yumetsuki.domain.repository.GameAccountRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SessionRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SettingsRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.UserRepo
import io.chaldeaprjkt.yumetsuki.ui.common.BaseViewModel
import io.chaldeaprjkt.yumetsuki.ui.events.LocalEventContainer
import io.chaldeaprjkt.yumetsuki.util.extension.copyToClipboard
import io.chaldeaprjkt.yumetsuki.worker.WorkerEventDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
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
    private val userRepo: UserRepo,
    private val sessionRepo: SessionRepo,
    private val settingsRepo: SettingsRepo,
    private val gameAccountRepo: GameAccountRepo,
    private val workerEventDispatcher: WorkerEventDispatcher,
) : BaseViewModel(localEventContainer) {
    private var _gameAccountsSyncState =
        MutableStateFlow<GameAccountsSyncState>(GameAccountsSyncState.Success)
    val gameAccountsSyncState = _gameAccountsSyncState.asStateFlow()
    val gameAccounts = gameAccountRepo.accounts
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val activeGameAccounts = gameAccountRepo.actives
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val users = userRepo.users
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val settings = settingsRepo.data
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val genshinUser = flow {
        userRepo.activeUserOf(HoYoGame.Genshin)
            .collect(this)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private suspend fun syncUserInfo(user: User): Flow<RepoResult> = flow {
        emit(RepoResult.Loading(R.string.fetching_user_info))
        userRepo.fetch(user.cookie).collect { userResult ->
            if (userResult is HoYoResult.Success && userResult.data.info != UserInfo.Empty) {
                val newInfo = User.fromNetworkSource(user.cookie, userResult.data.info)
                userRepo.update(newInfo)
                emit(RepoResult.Success(R.string.sync_user_info_success))
            } else {
                emit(RepoResult.Error(R.string.fail_connect_hoyolab))
            }
        }
    }

    fun syncUser(user: User) {
        val start = System.currentTimeMillis()
        viewModelScope.launch {
            _gameAccountsSyncState.emit(GameAccountsSyncState.Loading)
            syncUserInfo(user).collect()
            delay(max(2000 - (System.currentTimeMillis() - start), 500))
            gameAccountRepo.syncGameAccount(user).collect {
                if (it is RepoResult.Success) {
                    _gameAccountsSyncState.emit(GameAccountsSyncState.Success)
                } else if (it is RepoResult.Error) {
                    _gameAccountsSyncState.emit(GameAccountsSyncState.Error(it.messageId))
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

    fun activateCard(account: GameAccount) {
        viewModelScope.launch {
            gameAccountRepo.update(account.copy(active = true))
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
            settingsRepo.updateCheckIn {
                if (game == HoYoGame.Houkai) {
                    it.copy(houkai = value)
                } else {
                    it.copy(genshin = value)
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

sealed interface GameAccountsSyncState {
    object Loading : GameAccountsSyncState
    object Success : GameAccountsSyncState
    data class Error(@StringRes val messageId: Int) : GameAccountsSyncState
}
