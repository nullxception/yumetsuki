package io.chaldeaprjkt.yumetsuki.ui.root

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import io.chaldeaprjkt.yumetsuki.ui.navigation.Destination

object RootDestination : Destination {
    override val route = "root"
}

fun NavGraphBuilder.rootDirector(onDestinationChange: (Destination) -> Unit) {
    composable(route = RootDestination.route) {
        val viewModel = hiltViewModel<RootViewModel>()
        val destination by viewModel.destination.collectAsState()
        destination?.let { onDestinationChange(it) }
    }
}
