package com.ayub.khosa.chatapplication.screens.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationView(
    navController: NavController,
    bottomBarState: Boolean,
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    AnimatedVisibility(
        visible = bottomBarState,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {

        NavigationBar(containerColor = Color(0xFF0F9D58)) {
            BottomNavigationItem().bottomNavigationItems().forEachIndexed { index, navigationItem ->
                NavigationBarItem(

                    // it currentRoute is equal then its selected route
                    selected = navigationItem.route == currentRoute,
                    // navigate on click
                    onClick = {
                        navController.navigate(navigationItem.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    // Icon of navItem
                    icon = {
                        Icon(
                            navigationItem.icon,
                            contentDescription = navigationItem.label
                        )
                    },
                    // label
                    label = {
                        Text(navigationItem.label)
                    },
                    alwaysShowLabel = true,

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Cyan, // Icon color when selected
                        unselectedIconColor = Color.White, // Icon color when not selected
                        selectedTextColor = Color.Red, // Label color when selected
                        indicatorColor = Color(0xFF195334) // Highlight color for selected item
                    )


                )
            }
        }
    }

}