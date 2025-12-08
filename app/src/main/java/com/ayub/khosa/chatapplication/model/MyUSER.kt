package com.ayub.khosa.chatapplication.model

data class MyUSER(
    var userId: String,
    var name: String,
    var email: String,
    var fcmToken: String,
) {


    constructor() : this("", "", "", "")
}

