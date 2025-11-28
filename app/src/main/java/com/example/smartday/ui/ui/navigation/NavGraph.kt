package com.example.smartday.ui.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.smartday.R
import com.example.smartday.ui.main.view_models.MainViewModel
import com.example.smartday.ui.main.view_models.TaskViewModel
import com.example.smartday.ui.ui.components.bars.CustomBottomBar
import com.example.smartday.ui.ui.components.bars.CustomButtonBottomBar
import com.example.smartday.ui.ui.components.bars.CustomNavBar
import com.example.smartday.ui.ui.screens.*
import com.example.smartday.ui.ui.screens.settings.SettingsScreen
import com.example.smartday.ui.ui.screens.settings.ThemeScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavGraph(navController: NavHostController, viewModel: MainViewModel) {
    val taskViewModel: TaskViewModel = koinViewModel()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val bottomNavBarScreensList = listOf(
        Screen.Home::class.qualifiedName, Screen.Tasks::class.qualifiedName, Screen.Settings::class.qualifiedName
    )

    val inBottomNavBarScreensList = currentRoute in bottomNavBarScreensList

    val targetAlpha = if (inBottomNavBarScreensList) 1f else 0f

    val targetColor =
        if (inBottomNavBarScreensList) MaterialTheme.colorScheme.surfaceContainerHigh else MaterialTheme.colorScheme.surface

    val animatedAlpha by animateFloatAsState(
        targetValue = targetAlpha
    )

    val animatedColor by animateColorAsState(
        targetValue = targetColor
    )

    val taskDeleteMode by taskViewModel.deleteMode.collectAsState()

    val context = LocalContext.current

    Scaffold(
        modifier = Modifier
            .background(animatedColor)
            .navigationBarsPadding(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            AnimatedVisibility(
                visible = taskDeleteMode.isDeleting, enter = fadeIn(), exit = fadeOut()
            ) {
                CustomBottomBar {
                    CustomButtonBottomBar(
                        imageVector = R.drawable.ic_trash, text = R.string.delete
                    ) {
                        taskViewModel.deleteTask(context)
                    }
                }
            }
            AnimatedVisibility(
                modifier = Modifier
                    .alpha(animatedAlpha),
                visible = !taskDeleteMode.isDeleting,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CustomNavBar(
                    navController = navController
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home,
            modifier = Modifier.padding(innerPadding.copy(bottom = 0.dp)),
            enterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(400))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(400))
            }
        ) {
            composable<Screen.Home>(
                enterTransition = null,
                exitTransition = null
            ) {
                HomeScreen(viewModel = viewModel, taskViewModel = taskViewModel)
            }
            composable<Screen.Search> {
                SearchScreen(navController = navController, taskViewModel = taskViewModel)
            }
            composable<Screen.Task>(enterTransition = {
                slideInHorizontally() + fadeIn(animationSpec = tween(400))
            }, exitTransition = {
                slideOutHorizontally() + fadeOut(animationSpec = tween(400))
            }, popEnterTransition = {
                slideInHorizontally() + fadeIn(animationSpec = tween(400))
            }, popExitTransition = {
                slideOutHorizontally() + fadeOut(animationSpec = tween(400))
            }) {
                TaskScreen(navController = navController, taskViewModel = taskViewModel)
            }
            composable<Screen.Tasks>(
                enterTransition = null,
                exitTransition = null
            ) {
                TasksScreen(navController = navController, viewModel = viewModel, taskViewModel = taskViewModel)
            }
            composable<Screen.Settings>(
                enterTransition = null,
                exitTransition = null
            ) {
                SettingsScreen(navController = navController)
            }
            composable<Screen.Theme>(
                enterTransition = null,
                exitTransition = null
            ) {
                ThemeScreen(navController = navController)
            }
        }
    }
}

@Composable
fun PaddingValues.copy(
    start: Dp = calculateStartPadding(LocalLayoutDirection.current),
    top: Dp = calculateTopPadding(),
    end: Dp = calculateEndPadding(LocalLayoutDirection.current),
    bottom: Dp = calculateBottomPadding(),
) = PaddingValues(
    start = start,
    top = top,
    end = end,
    bottom = bottom
)

