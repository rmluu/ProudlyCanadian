package com.example.proudlycanadian

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
import androidx.navigation.compose.rememberNavController
import com.example.proudlycanadian.ui.navigation.BottomNav
import com.example.proudlycanadian.ui.navigation.Destination
import com.example.proudlycanadian.ui.screens.ListsScreen
import com.example.proudlycanadian.ui.screens.ProfileScreen
import com.example.proudlycanadian.ui.screens.ScanScreen
import com.example.proudlycanadian.ui.screens.SplashScreen
import com.example.proudlycanadian.ui.theme.ProudlyCanadianTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ProudlyCanadianTheme {
                DisplayContent()
            }
        }
    }
}

@Composable
fun DisplayContent() {
    val isLoggedIn = remember { mutableStateOf(false) }

    if (isLoggedIn.value) {
        // Show main content
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val navController = rememberNavController()
            App(navController = navController, modifier = Modifier.padding(innerPadding))
        }
    } else {
        // Show splash screen until user is logged in
        SplashScreen(
            onLoginClick = {
                // Temporary - set logged-in state to true when log in button pressed
                isLoggedIn.value = true
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(navController: NavHostController, modifier: Modifier) {
    Scaffold(
        topBar = { TopAppBar(
            title = {
                Text(
                    "Proudly Canadian",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        ) },
        bottomBar = { BottomNav(navController = navController) }
    ) { paddingValues ->
        Spacer(modifier.padding(paddingValues))

        NavHost(
            navController = navController,
            startDestination = Destination.Scan.route
        ) {
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