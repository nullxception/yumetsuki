package io.chaldeaprjkt.yumetsuki.ui.dashboard.notification

import androidx.compose.animation.ExitTransition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.ui.navigation.NavbarDestination
import io.chaldeaprjkt.yumetsuki.ui.navigation.fadeSlideInDown

object NotificationDestination : NavbarDestination() {

    override val route = "notification"
    override val label = R.string.notification
    override val icon = Icons.Outlined.Notifications
    override val selectedIcon = Icons.Filled.Notifications
}

fun NavGraphBuilder.notificationGraph() {
    composable(
        route = NotificationDestination.route,
        enterTransition = { fadeSlideInDown() },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { fadeSlideInDown() },
        popExitTransition = { ExitTransition.None },
    ) {
        NotificationRoute(viewModel = hiltViewModel())
    }
}

@Composable
fun NotificationRoute(viewModel: NotificationViewModel) {
    NotificationScreen(viewModel)
}
