package com.ayub.khosa.chatapplication.screens.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ayub.khosa.chatapplication.screens.home.HomeScreen
import com.ayub.khosa.chatapplication.screens.auth.signin.SignInScreen
import com.ayub.khosa.chatapplication.screens.auth.signup.SignUpScreen


@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues
) {

    NavHost(
        navController = navController,

        // set the start destination as home
        startDestination = Screens.SignIn.route,

        // Set the padding provided by scaffold
        modifier = Modifier.padding(paddingValues = padding),

        builder = {

            composable(Screens.Home.route) {
                HomeScreen(navController)
            }
            composable(Screens.SignIn.route) {
                SignInScreen(navController)
            }
            composable(Screens.SignUp.route) {
                SignUpScreen(navController)
            }
        }
    )
}
