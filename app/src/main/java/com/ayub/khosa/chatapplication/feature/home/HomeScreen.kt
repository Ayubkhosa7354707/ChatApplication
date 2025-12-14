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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ayub.khosa.chatapplication.MainActivity
import com.ayub.khosa.chatapplication.feature.auth.signin.google.AuthenticationButton
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlin.random.Random

@SuppressLint("ContextCastToActivity")
@Composable
fun HomeScreen(navController: NavHostController ) {

    val viewModel = hiltViewModel<HomeViewModel>()




    LocalContext.current as MainActivity
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

            Button(
                onClick = {

                    val randomIntUntil = Random.nextInt(100)
                    viewModel.sendMessage("hello World " + randomIntUntil)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "send Message")
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