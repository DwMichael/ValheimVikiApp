package com.rabbitv.valheimviki.domain.mapper

import com.rabbitv.valheimviki.data.mappers.creatures.toAggressiveCreature
import com.rabbitv.valheimviki.data.mappers.creatures.toCreature
import com.rabbitv.valheimviki.data.mappers.creatures.toMainBoss
import com.rabbitv.valheimviki.data.mappers.creatures.toMiniBoss
import com.rabbitv.valheimviki.data.mappers.creatures.toNPC
import com.rabbitv.valheimviki.data.mappers.creatures.toPassiveCreature
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.aggresive.AggressiveCreature
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.model.creature.mini_boss.MiniBoss
import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.model.creature.passive.PassiveCreature

object CreatureFactory {

    inline fun <reified T> createFromCreature(creature: Creature): T {
        return when(T::class){
            MainBoss::class -> creature.toMainBoss() as T
            MiniBoss::class -> creature.toMiniBoss() as T
            AggressiveCreature::class -> creature.toAggressiveCreature() as T
            PassiveCreature::class -> creature.toPassiveCreature() as T
            NPC::class -> creature.toNPC() as T
            else -> throw IllegalArgumentException("Unsupported type: ${T::class.simpleName}")
        }
    }

    fun createCreatureFrom(any: Any): Creature {
        return when (any) {
            is MainBoss -> any.toCreature()
            is MiniBoss -> any.toCreature()
            is AggressiveCreature -> any.toCreature()
            is PassiveCreature -> any.toCreature()
            is NPC -> any.toCreature()
            is Creature -> any
            else -> throw IllegalArgumentException("Unsupported type: ${any::class.simpleName}")
        }
    }

}