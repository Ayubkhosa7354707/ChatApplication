package com.ayub.khosa.chatapplication.feature.home

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayub.khosa.chatapplication.feature.auth.signin.google.AccountService
import com.ayub.khosa.chatapplication.model.AuthUser
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

    private val _uiState = MutableStateFlow(AuthUser())
    val uiState: StateFlow<AuthUser> = _uiState.asStateFlow()
    // Expose as StateFlow


    init {

        PrintLogs.printD(" HomeViewModel init ")
        getAuthUser()
    }

    fun getAuthUser() {
        var firebaseuser: FirebaseUser? = Firebase.auth.currentUser

        _uiState.value.email = firebaseuser?.email as String
        _uiState.value.id = firebaseuser?.uid as String
        _uiState.value.displayName = firebaseuser?.displayName as String
        getfcmtoken()
    }

    fun getfcmtoken() {
        var token = ""
        PrintLogs.printD("getfcmtoken  " + token)
        viewModelScope.launch {
            try {
                token = repository.getfcmtoken()
                pref.edit().putString(Constant.KEY_FCM_TOKEN, token).apply()
                PrintLogs.printInfo("Home view model FCM token  --> " + token)
//                _uiState.value..fcmToken = token
            } catch (e: Exception) {
                PrintLogs.printD("Exception  " + e.message)
            }
        }
        PrintLogs.printD("getfcmtoken -----> " + token)

    }


    fun logout() {
        PrintLogs.printInfo("HomeViewModel logout   ")

        Firebase.auth.signOut()



    }


    private val firebaseDatabase = Firebase.database

    val databaseReference = firebaseDatabase.getReference(Constant.KEY_COLLECTION_USERS)


    fun saveSharedPreferences() {

        pref.edit().putString(Constant.KEY_USER_ID, _uiState.value.id).apply()
        pref.edit().putString(Constant.KEY_USER_NAME, _uiState.value.displayName).apply()
        pref.edit().putString(Constant.KEY_USER_EMAIL, _uiState.value.email).apply()

    }

    fun myRefsetValue() {

        PrintLogs.printInfo("firebase database Write database  " )

        databaseReference.child(Constant.KEY_USER_ID).setValue(_uiState.value.id)
            .addOnSuccessListener {
                PrintLogs.printInfo("User created successfully ")
            }.addOnFailureListener {
                PrintLogs.printInfo("Failed to create user ")
            }

        databaseReference.child(_uiState.value.id).child(Constant.KEY_USER_EMAIL)
            .setValue(_uiState.value.email)
            .addOnSuccessListener {
                PrintLogs.printInfo("User email created successfully ")
            }.addOnFailureListener {
                PrintLogs.printInfo("Failed to create user email")
            }

        databaseReference.child(_uiState.value.id).child(Constant.KEY_USER_NAME)
            .setValue(_uiState.value.displayName)
            .addOnSuccessListener {
                PrintLogs.printInfo("User name created successfully ")
            }.addOnFailureListener {
                PrintLogs.printInfo("Failed to create user name")
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



        }.addOnFailureListener {
            PrintLogs.printInfo("firebase Error getting data id")
        }

    }


    @SuppressLint("SuspiciousIndentation")
    fun FirestoreWriteValue() {

        PrintLogs.printInfo(" FirestoreWriteValue ")
        val db = FirebaseFirestore.getInstance()




        PrintLogs.printInfo("FirebaseFirestore  db  -> document " + _uiState.value.id)
        db.collection(Constant.KEY_COLLECTION_USERS).document(_uiState.value.id).get()
            .addOnSuccessListener { document ->
                PrintLogs.printD("insert user with id: ${document.id}")
                firestoreupdateUser(_uiState.value.copy(id = document.id))

            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                PrintLogs.printD("error inserting user: ${e.message}")
            }

    }

    private fun firestoreupdateUser(myuser: AuthUser) {


        PrintLogs.printInfo(" firestoreupdateUser id --  " + myuser.id)
        val db = FirebaseFirestore.getInstance()
        db.collection(Constant.KEY_COLLECTION_USERS)
            .document(myuser.id)
            .set(myuser.toAuthUser())
            .addOnSuccessListener {
                PrintLogs.printD("update user with id: ${myuser.id}")

            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                PrintLogs.printD("error updating user: ${e.message}")

            }

    }

    fun FirestoreReadValue() {
        PrintLogs.printInfo(" FirestoreReadValue id-- " + _uiState.value.id)

        val db = FirebaseFirestore.getInstance()

        db.collection(Constant.KEY_COLLECTION_USERS)
            .get()
            .addOnSuccessListener { result ->
                var myUSER: AuthUser? = null

                for (document in result) {

                    myUSER = document.data.toMyUSER()
                    PrintLogs.printInfo("FirestoreReadValue ->  "+document.data)
                    PrintLogs.printD("user Name:  " + myUSER?.displayName + " Email : " + myUSER?.email + " id : " + myUSER?.id)

                    myUSER?.id = document.data.get(Constant.KEY_USER_ID) as String
                    myUSER?.displayName = document.data.get(Constant.KEY_USER_NAME) as String
                    myUSER?.email = document.data.get(Constant.KEY_USER_EMAIL) as String

                    PrintLogs.printD("user id found: " + myUSER?.id)
                    PrintLogs.printD("user name found: " + myUSER?.displayName)
                    PrintLogs.printD("user email found: " + myUSER?.email)


                }


            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                PrintLogs.printD("error getting user: ${e.message}")

            }


    }

    private fun Map<String, Any>.toMyUSER(): AuthUser? {


        return AuthUser(
            id = "" + this[Constant.KEY_USER_ID],
            displayName = "" + this[Constant.KEY_USER_NAME],
            email = "" + this[Constant.KEY_USER_EMAIL],

            )
    }

    fun SharedPreferencesReadValue() {

        var myUSER: AuthUser = AuthUser(
            pref.getString(Constant.KEY_USER_ID, "...") as String,
            pref.getString(Constant.KEY_USER_EMAIL, "...") as String,
             "Home view model SharedPreferencesReadValue ",
            pref.getString(Constant.KEY_USER_NAME, "...") as String

        )

        PrintLogs.printInfo("SharedPreferencesReadValue myUSER " + myUSER.toString())


    }


    fun sendMessage(message: String) {

        PrintLogs.printD("Home view model sendMessage  ")
        viewModelScope.launch {
            try {

                repository.sendMessage(message)


            } catch (e: Exception) {
                PrintLogs.printD("sendMessage Exception  " + e.message)
            }
        }
    }




}


private fun AuthUser.toAuthUser(): AuthUser {
    return AuthUser(
        id = this.id as String,
        displayName = this.displayName as String,
        email = this.email as String

    )

}




