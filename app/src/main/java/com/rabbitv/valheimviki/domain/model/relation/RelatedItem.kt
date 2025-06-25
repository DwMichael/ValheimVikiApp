package com.rabbitv.valheimviki.domain.model.relation

data class RelatedItem(
	val id: String,
	val quantity: Int? = null,
	val relationType: String? = null,
	val quantity2star: Int? = null,
	val quantity3star: Int? = null,
	val quantity4star: Int? = null,
	val chance1star: Int? = null,
	val chance2star: Int? = null,
	val chance3star: Int? = null,
)