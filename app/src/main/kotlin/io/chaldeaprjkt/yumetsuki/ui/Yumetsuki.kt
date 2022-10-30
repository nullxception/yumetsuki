package io.chaldeaprjkt.yumetsuki.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import io.chaldeaprjkt.yumetsuki.ui.dashboard.dashboardGraph
import io.chaldeaprjkt.yumetsuki.ui.hoyoweb.hoYoWebGraph
import io.chaldeaprjkt.yumetsuki.ui.license.licenseGraph
import io.chaldeaprjkt.yumetsuki.ui.login.loginGraph
import io.chaldeaprjkt.yumetsuki.ui.navigation.NavEntry
import io.chaldeaprjkt.yumetsuki.ui.navigation.fadeSlideInLeft
import io.chaldeaprjkt.yumetsuki.ui.navigation.fadeSlideInRight
import io.chaldeaprjkt.yumetsuki.ui.navigation.fadeSlideOutLeft
import io.chaldeaprjkt.yumetsuki.ui.navigation.fadeSlideOutRight
import io.chaldeaprjkt.yumetsuki.ui.navigation.rememberNavEntry
import io.chaldeaprjkt.yumetsuki.ui.root.RootDestination
import io.chaldeaprjkt.yumetsuki.ui.root.rootDirector
import io.chaldeaprjkt.yumetsuki.ui.theme.AppTheme

@Composable
fun Yumetsuki(entry: NavEntry = rememberNavEntry()) {
    AppTheme {
        ModalBottomSheetLayout(entry.bottomSheetNavigator) {
            var startDestination by remember { mutableStateOf(RootDestination.route) }

            AnimatedNavHost(
                modifier = Modifier.fillMaxSize(),
                navController = entry.navController,
                startDestination = startDestination,
                enterTransition = { fadeSlideInLeft() },
                exitTransition = { fadeSlideOutLeft() },
                popEnterTransition = { fadeSlideInRight() },
                popExitTransition = { fadeSlideOutRight() },
            ) {
                rootDirector { startDestination = it.route }
                loginGraph(entry = entry)
                hoYoWebGraph(entry = entry)
                dashboardGraph(entry = entry)
                licenseGraph(entry = entry)
            }
        }
    }
}
