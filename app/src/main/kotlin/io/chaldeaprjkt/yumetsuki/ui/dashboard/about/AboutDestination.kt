package io.chaldeaprjkt.yumetsuki.ui.dashboard.about

import androidx.compose.animation.ExitTransition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Info
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.ui.license.LicenseDestination
import io.chaldeaprjkt.yumetsuki.ui.navigation.NavEntry
import io.chaldeaprjkt.yumetsuki.ui.navigation.NavbarDestination
import io.chaldeaprjkt.yumetsuki.ui.navigation.fadeSlideInDown

object AboutDestination : NavbarDestination() {

    override val route = "about"
    override val label = R.string.about_label
    override val icon = Icons.Outlined.Info
    override val selectedIcon = Icons.Filled.Info
}

fun NavGraphBuilder.aboutGraph(rootEntry: NavEntry) {
    composable(
        route = AboutDestination.route,
        enterTransition = { fadeSlideInDown() },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { fadeSlideInDown() },
        popExitTransition = { ExitTransition.None },
    ) {
        AboutScreen(onOpenLicense = { rootEntry.goTo(LicenseDestination) })
    }
}
