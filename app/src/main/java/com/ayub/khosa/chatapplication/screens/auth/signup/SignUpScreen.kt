package com.ayub.khosa.chatapplication.screens.auth.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ayub.khosa.chatapplication.screens.auth.AuthViewModel


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayub.khosa.chatapplication.R
import com.ayub.khosa.chatapplication.screens.common.TitleText
import com.ayub.khosa.chatapplication.screens.navigation.Screens
import com.ayub.khosa.chatapplication.ui.theme.ChatApplicationTheme
import com.ayub.khosa.chatapplication.utils.showToast
import com.ayub.khosa.chatapplication.screens.auth.signin.google.AuthenticationButton

@Composable
fun  SignUpScreen (navController: NavController) {

    val viewModel: AuthViewModel = hiltViewModel()


    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
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
            TitleText(Modifier.padding(top = 30.dp, start = 10.dp, end = 10.dp), "Sign Up Screen Firebase")

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.LightGray)
            )


            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Email") })
            OutlinedTextField(
                value = password, onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.size(16.dp))

                Button(
                    onClick = {
                        viewModel.signUp( email, password)
                    }, modifier = Modifier.fillMaxWidth(),
                    enabled =  email.isNotEmpty() && password.isNotEmpty()  && password.length >6
                ) {
                    Text(text = "Sign Up")
                }
                TextButton(onClick = {
                    navController.navigate(Screens.SignIn.route) {
                        popUpTo(Screens.SignIn.route) { inclusive = true

                        }
                    }
                }) {
                    Text(text = "Already have an account? Sign In")
                }
            }


    }
}