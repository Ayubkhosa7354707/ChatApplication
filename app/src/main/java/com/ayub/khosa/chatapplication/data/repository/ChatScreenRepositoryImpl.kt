package com.ayub.khosa.chatapplication.data.repository

import android.annotation.SuppressLint
import com.ayub.khosa.chatapplication.domain.model.ChatMessage
import com.ayub.khosa.chatapplication.domain.model.Data
import com.ayub.khosa.chatapplication.domain.model.Notification
import com.ayub.khosa.chatapplication.domain.model.User
import com.ayub.khosa.chatapplication.domain.repository.ChatScreenRepository
import com.ayub.khosa.chatapplication.utils.Constant
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.ayub.khosa.chatapplication.utils.Response
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.UUID
import javax.inject.Inject


class ChatScreenRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
) : ChatScreenRepository {
    override fun insertMessageToFirebase(
        chatRoomUUID: String,
        messageTitle:String,
        messageContent: String,
        reciver_UUID: String,
        reciver_fcmtoken: String
    ): Flow<Response<Boolean>> = callbackFlow {
        try {
            this@callbackFlow.trySendBlocking(Response.Loading)



            if (firebaseAuth.currentUser != null) {
                val userUUID: String = firebaseAuth.currentUser?.uid.toString()
                val userEmail = firebaseAuth.currentUser?.email
                UUID.randomUUID().toString()
                var accessToken = "empty access token"
                val deferredResult = async(Dispatchers.IO) {
                try {

                    val jsonString = "{\n" +
                            "  \"type\": \"service_account\",\n" +
                            "  \"project_id\": \"chatapplication-563d0\",\n" +
                            "  \"private_key_id\": \"7101fde523d0592a5f6c8a11b0d6e6a5de94b2ad\",\n" +
                            "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDGitIW+E5mOZua\\n70FUTfO2g9i6OLD/x4mlIqcLvbOwnFrkJPcyUG7su/BBFx+an1WdadNuhzD1RpMB\\nrfFO/X1vyYO8w+vPetbPLxJA3mHFLTXp+EnHBnF7iYt8jBX6nQK/Kgtq5pk8f+KO\\n0iGCW7y9+WMgoYF+b2WI31goSkvZea+J04m/PjfwxmVhS7/6mUZwMHmbcm1H02+Q\\nmhcy/IP7t+aVDWzxVzoVdZ0IvOgCpVL/sSsb6FmLrtixnW4d6aZclE9AI0A3l5Q/\\niYpk4cYWaUkPH9L0UclwEv46742EtuYYabg3dsHS3vTezmLGbFclXy+NHnHfw3II\\ndHXoiP/TAgMBAAECggEAB/JAbFjt0luV5cKTq8se6Pwn7XQdaW9UC/7yDp8+FOB9\\nJspv9lyPw/vVptVFF4wvAgjdRHw2smprsdKDaPTuU04ObryssHYrKMmqglwkL58a\\nnE2qrkOMwTQFGCwHKSulk38cS+AE3A4GRE8Wtj/zsurLU+AC7cuJlcvSnHI0zPRm\\nzPnhNyj9eKstDMM5n8W6rxoVfONJBvHro/vuhSntUpl9sGhV1ZnN8P8W/T+zSmek\\npoiJBJx1p1lG/dpxoxHBkvRIBwY7jqq1nZISs6gQLdp2tO7uA4U3d2KV6Ky+9bb8\\nKFbMm1zBSfeEHRmbuuyZuEuP2UWyyXxA4RweOYXLEQKBgQDj81WIFBWV1el2jgfn\\nr6L/SwsQoUfha82bpCvNdH+NR16NTtAm75fYwyWS9lrwx4XMstc8FNp8pSWTOMso\\nykdT3OiMwoau2FmhReKl1pi1VpjoQC7dzItV1FhcDpdzfE98kIQL/7XyPsS8cPE4\\n4tJ4KZ0fmGx3uKcnQGNPvDqfywKBgQDe+Rj5RXD9S4BW9+9iffisXvJoAQUsrcQv\\nSOXk6nYVl3poBYeFpaZwkPe24pcBqedNexg+wD+1r01pFSGR/R+sUTpzNgv33CRA\\n1MpLWmLY7mc105BXMpYLUJLI5O+cFhK1oDQMoQa5pWV19BaqX7ayfSVOPVo7CaHF\\nVFDZGwePGQKBgClRRvSWZ180Wc/iaK5nGI1bpdLK2QOUDb2jYUkpT6FlCU9ltLcK\\nAKUKojnkK5GvKKJ03CyIvx2OtaPczH5FQ+6AgSpabB0vG/fueemDX0SnQKoAUD5l\\nv++7wJRXFL6bDrFOZJDkNa+GDxT1B9Q+0NV0/YTnlOmSb8HemuGuBZZHAoGBAIpu\\nPIMGCdA4P8W+yHBtpSXfDMBlYcELYPG6YIBu0EE3eld6l1jyLxNT7Xct1nLB+Uix\\nU6whz2wNGhJTfDQW4sMo6xD8TRHlKtzVP4Lo6tWjrZBNeR+p2F3N+FOhTuGpEpSQ\\nkcAFeRdpCETi9e9w3QyThcW9cXeV00mfv1IK5faBAoGAGKFxqM8gsOB1NJTWpxsI\\nIAySoJ+oSyVneFHMoV58VXKmZ/PMrQDzd9WzK8N88edDNnrSCkYZK4/3hZiGG/dI\\n1PIbyugGm/5wUrf3RFUBKaNis2xIk87hiqcQ0+UvNSNKutz8d922f6rc1tgVApLD\\nsCwe/m9j376/6iU3fXiYeW4=\\n-----END PRIVATE KEY-----\\n\",\n" +
                            "  \"client_email\": \"firebase-adminsdk-fbsvc@chatapplication-563d0.iam.gserviceaccount.com\",\n" +
                            "  \"client_id\": \"105318778642210417528\",\n" +
                            "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                            "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                            "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                            "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-fbsvc%40chatapplication-563d0.iam.gserviceaccount.com\",\n" +
                            "  \"universe_domain\": \"googleapis.com\"\n" +
                            "}"
                    PrintLogs.printInfo("  credentials.json ->  " + jsonString)
                    var stream: InputStream =
                        ByteArrayInputStream(jsonString.toByteArray(StandardCharsets.UTF_8))
                    val credentials = GoogleCredentials.fromStream(stream)
                        .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
                    credentials.refreshIfExpired()

                    PrintLogs.printInfo("Google Access Token   -> " + credentials.accessToken.tokenValue)
                    accessToken = credentials.accessToken.tokenValue // This is the result
                } catch (e: Exception) {
                    e.printStackTrace()
                    PrintLogs.printD("sendMessage Exception  " + e.message)
                    this@callbackFlow.trySendBlocking(Response.Error("Error ->" + e.message))
                }
                }
                 deferredResult.await()


                PrintLogs.printInfo("Google Access Token: $accessToken")

                val deferredResult1 = async(Dispatchers.IO) {
                    try {
                        val deferredResult1 = async(Dispatchers.IO) {
                            val client = OkHttpClient.Builder()
                                .protocols(
                                    listOf(
                                        Protocol.HTTP_2,
                                        Protocol.HTTP_1_1
                                    )
                                ) // Prioritize H2, fallback to HTTP/1.1
                                .build()

                            // Build JSON payload
                            val jsonObject = JSONObject()
                            val messageObject = JSONObject()
                            val notificationObject = JSONObject()
                            val dataObject = JSONObject()

                            // Set notification title and body
                            notificationObject.put("title", messageTitle)
                            notificationObject.put("body", messageContent)
//               Add user data

                            dataObject.put("sender_Id", userUUID)
                            dataObject.put("sender_email", userEmail)

                            dataObject.put("reciver_Id", reciver_UUID)

                            // Construct message with notification and data
                            val messageUUID = UUID.randomUUID().toString()
                            dataObject.put("messageUUID", messageUUID)
                            messageObject.put("notification", notificationObject)
                            messageObject.put("data", dataObject)
                            messageObject.put("token", reciver_fcmtoken)
//                            messageObject.put("token", "fcmtoken")
                            jsonObject.put("message", messageObject)
                            PrintLogs.printInfo(" jsonObject ->  " + jsonObject.toString())


                            val JSON: MediaType? =
                                "application/json; charset=utf-8".toMediaTypeOrNull()

                            val requestBody: RequestBody = jsonObject.toString().toRequestBody(JSON)

                            val url = Constant.FCM_URL
                            val request = Request.Builder()
                                .url(url)
                                .post(requestBody)
                                .addHeader("Authorization", "Bearer $accessToken")
                                .addHeader("Content-Type", "application/json")
                                .build()
                            val call = client.newCall(request)

                            call.enqueue(object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                    e.printStackTrace()
                                    PrintLogs.printInfo(" onFailure  " + e.message)
                                    this@callbackFlow.trySendBlocking(Response.Error("Error ->" + e.message))
                                }
                                @SuppressLint("SuspiciousIndentation")
                                override fun onResponse(call: Call, response: okhttp3.Response) {


                                    PrintLogs.printInfo(" response -> " + response.toString())
                                    if (response.isSuccessful) {
                                        PrintLogs.printInfo(" response.isSuccessful ")

                                        var databaseReference =
                                            firebaseDatabase.getReference("Chat_Rooms")
                                                .child(chatRoomUUID)
                                                .child(messageUUID )

                                        var chatMessage : ChatMessage = ChatMessage(
                                            messageUUID = messageUUID,
                                            notification = Notification(messageTitle, body = messageContent),
                                            data = Data(reciverID = reciver_UUID,userUUID),
                                        )
                                        PrintLogs.printInfo(" chatMessage : "+chatMessage.toString())
                                        databaseReference.setValue(chatMessage)
                                        this@callbackFlow.trySendBlocking(Response.Success(true))


                                    } else {
                                        PrintLogs.printInfo(" response.isSuccessful not ")
                                        this@callbackFlow.trySendBlocking(Response.Error("Error ->" +"response.isSuccessful not  "))
                                    }

                                }
                            })

                        }

                    } catch (e: Exception) {
                        PrintLogs.printE("Exception  " + e.message)
                        this@callbackFlow.trySendBlocking(Response.Error("Error ->" + e.message))
                    }
                }
                deferredResult1.await()


            } else {
                this@callbackFlow.trySendBlocking(Response.Success(false))
            }

        } catch (e: Exception) {
            this@callbackFlow.trySendBlocking(Response.Error("Error ->" + e.message))
        }

        awaitClose {
            channel.close()
            cancel()
        }
    }

    override fun loadMessageFromFirebase(
        chatRoomUUID: String,
        registerUUID: String
    ): Flow<Response<List<ChatMessage>>> = callbackFlow {

        PrintLogs.printInfo("loadMessageFromFirebase  Chat repository impl "+chatRoomUUID+"  "+registerUUID)
        try{
            this@callbackFlow.trySendBlocking(Response.Loading)


            var databaseReference =
                firebaseDatabase.getReference("Chat_Rooms")
                    .child(chatRoomUUID)
            var messageList = arrayListOf<ChatMessage>()

            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    messageList = arrayListOf<ChatMessage>()
                    // Get the data as a specific type, e.g., String
                    for (i in dataSnapshot.children) {

                        val chatMessage: ChatMessage = i.getValue(ChatMessage::class.java) as ChatMessage
                        PrintLogs.printInfo(  "chatMessage id -> ` "+chatMessage.messageUUID)

                        messageList.add(chatMessage)
                    }
                    this@callbackFlow.trySendBlocking(Response.Success(messageList))

                    // You can also get custom objects if you have a data class
                    // val user = dataSnapshot.getValue<User>()
                    // Log.d("TAG", "User name: ${user?.name}")
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle potential errors here
                    this@callbackFlow.trySendBlocking(Response.Error("Error -> " + error.message))
                }
            })



            awaitClose {
                channel.close()
                cancel()
            }
    } catch (e: Exception) {
            PrintLogs.printE("Error ->" + e.message)
        this@callbackFlow.trySendBlocking(Response.Error("Error ->" + e.message))
    }


}

//    private   fun getfcmtoken(registerUUID: String): Flow<Response<String>> = callbackFlow {
//
//
//        var fcmtoken = ""
//        val deferredResult = async(Dispatchers.IO) {
//            try {
//                var myfirebaseDatabase = FirebaseDatabase.getInstance()
//                myfirebaseDatabase.getReference("Profiles").child(registerUUID).child("profile")
//                    .child("fcmToken").get().addOnSuccessListener {
//                        fcmtoken = it.value.toString()
//                    }
//            }catch (e: Exception) {
//                PrintLogs.printE("Exception  " + e.message)
//            }
//        }
//        deferredResult.await()
//        this@callbackFlow.trySendBlocking(Response.Success(fcmtoken))
//
//
//
//    }

}