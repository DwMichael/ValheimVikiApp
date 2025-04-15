package com.rabbitv.valheimviki.domain.use_cases.tree

import com.rabbitv.valheimviki.domain.use_cases.tree.get_local_trees.GetLocalTreesUseCase
import com.rabbitv.valheimviki.domain.use_cases.tree.get_tree_by_id.GetTreeByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.tree.get_trees_by_ids.GetTreesByIdsUseCase

data class TreeUseCases(
    val getLocalTreesUseCase: GetLocalTreesUseCase,
    val getTreeByIdUseCase: GetTreeByIdUseCase,
    val getTreesByIdsUseCase: GetTreesByIdsUseCase
)
