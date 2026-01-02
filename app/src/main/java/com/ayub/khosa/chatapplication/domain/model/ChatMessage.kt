package com.ayub.khosa.chatapplication.domain.model

data class ChatMessage(
    var messageUUID: String? = "",
    val notification: Notification= Notification(),
    val data: Data= Data(),
    val Date: String? = System.currentTimeMillis().toString()
)

data class Data(
    val reciverID: String? = "",
    val senderID: String ? = "",
)

data class Notification(
    val title: String ?= "",
    val body: String ?= ""
)