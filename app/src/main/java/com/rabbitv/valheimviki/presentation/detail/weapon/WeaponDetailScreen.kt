package com.rabbitv.valheimviki.presentation.detail.weapon

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rabbitv.valheimviki.presentation.detail.weapon.viewmodel.WeaponDetailViewModel

@Composable
fun WeaponDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: WeaponDetailViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier) {
        Text(text = "WeaponDetailScreen")
    }
}


@Composable
fun WeaponDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: WeaponDetailViewModel = hiltViewModel()
) {

}

@Preview(name = "WeaponDetailScreen")
@Composable
private fun PreviewWeaponDetailScreen() {
    WeaponDetailScreen()
}