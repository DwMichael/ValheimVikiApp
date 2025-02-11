package com.rabbitv.valheimviki.data

import com.rabbitv.valheimviki.domain.model.creature.CreatureDtoX
import com.rabbitv.valheimviki.domain.model.creature.Type

object Creatures {
    val mockCreaturesLocal = listOf(
        CreatureDtoX(
            id = "creature-boss-1",
            biomeId = "biome-1",
            typeName = "Dragon Lord",
            type = Type.BOSS.name, // BOSS creature
            name = "Dragon",
            primarySpawn = "Volcano",
            summoningItems = "Dragon Scale",
            baseHP = 5000,
            baseDamage = "300",
            forsakenPower = "Firestorm",
            note = "A fearsome boss with unmatched power",
            weakness = "Ice Magic",
            resistance = "Fire",
            immune = "Poison",
            imageUrl = "https://example.com/dragon.jpg",
            order = 1
        ),
        CreatureDtoX(
            id = "creature-miniboss-1",
            biomeId = "biome-2",
            typeName = "Goblin King",
            type = Type.MINI_BOSS.name, // MINI_BOSS creature
            name = "Goblin King",
            primarySpawn = "Cave",
            summoningItems = "Goblin Crown",
            baseHP = 2000,
            baseDamage = "150",
            forsakenPower = "Command",
            note = "A cunning mini-boss who commands goblins",
            weakness = "Light",
            resistance = "Dark",
            immune = "Fear",
            imageUrl = "https://example.com/goblin_king.jpg",
            order = 2
        ),
        CreatureDtoX(
            id = "creature-aggro-1",
            biomeId = "biome-3",
            typeName = "Wild Boar",
            type = Type.AGGRESSIVE_CREATURE.name, // An aggressive creature
            name = "Wild Boar",
            primarySpawn = "Forest",
            summoningItems = null,
            baseHP = 500,
            baseDamage = "50",
            forsakenPower = null,
            note = "A ferocious creature found in the wild",
            weakness = "Fire",
            resistance = "Earth",
            immune = "None",
            imageUrl = "https://example.com/wild_boar.jpg",
            order = 3
        ),
        CreatureDtoX(
            id = "creature-npc-1",
            biomeId = "biome-4",
            typeName = "Forest Sprite",
            type = Type.NPC.name, // An NPC creature
            name = "Forest Sprite",
            primarySpawn = "Enchanted Forest",
            summoningItems = null,
            baseHP = 300,
            baseDamage = "30",
            forsakenPower = "Healing",
            note = "A gentle spirit of the forest",
            weakness = "Iron",
            resistance = "Magic",
            immune = "Poison",
            imageUrl = "https://example.com/forest_sprite.jpg",
            order = 4
        )
    )

    val expectedMockLocalCreatures = listOf(
        CreatureDtoX(
            id = "creature-boss-1",
            biomeId = "biome-1",
            typeName = "Dragon Lord",
            type = Type.BOSS.name, // BOSS creature
            name = "Dragon",
            primarySpawn = "Volcano",
            summoningItems = "Dragon Scale",
            baseHP = 5000,
            baseDamage = "300",
            forsakenPower = "Firestorm",
            note = "A fearsome boss with unmatched power",
            weakness = "Ice Magic",
            resistance = "Fire",
            immune = "Poison",
            imageUrl = "https://example.com/dragon.jpg",
            order = 1
        ),
        CreatureDtoX(
            id = "creature-miniboss-1",
            biomeId = "biome-2",
            typeName = "Goblin King",
            type = Type.MINI_BOSS.name, // MINI_BOSS creature
            name = "Goblin King",
            primarySpawn = "Cave",
            summoningItems = "Goblin Crown",
            baseHP = 2000,
            baseDamage = "150",
            forsakenPower = "Command",
            note = "A cunning mini-boss who commands goblins",
            weakness = "Light",
            resistance = "Dark",
            immune = "Fear",
            imageUrl = "https://example.com/goblin_king.jpg",
            order = 2
        ),
        CreatureDtoX(
            id = "creature-aggro-1",
            biomeId = "biome-3",
            typeName = "Wild Boar",
            type = Type.AGGRESSIVE_CREATURE.name, // An aggressive creature
            name = "Wild Boar",
            primarySpawn = "Forest",
            summoningItems = null,
            baseHP = 500,
            baseDamage = "50",
            forsakenPower = null,
            note = "A ferocious creature found in the wild",
            weakness = "Fire",
            resistance = "Earth",
            immune = "None",
            imageUrl = "https://example.com/wild_boar.jpg",
            order = 3
        ),
        CreatureDtoX(
            id = "creature-npc-1",
            biomeId = "biome-4",
            typeName = "Forest Sprite",
            type = Type.NPC.name, // An NPC creature
            name = "Forest Sprite",
            primarySpawn = "Enchanted Forest",
            summoningItems = null,
            baseHP = 300,
            baseDamage = "30",
            forsakenPower = "Healing",
            note = "A gentle spirit of the forest",
            weakness = "Iron",
            resistance = "Magic",
            immune = "Poison",
            imageUrl = "https://example.com/forest_sprite.jpg",
            order = 4
        )
    )

    val mockCreaturesApi = listOf(
        CreatureDtoX(
            id = "creature-api-boss-1",
            biomeId = "biome-10",
            typeName = "Hydra Overlord",
            type = Type.BOSS.name, // BOSS creature
            name = "Hydra Overlord",
            primarySpawn = "Mystic Lake",
            summoningItems = "Hydra Scale",
            baseHP = 6000,
            baseDamage = "350",
            forsakenPower = "Venomous Breath",
            note = "A mighty boss that commands the serpentine beasts",
            weakness = "Lightning",
            resistance = "Water",
            immune = "Cold",
            imageUrl = "https://example.com/hydra_overlord.jpg",
            order = 1
        ),
        CreatureDtoX(
            id = "creature-api-miniboss-1",
            biomeId = "biome-11",
            typeName = "Shadow Assassin",
            type = Type.MINI_BOSS.name, // MINI_BOSS creature
            name = "Shadow Assassin",
            primarySpawn = "Dark Alley",
            summoningItems = "Shadow Cloak",
            baseHP = 2500,
            baseDamage = "200",
            forsakenPower = "Evasive Strike",
            note = "A stealthy mini boss with lethal strikes",
            weakness = "Bright Light",
            resistance = "Dark",
            immune = "Silence",
            imageUrl = "https://example.com/shadow_assassin.jpg",
            order = 2
        ),
        CreatureDtoX(
            id = "creature-api-aggressive-1",
            biomeId = "biome-12",
            typeName = "Raging Wolf",
            type = Type.AGGRESSIVE_CREATURE.name, // Aggressive creature
            name = "Raging Wolf",
            primarySpawn = "Snowy Forest",
            summoningItems = null,
            baseHP = 700,
            baseDamage = "80",
            forsakenPower = null,
            note = "A ferocious creature known for its pack attacks",
            weakness = "Fire",
            resistance = "Cold",
            immune = "None",
            imageUrl = "https://example.com/raging_wolf.jpg",
            order = 3
        ),
        CreatureDtoX(
            id = "creature-api-passive-1",
            biomeId = "biome-13",
            typeName = "Gentle Deer",
            type = Type.PASSIVE_CREATURE.name, // Passive creature
            name = "Gentle Deer",
            primarySpawn = "Sunny Meadow",
            summoningItems = null,
            baseHP = 400,
            baseDamage = "20",
            forsakenPower = "Graceful Aura",
            note = "A peaceful creature that roams the meadows",
            weakness = "Predators",
            resistance = "Wind",
            immune = "None",
            imageUrl = "https://example.com/gentle_deer.jpg",
            order = 4
        )
    )

    val expectedMockApiCreatures = listOf(
        CreatureDtoX(
            id = "creature-api-boss-1",
            biomeId = "biome-10",
            typeName = "Hydra Overlord",
            type = Type.BOSS.name, // BOSS creature
            name = "Hydra Overlord",
            primarySpawn = "Mystic Lake",
            summoningItems = "Hydra Scale",
            baseHP = 6000,
            baseDamage = "350",
            forsakenPower = "Venomous Breath",
            note = "A mighty boss that commands the serpentine beasts",
            weakness = "Lightning",
            resistance = "Water",
            immune = "Cold",
            imageUrl = "https://example.com/hydra_overlord.jpg",
            order = 1
        ),
        CreatureDtoX(
            id = "creature-api-miniboss-1",
            biomeId = "biome-11",
            typeName = "Shadow Assassin",
            type = Type.MINI_BOSS.name, // MINI_BOSS creature
            name = "Shadow Assassin",
            primarySpawn = "Dark Alley",
            summoningItems = "Shadow Cloak",
            baseHP = 2500,
            baseDamage = "200",
            forsakenPower = "Evasive Strike",
            note = "A stealthy mini boss with lethal strikes",
            weakness = "Bright Light",
            resistance = "Dark",
            immune = "Silence",
            imageUrl = "https://example.com/shadow_assassin.jpg",
            order = 2
        ),
        CreatureDtoX(
            id = "creature-api-aggressive-1",
            biomeId = "biome-12",
            typeName = "Raging Wolf",
            type = Type.AGGRESSIVE_CREATURE.name, // Aggressive creature
            name = "Raging Wolf",
            primarySpawn = "Snowy Forest",
            summoningItems = null,
            baseHP = 700,
            baseDamage = "80",
            forsakenPower = null,
            note = "A ferocious creature known for its pack attacks",
            weakness = "Fire",
            resistance = "Cold",
            immune = "None",
            imageUrl = "https://example.com/raging_wolf.jpg",
            order = 3
        ),
        CreatureDtoX(
            id = "creature-api-passive-1",
            biomeId = "biome-13",
            typeName = "Gentle Deer",
            type = Type.PASSIVE_CREATURE.name, // Passive creature
            name = "Gentle Deer",
            primarySpawn = "Sunny Meadow",
            summoningItems = null,
            baseHP = 400,
            baseDamage = "20",
            forsakenPower = "Graceful Aura",
            note = "A peaceful creature that roams the meadows",
            weakness = "Predators",
            resistance = "Wind",
            immune = "None",
            imageUrl = "https://example.com/gentle_deer.jpg",
            order = 4
        )
    )

    val expectedBossList = listOf(
        CreatureDtoX(
            id = "creature-boss-1",
            biomeId = "biome-1",
            typeName = "Dragon Lord",
            type = Type.BOSS.name, // BOSS creature
            name = "Dragon",
            primarySpawn = "Volcano",
            summoningItems = "Dragon Scale",
            baseHP = 5000,
            baseDamage = "300",
            forsakenPower = "Firestorm",
            note = "A fearsome boss with unmatched power",
            weakness = "Ice Magic",
            resistance = "Fire",
            immune = "Poison",
            imageUrl = "https://example.com/dragon.jpg",
            order = 1
        ),
    )


    val expectedMiniBossList = listOf(
        CreatureDtoX(
            id = "creature-api-miniboss-1",
            biomeId = "biome-11",
            typeName = "Shadow Assassin",
            type = Type.MINI_BOSS.name, // MINI_BOSS creature
            name = "Shadow Assassin",
            primarySpawn = "Dark Alley",
            summoningItems = "Shadow Cloak",
            baseHP = 2500,
            baseDamage = "200",
            forsakenPower = "Evasive Strike",
            note = "A stealthy mini boss with lethal strikes",
            weakness = "Bright Light",
            resistance = "Dark",
            immune = "Silence",
            imageUrl = "https://example.com/shadow_assassin.jpg",
            order = 2
        ),
    )


}