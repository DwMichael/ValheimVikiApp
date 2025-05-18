package com.rabbitv.valheimviki.domain.use_cases.crafting_object.get_local_crafting_object_use_case

import com.rabbitv.valheimviki.domain.exceptions.CraftingObjectFetchLocalException
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.repository.CraftingObjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLocalCraftingObjectsUseCase @Inject constructor(
    private val craftingObjectRepository: CraftingObjectRepository
) {
    operator fun invoke(): Flow<List<CraftingObject>> {
        return try {
            craftingObjectRepository.getLocalCraftingObjects().map { craftingObjects ->
                craftingObjects.sortedBy { it.order }
            }
        } catch (e: Exception) {
            throw CraftingObjectFetchLocalException("Get local crafting Objects encounter exception $e")
        }

    }
}