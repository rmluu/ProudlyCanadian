package com.example.proudlycanadian.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proudlycanadian.R

/**
 * Purpose: Displays a Bottom Navigation Bar for easy navigation between the main app screens.
 * @param navController: the NavController responsible for handling navigation actions within the app.
 */
@Composable
fun BottomNav(navController: NavController) {
    NavigationBar (
        containerColor = Color(0xFFB22222),
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry.value?.destination

        val items = listOf(
            Destination.Scan to R.drawable.ic_scan,
            Destination.Profile to R.drawable.ic_profile,
            Destination.Lists to R.drawable.ic_lists
        )

        items.forEach { (destination, icon) ->
            val contentDesc = when (destination.route) {
                Destination.Scan.route -> "Scan products"
                Destination.Profile.route -> "View profile"
                Destination.Lists.route -> "View saved lists"
                else -> "Navigation item"
            }

            NavigationBarItem(
                selected = currentDestination?.route == destination.route,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = contentDesc,
                        modifier = Modifier
                            .size(30.dp)
                    )
                },
                label = {
                    Text(
                        text = destination.route,
                        fontSize = 15.sp
                    )
                },
                modifier = Modifier.padding(top = 8.dp),
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color(0xFFDDDDDD),
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color(0xFFDDDDDD),
                    indicatorColor = Color(0xFF8B1A1A)
                )
            )
        }
    }
}