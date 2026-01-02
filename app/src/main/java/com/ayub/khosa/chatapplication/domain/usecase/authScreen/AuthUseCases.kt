package com.ayub.khosa.chatapplication.domain.usecase.authScreen

data class AuthUseCases(
    val isUserAuthenticated: IsUserAuthenticatedInFirebase,
    val signIn: SignIn,
    val onSignInWithGoogle: OnSignInWithGoogle,
    val setUserStatusToFirebase: SetUserStatusToFirebase,
    val signUp: SignUp,
    val getfcmtoken: Getfcmtoken,
)