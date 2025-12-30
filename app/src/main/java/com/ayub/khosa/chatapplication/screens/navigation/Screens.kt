package com.ayub.khosa.chatapplication.screens.navigation

sealed class Screens(val route : String) {
    object Home : Screens("home")
//    object Profile : Screens("profile")
    object SignIn : Screens("SignIn")
    object SignUp : Screens("SignUp")
}