package com.ayub.khosa.chatapplication.feature.firestore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayub.khosa.chatapplication.model.AuthUser
import com.ayub.khosa.chatapplication.utils.PrintLogs
import kotlin.random.Random

@Composable
fun FireStoreScreen() {

    val viewModel = hiltViewModel<FireStoreViewModel>()
    var authUser = AuthUser()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {


        Button(
            onClick = {


                var randomInttitle = Random.nextInt(100)
                authUser.id = "id" + randomInttitle
                randomInttitle = Random.nextInt(100)
                authUser.email = "email " + randomInttitle
                randomInttitle = Random.nextInt(100)
                authUser.displayName = "displayName " + randomInttitle
                randomInttitle = Random.nextInt(100)
                authUser.fcmToken = "fcmToken " + randomInttitle

                viewModel.FirestoreInsert(authUser)
            }, shape = RectangleShape,
            modifier = Modifier.wrapContentSize()
        ) {
            Text(text = "Insert")
        }

        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {

                PrintLogs.printInfo("FirestoreUpdate id " + authUser.id)

                var randomInttitle = Random.nextInt(100)

                randomInttitle = Random.nextInt(100)
                authUser.email = "email " + randomInttitle
                randomInttitle = Random.nextInt(100)
                authUser.displayName = "displayName " + randomInttitle
                randomInttitle = Random.nextInt(100)
                authUser.fcmToken = "fcmToken " + randomInttitle

                viewModel.FirestoreUpdate(authUser)


            }, shape = RectangleShape,
            modifier = Modifier.wrapContentSize()
        ) {
            Text(text = "Update")
        }

    }



    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {


        Button(
            onClick = {
                PrintLogs.printInfo("FirestoregetOneUser id " + authUser.id)
                viewModel.FirestoregetOneUser(authUser)
            }, shape = RectangleShape,
            modifier = Modifier.wrapContentSize()
        ) {
            Text(text = "Get")
        }

        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                viewModel.FirestoregetAllUsers()
            }, shape = RectangleShape,
            modifier = Modifier.wrapContentSize()
        ) {
            Text(text = "Read All")
        }

    }
}