package com.example.proudlycanadian.ui.navigation

sealed class Destination (val route: String) {
    object Splash:   Destination("Splash")
    object Scan:     Destination("Scan")
    object Profile:  Destination("Profile")
    object Lists:    Destination("Lists")
    object SignUp:   Destination("SignUp")
}