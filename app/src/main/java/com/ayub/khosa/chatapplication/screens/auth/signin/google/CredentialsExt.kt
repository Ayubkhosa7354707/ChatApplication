package com.ayub.khosa.chatapplication.screens.auth.signin.google


import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.ayub.khosa.chatapplication.R
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import kotlinx.coroutines.launch

@Composable
fun AuthenticationButton(buttonText: String, onRequestResult: (Credential) -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = { coroutineScope.launch { launchCredManButtonUI(context, onRequestResult) } },

        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_google_logo),
            modifier = Modifier.padding(horizontal = 16.dp),
            contentDescription = "Google logo"
        )

        Text(
            text = buttonText,
            fontSize = 16.sp,
            modifier = Modifier.padding(0.dp, 6.dp)
        )
    }
}

private suspend fun launchCredManButtonUI(
    context: Context,
    onRequestResult: (Credential) -> Unit
) {
    try {
        val signInWithGoogleOption = GetSignInWithGoogleOption
            .Builder(serverClientId = context.getString(R.string.default_web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        val result = CredentialManager.create(context).getCredential(
            request = request,
            context = context
        )

        onRequestResult(result.credential)


    } catch (e: NoCredentialException) {
        PrintLogs.printE("launchCredManButtonUI NoCredentialException " + e.message)
    } catch (e: GetCredentialException) {
        PrintLogs.printE("launchCredManButtonUI GetCredentialException " + e.message)
    }
}

suspend fun launchCredManBottomSheet(
    context: Context,
    hasFilter: Boolean = true,
    onRequestResult: (Credential) -> Unit
) {
    try {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(hasFilter)
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = CredentialManager.create(context).getCredential(
            request = request,
            context = context
        )

        onRequestResult(result.credential)
    } catch (e: NoCredentialException) {
        PrintLogs.printE("launchCredManBottomSheet NoCredentialException " + e.message)

        //If the bottom sheet was launched with filter by authorized accounts, we launch it again
        //without filter so the user can see all available accounts, not only the ones that have
        //been previously authorized in this app
        if (hasFilter) {
            launchCredManBottomSheet(context, hasFilter = false, onRequestResult)
        }
    } catch (e: GetCredentialException) {
        PrintLogs.printE("launchCredManBottomSheet GetCredentialException " + e.message)
    }
}