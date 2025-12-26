package com.ayub.khosa.chatapplication.repository

import com.ayub.khosa.chatapplication.model.Resource
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            PrintLogs.printE("login Error  "+e.message)
            Resource.Failure(e)
        }
    }

    override suspend fun signup(name: String, email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
            return Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            PrintLogs.printE("signup Error  "+e.message)
            Resource.Failure(e)
        }
    }

    override suspend fun logout():  Resource<Boolean>  {
        return try {
            firebaseAuth.signOut()
            return Resource.Success<Boolean> (true)
        } catch (e: Exception) {
            e.printStackTrace()
            PrintLogs.printE("logout Error  "+e.message)
            return Resource.Failure(e)
        }
    }


}
