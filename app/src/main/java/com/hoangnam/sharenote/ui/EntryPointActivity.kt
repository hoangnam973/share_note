package com.hoangnam.sharenote.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hoangnam.sharenote.ui.NavigationKeys.Arg.USERNAME
import com.hoangnam.sharenote.ui.features.login.LoginScreen
import com.hoangnam.sharenote.ui.features.login.LoginViewModel
import com.hoangnam.sharenote.ui.features.main.MainScreen
import com.hoangnam.sharenote.ui.features.main.MainViewModel
import com.hoangnam.sharenote.ui.theme.ShareNoteTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow

@AndroidEntryPoint
class EntryPointActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShareNoteTheme() {
                ShareNoteApp()
            }
        }
    }

}

@Composable
private fun ShareNoteApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = NavigationKeys.Route.LOGIN) {
        composable(route = NavigationKeys.Route.LOGIN) {
            LoginDestination(navController)
        }
        composable(
            route = NavigationKeys.Route.MAIN,
            arguments = listOf(navArgument(NavigationKeys.Arg.USERNAME) {
                type = NavType.StringType
            })
        ) {
            MainDestination(navController)
        }
    }
}

@Composable
private fun LoginDestination(navController: NavHostController) {
    val viewModel: LoginViewModel = hiltViewModel()
    LoginScreen(
        viewModel = viewModel,
        state = viewModel.state,
        effectFlow = viewModel.effects.receiveAsFlow(),
        onNavigationRequested = { username ->
            navController.popBackStack()
            navController.navigate("${NavigationKeys.Route.LOGIN}/${username}")
        })
}

@Composable
private fun MainDestination(navController: NavHostController) {
    val viewModel: MainViewModel = hiltViewModel()
    MainScreen(viewModel = viewModel,
        state = viewModel.state,
        effectFlow = viewModel.effects.receiveAsFlow(),
        onNavigationRequested = {
            navController.popBackStack()
            navController.navigate("${NavigationKeys.Route.LOGIN}")
        })
}

object NavigationKeys {

    object Arg {
        const val USERNAME = "username"
    }

    object Route {
        const val LOGIN = "login"
        const val MAIN = "$LOGIN/{$USERNAME}"
    }

}