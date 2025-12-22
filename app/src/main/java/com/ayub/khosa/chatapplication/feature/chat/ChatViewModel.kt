package com.ayub.khosa.chatapplication.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayub.khosa.chatapplication.feature.rtdb.RTDBViewModel
import com.ayub.khosa.chatapplication.model.AuthUser
import com.ayub.khosa.chatapplication.model.message.Data
import com.ayub.khosa.chatapplication.model.message.Message
import com.ayub.khosa.chatapplication.model.message.Notification
import com.ayub.khosa.chatapplication.utils.Constant
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
import okhttp3.Response
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import javax.inject.Inject

class ChatViewModel @Inject constructor(
) : ViewModel() {

    private val _uiStatetextValue = MutableStateFlow("Not")
    val textValue: StateFlow<String> = _uiStatetextValue
    fun updateState(newValue: String) {
        _uiStatetextValue.value = newValue
        PrintLogs.printD(" LoginViewModel updateState " + newValue)
    }



    private val _uiState = MutableStateFlow(AuthUser())
    val uiState: StateFlow<AuthUser> = _uiState.asStateFlow()
    // Expose as StateFlow


    init {
        PrintLogs.printD(" ChatViewModel init ")
        getAuthUser()


    }

    fun getAuthUser() {
        var firebaseuser: FirebaseUser? = Firebase.auth.currentUser
        _uiState.value.email = firebaseuser?.email as String
        _uiState.value.id = firebaseuser?.uid as String
        _uiState.value.displayName = firebaseuser?.displayName as String
    }


    fun sendMessage(reciver_authUser: AuthUser, title: String, body: String) {



        val message: Message =
            Message(Notification(title, body), Data(reciver_authUser.id, _uiState.value.id))


        updateState("Not")
        viewModelScope.launch {
            try {


                val deferredResult = async(Dispatchers.IO) {
                    try {


                        val jsonString = "{\n" +
                                "  \"type\": \"service_account\",\n" +
                                "  \"project_id\": \"chatapplication-563d0\",\n" +
                                "  \"private_key_id\": \"d89abdc5f2475b446c08cdfdd6aac40b9ad1fa13\",\n" +
                                "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDUT/d7XQrNT1qr\\nyLc/uvMNPT5AT4H8PNOMZAZLrkIabYxD2qb33EkG+JuPl+avghGa0rOxC+Dal3mf\\nJUjicH7JnNHa8ufo1cr6pHDL0enKjdfhYmHDfxHYPZ8vjKe9F7WoeaBGon2qi8g9\\n/ZPYStqArVwznj79XtudFaiWhaI8mW/rVp0G0jASy2sIO0S4Ei2Nl7jZivaX8xg+\\n8uULhjY7EgAEOM+MKhLcbWUPAhQy5+DcL3/PkS/JMhZUes/+EWgSQ38UDCvz0Uhe\\nCoiFE/NNImGnTWbGDrSJKmTscxw0KUBtLQQqW11fELQKS8iV5GDbQOq9vlk/w7tE\\nV3FsrqJhAgMBAAECggEAAdW75DhACT/P6/9Zr7Ad8Aj0Q/oFiMVXaE80/rjZOWel\\nxRbB4Q9P69xNYka3Z6zoGgs+iLkuW4IToWlHey2EHjOMwyepeXASx2r3m2Tx1rfp\\ngoXz8lljl7E9ciiOpCogSv4O58CS9XZfzkLyx1vEp6s+SlpQkQzQ0q3tPXljylve\\nsNwrmX/90GbRVlg4i0/AQzEKZVBe4kssuFEwWP2cCoW+WAkziDVvgE2Qmj1gwUxm\\n1zteXlKSKogXzpVZPPM8J3tunNNJOy86TFYcxAEZlFw6vrbCV2PbOZtZtS86yRfn\\n6OioLdyxOFmesYVWxpm+ZEjp51SJ5P+Y77q699U7QQKBgQDr16rBGVTn19SpLDOS\\nERmzETjMTy8WMasQCvhfx6NomRryVLsZRcXNBYM57cDcTwiyJqJ+XZ6PKNkH5ha3\\nSsgtRrT5m2brKnIR2LWCC1wszd+q1eh+JW//fsAA/Dc2b5RguBdadOHFWxw6Ejb1\\nWKEtiSF46egDkzzjW9EMDTrUjwKBgQDmdXObLDpWhBRYHSI75zLivN3YT/qFheSu\\nZhlQ73nW+WJpzsrsmR5BzKuRLy87YYqCZnNVSnjqQJejRy3wnErmZzlTdOUL9rlM\\nxjs3asXiZvCtiGXgyTVjvyCTbOrqrx3SUnpqBER+mF3Ee1Io9ihbQ0AVJvtiuCVt\\nLy8eymPyDwKBgQCTxgaQB7dTrcDqX2/QzjY0JU5ZdiQi2+w7mSQXn5ecKpLf1IeH\\nRliyJgobegWmBT+FoP+f/jkjqoySjnEdWBYliHBNFjpej7H5zLeuTD/tMvI6sN2E\\nbSDIDjtwYpiV20Q1diPr4eC1MGl3WYJmKKqVRZ9bi/R8iP1V2GocCw4ZsQKBgQCY\\nlF6L+AZPzXait/c4beP01lKSqrEUwgcbqCFOwp6KQ+ZyYyVa7C1qJxXYM8Mzr2rC\\n1w25BGt1vHoo8jFqQjAcx27e4F/dQeaA/CWslRaaxMjIO6wn3U9p0NrCgLQv2O0H\\nmO/PTTAEGODL+1H0KLWjZq0A1CRPwdeVqE2sNxdg5QKBgQC/10fej7z36TKWEjMZ\\n3SMM4aRS087yL4EumLpy5Og0tudl6yJbeuGX3D1x3IDVCtPAg39WTzzjrj25gY99\\ninl51jUaTpEG780XKVkPX4QZkcuWuyTkzxVSqwfocQtUbed+reUCxjsbkls0xZcT\\naGT4zJ/MI/pny66/2XycBJSY+Q==\\n-----END PRIVATE KEY-----\\n\",\n" +
                                "  \"client_email\": \"firebase-adminsdk-fbsvc@chatapplication-563d0.iam.gserviceaccount.com\",\n" +
                                "  \"client_id\": \"105318778642210417528\",\n" +
                                "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                                "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                                "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                                "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-fbsvc%40chatapplication-563d0.iam.gserviceaccount.com\",\n" +
                                "  \"universe_domain\": \"googleapis.com\"\n" +
                                "}\n"
                        PrintLogs.printInfo("  credentials.json ->  " + jsonString)
                        var stream: InputStream =
                            ByteArrayInputStream(jsonString.toByteArray(StandardCharsets.UTF_8))
                        val credentials = GoogleCredentials.fromStream(stream)
                            .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
                        credentials.refreshIfExpired()
                        PrintLogs.printInfo("Google Access Token   -> " + credentials.accessToken.tokenValue)
                        credentials.accessToken.tokenValue // This is the result
                    } catch (e: Exception) {
                        e.printStackTrace()
                        PrintLogs.printD("sendMessage Exception  " + e.message)
                    }
                }
                val accessToken = deferredResult.await()

                PrintLogs.printInfo("Google Access Token: $accessToken")

                viewModelScope.launch(Dispatchers.IO) {
                    try {

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
                        JSONObject()

                        // Set notification title and body
                        notificationObject.put("title", title)
                        notificationObject.put("body", body)

                        // Add user data

//                          dataObject.put("userId", _uiState.value.id)
                        dataObject.put("sender_Id", _uiState.value.id)

                        dataObject.put("reciver_Id", reciver_authUser.id)
                        // Construct message with notification and data
                        messageObject.put("notification", notificationObject)
                        messageObject.put("data", dataObject)
                        messageObject.put("token", reciver_authUser.fcmToken)
                        jsonObject.put("message", messageObject)
                        PrintLogs.printInfo(" jsonObject ->  " + jsonObject.toString())


                        val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()

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
                            }

                            override fun onResponse(call: Call, response: Response) {
                                PrintLogs.printInfo(" response -> " + response.toString())
                                if (response.isSuccessful) {
                                    PrintLogs.printInfo(" response.isSuccessful ")
                                    // Send message to the firbase RTDB
                                    RTDBViewModel().RTDB_Write_Message(message)
                                    updateState("Sent")
                                } else {
                                    PrintLogs.printInfo(" response.isSuccessful not ")
                                }

                            }
                        })
                    } catch (e: Exception) {
                        e.printStackTrace()
                        PrintLogs.printD("sendMessage Exception  " + e.message)
                    }
                }



                PrintLogs.printInfo("Home view model send Message End  ")

            } catch (e: Exception) {
                e.printStackTrace()
                PrintLogs.printD("sendMessage Exception  " + e.message)
            }
        }
    }


}