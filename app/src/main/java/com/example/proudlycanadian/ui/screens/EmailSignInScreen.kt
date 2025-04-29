package com.example.proudlycanadian.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proudlycanadian.navigation.Destination
import com.google.firebase.auth.FirebaseAuth

/**
 * Purpose: Displays a Sign-In screen allowing users to enter email and password to authenticate.
 * @param navController: NavHostController - Used to navigate to the Scan screen after successful login.
 */
@Composable
fun SignInScreen(navController: NavHostController) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var email by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )
        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text("Password")},
            visualTransformation = PasswordVisualTransformation(), // Hide the password while its entered
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
        )
        Button(
            onClick = {
                performSignIn(email, password, context, keyboardController, navController)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Sign In", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.weight(2f))
    }
}

/**
 * Purpose: Authenticates the user using FirebaseAuth and navigates to the Scan screen on success.
 * @param email: String - User's email address.
 * @param password: String - User's password.
 * @param context: Context - Used to show toast messages.
 * @param keyboardController: SoftwareKeyboardController? - Optional keyboard controller to hide keyboard after login.
 * @param navController: NavHostController - Used to navigate to the Scan screen after successful login.
 */
private fun performSignIn(
    email: String,
    password: String,
    context: Context,
    keyboardController: SoftwareKeyboardController?,
    navController: NavHostController
) {
    val auth = FirebaseAuth.getInstance()

    // Attempt Firebase sign-in with email and password
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Sign In Successful", Toast.LENGTH_SHORT).show()

                // Navigate to Scan screen and clear the Sign-In screen from the back stack
                navController.navigate(Destination.Scan.route) {
                    popUpTo(Destination.SignIn.route) { inclusive = true }
                }
            } else {
                Toast.makeText(context, "Sign In Failed", Toast.LENGTH_LONG).show()
            }
            // Hide keyboard after attempting login
            keyboardController?.hide()
        }
}