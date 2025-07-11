package com.rabbitv.valheimviki.domain.model.creature

import com.rabbitv.valheimviki.domain.repository.ItemData

abstract class CombatCreature : ItemData {
	abstract override val description: String?
	abstract val order: Int
	abstract val baseHP: Int?
	abstract val weakness: String?
	abstract val resistance: String?
	abstract val baseDamage: String?
}