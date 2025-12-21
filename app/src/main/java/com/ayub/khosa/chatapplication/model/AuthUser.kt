package com.ayub.khosa.chatapplication.model

data class AuthUser(
    var id: String = "",
    var email: String = "",
    var displayName: String = "",
    var fcmToken: String = "temp token",
)