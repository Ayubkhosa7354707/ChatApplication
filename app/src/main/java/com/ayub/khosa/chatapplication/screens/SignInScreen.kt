package com.ayub.khosa.chatapplication.screens



import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayub.khosa.chatapplication.screens.common.TitleText
import com.ayub.khosa.chatapplication.ui.theme.ChatApplicationTheme
import com.ayub.khosa.chatapplication.utils.showToast
import com.ayub.khosa.chatapplication.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(navController: NavController) {

    val viewModel: AuthViewModel = hiltViewModel()

    // email --> ayubkhosa@test.com
    // pasword --> ayub.khosa

    var input_email by rememberSaveable { mutableStateOf("") }
    var input_password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    //Check User Authenticated
    val isUserAuthenticated = viewModel.isUserSignInState.value
    val context = LocalContext.current





    TitleText(Modifier.padding(top = 30.dp, start = 10.dp, end = 10.dp), "Login Screen Firebase")


    if (isUserAuthenticated ) {
        showToast(context, "Resource.Success Good loged in ")
        navController.navigate("ROUTE_HOME")
    }else{
      //  showToast(context, "Resource.Success Not loged in ")



    Column(modifier = Modifier.padding(top = 80.dp, start = 10.dp, end = 10.dp)) {
        Text(text = "Email    --> ayubkhosa@test.com")
        Text(text = "Password --> test123")
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

        Button(
            onClick = {

                // email --> ayubkhosa@test.com
                // pasword --> test123
                input_email = "ayubkhosa@test.com"
                input_password = "test123"
                viewModel?.signIn(input_email, input_password)

            },
            shape = RectangleShape,

            ) {
            Text(text = "Login Button", style = MaterialTheme.typography.titleMedium)
        }
    }

    }


}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun LoginScreenPreviewLight() {
    ChatApplicationTheme {
        SignInScreen( rememberNavController())
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenPreviewDark() {
    ChatApplicationTheme {
        SignInScreen( rememberNavController())
    }
}