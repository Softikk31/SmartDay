package com.example.smartday.ui.ui.navigation

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.smartday.R
import com.example.smartday.core.models.task.repetition.TaskRepetitionModel
import com.example.smartday.ui.main.view_models.MainViewModel
import com.example.smartday.ui.main.view_models.TaskViewModel
import com.example.smartday.ui.main.view_models.ThemeViewModel
import com.example.smartday.ui.ui.components.bars.CustomBottomBar
import com.example.smartday.ui.ui.components.bars.CustomButtonBottomBar
import com.example.smartday.ui.ui.components.bars.CustomNavBar
import com.example.smartday.ui.ui.components.button.CustomActionButton
import com.example.smartday.ui.ui.screens.HomeScreen
import com.example.smartday.ui.ui.screens.SearchScreen
import com.example.smartday.ui.ui.screens.TaskScreen
import com.example.smartday.ui.ui.screens.TasksScreen
import com.example.smartday.ui.ui.screens.settings.SettingsScreen
import com.example.smartday.ui.ui.screens.settings.ThemeScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    val viewModel: MainViewModel = koinViewModel()
    val taskViewModel: TaskViewModel = koinViewModel()
    val themeViewModel: ThemeViewModel = koinViewModel()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val bottomNavBarScreensList = listOf(
        Home::class.qualifiedName, Tasks::class.qualifiedName, Settings::class.qualifiedName
    )

    val inBottomNavBarScreensList = currentRoute in bottomNavBarScreensList

    val targetAlpha = if (inBottomNavBarScreensList) 1f else 0f

    val animatedAlpha by animateFloatAsState(
        targetValue = targetAlpha
    )

    val taskDeleteMode by taskViewModel.deleteMode.collectAsState()

    val context = LocalContext.current

    val state by taskViewModel.stateTaskForm.collectAsState()

    val toastEmptyTitle = Toast.makeText(
        context, stringResource(R.string.toast_warning_empty_title), Toast.LENGTH_SHORT
    )

    val toastNullTime = Toast.makeText(
        context,
        stringResource(R.string.toast_warning_null_date_and_not_null_time),
        Toast.LENGTH_SHORT
    )

    val toastNullRepetition = Toast.makeText(
        context,
        stringResource(R.string.toast_warning_null_date_and_not_null_repetition),
        Toast.LENGTH_SHORT
    )

    val showBottomSheet by taskViewModel.showCreateTaskBottomSheet.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        floatingActionButton = {
            if (!showBottomSheet) {
                if (currentRoute == Task::class.qualifiedName) {
                    CustomActionButton(
                        modifier = Modifier
                            .imePadding(),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_check)
                    ) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            val alarmManager =
                                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                            if (!alarmManager.canScheduleExactAlarms()) {
                                val intent =
                                    Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                                        data = Uri.fromParts("package", context.packageName, null)
                                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    }

                                context.startActivity(intent)
                            } else {
                                if ((state.title.isNotEmpty() and !((state.date == null) and
                                            (state.time != null)) and !((state.date == null) and
                                            (state.repetition != TaskRepetitionModel())))
                                ) {
                                    if (state.id != null) {
                                        taskViewModel.editTask(context = context)
                                        navController.popBackStack()
                                    } else {
                                        taskViewModel.createTask(context = context)
                                        navController.popBackStack()
                                    }
                                    taskViewModel.onDismissDeleteAndEditTask()
                                } else if (state.title.isEmpty()) {
                                    toastEmptyTitle.show()
                                } else if ((state.date == null) and (state.time != null)) {
                                    toastNullTime.show()
                                } else if ((state.date == null) and (state.time != TaskRepetitionModel())) {
                                    toastNullRepetition.show()
                                }
                            }
                        } else {
                            if ((state.title.isNotEmpty() and !((state.date == null) and
                                        (state.time != null)) and !((state.date == null) and
                                        (state.repetition != TaskRepetitionModel())))
                            ) {
                                if (state.id != null) {
                                    taskViewModel.editTask(context = context)
                                    navController.popBackStack()
                                } else {
                                    taskViewModel.createTask(context = context)
                                    navController.popBackStack()
                                }
                                taskViewModel.onDismissDeleteAndEditTask()
                            } else if (state.title.isEmpty()) {
                                toastEmptyTitle.show()
                            } else if ((state.date == null) and (state.time != null)) {
                                toastNullTime.show()
                            } else if ((state.date == null) and (state.time != TaskRepetitionModel())) {
                                toastNullRepetition.show()
                            }
                        }
                    }
                } else if (currentRoute == Tasks::class.qualifiedName) {
                    AnimatedVisibility(
                        visible = !taskDeleteMode.isDeleting,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        CustomActionButton(
                            onClick = {
                                taskViewModel.editShowCreateTaskBottomSheet(true)
                            }
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (!showBottomSheet) {
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
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding.copy(bottom = 0.dp)),
            navController = navController,
            startDestination = Home
        ) {
            composable<Home>(
                enterTransition = {
                    fadeIn(animationSpec = tween(100))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(100))
                },
                popEnterTransition = {
                    fadeIn(animationSpec = tween(100))
                },
                popExitTransition = {
                    fadeOut(animationSpec = tween(100))
                }
            ) {
                HomeScreen(viewModel = viewModel, taskViewModel = taskViewModel)
            }
            composable<Tasks>(
                enterTransition = {
                    fadeIn(animationSpec = tween(100))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(100))
                },
                popEnterTransition = {
                    fadeIn(animationSpec = tween(100))
                },
                popExitTransition = {
                    fadeOut(animationSpec = tween(100))
                }
            ) {
                TasksScreen(
                    navController = navController,
                    viewModel = viewModel,
                    taskViewModel = taskViewModel
                )
            }
            composable<Settings>(
                enterTransition = {
                    fadeIn(animationSpec = tween(100))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(100))
                },
                popEnterTransition = {
                    fadeIn(animationSpec = tween(100))
                },
                popExitTransition = {
                    fadeOut(animationSpec = tween(100))
                }
            ) {
                SettingsScreen(navController = navController, themeViewModel = themeViewModel)
            }

            composable<Search>(
                enterTransition = {
                    slideInHorizontally() + fadeIn(animationSpec = tween(100))
                }, exitTransition = {
                    slideOutHorizontally() + fadeOut(animationSpec = tween(100))
                }, popEnterTransition = {
                    slideInHorizontally() + fadeIn(animationSpec = tween(100))
                }, popExitTransition = {
                    slideOutHorizontally() + fadeOut(animationSpec = tween(100))
                }
            ) {
                SearchScreen(navController = navController, taskViewModel = taskViewModel)
            }
            composable<Task>(
                enterTransition = {
                    slideInHorizontally() + fadeIn(animationSpec = tween(100))
                }, exitTransition = {
                    slideOutHorizontally() + fadeOut(animationSpec = tween(100))
                }, popEnterTransition = {
                    slideInHorizontally() + fadeIn(animationSpec = tween(100))
                }, popExitTransition = {
                    slideOutHorizontally() + fadeOut(animationSpec = tween(100))
                }
            ) {
                TaskScreen(navController = navController, taskViewModel = taskViewModel)
            }
            composable<Theme>(
                enterTransition = {
                    slideInHorizontally() + fadeIn(animationSpec = tween(100))
                }, exitTransition = {
                    slideOutHorizontally() + fadeOut(animationSpec = tween(100))
                }, popEnterTransition = {
                    slideInHorizontally() + fadeIn(animationSpec = tween(100))
                }, popExitTransition = {
                    slideOutHorizontally() + fadeOut(animationSpec = tween(100))
                }
            ) {
                ThemeScreen(navController = navController, themeViewModel = themeViewModel)
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

