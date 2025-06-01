package com.rabbitv.valheimviki.domain.model.ui_state.detail.biome_detail_state

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.tree.Tree


sealed class UiBiomeDetailState {
    data object Loading : UiBiomeDetailState()
    data class Success(
        val biome: Biome?,
        val mainBoss: MainBoss?,
        val relatedCreatures: List<Creature>,
        val relatedOreDeposits: List<OreDeposit>,
        val relatedMaterials: List<Material>,
        val relatedPointOfInterest: List<PointOfInterest>,
        val relatedTrees: List<Tree>
    ) : UiBiomeDetailState()

    data class Error(val message: String) : UiBiomeDetailState()
}