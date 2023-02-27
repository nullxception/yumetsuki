package io.chaldeaprjkt.yumetsuki.ui.license

import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import io.chaldeaprjkt.yumetsuki.ui.navigation.Destination
import io.chaldeaprjkt.yumetsuki.ui.navigation.NavEntry
import io.chaldeaprjkt.yumetsuki.ui.navigation.fadeSlideInLeft
import io.chaldeaprjkt.yumetsuki.ui.navigation.fadeSlideOutRight

object LicenseDestination : Destination {
    override val route = "license"
}

fun NavGraphBuilder.licenseGraph(entry: NavEntry) {
    composable(
        route = LicenseDestination.route,
        enterTransition = { fadeSlideInLeft() },
        exitTransition = { fadeSlideOutRight() },
        popEnterTransition = { fadeSlideInLeft() },
        popExitTransition = { fadeSlideOutRight() },
    ) {
        LicenseScreen(onPopBack = { entry.pop() })
    }
}
