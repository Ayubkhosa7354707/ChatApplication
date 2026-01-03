package com.ayub.khosa.chatapplication.screens.auth.signup


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ayub.khosa.chatapplication.R
import com.ayub.khosa.chatapplication.screens.auth.AuthViewModel
import com.ayub.khosa.chatapplication.screens.common.TitleText
import com.ayub.khosa.chatapplication.screens.navigation.Screens
import com.ayub.khosa.chatapplication.utils.Utils
import com.ayub.khosa.chatapplication.utils.showToast

@Composable
fun SignUpScreen(navController: NavController) {

    val viewModel: AuthViewModel = hiltViewModel()
    val context = LocalContext.current
    if (!Utils.isNetworkAvailable(context)) {
        showToast(context, "Network is not available")
    }
    var input_email by remember {
        mutableStateOf("")
    }
    var input_password by remember {
        mutableStateOf("")
    }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

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
                "Sign Up Screen Firebase"
            )

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.LightGray)
            )


            OutlinedTextField(
                value = input_email,
                onValueChange = { input_email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Email") })
            /// password field
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                value = input_password,
                onValueChange = { newText ->
                    input_password = newText
                },
                label = {
                    Text(text = "Password")
                },
                trailingIcon = {
                    if (isPasswordVisible) {
                        IconButton(onClick = { isPasswordVisible = false }) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = "hide_password"
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { isPasswordVisible = true }) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = "hide_password"
                            )
                        }
                    }
                },
                placeholder = { Text(text = "Type password here") },
                shape = RoundedCornerShape(percent = 0),
            )
            Spacer(modifier = Modifier.size(16.dp))
            Button(
                onClick = {
                    viewModel.signUp(input_email, input_password)
                }, modifier = Modifier.fillMaxWidth(),
                enabled = input_email.isNotEmpty() && input_password.isNotEmpty() && input_password.length > 6
            ) {
                Text(text = "Sign Up")
            }
            TextButton(onClick = {
                navController.navigate(Screens.SignIn.screen_route) {
                    popUpTo(Screens.SignIn.screen_route) {
                        inclusive = true
                    }
                }
            }) {
                Text(text = "Already have an account? Sign In")
            }
        }
    }
}