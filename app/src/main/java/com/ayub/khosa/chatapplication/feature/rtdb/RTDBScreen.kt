package com.ayub.khosa.chatapplication.feature.rtdb

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayub.khosa.chatapplication.model.AuthUser
import kotlin.random.Random

@Composable
fun RTDBScreen() {

    val viewModel = hiltViewModel<RTDBViewModel>()
    var authUser = AuthUser()
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp), // Adds 8dp between items
        modifier = Modifier.wrapContentSize()
    ) {


        Button(
            onClick = {


                var randomInttitle = Random.nextInt(100)
                authUser.id = "id " + randomInttitle
                randomInttitle = Random.nextInt(100)
                authUser.email = "email " + randomInttitle
                randomInttitle = Random.nextInt(100)
                authUser.displayName = "displayName " + randomInttitle
                randomInttitle = Random.nextInt(100)
                authUser.fcmToken = "fcmToken " + randomInttitle

                viewModel.RTDB_Authuser_Write(authUser)
            }, shape = RectangleShape,
            modifier = Modifier.wrapContentSize()
        ) {
            Text(text = "Write")
        }
        Button(
            onClick = {

                viewModel.RTDB_AuthUser_Read(authUser.id)
            }, shape = RectangleShape,
            modifier = Modifier.wrapContentSize()
        ) {
            Text(text = "Read One")
        }
        Button(
            onClick = {
                viewModel.RTDB_Read_All_AuthUser()
            }, shape = RectangleShape,
            modifier = Modifier.wrapContentSize()
        ) {
            Text(text = "Read All")
        }
    }


}