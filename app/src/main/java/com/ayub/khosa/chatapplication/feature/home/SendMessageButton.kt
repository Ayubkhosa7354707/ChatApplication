package com.ayub.khosa.chatapplication.feature.home

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.ayub.khosa.chatapplication.R
import com.ayub.khosa.chatapplication.utils.PrintLogs
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID


@Composable
fun SendMessageButton(buttonText: String, onRequestResult: (Credential) -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = {

            coroutineScope.launch { sendMessageButtonUI(context, onRequestResult) }

        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = buttonText)
    }

}


private suspend fun sendMessageButtonUI(
    context: Context,
    onRequestResult: (Credential) -> Unit
) {
    try {


//        val signInWithGoogleOption = GetSignInWithGoogleOption
//            .Builder(serverClientId = context.getString(R.string.default_web_client_id))
//            .build()
//
//        var request = GetCredentialRequest.Builder()
//            .addCredentialOption(signInWithGoogleOption)
//            .build()
//
//        var result = CredentialManager.create(context).getCredential(
//            request = request,
//            context = context
//        )

        //   onRequestResult(result.credential)

        val ranNonce: String = UUID.randomUUID().toString()
        val bytes: ByteArray = ranNonce.toByteArray()
        val md: MessageDigest = MessageDigest.getInstance("SHA-256")
        val digest: ByteArray = md.digest(bytes)
        val hashedNonce: String = digest.fold("") { str, it -> str + "%02x".format(it) }


        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .setAutoSelectEnabled(true)

            // nonce string to use when generating a Google ID token
            .setNonce(hashedNonce)
            .build()


        var request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        var result = CredentialManager.create(context).getCredential(
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



