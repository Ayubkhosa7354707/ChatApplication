package com.ayub.khosa.chatapplication.screens.navigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.ayub.khosa.chatapplication.screens.navigation.Screens


data class BottomNavigationItem(
    val label : String = "",
    val icon : ImageVector = Icons.Filled.Home,
    val route : String = ""
) {
    fun bottomNavigationItems() : List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Home",
                icon = Icons.Filled.Home,
                route = Screens.Home.route
            ),
            BottomNavigationItem(
                label = "SignIn",
                icon = Icons.Filled.AccountCircle,
                route = Screens.SignIn.route
            ),BottomNavigationItem(
                label = "SignUp",
                icon = Icons.Filled.AccountCircle,
                route = Screens.SignUp.route
            )
        )
    }
}