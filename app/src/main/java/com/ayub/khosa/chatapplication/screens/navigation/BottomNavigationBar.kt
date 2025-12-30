package com.ayub.khosa.chatapplication.screens.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


@Composable
fun BottomNavigationBar() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            bottomBarState.value = currentRoute != Screens.Home.route
                    && currentRoute != Screens.SignUp.route && currentRoute != Screens.SignIn.route
            BottomNavigationView(navController = navController, bottomBarState = bottomBarState.value )

        }
    ) {paddingValues ->

        NavHostContainer(navController = navController, padding = paddingValues)


    }
}