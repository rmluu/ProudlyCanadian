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

/**
 * ProfileScreen - Displays user details such as name and email.
 * Provides an option to change the password (functionality to be added later).
 */
@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 18.dp, top = 90.dp, end = 18.dp),
        horizontalAlignment = Alignment.Start
    ) {
        var username = "rluu"
        var email = "rluu@rrc.ca"

        // Username Field
        Text(text = "Username: $username")
        Spacer(modifier = Modifier.height(20.dp))

        // Email Field
        Text(text = "Email: $email")
        Spacer(modifier = Modifier.height(15.dp))

        // Change Password Button
        Button(
            onClick = { /* Handle change password */ },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text(text = "Change password", fontSize = 18.sp)
        }
    }
}