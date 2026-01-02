package com.ayub.khosa.chatapplication.screens.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


@Composable
fun BottomNavigationBar() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            bottomBarState.value =
                currentRoute != Screens.SignUp.fullRoute && currentRoute != Screens.SignIn.fullRoute && currentRoute != Screens.Chat.fullRoute

            BottomNavigationView(
                navController = navController,
                bottomBarState = bottomBarState.value
            )

        }
    ) { paddingValues ->

        NavHostContainer(navController = navController, padding = paddingValues)


    }
}