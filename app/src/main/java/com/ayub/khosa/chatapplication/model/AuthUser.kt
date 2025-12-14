package com.ayub.khosa.chatapplication.model

data class AuthUser(
    var id: String = "",
    var email: String = "",
    val provider: String = "",
    var displayName: String = "",
    val isAnonymous: Boolean = true
)