package com.ayub.khosa.chatapplication.screens




import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ayub.khosa.chatapplication.screens.common.TitleText
import com.ayub.khosa.chatapplication.viewmodel.AuthViewModel

@Composable
fun HomeScreen(navController: NavHostController) {

    val viewModel: AuthViewModel = hiltViewModel()
    TitleText(Modifier.padding(top = 30.dp, start = 10.dp, end = 10.dp), "Welcome to Home Screen")

    Column(modifier = Modifier.padding(top = 80.dp, start = 10.dp, end = 10.dp)) {

        Button(
            onClick = {
                viewModel?.logout()
                navController.popBackStack()
                navController.navigate("ROUTE_LOGIN") {
                    popUpTo("ROUTE_LOGIN") { inclusive = true }
                }
            },
            shape = RectangleShape,
            modifier = Modifier.wrapContentSize(),
        ) {
            Text(text = "Sign Out")
        }


    }

}

