package com.rabbitv.valheimviki.presentation.weapons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun WeaponListScreen(
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit,
    paddingValues: PaddingValues
) {
    Box(modifier) {
        Text(text = "WeaponListScreen")
    }
}

@Preview(name = "WeaponListScreen")
@Composable
private fun PreviewWeaponListScreen() {
    WeaponListScreen()
}