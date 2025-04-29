import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.proudlycanadian.R
import com.example.proudlycanadian.navigation.Destination
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest

/**
 * Purpose: Displays a Google Sign-In screen. Launches the sign-in flow and handles authentication with Firebase.
 * @param navController: NavController - Used to navigate to the next screen after successful login.
 * @param auth: FirebaseAuth - Instance used to authenticate with Firebase (default is FirebaseAuth.getInstance()).
 */
@Composable
fun GoogleSignInScreen(
    navController: NavController,
    auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Receive the result from the Google Sign-In intent
    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task, navController, context)
        } else {
            Toast.makeText(context, "Google Sign-In failed.", Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(2f))

        Image(
            painter = painterResource(id = R.mipmap.ic_google),
            contentDescription = "Google Logo",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 40.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(onClick = {
            coroutineScope.launch {
                val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

                val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
                val signInIntent = googleSignInClient.signInIntent

                // Launch sign-in intent
                signInLauncher.launch(signInIntent)
                }
            },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFF212121)
            )
        ) {
            Text("Sign in with Google", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.weight(3f))
    }
}

/**
 * Purpose: Handles the result of the Google sign-in process and authenticates the user with Firebase.
 * @param task: Task<GoogleSignInAccount> - The task returned from Google Sign-In API.
 * @param navController: NavController - Used to navigate to the next screen upon successful sign-in.
 */
fun handleSignInResult(
    task: Task<GoogleSignInAccount>,
    navController: NavController,
    context: Context
) {
    if (task.isSuccessful) {
        val account = task.result
        if (account != null) {
            val idToken = account.idToken
            val email = account.email
            val googleDisplayName = account.displayName

            // Use the idToken to authenticate with Firebase
            val credential = GoogleAuthProvider.getCredential(idToken, null)

            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        val user = authTask.result?.user
                        if (user != null) {
                            // Update the displayName in Firebase Authentication if needed
                            if (user.displayName != googleDisplayName) {
                                val profileUpdates = userProfileChangeRequest {
                                    displayName = googleDisplayName
                                }

                                user.updateProfile(profileUpdates).addOnCompleteListener { profileUpdateTask ->
                                    if (profileUpdateTask.isSuccessful) {
                                        Log.d("Profile Update", "Firebase displayName updated to $googleDisplayName.")
                                    } else {
                                        Log.e("Profile Update", "Error updating displayName in Firebase Authentication.")
                                    }
                                }
                            }
                        }
                        // Navigate to the Scan screen on success
                        navController.navigate(Destination.Scan.route)
                    } else {
                        Toast.makeText(context, "Firebase Authentication Failed", Toast.LENGTH_LONG).show()
                    }
                }
        }
    } else {
        Toast.makeText(context, "Google Sign-In failed.", Toast.LENGTH_LONG).show()
    }
}