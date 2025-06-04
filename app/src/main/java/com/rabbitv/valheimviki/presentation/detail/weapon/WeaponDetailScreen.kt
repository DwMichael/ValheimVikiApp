package com.rabbitv.valheimviki.presentation.detail.weapon

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun WeaponDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: WeaponDetailScreenViewModel = hiltViewModel()
) {
    Box(modifier) {
        Text(text = "WeaponDetailScreen")
    }
}

@Preview(name = "WeaponDetailScreen")
@Composable
private fun PreviewWeaponDetailScreen() {
    WeaponDetailScreen()
}