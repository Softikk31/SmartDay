package com.example.smartday.ui.ui.components.bars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.smartday.R
import com.example.smartday.ui.ui.navigation.Home
import com.example.smartday.ui.ui.navigation.Settings
import com.example.smartday.ui.ui.navigation.Tasks

private val navigationBarItemHeight = 56.dp

@Composable
fun CustomNavBar(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination: NavDestination? = navBackStackEntry?.destination

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Top
    ) {
        NavigationBarItem(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(navigationBarItemHeight),
            selected = currentDestination?.route == Home::class.qualifiedName,
            onClick = {
                if (currentDestination?.route != Home::class.qualifiedName) navController.navigate(
                    Home
                )
            },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_home),
                    contentDescription = stringResource(R.string.nav_home)
                )
            },
            label = {
                Text(
                    stringResource(R.string.nav_home),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.primary.copy(0.15f)
            )
        )

        NavigationBarItem(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(navigationBarItemHeight),
            selected = currentDestination?.route == Tasks::class.qualifiedName,
            onClick = {
                if (currentDestination?.route != Tasks::class.qualifiedName) navController.navigate(
                    Tasks
                )
            },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_tasks),
                    contentDescription = stringResource(R.string.nav_tasks)
                )
            },
            label = {
                Text(
                    stringResource(R.string.nav_tasks),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.primary.copy(0.15f)
            )
        )

        NavigationBarItem(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(navigationBarItemHeight),
            selected = currentDestination?.route == Settings::class.qualifiedName,
            onClick = {
                if (currentDestination?.route != Settings::class.qualifiedName) navController.navigate(
                    Settings
                )
            },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.nav_settings)
                )
            },
            label = {
                Text(
                    stringResource(R.string.nav_settings),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.primary.copy(0.15f)
            )
        )
    }
}
