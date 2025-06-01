package com.rabbitv.valheimviki.domain.use_cases.tree.get_tree_by_id

import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.repository.TreeRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetTreeByIdUseCase @Inject constructor(
    private val treeRepository: TreeRepository
) {
    operator fun invoke(id: String): Flow<Tree?> {
        return treeRepository.getTreeById(id).flowOn(Dispatchers.IO)
    }
}