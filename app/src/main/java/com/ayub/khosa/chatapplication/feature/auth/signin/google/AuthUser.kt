package com.ayub.khosa.chatapplication.feature.auth.signin.google

data class AuthUser(
    val id: String = "",
    val email: String = "",
    val provider: String = "",
    val displayName: String = "",
    val isAnonymous: Boolean = true
)