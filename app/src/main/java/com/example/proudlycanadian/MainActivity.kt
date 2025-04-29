package com.example.proudlycanadian

import GoogleSignInScreen
import SignUpScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proudlycanadian.api.ProductManager
import com.example.proudlycanadian.navigation.BottomNav
import com.example.proudlycanadian.navigation.Destination
import com.example.proudlycanadian.ui.screens.AddToListScreen
import com.example.proudlycanadian.ui.screens.ListsScreen
import com.example.proudlycanadian.ui.screens.ProfileScreen
import com.example.proudlycanadian.ui.screens.ScanScreen
import com.example.proudlycanadian.ui.screens.SignInScreen
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

/**
 * Purpose: Main app composable that handles navigation and conditional UI elements like TopAppBar and BottomNav.
 * @param navController: NavHostController - Controller that manages app navigation.
 **/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val productManager = remember { ProductManager() }

    Scaffold(
        // Conditionally show the TopAppBar
        topBar = {
            if (currentRoute != Destination.Splash.route) {
                TopAppBar(
                    title = {
                        Text(
                            "Proudly Canadian",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    modifier = Modifier
                        .height(60.dp)
                        .padding(top=10.dp)
                )
            }
        },
        // Conditionally show the BottomNav
        bottomBar = {
            if (currentRoute != Destination.Splash.route &&
                currentRoute != Destination.SignIn.route &&
                currentRoute != Destination.GoogleSignIn.route &&
                currentRoute != Destination.SignUp.route
            ) {
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
                    onLoginClick = { navController.navigate(Destination.SignIn.route) },
                    onGoogleSignInClick = { navController.navigate(Destination.GoogleSignIn.route) },
                    onSignUpClick = { navController.navigate(Destination.SignUp.route) }
                )
            }

            // Sign-Up Screen
            composable(Destination.SignUp.route) {
                SignUpScreen(navController)
            }

            // Scan Screen
            composable(Destination.Scan.route) {
                ScanScreen(
                    productManager = productManager,
                    navController = navController
                )
            }

            // Profile Screen
            composable(Destination.Profile.route) {
                ProfileScreen(navController)
            }

            // Lists Screen
            composable(Destination.Lists.route) {
                ListsScreen()
            }

            // Email Sign-In Screen
            composable(Destination.SignIn.route) {
                SignInScreen(navController)
            }

            // Google Sign-In Screen
            composable(Destination.GoogleSignIn.route) {
                GoogleSignInScreen(navController = navController)
            }

            // Add to List Screen
            composable(
                route = "add_to_list?upc={upc}&name={name}&image={image}&origin={origin}",
                arguments = listOf(
                    navArgument("upc") { type = NavType.StringType; defaultValue = "" },
                    navArgument("name") { type = NavType.StringType; defaultValue = "" },
                    navArgument("image") { type = NavType.StringType; defaultValue = "" },
                    navArgument("origin") { type = NavType.StringType; defaultValue = "" },
                )
            ) { backStackEntry ->
                val upc = backStackEntry.arguments?.getString("upc")
                val name = backStackEntry.arguments?.getString("name")
                val image = backStackEntry.arguments?.getString("image")
                val origin = backStackEntry.arguments?.getString("origin")

                AddToListScreen(
                    navController = navController,
                    upc = upc,
                    name = name,
                    image = image,
                    origin = origin
                )
            }
        }
    }
}