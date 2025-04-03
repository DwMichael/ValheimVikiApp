package com.rabbitv.valheimviki.data.mappers

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss

fun MainBoss.toCreate(): Creature{
    return Creature(
        id = this.id,
        category = this.category,
        subCategory = this.subCategory,
        imageUrl = this.imageUrl,
        name = this.name,
        nameContent = null, // Boss uses name field, not nameContent
        description = this.description,
        descriptionContent = null, // Boss uses description field, not descriptionContent
        order = this.order,
        levels = this.levels,
        baseHP = this.baseHP.toInt(), // Convert Long to Int since Creature uses Int
        weakness = this.weakness,
        resistance = this.resistance,
        baseDamage = this.baseDamage,
        collapseImmune = this.collapseImmune,
        forsakenPower = this.forsakenPower,
        imageStarOne = null, // Boss doesn't have imageStarOne
        imageStarTwo = null, // Boss doesn't have imageStarTwo
        abilities = null // Boss doesn't have abilities
    )
}

fun List<MainBoss>.toCreatures():List<Creature>{
    return this.map { it.toCreate() }
}
