package io.chaldeaprjkt.yumetsuki.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import io.chaldeaprjkt.yumetsuki.ui.dashboard.about.AboutDestination
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.CustomWidgetDestination
import io.chaldeaprjkt.yumetsuki.ui.dashboard.home.HomeDestination
import io.chaldeaprjkt.yumetsuki.ui.dashboard.notification.NotificationDestination
import io.chaldeaprjkt.yumetsuki.ui.navigation.Destination
import io.chaldeaprjkt.yumetsuki.ui.navigation.NavEntry

object DashboardDestination : Destination {

    override val route = "dashboard"
}

fun NavGraphBuilder.dashboardGraph(entry: NavEntry) {
    composable(DashboardDestination.route) {
        DashboardRoute(entry = entry)
    }
}

@Composable
fun DashboardRoute(entry: NavEntry) {
    DashboardScreen(
        navbarDestinations = listOf(
            HomeDestination,
            NotificationDestination,
            CustomWidgetDestination,
            AboutDestination,
        ),
        rootEntry = entry,
    )
}
