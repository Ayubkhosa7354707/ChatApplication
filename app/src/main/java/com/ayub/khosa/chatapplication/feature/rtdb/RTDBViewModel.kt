package com.ayub.khosa.chatapplication.feature.rtdb

import androidx.lifecycle.ViewModel
import com.ayub.khosa.chatapplication.model.AuthUser
import com.ayub.khosa.chatapplication.utils.Constant
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class RTDBViewModel @Inject constructor(
) : ViewModel() {


    private val firebaseDatabase = Firebase.database

    val databaseReference = firebaseDatabase.getReference(Constant.KEY_COLLECTION_USERS)


    private var myproducts: ArrayList<AuthUser> = ArrayList<AuthUser>()
    private var _tasks = MutableStateFlow(myproducts)
    val tasks: ArrayList<AuthUser>
        get() = _tasks.value


    fun getusersItems(): List<AuthUser> {
        PrintLogs.printInfo(" getusersItems ---   "+tasks.size)
        return tasks
    }

    fun RTDB_Write(authuser: AuthUser) {

        PrintLogs.printInfo("RTDB_Write  ")


        databaseReference.child(authuser.id)
            .setValue(authuser)
            .addOnSuccessListener {
                PrintLogs.printInfo("User  created successfully ")
            }.addOnFailureListener {
                PrintLogs.printInfo("Failed to create User")
            }


    }

    fun RTDB_Read(id: String): AuthUser? {

        var user: AuthUser? = AuthUser()
        PrintLogs.printInfo("firebase database Read database end")
        databaseReference.child(id).get().addOnSuccessListener { dataSnapshot ->
            user = dataSnapshot.getValue<AuthUser>()
            if (user != null) {
                PrintLogs.printInfo("firebaseDatabaseRead Single User ")
                PrintLogs.printInfo("UserData  Name     : ${user?.displayName}")
                PrintLogs.printInfo("UserData  id       : ${user?.id}")
                PrintLogs.printInfo("UserData  Email    : ${user?.email}")
                PrintLogs.printInfo("UserData  fcmToken : ${user?.fcmToken}")
                // Update your UI here
            }
        }.addOnFailureListener {
            PrintLogs.printInfo("UserData Error getting data " + it)
        }

        return user
    }

    fun RTDB_Read_All() {



        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val usersList = kotlin.collections.ArrayList<AuthUser>()
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue<AuthUser>()
                    if (user != null) {
                        usersList.add(user)
                        PrintLogs.printInfo("firebaseDatabaseRead Users List ")
                        PrintLogs.printInfo("UserData  Name      : ${user.displayName}")
                        PrintLogs.printInfo("UserData  id        : ${user.id}")
                        PrintLogs.printInfo("UserData  Email     : ${user.email}")
                        PrintLogs.printInfo("UserData  fcm token : ${user.fcmToken}")
                    }
                }
                _tasks.value = usersList

                // Process your list of messages (e.g., update a RecyclerView)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
            // ... onCancelled implementation
        })






    }


}