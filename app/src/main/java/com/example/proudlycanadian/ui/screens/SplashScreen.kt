package com.example.proudlycanadian.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

/**
 * Purpose: Displays the welcome screen with the app name, logo, and sign-in options.
 * @param onLoginClick: () -> Unit - Triggered when the user clicks the "Log In with Email" button.
 * @param onGoogleSignInClick: () -> Unit - Triggered when the user clicks the "Sign In with Google" button.
 */
@Composable
fun SplashScreen(
    onLoginClick: () -> Unit,
    onGoogleSignInClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
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
                "Proudly Canadian",
                fontSize = 35.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )

            // Logo
            Image(
                painter = painterResource(id = R.mipmap.ic_welcome),
                contentDescription = "Proudly Canadian Logo",
                modifier = Modifier
                    .size(400.dp)
                    .padding(top = 50.dp, bottom = 30.dp)
            )

            // Buttons for Email Log in and Google Sign-In
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                val buttonWidth = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .then(Modifier.widthIn(max = 280.dp))

                Button(
                    onClick = { onLoginClick() },
                    modifier = buttonWidth,
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text("Sign In with Email", fontSize = 18.sp)
                }

                Button(
                    onClick = { onGoogleSignInClick() },
                    modifier = buttonWidth,
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text("Sign In with Google", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(
                    modifier = Modifier
                        .width(200.dp)
                        .align(Alignment.CenterHorizontally),
                    thickness = 1.dp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { onSignUpClick() },
                    modifier = buttonWidth,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF212121)
                    )
                ) {
                    Text("Register an Account", fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.weight(3f))
        }
    }
}