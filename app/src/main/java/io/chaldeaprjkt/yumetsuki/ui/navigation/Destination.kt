package io.chaldeaprjkt.yumetsuki.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

interface Destination {

    val route: String
}

abstract class NavbarDestination : Destination {

    @get:StringRes
    abstract val label: Int

    abstract val icon: ImageVector
    abstract val selectedIcon: ImageVector
}
