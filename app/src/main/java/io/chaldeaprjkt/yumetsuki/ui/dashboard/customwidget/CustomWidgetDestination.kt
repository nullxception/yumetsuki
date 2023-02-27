package io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget

import androidx.compose.animation.ExitTransition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Edit
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.ui.navigation.NavbarDestination
import io.chaldeaprjkt.yumetsuki.ui.navigation.fadeSlideInDown

object CustomWidgetDestination : NavbarDestination() {

    override val route = "customwidget"
    override val label = R.string.widget_customize
    override val icon = Icons.Outlined.Edit
    override val selectedIcon = Icons.Filled.Edit
}

fun NavGraphBuilder.customWidgetGraph() {
    composable(
        route = CustomWidgetDestination.route,
        enterTransition = { fadeSlideInDown() },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { fadeSlideInDown() },
        popExitTransition = { ExitTransition.None },
    ) {
        CustomWidgetScreen(viewModel = hiltViewModel())
    }
}
