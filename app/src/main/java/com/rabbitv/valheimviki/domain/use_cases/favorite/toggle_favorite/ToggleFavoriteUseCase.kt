package com.rabbitv.valheimviki.domain.use_cases.favorite.toggle_favorite

import com.rabbitv.valheimviki.domain.model.favorite.Favorite
import com.rabbitv.valheimviki.domain.use_cases.favorite.add_to_favorite.AddFavoriteUseCase
import com.rabbitv.valheimviki.domain.use_cases.favorite.delete_from_favorite.DeleteFavoriteUseCase
import javax.inject.Inject


class ToggleFavoriteUseCase @Inject constructor(
	private val addFavoriteUseCase: AddFavoriteUseCase,
	private val deleteFavoriteUseCase: DeleteFavoriteUseCase
) {

	suspend operator fun invoke(favorite: Favorite, shouldBeFavorite: Boolean) {
		if (shouldBeFavorite) {
			addFavoriteUseCase(favorite)
		} else {
			deleteFavoriteUseCase(favorite)
		}
	}
}