package io.chaldeaprjkt.yumetsuki.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator

@Composable
fun rememberNavEntry(
    bottomSheetNavigator: BottomSheetNavigator = rememberBottomSheetNavigator(),
    navController: NavHostController = rememberAnimatedNavController(bottomSheetNavigator),
): NavEntry {
    NavTrackingSideEffect(navController)
    return remember(navController) {
        NavEntry(bottomSheetNavigator, navController)
    }
}

@Stable
class NavEntry(
    val bottomSheetNavigator: BottomSheetNavigator,
    val navController: NavHostController,
) {

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    fun goTo(destination: Destination) {
        navController.navigate(destination.route) {
            if (destination is NavbarDestination) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    fun switchTo(destination: Destination) {
        val current = navController.currentDestination
            ?: navController.graph.findStartDestination()

        navController.navigate(destination.route) {
            popUpTo(current.route ?: destination.route) {
                inclusive = true
            }
        }
    }

    fun pop(data: Pair<String, String>? = null) {
        if (data != null) {
            navController.previousBackStackEntry?.savedStateHandle?.set(data.first, data.second)
        }

        navController.popBackStack()
    }
}

@Composable
private fun NavTrackingSideEffect(navController: NavHostController) {
    DisposableEffect(navController) {
        val listener = NavTracker()
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}
