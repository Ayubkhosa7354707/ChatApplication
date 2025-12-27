package com.ayub.khosa.chatapplication.screens




import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ayub.khosa.chatapplication.domain.model.UserStatus
import com.ayub.khosa.chatapplication.screens.common.TitleText
import com.ayub.khosa.chatapplication.utils.showToast
import com.ayub.khosa.chatapplication.viewmodel.AuthViewModel
import com.ayub.khosa.chatapplication.viewmodel.HomeViewModel

@SuppressLint("SuspiciousIndentation")
@Composable
fun HomeScreen(navController: NavHostController) {

    val viewModel: HomeViewModel  = hiltViewModel()

    //Check User Authenticated
    val isUserAuthenticated = viewModel.isUserSignOutInFirebase.value
    val context = LocalContext.current

        if (isUserAuthenticated) {
            showToast(context, "loged Out ")
            navController.popBackStack()
            navController.navigate("ROUTE_LOGIN") {
                popUpTo("ROUTE_LOGIN") { inclusive = true }
            }
        }


    TitleText(Modifier.padding(top = 30.dp, start = 10.dp, end = 10.dp), "Welcome to Home Screen")

    Column(modifier = Modifier.padding(top = 80.dp, start = 10.dp, end = 10.dp)) {

        Button(
            onClick = {
                viewModel.setUserStatusToFirebase(UserStatus.OFFLINE)


            },
            shape = RectangleShape,
            modifier = Modifier.wrapContentSize(),
        ) {
            Text(text = "Sign Out")
        }


    }

}

