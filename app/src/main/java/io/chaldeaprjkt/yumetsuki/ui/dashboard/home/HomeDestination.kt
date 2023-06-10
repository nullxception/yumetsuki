package io.chaldeaprjkt.yumetsuki.ui.dashboard.home

import androidx.compose.animation.ExitTransition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.ui.login.LoginDestination
import io.chaldeaprjkt.yumetsuki.ui.navigation.NavEntry
import io.chaldeaprjkt.yumetsuki.ui.navigation.NavbarDestination
import io.chaldeaprjkt.yumetsuki.ui.navigation.fadeSlideInDown

object HomeDestination : NavbarDestination() {

    override val route = "home"
    override val label = R.string.home
    override val icon = Icons.Outlined.Home
    override val selectedIcon = Icons.Filled.Home
}

fun NavGraphBuilder.homeGraph(rootEntry: NavEntry) {
    composable(
        route = HomeDestination.route,
        enterTransition = { fadeSlideInDown() },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { fadeSlideInDown() },
        popExitTransition = { ExitTransition.None },
    ) {
        HomeScreen(
            viewModel = hiltViewModel(),
            dataSyncViewModel = hiltViewModel(),
            onAddAccount = { rootEntry.goTo(LoginDestination.AddAccount) },
            onNavigateLogin = { rootEntry.switchTo(LoginDestination) },
        )
    }
}
