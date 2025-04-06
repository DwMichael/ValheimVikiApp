package com.rabbitv.valheimviki.presentation.detail.creature

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CreatureDetailScreen(
    animatedVisibilityScope : AnimatedVisibilityScope,
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(
    name = "CreatureDetail",
    showBackground = true
    )
@Composable
private fun PreviewCreatureDetail() {
    SharedTransitionLayout {
        AnimatedVisibility(visible = true){
            CreatureDetailScreen(
                animatedVisibilityScope = this,
                paddingValues = PaddingValues(0.dp))
        }
    }

}