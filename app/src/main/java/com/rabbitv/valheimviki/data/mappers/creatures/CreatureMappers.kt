package com.rabbitv.valheimviki.data.mappers.creatures

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.creature.mini_boss.MiniBoss
import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.model.creature.passive.PassiveCreature

fun Creature.toMainBoss(): MainBoss{
    return MainBoss(
        id = this.id,
        category = this.category.toString(),
        subCategory = this.subCategory.toString(),
        imageUrl = this.imageUrl.toString(),
        name = this.name.toString(),
        description = this.description.toString(),
        order = this.order,
        levels = this.levels?.toInt() ?: 0,
        baseHP = this.baseHP ?:0,
        weakness = this.weakness.toString(),
        resistance = this.resistance.toString(),
        baseDamage = this.baseDamage.toString(),
        collapseImmune = this.collapseImmune.toString(),
        forsakenPower = this.forsakenPower.toString(),
    )
}

fun Creature.toMiniBoss(): MiniBoss{
    return MiniBoss(
        id = this.id,
        category = this.category.toString(),
        subCategory = this.subCategory.toString(),
        imageUrl = this.imageUrl.toString(),
        name = this.name.toString(),
        description = this.description.toString(),
        order = this.order,
        levels = this.levels?.toInt() ?: 0,
        baseHP = this.baseHP ?:0,
        weakness = this.weakness.toString(),
        resistance = this.resistance.toString(),
        baseDamage= this.baseDamage.toString(),
        collapseImmune = this.collapseImmune.toString(),
    )
}
fun Creature.toAggressiveCreature(): AggressiveCreature{
    return AggressiveCreature(
        id = this.id,
        category = this.category.toString(),
        subCategory = this.subCategory.toString(),
        imageUrl = this.imageUrl.toString(),
        name = this.name.toString(),
        description = this.description.toString(),
        order = this.order,
        levels = this.levels?.toInt() ?: 0,
        baseHP = this.baseHP ?:0,
        weakness = this.weakness.toString(),
        resistance = this.resistance.toString(),
        baseDamage= this.baseDamage.toString(),
        imageStarOne= this.imageStarOne.toString(),
        imageStarTwo= this.imageStarTwo.toString(),

    )
}

fun Creature.toPassiveCreature(): PassiveCreature{
    return PassiveCreature(
        id = this.id,
        category = this.category.toString(),
        subCategory = this.subCategory.toString(),
        imageUrl = this.imageUrl.toString(),
        name = this.name.toString(),
        description = this.description.toString(),
        order = this.order,
        levels = this.levels?.toInt() ?: 0,
        baseHP = this.baseHP ?:0,
        abilities = this.abilities.toString(),
        imageStarOne = this.imageStarOne.toString(),
        imageStarTwo= this.imageStarTwo.toString(),
    )
}
fun Creature.toNPC(): NPC{
    return NPC(
        id = this.id,
        category = this.category.toString(),
        subCategory = this.subCategory.toString(),
        imageUrl = this.imageUrl.toString(),
        name = this.name.toString(),
        description = this.description.toString(),
        biography = this.description.toString(),
        location = this.description.toString(),
        order = this.order,
    )
}



fun MainBoss.toCreature(): Creature{
    return Creature(
        id = this.id,
        category = this.category,
        subCategory = this.subCategory,
        imageUrl = this.imageUrl,
        name = this.name,
        description = this.description,
        order = this.order,
        levels = this.levels,
        baseHP = this.baseHP,
        weakness = this.weakness,
        resistance = this.resistance,
        baseDamage = this.baseDamage,
        collapseImmune = this.collapseImmune,
        forsakenPower = this.forsakenPower,
        imageStarOne = null,
        imageStarTwo = null,
        abilities = null
    )
}

fun MiniBoss.toCreature(): Creature {
    return Creature(
        id = this.id,
        category = this.category,
        subCategory = this.subCategory,
        imageUrl = this.imageUrl,
        name = this.name,
        description = this.description,
        order = this.order,
        levels = this.levels,
        baseHP = this.baseHP,
        weakness = this.weakness,
        resistance = this.resistance,
        baseDamage = this.baseDamage,
        collapseImmune = this.collapseImmune,
        forsakenPower = null,
        imageStarOne = null,
        imageStarTwo = null,
        abilities = null
    )
}

fun AggressiveCreature.toCreature(): Creature {
    return Creature(
        id = this.id,
        category = this.category,
        subCategory = this.subCategory,
        imageUrl = this.imageUrl,
        name = this.name,
        description = this.description,
        order = this.order,
        levels = this.levels,
        baseHP = this.baseHP,
        weakness = this.weakness,
        resistance = this.resistance,
        baseDamage = this.baseDamage,
        collapseImmune = null,
        forsakenPower = null,
        imageStarOne = this.imageStarOne,
        imageStarTwo = this.imageStarTwo,
        abilities = null
    )
}

fun PassiveCreature.toCreature(): Creature {
    return Creature(
        id = this.id,
        category = this.category,
        subCategory = this.subCategory,
        imageUrl = this.imageUrl,
        name = this.name,
        description = this.description,
        order = this.order,
        levels = this.levels,
        baseHP = this.baseHP,
        weakness = null,
        resistance = null,
        baseDamage = null,
        collapseImmune = null,
        forsakenPower = null,
        imageStarOne = this.imageStarOne,
        imageStarTwo = this.imageStarTwo,
        abilities = this.abilities
    )
}

fun NPC.toCreature(): Creature {
    return Creature(
        id = this.id,
        category = this.category,
        subCategory = this.subCategory,
        imageUrl = this.imageUrl,
        name = this.name,
        description = this.description,
        order = this.order,
        levels = null,
        baseHP = null,
        weakness = null,
        resistance = null,
        baseDamage = null,
        collapseImmune = null,
        forsakenPower = null,
        imageStarOne = null,
        imageStarTwo = null,
        abilities = null
    )
}



fun List<Creature>.toMainBosses():List<MainBoss>{
    return this.map { it.toMainBoss() }
}
fun List<Creature>.toMiniBosses():List<MiniBoss>{
    return this.map { it.toMiniBoss() }
}

fun List<Creature>.toAggressiveCreatures():List<AggressiveCreature>{
    return this.map { it.toAggressiveCreature() }
}

fun List<Creature>.toPassiveCreatures():List<PassiveCreature>{
    return this.map { it.toPassiveCreature() }
}

fun List<Creature>.toNPC():List<NPC>{
    return this.map { it.toNPC() }
}


