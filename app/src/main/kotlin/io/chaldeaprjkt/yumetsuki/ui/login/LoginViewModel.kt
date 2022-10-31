package io.chaldeaprjkt.yumetsuki.ui.login

import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.common.HoYoData
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import io.chaldeaprjkt.yumetsuki.data.user.entity.UserInfo
import io.chaldeaprjkt.yumetsuki.domain.common.HoYoCookie
import io.chaldeaprjkt.yumetsuki.domain.common.UseCaseResult
import io.chaldeaprjkt.yumetsuki.domain.repository.UserRepo
import io.chaldeaprjkt.yumetsuki.domain.usecase.SyncGameAccUseCase
import io.chaldeaprjkt.yumetsuki.ui.common.BaseViewModel
import io.chaldeaprjkt.yumetsuki.ui.events.LocalEventContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    localEventContainer: LocalEventContainer,
    private val userRepo: UserRepo,
    private val syncGameAccUsecase: SyncGameAccUseCase,
) : BaseViewModel(localEventContainer) {
    private var _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private suspend fun markLoggedIn(user: User) {
        userRepo.update(user.copy(loginTimestamp = System.currentTimeMillis()))
    }

    private suspend fun isUidExists(uid: Int) =
        userRepo.ofId(uid).map { user -> user?.let { it.loginTimestamp > 0L } ?: false }

    fun login(cookieStr: String) {
        viewModelScope.launch {
            val cookie = HoYoCookie(cookieStr)
            if (!cookie.isValid()) {
                _uiState.emit(LoginUiState.Error(R.string.err_cookie_invalid))
                return@launch
            }

            if (isUidExists(cookie.uid).first()) {
                _uiState.emit(LoginUiState.Error(R.string.err_cookie_exists))
                return@launch
            }

            _uiState.emit(LoginUiState.Loading(R.string.fetching_user_info))
            userRepo.fetch("$cookie").collect { res ->
                if (res is HoYoData && res.data.info != UserInfo.Empty) {
                    val user = User.fromNetworkSource("$cookie", res.data.info)
                    userRepo.add(user)
                    _uiState.emit(LoginUiState.Success(R.string.login_success))

                    syncGameAccUsecase(user).collect {
                        when (it) {
                            is UseCaseResult.Error -> _uiState.emit(LoginUiState.Error(it.messageId))
                            is UseCaseResult.Loading -> _uiState.emit(LoginUiState.Loading(it.messageId))
                            is UseCaseResult.Success -> _uiState.emit(LoginUiState.Success(it.messageId))
                        }
                    }

                    markLoggedIn(user)
                    _uiState.emit(LoginUiState.Success(R.string.game_accounts_synced))
                    _uiState.emit(LoginUiState.Done)
                } else {
                    _uiState.emit(LoginUiState.Error(R.string.fail_login))
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}

sealed interface LoginUiState {
    object Idle : LoginUiState
    data class Error(@StringRes val messageId: Int) : LoginUiState
    data class Loading(@StringRes val messageId: Int) : LoginUiState
    data class Success(@StringRes val messageId: Int? = null) : LoginUiState
    object Done : LoginUiState // Done with the screen - moving out.
}
