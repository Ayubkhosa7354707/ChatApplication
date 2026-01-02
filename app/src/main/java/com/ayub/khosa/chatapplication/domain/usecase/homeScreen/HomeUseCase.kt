package com.ayub.khosa.chatapplication.domain.usecase.homeScreen


data class HomeUseCase(

    val isUserAuthenticated: IsUserSignOutInFirebase,
    val signOut: SignOut,
    val setUserStatusToFirebase: SetUserStatusToFirebase,
    val getUserFirebase: GetUserFirebase
)
