package com.ayub.khosa.chatapplication.feature.home

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ayub.khosa.chatapplication.feature.chat.CustomChatDialog
import com.ayub.khosa.chatapplication.feature.rtdb.RTDBViewModel
import com.ayub.khosa.chatapplication.model.AuthUser
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(navController: NavHostController) {

    val homeViewModel = hiltViewModel<HomeViewModel>()


    val viewModelrtdb = hiltViewModel<RTDBViewModel>()


    var mydatalist = rememberMutableStateListOf<AuthUser>()

    if (mydatalist.isEmpty()) {
        viewModelrtdb.RTDB_Read_All_AuthUser()
        viewModelrtdb.getusersItems().forEach { authuser ->
            LaunchedEffect(Unit) {
                mydatalist.add(authuser)
            }
        }
    }

    var show_chat = rememberSaveable { mutableStateOf(false) }
    var reciver_authUser = AuthUser()


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
            Text(text = " Welcome to Home Screen ")
            Text(text = "Email :" + homeViewModel.uiState.collectAsState().value.email)
            Text(text = "Id :" + homeViewModel.uiState.collectAsState().value.id)
            Text(text = "Name :" + homeViewModel.uiState.collectAsState().value.displayName)
//            Text(text = "FCM token :" + homeViewModel.uiState.collectAsState().value.fcmToken)


            viewModelrtdb.RTDB_Authuser_Write(homeViewModel.uiState.collectAsState().value)

            Button(
                onClick = {
                    homeViewModel.logout()
                    navController.popBackStack()
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                shape = RectangleShape,
                modifier = Modifier.wrapContentSize(),
            ) {
                Text(text = "Sign Out")
            }








            Button(
                onClick = {
                    viewModelrtdb.RTDB_Read_All_AuthUser()
                    mydatalist.clear()
                    viewModelrtdb.getusersItems().forEach { authuser ->
                        mydatalist.add(authuser)

                    }


                },
                shape = RectangleShape,
                modifier = Modifier.wrapContentSize(),
            ) {
                Text(text = "Update list RTDB_Read_All ")
            }

            Text(text = "List of Users ")
            LazyColumn(
                modifier = Modifier.align(Alignment.Start)
            ) {
                items(
                    items = mydatalist,
                    key = { authUser -> authUser.id }

                ) { authUser ->


                    Text(
                        modifier = Modifier
                            .clickable {
                                show_chat.value = true
                                reciver_authUser = authUser

                            }
                            .padding(10.dp)
                            .fillMaxWidth(),
                        text = "Name ->" + authUser.displayName + " " + authUser.email,
                        color = Color.Blue, fontSize = 18.sp,
                    )
                }
            }

            if (show_chat.value == true) {
                CustomChatDialog(reciver_authUser, onDismissRequest = { show_chat.value = false })
            }


        }
    }
}

@Composable
fun <T : Any> rememberMutableStateListOf(vararg elements: T): SnapshotStateList<T> {
    return rememberSaveable(saver = snapshotStateListSaver()) {
        elements.toList().toMutableStateList()
    }
}

private fun <T : Any> snapshotStateListSaver() = listSaver<SnapshotStateList<T>, T>(
    save = { stateList -> stateList.toList() },
    restore = { it.toMutableStateList() },
)


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(navController = rememberNavController())
}