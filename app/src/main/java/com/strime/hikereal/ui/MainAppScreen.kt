package com.strime.hikereal.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.filled.Stream
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.strime.hikereal.R
import com.strime.hikereal.navigation.AppRoutes
import com.strime.hikereal.ui.components.ActiveHikeBanner
import com.strime.hikereal.ui.components.HandleErrorState
import com.strime.hikereal.ui.screens.feed.FeedScreen
import com.strime.hikereal.ui.screens.live.LiveScreen
import com.strime.hikereal.ui.screens.profile.ProfileScreen
import com.strime.hikereal.ui.screens.snap.SnapScreen
import com.strime.hikereal.ui.screens.start_live.StartLiveScreen
import com.strime.hikereal.ui.viewmodel.SharedViewModel
import kotlinx.coroutines.delay

@Composable
fun MainAppScreen(
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val activeHikeState by sharedViewModel.activeHikeState.collectAsState()
    val operationState by sharedViewModel.operationState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var showBottomBar by rememberSaveable { mutableStateOf(true) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(currentRoute) {
        if (currentRoute == AppRoutes.START_LIVE) {
            delay(150)
            showBottomBar = false
        } else {
            showBottomBar = true
        }
    }

    HandleErrorState(operationState, snackbarHostState)

    val items = listOf(
        Screen.Feed,
        Screen.Live,
        Screen.Profile
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        bottomBar = {
            if (showBottomBar) {
                Column {
                    if (activeHikeState.hikeId != null) {
                        ActiveHikeBanner(
                            activeHikeState = activeHikeState,
                            duration = activeHikeState.formattedDuration,
                            distance = activeHikeState.formattedDistance,
                            onCompleteClick = { sharedViewModel.completeHike() },
                            onCapturePhotoClick = {
                                navController.navigate(
                                    AppRoutes.SNAP
                                )
                            }
                        )
                    }

                    NavigationBar {
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
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppRoutes.FEED,
            modifier = Modifier.padding(
                if (showBottomBar) innerPadding else androidx.compose.foundation.layout.PaddingValues()
            )
        ) {
            composable(AppRoutes.FEED) {
                FeedScreen(navController)
            }

            composable(AppRoutes.LIVE) {
                LiveScreen(navController)
            }

            composable(AppRoutes.PROFILE) {
                ProfileScreen(navController)
            }

            composable(
                route = AppRoutes.SNAP,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = tween(500)
                    ) + fadeIn(animationSpec = tween(500))
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(500)
                    ) + fadeOut(animationSpec = tween(500))
                },
                popEnterTransition = {
                    EnterTransition.None
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(500)
                    ) + fadeOut(animationSpec = tween(300))
                }
            ) {
                SnapScreen(navController)
            }

            composable(
                route = AppRoutes.START_LIVE,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = tween(500)
                    ) + fadeIn(animationSpec = tween(500))
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(500)
                    ) + fadeOut(animationSpec = tween(500))
                },
                popEnterTransition = {
                    EnterTransition.None
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(500)
                    ) + fadeOut(animationSpec = tween(300))
                }
            ) {
                StartLiveScreen(navController)
            }
        }
    }
}

sealed class Screen(
    val route: String,
    val resourceId: Int,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    data object Feed : Screen(AppRoutes.FEED, R.string.feed, Icons.Filled.Hiking)
    data object Live : Screen(AppRoutes.LIVE, R.string.live, Icons.Filled.Stream)
    data object Profile : Screen(AppRoutes.PROFILE, R.string.profile, Icons.Filled.AccountCircle)
}