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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import com.ayub.khosa.chatapplication.domain.model.Data
import com.ayub.khosa.chatapplication.domain.model.Notification
import com.ayub.khosa.chatapplication.screens.auth.AuthViewModel
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
    navController: NavHostController, viewModel: ChatViewModel = hiltViewModel()
) {


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
                        .padding(0.dp)
                        .background(Color.LightGray),
                    horizontalArrangement = Arrangement.SpaceBetween, // Distributes space evenly between children
                    verticalAlignment = Alignment.CenterVertically // Aligns children vertically in the center
                ) {
                    var messageBody by remember { mutableStateOf("") }
                    // TextField for user input

                    OutlinedTextField(
                        value = messageBody, // 2. The current text value
                        onValueChange = { messageBody = it }, // 3. Callback to update the state
                        label = { Text("Enter your Message") }, // 4. Optional: A floating label
                        singleLine = true ,// 5. Optional: Constrains to a single line
                        modifier = Modifier.weight(1f).padding(start = 10.dp),
                        shape = RectangleShape,
                    )
                    Spacer(modifier = Modifier.size(5.dp).padding(vertical = 2.dp))
//                    Button(
//                        onClick = {
//                            val randomInttitle = Random.nextInt(100)
//                            val randomIntbody = Random.nextInt(100)
//
//                            viewModel.insertMessageToFirebase(
//                                chatRoomId.toString(),
//                                "Message Title " + randomInttitle,
//                                messageBody + " " + randomIntbody,
//                                reciver_UUID.toString(),
//                                reciver_fcmtoken.toString()
//                            )
//                            messageBody=""
//
//                        },
//                        shape = RectangleShape,
//                        modifier = Modifier.wrapContentSize().padding(end = 10.dp)
//                        ,
//                    ) {
//                        Text(text = "Send")
//                    }


                    IconButton(
                        onClick = {
                            if (messageBody.isNotBlank()) {
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
                            }
                        },
                        enabled = messageBody.isNotBlank(), // Button is only enabled when there is text
                        modifier = Modifier.wrapContentSize().padding(end = 10.dp)
                    ) {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = "Send Message"
                        )
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
                .padding(start = 0.dp, top = 2.dp, end = 60.dp, bottom = 2.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White, // Set the background color
                contentColor = Color.Black // Set the color for content (text/icons) inside the card
            )
        ) {
//            Text(text = "Reciver", fontSize = 15.sp, color = Color.Red)
            Text(
                text = "" + chatmessage.notification.title,
                fontSize = 16.sp,
                modifier = Modifier.padding(2.dp),
                color = Color.Black
            )
            Text(
                text = "" + chatmessage.notification.body,
                fontSize = 15.sp,
                modifier = Modifier.padding(2.dp),
                color = Color.Black
            )

        }
    } else {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 60.dp, top = 2.dp, end = 0.dp, bottom = 2.dp),
                colors = CardDefaults.cardColors(
                containerColor = Color.White, // Set the background color
                contentColor = Color.Black // Set the color for content (text/icons) inside the card
            )
        ) {

//            Text(text = "Sender", fontSize = 15.sp, color = Color.Magenta)


            Text(
                text = "" + chatmessage.notification.title,
                fontSize = 16.sp,
                modifier = Modifier.padding(2.dp),
                color = Color.Black
            )
            Text(
                text = "" + chatmessage.notification.body,
                fontSize = 15.sp,
                modifier = Modifier.padding(2.dp),
                color = Color.Black
            )
        }
    }


}

@Preview(showBackground = true)
@Composable
fun MyChatMessagePreview() {
    MyChatMessage(
        chatmessage = ChatMessage("uuid", Notification("title", "body"), Data("uuid", "uuid",), 0),
        registerUUID = ""
    )
}
//
//@Preview(showBackground = true)
//@Composable
//fun ChatScreenPreview() {
//    ChatScreen(
//        chatRoomId = "1",
//        reciver_UUID = "2",
//        reciver_fcmtoken = "3",
//        navController = NavHostController(LocalContext.current)
//    )
//}