package com.rabbitv.valheimviki.domain.use_cases.crafting_object.get_crafting_object_by_ids

import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.repository.CraftingObjectRepository
import kotlinx.coroutines.flow.Flow


import javax.inject.Inject


class GetCraftingObjectByIdsUseCase @Inject constructor( private val craftingObjectRepository: CraftingObjectRepository) {
    operator fun invoke(
        ids: List<String>
    ): Flow<CraftingObject?> = craftingObjectRepository.getCraftingObjectByIds(ids)
}