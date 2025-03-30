package com.example.proudlycanadian.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proudlycanadian.R

@Composable
fun BottomNav(navController: NavController) {
    NavigationBar {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry.value?.destination

        val items = listOf(
            Destination.Scan to R.drawable.ic_scan,
            Destination.Profile to R.drawable.ic_profile,
            Destination.Lists to R.drawable.ic_lists
        )

        items.forEach { (destination, icon) ->
            NavigationBarItem(
                selected = currentDestination?.route == destination.route,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                icon = { Icon(painter = painterResource(id = icon), contentDescription = null) },
                label = { Text(text = destination.route) }
            )
        }
    }
}