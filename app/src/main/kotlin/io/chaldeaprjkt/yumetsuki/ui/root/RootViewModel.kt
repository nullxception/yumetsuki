package io.chaldeaprjkt.yumetsuki.ui.root

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import io.chaldeaprjkt.yumetsuki.domain.repository.UserRepo
import io.chaldeaprjkt.yumetsuki.ui.common.BaseViewModel
import io.chaldeaprjkt.yumetsuki.ui.dashboard.DashboardDestination
import io.chaldeaprjkt.yumetsuki.ui.events.LocalEventContainer
import io.chaldeaprjkt.yumetsuki.ui.login.LoginDestination
import io.chaldeaprjkt.yumetsuki.ui.navigation.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    localEventContainer: LocalEventContainer,
    private val userRepo: UserRepo,
) : BaseViewModel(localEventContainer) {
    private val dest = MutableStateFlow<Destination?>(null)
    val destination = dest.asStateFlow()

    init {
        watchUserChanges()
    }

    private val List<User>.isLoggedIn get() = any { x -> x.loginTimestamp > 0 }

    private fun watchUserChanges() = viewModelScope.launch {
        userRepo.users.distinctUntilChangedBy { it.isLoggedIn }.collect {
            dest.value = if (it.isLoggedIn) {
                DashboardDestination
            } else {
                LoginDestination
            }
        }
    }
}
