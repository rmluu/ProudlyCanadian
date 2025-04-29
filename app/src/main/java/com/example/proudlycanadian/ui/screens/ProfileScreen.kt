package com.example.proudlycanadian.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proudlycanadian.navigation.Destination
import com.google.firebase.auth.FirebaseAuth

/**
 * Purpose: Displays the user's profile details.
 *          Provides a button to log out and navigate back to the Splash screen.
 * @param navController: NavController - A navigation controller to navigate between screens.
 */
@Composable
fun ProfileScreen(navController: NavController) {
    // Get the current user from Firebase Authentication
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    // Check if the user is logged in
    if (user != null) {
        // Display user details
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
                .padding(vertical = 18.dp),
            horizontalAlignment = Alignment.Start
        ) {
            val email = user.email ?: "Email not available"
            val username = user.displayName?.takeIf { it.isNotBlank() } ?: email // Use email if display name is not available

            // Username Field
            Text(text = "Username: $username", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(20.dp))

            // Email Field
            Text(text = "Email: $email", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(20.dp))

            // Log Out Button
            Button(
                onClick = {
                    auth.signOut() // Log the user out
                    navController.navigate(Destination.Splash.route) { // Navigate to splash screen after log out
                        popUpTo(Destination.Profile.route) { inclusive = true } // Clear all screens up to Profile
                    }
                },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(text = "Log Out", fontSize = 18.sp)
            }
        }
    }
}