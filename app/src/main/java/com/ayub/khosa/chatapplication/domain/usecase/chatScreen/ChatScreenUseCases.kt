package com.ayub.khosa.chatapplication.domain.usecase.chatScreen

data class ChatScreenUseCases(
    val insertMessageToFirebase: InsertMessageToFirebase,
    val loadMessageFromFirebase: LoadMessageFromFirebase,

)