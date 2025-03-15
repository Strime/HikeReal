package com.strime.hikereal.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Stream
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.strime.hikereal.R
import com.strime.hikereal.ui.screens.feed.FeedScreen
import com.strime.hikereal.ui.screens.live.LiveScreen
import com.strime.hikereal.ui.screens.profile.ProfileScreen
import com.strime.hikereal.ui.screens.snap.SnapScreen

@Composable
fun HikeRealApp() {
    val navController = rememberNavController()

    val items = listOf(
        Screen.Feed,
        Screen.Live,
        Screen.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(stringResource(screen.resourceId)) },
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
        NavHost(
            navController = navController,
            startDestination = Screen.Feed.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Feed.route) { FeedScreen(navController) }
            composable(Screen.Live.route) { LiveScreen(navController) }
            composable(Screen.Profile.route) { ProfileScreen(navController) }

            composable("snap/{hikeId}") { backStackEntry ->
                val hikeId = backStackEntry.arguments?.getString("hikeId")
                SnapScreen(navController/*, hikeId!!*/)
            }
        }
    }
}

sealed class Screen(val route: String, val resourceId: Int, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    data object Feed : Screen("feed", R.string.feed, Icons.Filled.Hiking)
    data object Live : Screen("live", R.string.live, Icons.Filled.Stream)
    data object Profile : Screen("profile", R.string.profile, Icons.Filled.AccountCircle)
}