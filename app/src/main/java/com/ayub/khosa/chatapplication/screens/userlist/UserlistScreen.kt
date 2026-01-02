package com.ayub.khosa.chatapplication.screens.userlist

import android.R.attr.onClick
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ayub.khosa.chatapplication.domain.model.User
import com.ayub.khosa.chatapplication.screens.common.TitleText
import com.ayub.khosa.chatapplication.screens.navigation.Screens
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.ayub.khosa.chatapplication.utils.Utils
import com.ayub.khosa.chatapplication.utils.showToast

@Composable
fun UserlistScreen(navController: NavHostController) {

    val viewModel: UserListViewModel = hiltViewModel()
    val context = LocalContext.current
    if(!Utils.isNetworkAvailable(context)){
        showToast(context, "Network is not available")
    }

    val loadFriendList = viewModel.loadFriendList
    var reciver_fcmtoken by rememberSaveable { mutableStateOf("") }
    var registerUUID by rememberSaveable { mutableStateOf("") }


    if (viewModel.chatRoomId.value.toString().isNotEmpty()  ) {
        // go to chat screen
        var chatRoomId = viewModel.chatRoomId.value
        navController.navigate(Screens.Chat.screen_route + "/$chatRoomId/$registerUUID/$reciver_fcmtoken" )
        {
            popUpTo(Screens.Chat.screen_route) {
                inclusive = true
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) {

        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
                .padding(16.dp), verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleText(
                Modifier.padding(top = 30.dp, start = 10.dp, end = 10.dp),
                "Welcome to users List Screen"
            )





            LazyColumn(
                modifier = Modifier
                    .fillMaxSize().background(Color.LightGray),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(loadFriendList.value) { item ->
                    MyUser(item, onClick = {item1->
                        registerUUID=item1.profileUUID
                        reciver_fcmtoken=item1.fcmToken
                        viewModel.checkChatRoomExistedFromFirebase(registerUUID)
                    })
                }
            }
        }
    }
}

@Composable
fun MyUser(user: User , onClick: (User) -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxSize().clickable { onClick(user) }
            .padding(vertical = 4.dp)
    ) {
        Text( text = ""+user.userName , fontSize = 20.sp , modifier = Modifier.padding(5.dp), color = Color.Red)

        Text(text = ""+user.userEmail , fontSize = 15.sp ,modifier = Modifier.padding(5.dp) , color = Color.Blue)

    }



}