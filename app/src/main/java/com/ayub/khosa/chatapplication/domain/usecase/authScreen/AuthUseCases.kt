package com.ayub.khosa.chatapplication.domain.usecase.authScreen

import com.ayub.khosa.chatapplication.domain.usecase.authScreen.SetUserStatusToFirebase

data class AuthUseCases(
    val isUserAuthenticated: IsUserAuthenticatedInFirebase,
    val signIn: SignIn,
    val onSignInWithGoogle: OnSignInWithGoogle,
    val setUserStatusToFirebase: SetUserStatusToFirebase,
    val signUp: SignUp,
)