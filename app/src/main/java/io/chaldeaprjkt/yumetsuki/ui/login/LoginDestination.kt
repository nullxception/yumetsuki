package io.chaldeaprjkt.yumetsuki.ui.login

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.material.bottomSheet
import io.chaldeaprjkt.yumetsuki.ui.navigation.Destination
import io.chaldeaprjkt.yumetsuki.ui.navigation.NavEntry

object LoginDestination : Destination {

    const val cookieArg = "cookieData"
    override val route = "login"

    fun cookieFromEntry(it: NavBackStackEntry): String {
        return it.savedStateHandle[cookieArg] ?: ""
    }

    object AddAccount : Destination {
        override val route = "add_account"
    }
}

object SNSHelpDestination : Destination {
    override val route = "sns_help"
}

fun NavGraphBuilder.loginGraph(entry: NavEntry) {
    bottomSheet(route = SNSHelpDestination.route) { SNSHelpBottomSheet() }
    composable(route = LoginDestination.route) {
        LoginRoute(entry = entry, viewModel = hiltViewModel(), backStackEntry = it)
    }
    composable(route = LoginDestination.AddAccount.route) {
        LoginRoute(isAdding = true, entry = entry, backStackEntry = it, viewModel = hiltViewModel())
    }
}

@Composable
fun LoginRoute(
    isAdding: Boolean = false,
    entry: NavEntry,
    viewModel: LoginViewModel,
    backStackEntry: NavBackStackEntry,
) {
    val cookie = LoginDestination.cookieFromEntry(backStackEntry)
    LoginScreen(entry = entry, cookie = cookie, viewModel = viewModel, isAdding = isAdding)
}
