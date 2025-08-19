package com.rabbitv.valheimviki.presentation.components.ui_section

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.presentation.components.LoadingIndicator
import com.rabbitv.valheimviki.presentation.components.trident_divider.TridentsDividedRow

@Composable
fun <T> UiSection(
	state: UIState<List<T>>,
	divider: @Composable () -> Unit = { TridentsDividedRow() },
	sectionContent: @Composable (List<T>) -> Unit
) {
	when (state) {
		is UIState.Error -> Unit
		is UIState.Loading -> {
			divider()
			LoadingIndicator(paddingValues = PaddingValues(16.dp))
		}

		is UIState.Success -> if (state.data.isNotEmpty()) {
			divider()
			sectionContent(state.data)
		}
	}
}