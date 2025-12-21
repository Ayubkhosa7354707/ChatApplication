package com.ayub.khosa.chatapplication.feature.firestore.firestoreclient


import com.ayub.khosa.chatapplication.model.AuthUser
import com.ayub.khosa.chatapplication.utils.Constant
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.google.firebase.firestore.FirebaseFirestore


class FirestoreClient {


    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection(Constant.KEY_COLLECTION_USERS)


    fun createUser(
        user: AuthUser,
    ) {
        // Add a new document with a generated ID
        usersCollection.document(user.id).set(user)
            .addOnSuccessListener { documentReference ->
                // Update the user object with the generated ID if needed
//                 usersCollection.document(documentReference.id).update("id", documentReference.id)
                PrintLogs.printD("Firestore Document successfully written!")

            }
            .addOnFailureListener { e ->
                PrintLogs.printE(" Error " + e.message)
            }
    }


    fun updateUser(updates: AuthUser) {
        usersCollection.document(updates.id).set(updates)
            .addOnSuccessListener {
                PrintLogs.printD("Upded ok ")
            }
            .addOnFailureListener {
                PrintLogs.printE("Error updating document: $it")
            }
    }


    fun getUser(
        id: String
    ): AuthUser? {
        var user: AuthUser? = null
        usersCollection.get().addOnSuccessListener { result ->


            for (document in result) {
                if (document.data[Constant.KEY_USER_ID] == id) {
                    user = document.data.toAuthUserMap()
                    PrintLogs.printD("user found id: " + user?.id + " Name : " + user?.displayName + " Email : " + user?.email + " FCM token : " + user?.fcmToken)

                }
            }

            if (user == null) {
                PrintLogs.printD("user not found: $id")

            }

        }
            .addOnFailureListener { e ->
                e.printStackTrace()
                PrintLogs.printD("error getting user: ${e.message}")

            }
        return user

    }


    fun getAllUsers(
    ): MutableList<AuthUser> {

        val usersList = mutableListOf<AuthUser>()
        usersCollection.get().addOnSuccessListener { result ->

            var user: AuthUser? = null

            for (document in result) {
                user = document.data.toAuthUserMap()
                PrintLogs.printD("user found id: " + user?.id + " Name : " + user?.displayName + " Email : " + user?.email + " FCM token : " + user?.fcmToken)

                usersList.add(user)
            }


        }
            .addOnFailureListener { e ->
                e.printStackTrace()
                PrintLogs.printD("error getting user: ${e.message}")

            }

        return usersList
    }

    fun Map<String, Any>.toAuthUserMap(): AuthUser {
        return AuthUser(
            id = this[Constant.KEY_USER_ID] as String,
            displayName = this[Constant.KEY_USER_NAME] as String,
            email = this[Constant.KEY_USER_EMAIL] as String,
            fcmToken = this[Constant.KEY_FCM_TOKEN] as String,
        )
    }


}