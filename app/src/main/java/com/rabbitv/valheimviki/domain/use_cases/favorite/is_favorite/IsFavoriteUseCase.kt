package com.rabbitv.valheimviki.domain.use_cases.favorite.is_favorite

import com.rabbitv.valheimviki.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor(
	private val favoriteRepository: FavoriteRepository,
) {
	operator fun invoke(id: String): Flow<Boolean> {
		return favoriteRepository.isFavorite(id)
			.map { favorite ->
				favorite != null
			}
			.catch { exception ->
				emit(false)
			}
	}
}

