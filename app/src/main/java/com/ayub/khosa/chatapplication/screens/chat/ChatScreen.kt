package com.ayub.khosa.chatapplication.screens.chat

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun ChatScreen(
    chatRoomId: String,
    reciver_UUID: String,
    reciver_fcmtoken: String,
    navController: NavHostController
) {



    val viewModel: ChatViewModel = hiltViewModel()
    val loadchatmessageList: MutableState<List<ChatMessage>> = viewModel.loadchatmessageList
    if (loadchatmessageList.value.isEmpty()) {
        viewModel.loadAllchatmessages(chatRoomId)
    }

    val context = LocalContext.current
    if (!Utils.isNetworkAvailable(context)) {
        showToast(context, "Network is not available")
    }


    if(viewModel.isMessageSent.value){
         showToast(context, ""+viewModel.messageSentState.value)
        viewModel.isMessageSent.value=false
        viewModel.messageSentState.value="..."
    }
    var endchatScreen by remember { mutableStateOf(false) }
    BackHandler(enabled = true) {
        endchatScreen = true
    }
    if (endchatScreen) {
        navController.navigate(Screens.UserList.screen_route) {
            popUpTo(Screens.UserList.screen_route) {
                inclusive = true
            }
            launchSingleTop = true
            restoreState = true
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
                    Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp),
                    "Welcome to Chat Screen ",
                )



//            Button(
//                onClick = {
//
//                    viewModel.loadAllchatmessages(chatRoomId, registerUUID)
//
//                },
//                shape = RectangleShape,
//                modifier = Modifier.wrapContentSize(),
//            ) {
//                Text(text = "Load Message List")
//            }
                LazyColumn(
                    modifier = Modifier.weight(1f)
                        .background(Color.LightGray)
                        ,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    items(loadchatmessageList.value) { chatmessage ->
                        MyChatMessage(chatmessage, reciver_UUID)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, // Distributes space evenly between children
                    verticalAlignment = Alignment.CenterVertically // Aligns children vertically in the center
                ) {
                    var messageBody by remember { mutableStateOf("") }
                    // TextField for user input
                    TextField(
                        value = messageBody,
                        onValueChange = { messageBody = it },
                        label = { Text("Enter your Message ") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                    )
                    Spacer(modifier = Modifier.size(5.dp))
                    Button(
                        onClick = {
                            val randomInttitle = Random.nextInt(100)
                            val randomIntbody = Random.nextInt(100)

                            viewModel.insertMessageToFirebase(
                                chatRoomId.toString(),
                                "Message Title " + randomInttitle,
                                messageBody + " " + randomIntbody,
                                reciver_UUID.toString(),
                                reciver_fcmtoken.toString()
                            )
                            messageBody=""

                        },
                        shape = RectangleShape,
                        modifier = Modifier.wrapContentSize(),
                    ) {
                        Text(text = "Send")
                    }

            }
        }
    }
}

@Composable
fun MyChatMessage(chatmessage: ChatMessage, registerUUID: String) {

    if (chatmessage.data.senderID == registerUUID) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray, // Set the background color
                contentColor = Color.White // Set the color for content (text/icons) inside the card
            )
        ) {
            Text(text = "Reciver", color = Color.Red)
            Text(
                text = "" + chatmessage.notification.title,
                fontSize = 20.sp,
                modifier = Modifier.padding(5.dp),
                color = Color.White
            )
            Text(
                text = "" + chatmessage.notification.body,
                fontSize = 18.sp,
                modifier = Modifier.padding(2.dp),
                color = Color.White
            )

        }
    } else {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Cyan, // Set the background color
                contentColor = Color.White // Set the color for content (text/icons) inside the card
            )
        ) {

            Text(text = "Sender", color = Color.Magenta)


            Text(
                text = "" + chatmessage.notification.title,
                fontSize = 20.sp,
                modifier = Modifier.padding(5.dp),
                color = Color.Blue
            )
            Text(
                text = "" + chatmessage.notification.body,
                fontSize = 18.sp,
                modifier = Modifier.padding(2.dp),
                color = Color.Blue
            )
        }
    }


}

@Preview(showBackground = true)
@Composable
fun MyChatMessagePreview() {
    MyChatMessage(
        chatmessage = ChatMessage(),
        registerUUID = ""
    )
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    ChatScreen(
        chatRoomId = "1",
        reciver_UUID = "2",
        reciver_fcmtoken = "3",
        navController = NavHostController(LocalContext.current)
    )
}