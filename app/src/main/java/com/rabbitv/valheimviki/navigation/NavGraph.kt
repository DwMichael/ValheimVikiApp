package com.rabbitv.valheimviki.navigation

import com.rabbitv.valheimviki.presentation.home.HomeScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rabbitv.valheimviki.presentation.intro.SecondScreen
import com.rabbitv.valheimviki.presentation.intro.WelcomeScreen


@Composable
fun SetupNavGraph(navController: NavHostController){

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
        ){
        composable(
            route = Screen.Welcome.route
        ){
            WelcomeScreen()
        }
        composable(
            route = Screen.Second.route
        ){
            SecondScreen()
        }
        composable(
            route = Screen.Home.route
        ){
            HomeScreen(
                modifier = Modifier,
            )
        }
    }

}