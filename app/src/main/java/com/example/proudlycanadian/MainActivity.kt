package com.example.proudlycanadian

import SignUpScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.proudlycanadian.ui.navigation.BottomNav
import com.example.proudlycanadian.ui.navigation.Destination
import com.example.proudlycanadian.ui.screens.ListsScreen
import com.example.proudlycanadian.ui.screens.ProfileScreen
import com.example.proudlycanadian.ui.screens.ScanScreen
import com.example.proudlycanadian.ui.screens.SplashScreen
import com.example.proudlycanadian.ui.theme.ProudlyCanadianTheme

/**
 * Proudly Canadian App
 *
 * This application helps users determine if a product is Canadian in origin by scanning barcodes
 * or entering UPC numbers. Users can create and manage collections of products, view product details,
 * and access their profile information. The app integrates with GS1 API (to be added later) to fetch
 * product details.
 **/
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ProudlyCanadianTheme {
                val navController = rememberNavController()
                App(navController = navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        // Conditionally render TopAppBar only on non-Splash screens
        topBar = {
            if (currentRoute != Destination.Splash.route) {
                TopAppBar(
                    title = {
                        Text(
                            "Proudly Canadian",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }
        },
        // Conditionally render BottomNav only on non-Splash screens
        bottomBar = {
            if (currentRoute != Destination.Splash.route) {
                BottomNav(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Destination.Splash.route, // Start at SplashScreen
            modifier = Modifier.padding(paddingValues)
        ) {
            // Splash Screen
            composable(Destination.Splash.route) {
                SplashScreen(
                    onLoginClick = { navController.navigate(Destination.Scan.route) },
                    onSignUpClick = { navController.navigate(Destination.SignUp.route) }
                )
            }

            // Sign-Up Screen
            composable(Destination.SignUp.route) {
                SignUpScreen(navController)
            }

            // Main Screens
            composable(Destination.Scan.route) {
                ScanScreen()
            }
            composable(Destination.Profile.route) {
                ProfileScreen()
            }
            composable(Destination.Lists.route) {
                ListsScreen()
            }
        }
    }
}