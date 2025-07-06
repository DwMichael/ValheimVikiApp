package com.rabbitv.valheimviki.domain.repository

interface ItemData {
	val id: String
	val name: String
	val imageUrl: String
	val category: String
	val subCategory: String?
}