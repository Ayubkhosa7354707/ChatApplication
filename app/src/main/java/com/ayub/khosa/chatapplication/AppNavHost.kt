package com.ayub.khosa.chatapplication

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ayub.khosa.chatapplication.screens.HomeScreen
import com.ayub.khosa.chatapplication.screens.LoginScreen
import com.ayub.khosa.chatapplication.viewmodel.AuthViewModel


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),

    ) {

    val viewModel: AuthViewModel = hiltViewModel()

    val currentUser = viewModel.currentUser
    val start = if (currentUser != null) "ROUTE_HOME" else "ROUTE_LOGIN"


    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = start
    ) {
        composable("ROUTE_LOGIN") {
            LoginScreen( navController)
        }
        composable("ROUTE_HOME") {
            HomeScreen(navController)
        }
    }
}

