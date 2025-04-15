package com.rabbitv.valheimviki.domain.use_cases.tree.get_local_trees

import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.repository.TreeRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetLocalTreesUseCase@Inject constructor(
    private val treeRepository: TreeRepository
) {
    operator fun invoke(): Flow<List<Tree>> {
        return treeRepository.getLocalTrees()
    }
}
