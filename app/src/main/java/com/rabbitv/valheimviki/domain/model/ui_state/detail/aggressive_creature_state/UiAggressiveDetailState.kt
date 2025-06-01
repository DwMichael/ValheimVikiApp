package com.rabbitv.valheimviki.domain.model.ui_state.detail.aggressive_creature_state

import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.tree.Tree


sealed class UiAggressiveDetailState {
    data object Loading : UiAggressiveDetailState()
    data class Success(
        val biome: Biome,
        val mainBoss: MainBoss,
        val relatedCreatures: List<Creature>,
        val relatedOreDeposits: List<OreDeposit>,
        val relatedMaterials: List<Material>,
        val relatedPointOfInterest: List<PointOfInterest>,
        val relatedTrees: List<Tree>
    ) : UiAggressiveDetailState()

    data class Error(val message: String) : UiAggressiveDetailState()
}