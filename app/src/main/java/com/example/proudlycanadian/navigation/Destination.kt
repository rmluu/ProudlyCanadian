package com.example.proudlycanadian.navigation

import android.net.Uri
import com.example.proudlycanadian.api.model.FirestoreProduct

/**
 * Purpose: Defines various screen destinations for navigation.
 * @param route: route string representing the destination's path in the app's navigation graph
 */
sealed class Destination (val route: String) {
    object Splash:   Destination("Splash")
    object Scan:     Destination("Scan")
    object Profile:  Destination("Profile")
    object Lists:    Destination("Lists")
    object SignUp:   Destination("SignUp")
    object AddToList: Destination("AddToList/{upc}/{name}/{image}/{origin}") {
        fun createRoute(product: FirestoreProduct): String {
            return "add_to_list?" +
                    "upc=${product.upc}" +
                    "&name=${Uri.encode(product.name)}" +
                    "&image=${Uri.encode(product.image)}" +
                    "&origin=${Uri.encode(product.origin)}"
        }
    }
    object SignIn : Destination("SignIn")
    object GoogleSignIn : Destination("GoogleSignIn")
}