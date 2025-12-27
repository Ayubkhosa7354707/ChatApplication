package com.ayub.khosa.chatapplication.domain.usecase.authScreen

import com.ayub.khosa.chatapplication.domain.usecase.authScreen.SetUserStatusToFirebase

data class AuthUseCases(
    val isUserAuthenticated: IsUserAuthenticatedInFirebase,
    val signIn: SignIn,
    val setUserStatusToFirebase: SetUserStatusToFirebase,
)