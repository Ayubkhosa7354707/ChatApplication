package com.ayub.khosa.chatapplication.screens.auth.signin


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ayub.khosa.chatapplication.R
import com.ayub.khosa.chatapplication.data.repository.AuthRepositoryImpl
import com.ayub.khosa.chatapplication.domain.repository.AuthRepository
import com.ayub.khosa.chatapplication.domain.usecase.authScreen.AuthUseCases
import com.ayub.khosa.chatapplication.domain.usecase.authScreen.IsUserAuthenticatedInFirebase
import com.ayub.khosa.chatapplication.screens.auth.AuthViewModel
import com.ayub.khosa.chatapplication.screens.auth.signin.google.AuthenticationButton
import com.ayub.khosa.chatapplication.screens.common.TitleText
import com.ayub.khosa.chatapplication.screens.navigation.Screens
import com.ayub.khosa.chatapplication.ui.theme.ChatApplicationTheme
import com.ayub.khosa.chatapplication.utils.Utils
import com.ayub.khosa.chatapplication.utils.showToast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(navController: NavController , viewModel: AuthViewModel = hiltViewModel() ){


    // email --> ayubkhosa@test.com
    // pasword --> ayub.khosa

    var input_email by rememberSaveable { mutableStateOf("") }
    var input_password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    //Check User Authenticated
    val isUserAuthenticated = viewModel.isUserSignInState.value
    val context = LocalContext.current
    if (!Utils.isNetworkAvailable(context)) {
        showToast(context, "Network is not available")
    }

    if (isUserAuthenticated) {
        showToast(context, "Resource.Success Good loged in ")
        navController.navigate(Screens.Home.screen_route) {
            popUpTo(Screens.Home.screen_route) {
                inclusive = true
            }
        }
    }
    //  showToast(context, "Resource.Success Not loged in ")
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
                Modifier.padding(top = 1.dp, start = 10.dp, end = 10.dp),
                "Sign IN Screen Firebase"
            )
            Text(text = "Email    --> ayubkhosa@test.com")
            Text(text = "Password --> test123")
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .background(Color.Transparent)
            )
// email field
            OutlinedTextField(
                value = input_email, singleLine = true,
                onValueChange = { newText -> input_email = newText },
                label = { Text("Enter your email") },
                modifier = Modifier.fillMaxWidth()
            )
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
            /////
            TextButton(onClick = {
                navController.navigate(Screens.SignUp.screen_route) {
                    popUpTo(Screens.SignUp.screen_route) {
                        inclusive = true
                    }
                }
            }) {
                Text(text = "Don't have an account? Sign Up")
            }
            Button(
                onClick = {
                    // email --> ayubkhosa@test.com
                    // pasword --> test123
                    input_email = "ayubkhosa@test.com"
                    input_password = "test123"
                    viewModel.signIn(input_email, input_password)
                },
//                shape = RectangleShape,
                enabled = input_email.isNotEmpty() && input_password.isNotEmpty() && input_password.length > 6,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp),
            ) {
                Text(text = "Login Button", style = MaterialTheme.typography.titleMedium)
            }
            AuthenticationButton(buttonText = "sign_in_with_google") { credential ->
                viewModel.onSignInWithGoogle(credential)
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    ChatApplicationTheme {
        SignInScreen(rememberNavController(),   viewModel = hiltViewModel())
    }
}