package com.ayub.khosa.chatapplication.feature.auth.signin.google

import com.ayub.khosa.chatapplication.model.AuthUser
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountService @Inject constructor() {

    val currentUser: Flow<AuthUser?>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser.toAuthUser())
                }
            Firebase.auth.addAuthStateListener(listener)
            awaitClose { Firebase.auth.removeAuthStateListener(listener) }
        }


//    val currentUserId: String
//        get() = Firebase.auth.currentUser?.uid.orEmpty()
//
//      fun hasUser(): Boolean {
//        return Firebase.auth.currentUser != null
//    }
//
//      fun getUserProfile(): AuthUser {
//        return Firebase.auth.currentUser.toAuthUser()
//    }


//      suspend fun linkAccountWithGoogle(idToken: String) {
//        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
//        Firebase.auth.currentUser!!.linkWithCredential(firebaseCredential).await()
//    }

//      suspend fun linkAccountWithEmail(email: String, password: String) {
//        val credential = EmailAuthProvider.getCredential(email, password)
//        Firebase.auth.currentUser!!.linkWithCredential(credential).await()
//    }

    suspend fun signInWithGoogle(idToken: String) {

        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        Firebase.auth.signInWithCredential(firebaseCredential).await()
    }


    private fun FirebaseUser?.toAuthUser(): AuthUser {
        return if (this == null) AuthUser() else AuthUser(
            id = this.uid,
            email = this.email ?: "",
            provider = this.providerId,
            displayName = this.displayName ?: "",
        )
    }
}