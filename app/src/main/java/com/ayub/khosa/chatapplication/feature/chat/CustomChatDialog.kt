package com.ayub.khosa.chatapplication.feature.chat

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayub.khosa.chatapplication.model.AuthUser
import kotlin.random.Random
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties

@Composable
fun CustomChatDialog(reciver_authUser: AuthUser, onDismissRequest: () -> Unit) {
    val chatViewModel = hiltViewModel<ChatViewModel>()

    val textValue by chatViewModel.textValue.collectAsState()


    Dialog(onDismissRequest = onDismissRequest ,properties = DialogProperties(
        dismissOnClickOutside = false, // Prevents dismissal by tapping outside
        dismissOnBackPress = false     // Prevents dismissal by pressing the back button
    )
    ) {

        if(textValue=="Sent"){
            val context = LocalContext.current
            showToast(context, "Message Sent" )
//            onDismissRequest()
        }
        var input_message by remember { mutableStateOf("") }
        // Your custom content goes here
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                OutlinedTextField(
                    value = input_message, singleLine = true,
                    onValueChange = { newText -> input_message = newText },
                    label = { Text("Enter your Message") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val randomInttitle = Random.nextInt(100)
                        val randomIntbody = Random.nextInt(100)

                        chatViewModel.sendMessage(reciver_authUser,
                            "title $randomInttitle",
                            input_message+" $randomIntbody"
                        )

                    }, shape = RectangleShape,
                    modifier = Modifier.wrapContentSize(),
                ) {
                    Text(text = "Send message")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onDismissRequest) {
                    Text("Dismiss")
                }
            }
        }
    }







}

fun showToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}