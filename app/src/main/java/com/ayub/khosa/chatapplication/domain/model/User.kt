package com.ayub.khosa.chatapplication.domain.model

data class User(
    var profileUUID: String = "",
    var userEmail: String = "",
    var userName: String = "",
    var fcmToken: String = "temp token",
)