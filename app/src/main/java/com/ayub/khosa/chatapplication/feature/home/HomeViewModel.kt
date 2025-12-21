package com.ayub.khosa.chatapplication.feature.home

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayub.khosa.chatapplication.model.AuthUser
import com.ayub.khosa.chatapplication.repo.MainActivityRepository
import com.ayub.khosa.chatapplication.utils.Constant
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.messaging.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import kotlin.collections.ArrayList


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pref: SharedPreferences,
    private val repository: MainActivityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUser())
    val uiState: StateFlow<AuthUser> = _uiState.asStateFlow()
    // Expose as StateFlow

















    init {
        PrintLogs.printD(" HomeViewModel init ")
        getAuthUser()
        viewModelScope.launch (Dispatchers.IO) {
            Firebase.messaging.subscribeToTopic(Constant.KEY_Firebase_Topic).await()
        }



    }

    fun getAuthUser() {
        var firebaseuser: FirebaseUser? = Firebase.auth.currentUser
        _uiState.value.email = firebaseuser?.email as String
        _uiState.value.id = firebaseuser?.uid as String
        _uiState.value.displayName = firebaseuser?.displayName as String

        getfcmtoken()



    }


    @SuppressLint("SuspiciousIndentation")
    fun getfcmtoken() {
        PrintLogs.printD("getfcmtoken  ")
        viewModelScope.launch (Dispatchers.IO) {
            try {
                    val token = repository.getfcmtoken()
                    _uiState.value.fcmToken = token
                    PrintLogs.printInfo("Home view model FCM token  --> " + token)
                    var authUser = AuthUser()
                    authUser.id = _uiState.value.id
                    authUser.displayName = _uiState.value.displayName
                    authUser.email = _uiState.value.email
                    authUser.fcmToken = _uiState.value.fcmToken








            } catch (e: Exception) {
                PrintLogs.printD("Exception  " + e.message)
            }
        }
    }


    fun logout() {
        PrintLogs.printInfo("HomeViewModel logout   ")

        Firebase.auth.signOut()


    }


//    @SuppressLint("SuspiciousIndentation")
//
//
//    fun sendMessage(title: String, body: String) {
//        viewModelScope.launch {
//            try {
//
//
//                val deferredResult = async(Dispatchers.IO) {
//                    try {
//                        val jsonString = "{\n" +
//                                "  \"type\": \"service_account\",\n" +
//                                "  \"project_id\": \"chatapplication-563d0\",\n" +
//                                "  \"private_key_id\": \"acf3dc483d4dc8ee7d609ce1f795422a306609c7\",\n" +
//                                "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDJJJjLlTllHOvF\\nDvcYD0Hc8VXHCEycJ+mzDLtVMVVNAvOa7ZZNfSVtcWnBj4e6IXh3IgLNKZHmAPoM\\nngAAM4KeOJRwXRdGC9LjDu5j2W2CPgCMf81zsAWX9cMTd8bgGC9BRVxrWIc5wrqw\\nj5a9zlMCBqMIWiF6tk9s/xcCFhUJxcCMRsG+HZ3YT/LLZwfxMLSOXSRwn7dPPguz\\ny3hpDVQgIM7l9U9SkZ6GmMopSrVSNLfYuEHXrjel9IxGl2gUIp70xj3lCuPWQpWB\\n2juG6iwCoc1AZJGXCO3++AtXW2iKl6Ap2nOMVUd7M97YGVUXm0z8YN0ryz1cS1WU\\nmrCEw9+FAgMBAAECggEAAvaYj6s/oLc32/vX9RFIeZ5OkrOTGVvXLA2cdulWpd9M\\nwnQMmlClLAMXOURJjhrvIOYt32Lk5NwP0v7C5j3cB0OzICKm9J+4+CJgrbTm0Zvj\\nOy6o0rL9qhrzSEaj9eo1TANfFK/FsjqlsHM1F8/B1k5NuZJGWv3zlpn9d0jm/3FV\\nMIDIWZJ8kd4S/GTC88NBudJgvQZuRtB7CiNhps/D56xCKmDBQyc6y9I29pDIj5IH\\nuKpMztxdNKH12yIYimwZ/EHIgRjC9dU9TlUz+z2NMiUKMLW++eueve0iVDiGMll3\\n2CorhTMRigKTWdboWchnfYc2yxX56OoZ4/6N5sKQIwKBgQDmeYQ9QjIlBWy+MLvo\\nCmW0JS+SZTdQ/RxH/5CwYqAs4N27YW33v5cwc+L8SqIuZW+LDhl1q0vsPfrNsGAQ\\ndIm6kuoRbTshfGCDyjduUBETUyv91jRzigGqZOozIf7KUoymacuYb75Vg+F92iMh\\n5i7Anvmkux+6UmT+XBeafXGk6wKBgQDfa3WH+C9P5azBq9ZicnhpQYLMqvtYEgfR\\nl79HPqji/O6JeOIeMavTvmPuRqnRvDldZO43Ygfck0h/DyLhOu+er5wSQA9hJkJH\\n67wsJu4xSsYxlfRG+NoDbHtIZ4N76nGJStvmBHAJrQzQglWaSPRdL75eaMbSvphO\\nchBOBiMxTwKBgAm+q5O3f81G7HEiOF+4SNKUG+3Rr8QR36c/rAt5MBKVs2pf1Tl7\\ndVYMIFXQhgj2KwAzKWprQruLZ1ZZFca8VrJ30cPI3Y4t5xrLawqUAhTSGHMUIoWc\\nGTPuLmTZFB/T27SRlpUsbFzLRbZ/iaq+Q83LKv0HGHFVWIXUuP7EPRDfAoGBAJLc\\nyH2An1kd2NnWYy6DoLNP5PM00dOqxoj3/zyN6+aJ7SNg2wKOFC00UewndynvWKYT\\n2qN9RT2xc05SXNNBMpJVTXqRsYpi497zDtbXIGw2guHFeLXOj0EA5mlJ4hxqmAdv\\nte0VrbAumOXXRv4MRtWatMDKjyepMeUfBajEUA3xAoGBALO8YpAbdgrP4cHl2nMG\\nac/2e8jSTChcPDz8zF5myW3Q/p+SBqEBoSL7ZBSq7HuAjPzwqD2w2ZWhLbX6uWEq\\nRTFx4LC490l6kHE6SewBH1oalvTNTzzBTSO3lvwu4Wf77llgnvEjO+ukfCOls1+B\\njBlzKBJuyDv0r2ipEb+WICau\\n-----END PRIVATE KEY-----\\n\",\n" +
//                                "  \"client_email\": \"firebase-adminsdk-fbsvc@chatapplication-563d0.iam.gserviceaccount.com\",\n" +
//                                "  \"client_id\": \"105318778642210417528\",\n" +
//                                "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
//                                "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
//                                "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
//                                "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-fbsvc%40chatapplication-563d0.iam.gserviceaccount.com\",\n" +
//                                "  \"universe_domain\": \"googleapis.com\"\n" +
//                                "}"
//                        PrintLogs.printInfo("  credentials.json ->  " + jsonString)
//                        var stream: InputStream =
//                            ByteArrayInputStream(jsonString.toByteArray(StandardCharsets.UTF_8))
//                        val credentials = GoogleCredentials.fromStream(stream)
//                            .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
//                        credentials.refreshIfExpired()
//                        PrintLogs.printInfo("Google Access Token   -> " + credentials.accessToken.tokenValue)
//                        credentials.accessToken.tokenValue // This is the result
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                        PrintLogs.printD("sendMessage Exception  " + e.message)
//                    }
//                }
//                val accessToken = deferredResult.await()
//
//                PrintLogs.printInfo("Google Access Token: $accessToken")
//
//                viewModelScope.launch(Dispatchers.IO) {
//                    try {
//
//                        val client = OkHttpClient.Builder()
//                            .protocols(
//                                listOf(
//                                    Protocol.HTTP_2,
//                                    Protocol.HTTP_1_1
//                                )
//                            ) // Prioritize H2, fallback to HTTP/1.1
//                            .build()
//
//                        // Build JSON payload
//                        val jsonObject = JSONObject()
//                        val messageObject = JSONObject()
//                        val notificationObject = JSONObject()
//                        JSONObject()
//
//                        // Set notification title and body
//                        notificationObject.put("title", title)
//                        notificationObject.put("body", body)
//
//                        // Add user data
//
//                        //  dataObject.put("userId", _uiState.value.id)
//
//                        // Construct message with notification and data
//                        messageObject.put("notification", notificationObject)
//                        //  messageObject.put("data", dataObject)
//                        messageObject.put("token", pref.getString(Constant.KEY_FCM_TOKEN, "null"))
//                        jsonObject.put("message", messageObject)
//                        PrintLogs.printInfo(" jsonObject ->  " + jsonObject.toString())
//
//                        val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
//
//                        val requestBody: RequestBody = jsonObject.toString().toRequestBody(JSON)
//
//
//                        val url = Constant.FCM_URL
//                        val request = Request.Builder()
//                            .url(url)
//                            .post(requestBody)
//                            .addHeader("Authorization", "Bearer $accessToken")
//                            .addHeader("Content-Type", "application/json")
//                            .build()
//                        val call = client.newCall(request)
//                        call.enqueue(object : Callback {
//                            override fun onFailure(call: Call, e: IOException) {
//                                e.printStackTrace()
//                                PrintLogs.printInfo(" onFailure  " + e.message)
//                            }
//
//                            override fun onResponse(call: Call, response: Response) {
//                                PrintLogs.printInfo(" response -> " + response.toString())
//                                if (response.isSuccessful) {
//                                    PrintLogs.printInfo(" response.isSuccessful ")
//                                } else {
//                                    PrintLogs.printInfo(" response.isSuccessful not ")
//                                }
//
//                            }
//                        })
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                        PrintLogs.printD("sendMessage Exception  " + e.message)
//                    }
//                }
//
//
//
//                PrintLogs.printInfo("Home view model send Message End  ")
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//                PrintLogs.printD("sendMessage Exception  " + e.message)
//            }
//        }
//    }
//



}




