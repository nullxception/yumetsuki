package io.chaldeaprjkt.yumetsuki.ui.hoyoweb

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import io.chaldeaprjkt.yumetsuki.ui.navigation.Destination
import io.chaldeaprjkt.yumetsuki.ui.navigation.NavEntry

object HoYoWebDestination : Destination {

    override val route = "hoyoweb"
}

fun NavGraphBuilder.hoYoWebGraph(entry: NavEntry) {
    composable(route = HoYoWebDestination.route) {
        HoYoWebRoute(entry = entry)
    }
}

@Composable
fun HoYoWebRoute(entry: NavEntry) {
    HoYoWebScreen(onPopBack = entry::pop)
}
