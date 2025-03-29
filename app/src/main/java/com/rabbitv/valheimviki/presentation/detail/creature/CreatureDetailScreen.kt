package com.rabbitv.valheimviki.presentation.detail.creature

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CreatureDetailScreen(
    viewModel: CreatureScreenViewModel = hiltViewModel(),
    paddingValues: PaddingValues
) {
    val creature by viewModel.mainBoss.collectAsStateWithLifecycle()
    Box(
        modifier = Modifier.padding(paddingValues)
    ) {
        Text(text = creature.toString())
    }

}

@Preview(
    name = "CreatureDetail",
    showBackground = true
    )
@Composable
private fun PreviewCreatureDetail() {
    CreatureDetailScreen(paddingValues = PaddingValues(0.dp))
}