package com.rabbitv.valheimviki.presentation.detail.creature

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CreatureScreen(
    viewModel: CreatureScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    Text("CreatureScreen")

}

@Preview(
    name = "CreatureDetail",
    showBackground = true
    )
@Composable
private fun PreviewCreatureDetail() {
    CreatureScreen()
}