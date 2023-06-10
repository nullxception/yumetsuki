package io.chaldeaprjkt.yumetsuki.ui.navigation

import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy

class NavTracker : NavController.OnDestinationChangedListener {

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        val data =
            mapOf(
                "hierarchy" to
                    destination.hierarchy
                        .filter { it.route != null }
                        .map { mapOf("route" to it.route, "parent" to it.parent) }
                        .toList(),
                "bundleData" to arguments.toString()
            )
        Log.d(NavTracker::class.simpleName, data.toString())
    }
}
