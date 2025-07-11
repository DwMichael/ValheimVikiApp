package com.rabbitv.valheimviki.data.mappers.favorite

import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.repository.ItemData

fun ItemData.toFavorite(): Favorite {
	return Favorite(
		id = this.id,
		name = this.name,
		imageUrl = this.imageUrl,
		category = this.category,
		subCategory = this.subCategory,
		description = this.description
	)
}

