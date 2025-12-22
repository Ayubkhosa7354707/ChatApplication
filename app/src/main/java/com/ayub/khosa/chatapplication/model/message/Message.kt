package com.ayub.khosa.chatapplication.model.message


data class Message(
    val notification: Notification,
    val data: Data
)

data class Data(
    val reciverID: String = "",
    val senderID: String = "",
    val Date: String = System.currentTimeMillis().toString()
)

data class Notification(
    val title: String = "",
    val body: String = ""

)
