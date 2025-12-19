package com.ayub.khosa.chatapplication.feature.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import android.Manifest
import kotlin.random.Random



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(navController: NavHostController) {

    val viewModel = hiltViewModel<HomeViewModel>()



    val postNotificationPermission =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)


    LaunchedEffect(key1 = true) {
        if (!postNotificationPermission.status.isGranted) {
            postNotificationPermission.launchPermissionRequest()
        }
    }





    LaunchedEffect(Unit) {
        Firebase.auth.currentUser?.let {
//            context.initZegoService(
//                appID = AppID,
//                appSign = AppSign,
//                userID = it.email!!,
//                userName = it.email!!
//            )


        }
    }





    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Welcome :  " + viewModel.uiState.collectAsState().value.email)


            Button(
                onClick = {
                    viewModel.logout()
                    navController.popBackStack()
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }

                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Sign Out")
            }

            Button(
                onClick = {


                    viewModel.myRefsetValue()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Firebase.database Write")
            }
            Button(
                onClick = {
                    viewModel.myRefgetValue()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Firebase.database Read")
            }


            Button(
                onClick = {
                    viewModel.FirestoreWriteValue()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Firestore Write")
            }
            Button(
                onClick = {
                    viewModel.FirestoreReadValue()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Firestore  Read")
            }
            Button(
                onClick = {
                    viewModel.getfcmtoken()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "get fcmtoken  ")
            }
            Button(
                onClick = {
                    viewModel.SharedPreferencesReadValue()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "SharedPreferences  Read")
            }




            SendMessageButton(buttonText = "Save Googleid Token Button") { credential ->
                viewModel.savegoogleidtoken(credential)
            }



            Button(
                onClick = {


                    val randomInttitle = Random.nextInt(100)
                    val randomIntbody = Random.nextInt(100)

                    viewModel.sendMessage(
                        "title $randomInttitle",
                        "body $randomIntbody"
                    )

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Send message")
            }


        }
    }
}

//
//@Preview(showBackground = true)
//@Composable
//fun PreviewHomeScreen() {
//    HomeScreen(navController = rememberNavController())
//}