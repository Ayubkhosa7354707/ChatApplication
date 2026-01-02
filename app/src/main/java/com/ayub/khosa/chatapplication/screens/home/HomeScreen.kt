package com.ayub.khosa.chatapplication.screens.home


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ayub.khosa.chatapplication.domain.model.UserStatus
import com.ayub.khosa.chatapplication.screens.common.TitleText
import com.ayub.khosa.chatapplication.screens.navigation.Screens
import com.ayub.khosa.chatapplication.utils.Utils
import com.ayub.khosa.chatapplication.utils.showToast

@SuppressLint("SuspiciousIndentation")
@Composable
fun HomeScreen(navController: NavHostController) {

    val viewModel: HomeViewModel = hiltViewModel()

    //Check User Authenticated
    val isUserSignOutInFirebase = viewModel.isUserSignOutInFirebase.value
    val context = LocalContext.current
    if(!Utils.isNetworkAvailable(context)){
        showToast(context, "Network is not available")
    }
    if (isUserSignOutInFirebase) {
        showToast(context, "loged Out ")
        navController.popBackStack()
        navController.navigate(Screens.SignIn.screen_route) {
            popUpTo(Screens.SignIn.screen_route) {
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
                "Welcome to Home Screen"
            )
            Text(text = ""+viewModel.myUser.value.userName)
            Text(text = ""+viewModel.myUser.value.userEmail)

            Button(
                onClick = {
                    viewModel.setUserStatusToFirebase(UserStatus.OFFLINE)


                },
                shape = RectangleShape,
                modifier = Modifier.wrapContentSize(),
            ) {
                Text(text = "Sign Out")
            }

            //  UserlistScreen(navController)

        }
    }

}

