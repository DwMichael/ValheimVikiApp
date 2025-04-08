//package com.rabbitv.valheimviki.navigation
//
//import androidx.compose.animation.ExperimentalSharedTransitionApi
//import androidx.compose.animation.SharedTransitionScope
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.rabbitv.valheimviki.presentation.home.HomeScreen
//import com.rabbitv.valheimviki.presentation.intro.WelcomeScreen
//import com.rabbitv.valheimviki.presentation.splash.SplashScreen
//
//
//@OptIn(ExperimentalSharedTransitionApi::class)
//@Composable
//fun SetupNavGraph(navController: NavHostController) {
//    SharedTransitionScope {
//
//        NavHost(
//            navController = navController,
//            startDestination = Screen.Splash.route,
//            ) {
//            composable(route = Screen.Splash.route) {
//                SplashScreen(navController)
//            }
//            composable(route = Screen.Welcome.route) {
//                WelcomeScreen(navController)
//            }
//            composable(
//                route = Screen.Home.route
//            ) {
//                HomeScreen(
//                    sharedTransitionScope = this@SharedTransitionScope,
//                    modifier = Modifier,
//                    childNavController = rememberNavController()
//                )
//            }
//        }
//    }
//}