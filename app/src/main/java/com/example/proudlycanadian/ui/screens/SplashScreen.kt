package com.example.proudlycanadian.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proudlycanadian.R
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

/**
 * SplashScreen - The welcome screen of the app.
 * Displays the app name, logo, and provides options for the user to log in or sign up for an account.
 */
@Composable
fun SplashScreen(onLoginClick: () -> Unit) {
    // Temporary log-in bypass
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        Toast.makeText(context, "For testing purposes - hit 'log in' to continue", Toast.LENGTH_SHORT).show()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.weight(2f))

            // App name
            Text(
                text = "Proudly Canadian",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 50.dp)
            )

            // Logo
            Image(
                painter = painterResource(id = R.mipmap.ic_logo),
                contentDescription = "Proudly Canadian Logo",
                modifier = Modifier
                    .size(250.dp)
                    .padding(bottom = 50.dp)
            )

            // Buttons for Log in and Sign up
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = {
                        // Temporarily redirects to MainActivity on click
                        onLoginClick()
                    },
                    modifier = Modifier.padding(end = 60.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Log In", fontSize = 18.sp)
                }

                OutlinedButton(
                    onClick = { /* Handle signing up for an account */ },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Sign Up", fontSize = 18.sp)
                }
            }
            Spacer(modifier = Modifier.weight(3f))
        }
    }
}