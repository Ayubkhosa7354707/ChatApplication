package com.ayub.khosa.chatapplication.screens.userlist

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ayub.khosa.chatapplication.screens.common.TitleText

@Composable
fun Userlist(navController: NavHostController) {

    val viewModel: UserListViewModel  = hiltViewModel()

    TitleText(Modifier.padding(top = 30.dp, start = 10.dp, end = 10.dp), "Welcome to users List Screen")

    viewModel.searchUserFromFirebase("ayub.khosa@gmail.com")

}