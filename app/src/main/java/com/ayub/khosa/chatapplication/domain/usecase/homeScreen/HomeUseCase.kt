package com.ayub.khosa.chatapplication.domain.usecase.homeScreen


data class HomeUseCase (

    val isUserAuthenticated: IsUserSignOutInFirebase,
    val signOut: SignOut,
    val setUserStatusToFirebase: com.ayub.khosa.chatapplication.domain.usecase.homeScreen.SetUserStatusToFirebase,
)
