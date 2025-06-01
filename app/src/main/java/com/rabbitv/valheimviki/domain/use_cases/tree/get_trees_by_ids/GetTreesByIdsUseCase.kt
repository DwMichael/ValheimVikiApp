package com.rabbitv.valheimviki.domain.use_cases.tree.get_trees_by_ids

import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.repository.TreeRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetTreesByIdsUseCase @Inject constructor(
    private val treeRepository: TreeRepository
) {
    operator fun invoke(ids: List<String>): Flow<List<Tree>> {
        return treeRepository.getTreesByIds(ids).flowOn(Dispatchers.IO)
    }
}