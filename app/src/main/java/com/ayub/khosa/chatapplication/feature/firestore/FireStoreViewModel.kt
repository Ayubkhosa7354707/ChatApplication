package com.ayub.khosa.chatapplication.feature.firestore

import androidx.lifecycle.ViewModel
import com.ayub.khosa.chatapplication.feature.firestore.firestoreclient.FirestoreClient
import com.ayub.khosa.chatapplication.model.AuthUser
import javax.inject.Inject

class FireStoreViewModel @Inject constructor(
) : ViewModel() {
    private val firestoreClient = FirestoreClient()

    fun FirestoreInsert(authUser: AuthUser) {
        firestoreClient.createUser(authUser)
    }

    fun FirestoreUpdate(authUser: AuthUser) {
        firestoreClient.updateUser(authUser)


    }

    fun FirestoregetOneUser(authUser: AuthUser) {

        firestoreClient.getUser(authUser.id)
    }

    fun FirestoregetAllUsers() {

        firestoreClient.getAllUsers()
    }
}