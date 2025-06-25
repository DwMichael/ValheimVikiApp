package com.rabbitv.valheimviki.domain.use_cases.crafting_object.get_crafting_object_by_id

import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.repository.CraftingObjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCraftingObjectByIdUseCase @Inject constructor(
	private val _craftingObjectRepo: CraftingObjectRepository
) {
	operator fun invoke(id: String): Flow<CraftingObject?> =
		_craftingObjectRepo.getCraftingObjectById(id)
}