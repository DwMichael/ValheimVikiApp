package com.rabbitv.valheimviki.domain.model.relation

enum class RelationType {
	PRODUCES, //Items that Crafting station Produce
	REQUIRES, //Items needed to burn or transform to get refined item
	TO_BUILD, //Items needed to build
}