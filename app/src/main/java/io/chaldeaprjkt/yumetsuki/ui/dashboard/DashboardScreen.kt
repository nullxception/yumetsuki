package io.chaldeaprjkt.yumetsuki.ui.dashboard

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import io.chaldeaprjkt.yumetsuki.ui.dashboard.about.aboutGraph
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.customWidgetGraph
import io.chaldeaprjkt.yumetsuki.ui.dashboard.home.HomeDestination
import io.chaldeaprjkt.yumetsuki.ui.dashboard.home.homeGraph
import io.chaldeaprjkt.yumetsuki.ui.dashboard.notification.notificationGraph
import io.chaldeaprjkt.yumetsuki.ui.navigation.NavEntry
import io.chaldeaprjkt.yumetsuki.ui.navigation.NavbarDestination
import io.chaldeaprjkt.yumetsuki.ui.navigation.rememberNavEntry

@Composable
fun DashboardScreen(
    dashboardEntry: NavEntry = rememberNavEntry(),
    navbarDestinations: List<NavbarDestination> = emptyList(),
    rootEntry: NavEntry,
) {
    ModalBottomSheetLayout(dashboardEntry.bottomSheetNavigator) {
        Scaffold(
            bottomBar = {
                DashboardBottomNavBar(
                    destinations = navbarDestinations,
                    onNavigate = dashboardEntry::goTo,
                    currentDestination = dashboardEntry.currentDestination,
                )
            },
            contentWindowInsets = WindowInsets.navigationBars,
        ) {
            AnimatedNavHost(
                navController = dashboardEntry.navController,
                startDestination = HomeDestination.route,
                modifier = Modifier.padding(it)
            ) {
                homeGraph(rootEntry = rootEntry)
                notificationGraph()
                customWidgetGraph()
                aboutGraph(rootEntry = rootEntry)
            }
        }
    }
}

@Composable
private fun DashboardBottomNavBar(
    destinations: List<NavbarDestination>,
    onNavigate: (NavbarDestination) -> Unit,
    currentDestination: NavDestination?,
) {
    currentDestination ?: return

    NavigationBar {
        destinations.forEach { screen ->
            val isSelected = currentDestination.hierarchy.any { it.route == screen.route }
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(screen) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) screen.selectedIcon else screen.icon,
                        contentDescription = stringResource(screen.label),
                    )
                },
                label = { Text(text = stringResource(screen.label)) },
            )
        }
    }
}
