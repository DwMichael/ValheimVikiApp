package com.rabbitv.valheimviki.domain.model.creature

abstract class Boss : CombatCreature(){
    abstract val collapseImmune: String?
}