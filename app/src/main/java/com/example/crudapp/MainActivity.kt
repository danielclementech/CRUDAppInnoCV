package com.example.crudapp

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.crudapp.data.UserResponseModel
import com.example.crudapp.model.UserModel
import com.example.crudapp.ui.theme.CRUDAppTheme
import com.example.crudapp.ui.theme.LightBlue
import com.example.crudapp.viewModel.Status
import com.example.crudapp.viewModel.UserViewModel
import com.google.gson.Gson
import kotlinx.coroutines.InternalCoroutinesApi

class MainActivity : AppCompatActivity() {

    companion object {
        const val padding16 = 16
        const val padding8 = 8
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CRUDAppTheme {
                val viewModel = UserViewModel()
                val users = viewModel.usersList.observeAsState()

                CRUDApp(users, viewModel)
            }
        }
    }
}

@InternalCoroutinesApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CRUDApp(users: State<List<UserModel>?>, viewModel: UserViewModel) {

    val navController = rememberNavController()
    val loading = viewModel.loadingUsers.observeAsState()

    NavHost(
        navController = navController,
        startDestination = DrawerScreens.Home.route
    ) {
        composable(DrawerScreens.Home.route) {
            viewModel.loadData()
            when(loading.value) {
                Status.SUCCESS ->
                    HomeScreen(navController, users, viewModel)
                else -> {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(LightBlue.copy(0.5f))
                            .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                        Text(
                            text = stringResource(R.string.loading),
                        )
                    }
                }
            }
        }
        composable(DrawerScreens.NewUser.route) {
            NewUserScreen(navController, viewModel)
        }
        composable(DrawerScreens.UserDetail.route + "/{user}",
        arguments = listOf(
                navArgument("user") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            backStackEntry?.arguments?.getString("user")?.let { json ->
                val user = Gson().fromJson(json, UserResponseModel::class.java)
                UserDetail(navController, viewModel, user)
            }
        }
    }
}

sealed class DrawerScreens(val title: String, val route: String) {
    object Home : DrawerScreens("List Menu", "home")
    object NewUser : DrawerScreens("Add User", "new_user")
    object UserDetail : DrawerScreens("User", "user")
}