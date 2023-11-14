package com.arguvio.tp2Kotlin.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.arguvio.tp2Kotlin.models.User
import com.arguvio.tp2Kotlin.viewModel.UserViewModel
import com.example.myapplication.R
import com.example.tp2Kotlin.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Inject the UserViewModel
    private val userViewModel: UserViewModel by viewModels()

    private val items = listOf(
        Screen.CreateRoom,
        Screen.ThreadRooms,
        Screen.Profile
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                }


                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomNavigation {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            items.forEach { screen ->
                                BottomNavigationItem(
                                    icon = { Icon(screen.icon, contentDescription = null) },
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(navController, startDestination = Screen.Profile.route, Modifier.padding(innerPadding)) {
                        composable(Screen.Profile.route) { Profile(navController, userViewModel) }
                        composable(Screen.ThreadRooms.route) { ThreadRooms(navController) }
                        composable(Screen.CreateRoom.route) { CreateRoom(navController) }
                    }
                }





            }
        }
    }
}

@Composable
fun ThreadRooms(navHostController: NavHostController, modifier: Modifier = Modifier) {
    MyApplicationTheme {
        Text(
            text = "Thread Rooms",
            modifier = modifier
        )
    }
}

@Composable
fun CreateRoom(navHostController: NavHostController, modifier: Modifier = Modifier) {

    MyApplicationTheme {
        Text(
            text = "Create Room",
            modifier = modifier
        )
    }
}

@Composable
fun Profile(navHostController: NavHostController, userViewModel: UserViewModel, modifier: Modifier = Modifier) {

    val userList by remember { mutableStateOf<List<User>?>(null) }

    LaunchedEffect(userViewModel) {
        userViewModel.loadUsers()
    }

    MyApplicationTheme {
        Text(
            text = "Profile",
            modifier = modifier
        )

        if (userList != null) {
            for (user in userList!!) {
                Text(
                    text = "User : ${user._id} | Name : ${user.name} | Email : ${user.email} | Password : ${user.password}",
                    modifier = modifier
                )
            }
        } else {
            println("La liste des utilisateurs est nulle.")
        }
    }
}



sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object CreateRoom : Screen("createRoom", R.string.create_room, Icons.Default.Add)
    object ThreadRooms : Screen("threadRooms", R.string.thread_rooms, Icons.Default.Home)
    object Profile : Screen("profile", R.string.profile, Icons.Default.Person)
}