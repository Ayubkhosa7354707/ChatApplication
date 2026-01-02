package com.ayub.khosa.chatapplication.domain.usecase.userslist

data class UserListScreenUseCases(
    val searchUserFromFirebase: SearchUserFromFirebase,
    val createChatRoomToFirebase: CreateChatRoomToFirebase,
    val loadFriendListFromFirebase: LoadFriendListFromFirebase,
    val checkChatRoomExistedFromFirebase : CheckChatRoomExistedFromFirebase
)

