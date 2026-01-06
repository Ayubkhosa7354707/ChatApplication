package com.ayub.khosa.chatapplication.domain.model

data class ChatMessage(
    var messageUUID: String? = "",
    val notification: Notification = Notification(),
    val data: Data = Data(reciverID = "uuid", senderID = "uuid"),
    var date: Long = 0,
)

data class Data(
    val reciverID: String? = "",
    val senderID: String? = "",
)

data class Notification(
    val title: String? = "",
    val body: String? = ""
)