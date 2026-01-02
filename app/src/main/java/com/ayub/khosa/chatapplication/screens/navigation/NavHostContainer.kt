package com.ayub.khosa.chatapplication.screens.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.ayub.khosa.chatapplication.screens.auth.signin.SignInScreen
import com.ayub.khosa.chatapplication.screens.auth.signup.SignUpScreen
import com.ayub.khosa.chatapplication.screens.chat.ChatScreen
import com.ayub.khosa.chatapplication.screens.home.HomeScreen
import com.ayub.khosa.chatapplication.screens.userlist.UserlistScreen
import com.ayub.khosa.chatapplication.utils.PrintLogs


@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues
) {


    NavHost(
        navController = navController,

        // set the start destination as home
        startDestination = Screens.SignIn.fullRoute,

        // Set the padding provided by scaffold
        modifier = Modifier.padding(paddingValues = padding),

        builder = {

            composable(Screens.Home.fullRoute) {
                HomeScreen(navController)
            }
            composable(Screens.SignIn.fullRoute) {
                SignInScreen(navController)
            }
            composable(Screens.SignUp.fullRoute) {
                SignUpScreen(navController)
            }

            composable(Screens.UserList.fullRoute) {
                UserlistScreen(navController)
            }
            composable(Screens.Chat.fullRoute ,
                arguments = listOf(
                    navArgument("chatRoomId") { type = NavType.StringType },
                    navArgument("registerUUID") { type = NavType.StringType },
                    navArgument("reciver_fcmtoken") { type = NavType.StringType }
                )
            ) {backStackEntry ->

                val chatRoomId = backStackEntry.arguments?.getString("chatRoomId") as String
                val registerUUID = backStackEntry.arguments?.getString("registerUUID") as String
                val reciver_fcmtoken = backStackEntry.arguments?.getString("reciver_fcmtoken") as String

                PrintLogs.printInfo("registerUUID  -- " + registerUUID + " chatRoomId : " + chatRoomId +" reciver_fcmtoken : "+reciver_fcmtoken)
                PrintLogs.printInfo(" data -- " + Screens.Chat.fullRoute)
                ChatScreen(chatRoomId, registerUUID,reciver_fcmtoken, navController)
            }


        }
    )
}
