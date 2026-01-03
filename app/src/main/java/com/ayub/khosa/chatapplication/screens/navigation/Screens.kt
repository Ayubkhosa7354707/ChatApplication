package com.ayub.khosa.chatapplication.screens.navigation


sealed class Screens(
    var screen_route: String,
    var arguments: String
) {
    object Home : Screens("home", "") {
        val fullRoute = screen_route
    }

    object UserList : Screens("usrlist", "") {
        val fullRoute = screen_route + arguments
    }

    object SignIn : Screens("SignIn", "") {
        val fullRoute = screen_route + arguments
    }

    object SignUp : Screens("SignUp", "") {
        val fullRoute = screen_route + arguments
    }


    object Chat : Screens("chat", "/{chatRoomId}/{registerUUID}/{reciver_fcmtoken}") {
        val fullRoute = screen_route + arguments
    }
}