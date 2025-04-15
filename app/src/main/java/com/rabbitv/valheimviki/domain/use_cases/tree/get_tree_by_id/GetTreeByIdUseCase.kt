package com.rabbitv.valheimviki.domain.use_cases.tree.get_tree_by_id

import com.rabbitv.valheimviki.domain.exceptions.TreesByIdFetchLocalException
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.repository.TreeRepository
import jakarta.inject.Inject

class GetTreeByIdUseCase@Inject constructor(
    private val treeRepository: TreeRepository
) {
    operator fun invoke(id:String): Tree {
        return try {
            treeRepository.getTreeById(id) ?:throw TreesByIdFetchLocalException("Tree with id $id not found")
        }catch (e : Exception)
        {
            throw TreesByIdFetchLocalException("Error fetching from Room Tree by id : ${e.message}")
        }
    }
}