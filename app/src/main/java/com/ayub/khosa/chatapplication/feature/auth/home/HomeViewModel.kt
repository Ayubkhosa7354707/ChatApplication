package com.ayub.khosa.chatapplication.feature.auth.home

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayub.khosa.chatapplication.model.MyUSER
import com.ayub.khosa.chatapplication.repo.MainActivityRepository
import com.ayub.khosa.chatapplication.utils.Constant
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pref: SharedPreferences,
    private val repository: MainActivityRepository
) : ViewModel() {



    init {

        PrintLogs.printD(" HomeViewModel init ")
    }


    fun getfcmtoken(): String {
        var token = ""
        PrintLogs.printD("getfcmtoken  " + token)
        viewModelScope.launch {
            try {
                token = repository.getfcmtoken()
                pref.edit().putString(Constant.KEY_FCM_TOKEN, token).apply()
                PrintLogs.printInfo(" FCM token  --> " + token)
            } catch (e: Exception) {
                PrintLogs.printD("Exception  " + e.message)
            }
        }
        PrintLogs.printD("getfcmtoken -----> " + token)
        return token
    }


    fun logout() {
        PrintLogs.printInfo("HomeViewModel logout   ")
        FirebaseAuth.getInstance().signOut()
        FirebaseAuth.getInstance().currentUser?.email
    }


    private val firebaseDatabase = Firebase.database

    val databaseReference = firebaseDatabase.getReference(Constant.KEY_COLLECTION_USERS)


    fun saveSharedPreferences(myUSER: MyUSER?) {


        pref.edit().putString(Constant.KEY_USER_ID, myUSER?.userId).apply()
        pref.edit().putString(Constant.KEY_USER_NAME, myUSER?.name).apply()
        pref.edit().putString(Constant.KEY_USER_EMAIL, myUSER?.email).apply()
        pref.edit().putString(Constant.KEY_FCM_TOKEN, myUSER?.fcmToken).apply()

    }

    fun myRefsetValue(myUSER: MyUSER) {

        PrintLogs.printInfo("firebase database Write database  " + myUSER.fcmToken)

        databaseReference.child(Constant.KEY_USER_ID).setValue(myUSER.userId).addOnSuccessListener {
            PrintLogs.printInfo("User created successfully ")
        }.addOnFailureListener {
            PrintLogs.printInfo("Failed to create user ")
        }

        databaseReference.child(myUSER.userId).child(Constant.KEY_USER_EMAIL).setValue(myUSER.email)
            .addOnSuccessListener {
                PrintLogs.printInfo("User email created successfully ")
            }.addOnFailureListener {
                PrintLogs.printInfo("Failed to create user email")
            }

        databaseReference.child(myUSER.userId).child(Constant.KEY_USER_NAME).setValue(myUSER.name)
            .addOnSuccessListener {
                PrintLogs.printInfo("User name created successfully ")
            }.addOnFailureListener {
                PrintLogs.printInfo("Failed to create user name")
            }

        databaseReference.child(myUSER.userId).child(Constant.KEY_FCM_TOKEN)
            .setValue(myUSER.fcmToken)
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


    fun FirestoreWriteValue(myUSER: MyUSER) {

        PrintLogs.printInfo(" FirestoreWriteValue ")
        viewModelScope.launch {

            val db = FirebaseFirestore.getInstance()

            db.collection(Constant.KEY_COLLECTION_USERS).document(myUSER.userId).get()
                .addOnSuccessListener { document ->
                    PrintLogs.printD("insert user with id: ${document.id}")
                    CoroutineScope(Dispatchers.IO).launch {
                        firestoreupdateUser(myUSER.copy(userId = document.id))
                    }
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    PrintLogs.printD("error inserting user: ${e.message}")
                }
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

    fun FirestoreReadValue(myUSER: MyUSER) {
        PrintLogs.printInfo(" FirestoreReadValue id-- " + myUSER.userId)

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

                    saveSharedPreferences(myUSER)
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

                val messageMap = HashMap<String, Any>()
                messageMap[Constant.KEY_SENDER_ID] =
                    pref.getString(Constant.KEY_USER_ID, null).toString()
                messageMap[Constant.KEY_RECEIVER_ID] =
                    pref.getString(Constant.KEY_USER_ID, "...") as String
                messageMap[Constant.KEY_MESSAGE] = message
                messageMap[Constant.KEY_TIMESTAMP] = Date()

                repository.sendMessage(messageMap)


            } catch (e: Exception) {
                PrintLogs.printD("Exception  " + e.message)
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




