package com.rabbitv.valheimviki.domain.model.relation

import androidx.compose.runtime.Stable

@Stable
data class RelatedData(
	val ids: List<String>,
	val relatedMap: Map<String, RelatedItem>
)