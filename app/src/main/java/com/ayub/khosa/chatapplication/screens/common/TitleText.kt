package com.ayub.khosa.chatapplication.screens.common


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TitleText(
    modifier: Modifier = Modifier,
    text: String,
    textAlign: TextAlign = TextAlign.Center
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        fontFamily = FontFamily.Serif,
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.secondary
    )
}