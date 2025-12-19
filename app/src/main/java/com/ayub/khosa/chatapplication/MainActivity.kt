package com.ayub.khosa.chatapplication

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ayub.khosa.chatapplication.ui.theme.ChatApplicationTheme
import com.ayub.khosa.chatapplication.utils.Constant
import com.ayub.khosa.chatapplication.utils.PrintLogs
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var pref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            ChatApplicationTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }

                MainApp()

            }
        }

        pref.edit().putString(Constant.KEY_USER_ID, "ayub").apply()
        PrintLogs.printInfo(" KEY_USER_ID  value put in pref ")


        var KEY_USER_ID = pref.getString(Constant.KEY_USER_ID, "...")
        PrintLogs.printInfo("KEY_USER_ID  " + KEY_USER_ID)


    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChatApplicationTheme {
        Greeting("Android")
    }
}