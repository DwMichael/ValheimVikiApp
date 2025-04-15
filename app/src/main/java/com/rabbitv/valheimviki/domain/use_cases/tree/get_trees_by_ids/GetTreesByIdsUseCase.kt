package com.rabbitv.valheimviki.domain.use_cases.tree.get_trees_by_ids

import com.rabbitv.valheimviki.domain.exceptions.TreesByIdsFetchLocalException
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.repository.TreeRepository
import jakarta.inject.Inject

class GetTreesByIdsUseCase@Inject constructor(
    private val treeRepository: TreeRepository
) {
    operator fun invoke(ids:List<String>): List<Tree> {
        return try {
            val trees = treeRepository.getTreesByIds(ids)
            if (trees.isNotEmpty()) {
                trees
            }else
            {
                throw TreesByIdsFetchLocalException("No trees found with ids $ids")
            }
        } catch (e: Exception) {
            throw TreesByIdsFetchLocalException("Error fetching from Room trees by ids : ${e.message}")
        }
    }
}