package com.example.smartday.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.smartday.R
import com.example.smartday.presentation.ui.components.CustomScaffoldTopBar
import com.example.smartday.presentation.ui.components.CustomTopBar

@Composable
fun SettingsScreen() {
    CustomScaffoldTopBar(
        topBar = {
            CustomTopBar(
                text = stringResource(R.string.title_settings),
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

        }
    }
}
