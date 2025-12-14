package com.ayub.khosa.chatapplication.feature.home

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayub.khosa.chatapplication.feature.auth.signin.google.AccountService
import com.ayub.khosa.chatapplication.model.Data
import com.ayub.khosa.chatapplication.model.MessageBody
import com.ayub.khosa.chatapplication.model.MyUSER
import com.ayub.khosa.chatapplication.repo.MainActivityRepository
import com.ayub.khosa.chatapplication.utils.Constant
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pref: SharedPreferences,
    private val repository: MainActivityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyUSER())
    val uiState: StateFlow<MyUSER> = _uiState.asStateFlow()
    // Expose as StateFlow


    init {

        PrintLogs.printD(" HomeViewModel init ")
        getmyUser()
    }

    fun getmyUser() {
        var firebaseuser: FirebaseUser? = Firebase.auth.currentUser

        _uiState.value.email = firebaseuser?.email as String
        _uiState.value.userId = firebaseuser?.uid as String
        _uiState.value.name = firebaseuser?.displayName as String
        getfcmtoken()
    }

    fun getfcmtoken() {
        var token = ""
        PrintLogs.printD("getfcmtoken  " + token)
        viewModelScope.launch {
            try {
                token = repository.getfcmtoken()
                pref.edit().putString(Constant.KEY_FCM_TOKEN, token).apply()
                PrintLogs.printInfo(" FCM token  --> " + token)
                _uiState.value.fcmToken = token
            } catch (e: Exception) {
                PrintLogs.printD("Exception  " + e.message)
            }
        }
        PrintLogs.printD("getfcmtoken -----> " + token)

    }


    fun logout() {
        PrintLogs.printInfo("HomeViewModel logout   ")
        FirebaseAuth.getInstance().signOut()

    }


    private val firebaseDatabase = Firebase.database

    val databaseReference = firebaseDatabase.getReference(Constant.KEY_COLLECTION_USERS)


    fun saveSharedPreferences() {


        pref.edit().putString(Constant.KEY_USER_ID, _uiState.value.userId).apply()
        pref.edit().putString(Constant.KEY_USER_NAME, _uiState.value.name).apply()
        pref.edit().putString(Constant.KEY_USER_EMAIL, _uiState.value.email).apply()
        pref.edit().putString(Constant.KEY_FCM_TOKEN, _uiState.value.fcmToken).apply()

    }

    fun myRefsetValue() {

        PrintLogs.printInfo("firebase database Write database  " + _uiState.value.fcmToken)

        databaseReference.child(Constant.KEY_USER_ID).setValue(_uiState.value.userId)
            .addOnSuccessListener {
                PrintLogs.printInfo("User created successfully ")
            }.addOnFailureListener {
                PrintLogs.printInfo("Failed to create user ")
            }

        databaseReference.child(_uiState.value.userId).child(Constant.KEY_USER_EMAIL)
            .setValue(_uiState.value.email)
            .addOnSuccessListener {
                PrintLogs.printInfo("User email created successfully ")
            }.addOnFailureListener {
                PrintLogs.printInfo("Failed to create user email")
            }

        databaseReference.child(_uiState.value.userId).child(Constant.KEY_USER_NAME)
            .setValue(_uiState.value.name)
            .addOnSuccessListener {
                PrintLogs.printInfo("User name created successfully ")
            }.addOnFailureListener {
                PrintLogs.printInfo("Failed to create user name")
            }

        databaseReference.child(_uiState.value.userId).child(Constant.KEY_FCM_TOKEN)
            .setValue(_uiState.value.fcmToken)
            .addOnSuccessListener {
                PrintLogs.printInfo("User token created successfully ")
            }.addOnFailureListener {
                PrintLogs.printInfo("Failed to create user token")
            }


    }

    fun myRefgetValue() {


        PrintLogs.printInfo("firebase database Read database")
        databaseReference.child(Constant.KEY_USER_ID).get().addOnSuccessListener {

            PrintLogs.printInfo("firebase Got value ${it.value}")

            databaseReference.child(it.value as String).child(Constant.KEY_USER_EMAIL).get()
                .addOnSuccessListener {
                    PrintLogs.printInfo("firebase Email Got value ${it.value}")
                }.addOnFailureListener {
                    PrintLogs.printInfo("firebase Error getting data email")
                }
            databaseReference.child(it.value as String).child(Constant.KEY_USER_NAME).get()
                .addOnSuccessListener {
                    PrintLogs.printInfo("firebase Name Got value ${it.value}")
                }.addOnFailureListener {
                    PrintLogs.printInfo("firebase Error getting data name")
                }
            databaseReference.child(it.value as String).child(Constant.KEY_FCM_TOKEN).get()
                .addOnSuccessListener {
                    PrintLogs.printInfo("firebase token Got value ${it.value}")
                }.addOnFailureListener {
                    PrintLogs.printInfo("firebase Error getting data name")
                }


        }.addOnFailureListener {
            PrintLogs.printInfo("firebase Error getting data id")
        }

    }


    @SuppressLint("SuspiciousIndentation")
    fun FirestoreWriteValue() {

        PrintLogs.printInfo(" FirestoreWriteValue ")
        val db = FirebaseFirestore.getInstance()




        PrintLogs.printInfo("FirebaseFirestore  db  -> document " + _uiState.value.userId)
        db.collection(Constant.KEY_COLLECTION_USERS).document(_uiState.value.userId).get()
            .addOnSuccessListener { document ->
                PrintLogs.printD("insert user with id: ${document.id}")
                firestoreupdateUser(_uiState.value.copy(userId = document.id))

            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                PrintLogs.printD("error inserting user: ${e.message}")
            }

    }

    private fun firestoreupdateUser(myuser: MyUSER) {


        PrintLogs.printInfo(" firestoreupdateUser id --  " + myuser.userId)
        PrintLogs.printInfo(" firestoreupdateUser fcmToken --  " + myuser.fcmToken)
        val db = FirebaseFirestore.getInstance()
        db.collection(Constant.KEY_COLLECTION_USERS)
            .document(myuser.userId)
            .set(myuser.toMyUser())
            .addOnSuccessListener {
                PrintLogs.printD("update user with id: ${myuser.userId}")

            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                PrintLogs.printD("error updating user: ${e.message}")

            }

    }

    fun FirestoreReadValue() {
        PrintLogs.printInfo(" FirestoreReadValue id-- " + _uiState.value.userId)

        val db = FirebaseFirestore.getInstance()

        db.collection(Constant.KEY_COLLECTION_USERS)
            .get()
            .addOnSuccessListener { result ->
                var myUSER: MyUSER? = null

                for (document in result) {

                    myUSER = document.data.toMyUSER()
                    PrintLogs.printD("user Name:  " + myUSER?.name + " Email : " + myUSER?.email + " id : " + myUSER?.userId)

                    myUSER?.userId = document.data.get(Constant.KEY_USER_ID) as String
                    myUSER?.name = document.data.get(Constant.KEY_USER_NAME) as String
                    myUSER?.email = document.data.get(Constant.KEY_USER_EMAIL) as String
                    myUSER?.fcmToken = "" + document.data.get(Constant.KEY_FCM_TOKEN)
                    PrintLogs.printD("user name found: " + myUSER?.name)
                    PrintLogs.printD("user fcm found:  " + myUSER?.fcmToken)
                    PrintLogs.printD("user email found: " + myUSER?.email)


                }


            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                PrintLogs.printD("error getting user: ${e.message}")

            }


    }

    private fun Map<String, Any>.toMyUSER(): MyUSER? {


        return MyUSER(
            userId = "" + this[Constant.KEY_USER_ID],
            name = "" + this[Constant.KEY_USER_NAME],
            email = "" + this[Constant.KEY_USER_EMAIL],
            fcmToken = "" + this[Constant.KEY_FCM_TOKEN],

            )
    }

    fun SharedPreferencesReadValue() {

        var myUSER: MyUSER = MyUSER(
            pref.getString(Constant.KEY_USER_ID, "...") as String,
            pref.getString(Constant.KEY_USER_NAME, "...") as String,
            pref.getString(Constant.KEY_USER_EMAIL, "...") as String,
            pref.getString(Constant.KEY_FCM_TOKEN, "...") as String

        )

        PrintLogs.printInfo("SharedPreferencesReadValue myUSER " + myUSER.toString())


    }


    fun sendMessage(message: String) {

        PrintLogs.printD("sendMessage  ")
        viewModelScope.launch {
            try {

//                val messageMap = HashMap<String, Any>()
//                messageMap[Constant.KEY_SENDER_ID] =
//                    pref.getString(Constant.KEY_USER_ID, null).toString()
//                messageMap[Constant.KEY_RECEIVER_ID] =
//                    pref.getString(Constant.KEY_USER_ID, "...") as String
//                messageMap[Constant.KEY_MESSAGE] = message
//                messageMap[Constant.KEY_TIMESTAMP] = Date()
//
//                PrintLogs.printD("sendMessage messageMap   " + messageMap)

                val messageBody = MessageBody(
                    data = Data(
                        userId = _uiState.value.userId,
                        name = _uiState.value.name,
                        fcmToken = _uiState.value.fcmToken,
                        message = message
                    ),
                    regIs = listOf(_uiState.value.fcmToken)
                )
                PrintLogs.printInfo(" messageBody  " + messageBody.data.toString())

                repository.sendMessage(messageBody)


            } catch (e: Exception) {
                PrintLogs.printD("sendMessage Exception  " + e.message)
            }
        }
    }




}


private fun MyUSER.toMyUser(): MyUSER {
    return MyUSER(
        userId = this.userId as String,
        name = this.name as String,
        email = this.email as String,
        fcmToken = this.fcmToken as String
    )

}




