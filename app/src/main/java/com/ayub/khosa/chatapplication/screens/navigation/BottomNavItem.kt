package com.ayub.khosa.chatapplication.screens.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Message
import androidx.compose.ui.graphics.vector.ImageVector


data class BottomNavigationItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val route: String = ""
) {
    fun bottomNavigationItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Home",
                icon = Icons.Filled.Home,
                route = Screens.Home.screen_route
            ),
            BottomNavigationItem(
                label = "UserList",
                icon = Icons.Filled.Message,
                route = Screens.UserList.screen_route
            ),
//            BottomNavigationItem(
//                label = "Chat",
//                icon = Icons.Filled.AccountCircle,
//                route = Screens.Chat.screen_route
//            ),
//            BottomNavigationItem(
//                label = "SignUp",
//                icon = Icons.Filled.AccountCircle,
//                route = Screens.SignUp.route
//            )
        )
    }
}