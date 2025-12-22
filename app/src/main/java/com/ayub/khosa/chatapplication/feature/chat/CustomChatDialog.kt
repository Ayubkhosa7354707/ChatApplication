package com.ayub.khosa.chatapplication.feature.chat

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayub.khosa.chatapplication.feature.home.rememberMutableStateListOf
import com.ayub.khosa.chatapplication.feature.rtdb.RTDBViewModel
import com.ayub.khosa.chatapplication.model.AuthUser
import com.ayub.khosa.chatapplication.model.message.Message
import kotlin.random.Random

@Composable
fun CustomChatDialog(reciver_authUser: AuthUser, onDismissRequest: () -> Unit) {
    val chatViewModel = hiltViewModel<ChatViewModel>()

     val viewModelrtdb = hiltViewModel<RTDBViewModel>()
    val textValue by chatViewModel.textValue.collectAsState()


    var mydatalist = rememberMutableStateListOf<Message>()

    if (mydatalist.isEmpty()) {


        viewModelrtdb.RTDB_Read_Message(
            chatViewModel.uiState.collectAsState().value.id,
            reciver_authUser.id
        )
        viewModelrtdb.getmessagesItems().forEach { message ->
            LaunchedEffect(Unit) {
                mydatalist.add(message)
            }
        }
    }


    Dialog(
        onDismissRequest = onDismissRequest, properties = DialogProperties(
            dismissOnClickOutside = false, // Prevents dismissal by tapping outside
            dismissOnBackPress = false     // Prevents dismissal by pressing the back button
        )
    ) {

        if (textValue == "Sent") {
            val context = LocalContext.current
            chatViewModel.updateState("not")
            showToast(context, "Message Sent")
        }
        var input_message by remember { mutableStateOf("") }
        // Your custom content goes here
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(

                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly, // Distributes space evenly
                ) {
                    Button(
                        onClick = {

                            viewModelrtdb.RTDB_Read_Message(
                                chatViewModel.uiState.value.id,
                                reciver_authUser.id
                            )
                            mydatalist.clear()
                            viewModelrtdb.getmessagesItems().forEach { message ->

                                mydatalist.add(message)

                            }
                        },
                        shape = RectangleShape,
                        modifier = Modifier.fillMaxWidth(0.5f),
                    ) {
                        Text(text = "Read Conversation")
                    }
                    Button(onClick = onDismissRequest, shape = RectangleShape) {
                        Text("Dismiss")
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedTextField(
                        value = input_message, singleLine = true,
                        onValueChange = { newText -> input_message = newText },
                        label = { Text("Enter  Message") },
                        modifier = Modifier.fillMaxWidth(0.5f)
                    )
                    Button(
                        onClick = {
                            val randomInttitle = Random.nextInt(100)
                            val randomIntbody = Random.nextInt(100)

                            chatViewModel.sendMessage(
                                reciver_authUser,
                                " title $randomInttitle",
                                input_message + " $randomIntbody"
                            )

                        },
                        shape = RectangleShape,
                        modifier = Modifier.wrapContentSize(),
                    ) {
                        Text(text = "Send Message")
                    }
                }





                LazyColumn {
                    items(
                        items = mydatalist,
                        key = { message -> message.data.Date }

                    ) { message ->

                        Spacer(modifier = Modifier
                            .width(2.dp)
                            .height(2.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center // Aligns all children to the center
                        ) {
                            Column(

                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if(message.data.senderID == chatViewModel.uiState.collectAsState().value.id ){
                                    Text(text = "Sender")
                                }else{
                                    Text(text = "Reciver")
                                }

                                Text(text = message.data.senderID, color = Color.Red)
                                Text(text = message.notification.title, color = Color.Blue)
                                Text(text = message.notification.body, color = Color.Blue)
                                Text(text = message.data.reciverID, color = Color.Green)

                            }
                        }
                    }
                }


            }
        }
    }


}

fun showToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomChatDialog() {
    CustomChatDialog(reciver_authUser = AuthUser(), onDismissRequest = { })
}
