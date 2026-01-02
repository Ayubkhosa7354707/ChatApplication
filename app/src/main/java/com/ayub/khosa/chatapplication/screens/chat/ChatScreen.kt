package com.ayub.khosa.chatapplication.screens.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ayub.khosa.chatapplication.domain.model.ChatMessage
import com.ayub.khosa.chatapplication.screens.common.TitleText
import com.ayub.khosa.chatapplication.screens.navigation.Screens
import com.ayub.khosa.chatapplication.utils.Utils
import com.ayub.khosa.chatapplication.utils.showToast
import kotlin.random.Random

@SuppressLint("SuspiciousIndentation")
@Composable
fun ChatScreen(chatRoomId: String , registerUUID: String , reciver_fcmtoken: String,  navController: NavHostController) {

    val viewModel: ChatViewModel = hiltViewModel()

    val loadchatmessageList : MutableState<List<ChatMessage>> =viewModel.loadchatmessageList

    if(loadchatmessageList.value.isEmpty()){
        viewModel.loadAllchatmessages(chatRoomId, registerUUID)
    }


    val context = LocalContext.current
    if(!Utils.isNetworkAvailable(context)){
        showToast(context, "Network is not available")
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
                Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp),
                "Welcome to Chat Screen "
            )

//            Text("chatRoomId : " + chatRoomId)
//            Text("registerUUID : " + registerUUID)
//            Text("reciver_fcmtoken : " + reciver_fcmtoken)


            TextButton(onClick = {
                navController.navigate(Screens.UserList.screen_route) {
                    popUpTo(Screens.UserList.screen_route) {
                        inclusive = true
                    }
                }
            }) {
                Text(text = "Back UserList Screen ")
            }
            Text(text = "Status-> "+viewModel.messageSentState.value.toString())



            Button(
                onClick = {
                    val randomInttitle = Random.nextInt(100)
                    val randomIntbody = Random.nextInt(100)

                    viewModel.insertMessageToFirebase(
                        chatRoomId.toString(),
                        "Message Title " + randomInttitle,
                        "Message body " + randomIntbody,
                        registerUUID.toString(),
                        reciver_fcmtoken.toString()
                    )

                },
                shape = RectangleShape,
                modifier = Modifier.wrapContentSize(),
            ) {
                Text(text = "Send Message")
            }
            Button(
                onClick = {

                    viewModel.loadAllchatmessages(chatRoomId, registerUUID)

                },
                shape = RectangleShape,
                modifier = Modifier.wrapContentSize(),
            ) {
                Text(text = "Load Message List")
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize().background(Color.LightGray),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(loadchatmessageList.value) { chatmessage ->
                    MyChatMessage(chatmessage , registerUUID )
                }
            }


        }
    }

}

@Composable
fun MyChatMessage(chatmessage: ChatMessage , registerUUID: String ) {

    if(chatmessage.data.senderID == registerUUID ){
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray, // Set the background color
                contentColor = Color.White // Set the color for content (text/icons) inside the card
            )
        ) {
             Text(text = "Reciver" , color = Color.Red)
            Text( text = "Title -> "+chatmessage.notification.title , fontSize = 20.sp , modifier = Modifier.padding(5.dp))
            Text( text = "Body -> "+chatmessage.notification.body , fontSize = 15.sp , modifier = Modifier.padding(2.dp))

        }
    }else{
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Cyan, // Set the background color
                contentColor = Color.White // Set the color for content (text/icons) inside the card
            )
        ) {

                Text(text = "Sender" , color = Color.Blue)


            Text( text = "Title -> "+chatmessage.notification.title , fontSize = 20.sp , modifier = Modifier.padding(5.dp))
            Text( text = "Body -> "+chatmessage.notification.body , fontSize = 15.sp , modifier = Modifier.padding(2.dp))

        }
    }


}