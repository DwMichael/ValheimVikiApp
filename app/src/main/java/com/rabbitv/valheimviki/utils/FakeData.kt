package com.rabbitv.valheimviki.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.ui.graphics.Color
import com.composables.icons.lucide.Flame
import com.composables.icons.lucide.Hammer
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Shield
import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.armor.UpgradeArmorInfo
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.npc.NPC
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.material.MaterialUpgrade
import com.rabbitv.valheimviki.domain.model.weapon.UpgradeInfo
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.presentation.components.card.GridLevelInfo
import com.rabbitv.valheimviki.presentation.detail.creature.npc.model.NpcDetailUiState


object FakeData {

    fun fakeArmorList(count: Int = 10): List<Armor> {
        val baseArmor = Armor(
            id = "iron_helmet",
            imageUrl = "https://picsum.photos/200",
            category = "Headgear",
            subCategory = "Helmet",
            name = "Iron Helmet",
            description = "A sturdy iron helmet forged by village blacksmiths.",
            upgradeInfoList = listOf(
                UpgradeArmorInfo(
                    armor = 35,
                    effect = "+2 Defense",
                    upgradeLevel = 1,
                    durability = 120,
                    stationLevel = 2,
                    price = 50,
                    usage = "Protects the wearer’s head."
                )
            ),
            order = 1
        )

        return List(count) { idx ->
            baseArmor.copy(
                id = "${baseArmor.id}_$idx",
                order = idx + 1
            )
        }
    }


    val level1RequiredMaterials: List<MaterialUpgrade> = listOf(
        MaterialUpgrade(
            material =   Material(
                id = "mat_iron_ingot",
                imageUrl = "https://example.com/images/materials/iron_ingot.png",
                category = "Ore",
                subCategory = "Metal",
                name = "Iron Ingot",
                description = "A bar of smelted iron, still warm from the forge.",
                usage = "Crafting weapons and heavy armor.",
                growthTime = null,
                needCultivatorGround = null,
                price = 15,
                effect = null,
                sellPrice = null,
                order = 0,
                subType = null
            ),
            quantityList = listOf(5)
        ),
        MaterialUpgrade(
            material = Material(
                id = "mat_steel_sword",
                imageUrl = "https://example.com/images/items/steel_sword.png",
                category = "Weapon",
                subCategory = "Sword",
                name = "Steel Longsword",
                description = "Reliable blade forged from high-quality steel.",
                usage = "Primary weapon-slot item.",
                growthTime = null,
                needCultivatorGround = null,
                price = 120,
                effect = "+20 Slash Damage",
                sellPrice = null,
                order = 1,
                subType = "Melee"
            ),
            quantityList = listOf(3)
        )
        ,
        MaterialUpgrade(
            material = Material(
                id = "mat_steel_sword",
                imageUrl = "https://example.com/images/items/steel_sword.png",
                category = "Weapon",
                subCategory = "Sword",
                name = "Steel Longsword",
                description = "Reliable blade forged from high-quality steel.",
                usage = "Primary weapon-slot item.",
                growthTime = null,
                needCultivatorGround = null,
                price = 120,
                effect = "+20 Slash Damage",
                sellPrice = null,
                order = 1,
                subType = "Melee"
            ),
            quantityList = listOf(3)
        )
        ,
        MaterialUpgrade(
            material = Material(
                id = "mat_steel_sword",
                imageUrl = "https://example.com/images/items/steel_sword.png",
                category = "Weapon",
                subCategory = "Sword",
                name = "Steel Longsword",
                description = "Reliable blade forged from high-quality steel.",
                usage = "Primary weapon-slot item.",
                growthTime = null,
                needCultivatorGround = null,
                price = 120,
                effect = "+20 Slash Damage",
                sellPrice = null,
                order = 1,
                subType = "Melee"
            ),
            quantityList = listOf(3)
        )
        ,
        MaterialUpgrade(
            material = Material(
                id = "mat_steel_sword",
                imageUrl = "https://example.com/images/items/steel_sword.png",
                category = "Weapon",
                subCategory = "Sword",
                name = "Steel Longsword",
                description = "Reliable blade forged from high-quality steel.",
                usage = "Primary weapon-slot item.",
                growthTime = null,
                needCultivatorGround = null,
                price = 120,
                effect = "+20 Slash Damage",
                sellPrice = null,
                order = 1,
                subType = "Melee"
            ),
            quantityList = listOf(3)
        )
        ,
        MaterialUpgrade(
            material = Material(
                id = "mat_steel_sword",
                imageUrl = "https://example.com/images/items/steel_sword.png",
                category = "Weapon",
                subCategory = "Sword",
                name = "Steel Longsword",
                description = "Reliable blade forged from high-quality steel.",
                usage = "Primary weapon-slot item.",
                growthTime = null,
                needCultivatorGround = null,
                price = 120,
                effect = "+20 Slash Damage",
                sellPrice = null,
                order = 1,
                subType = "Melee"
            ),
            quantityList = listOf(3)
        )
        ,
        MaterialUpgrade(
            material = Material(
                id = "mat_steel_sword",
                imageUrl = "https://example.com/images/items/steel_sword.png",
                category = "Weapon",
                subCategory = "Sword",
                name = "Steel Longsword",
                description = "Reliable blade forged from high-quality steel.",
                usage = "Primary weapon-slot item.",
                growthTime = null,
                needCultivatorGround = null,
                price = 120,
                effect = "+20 Slash Damage",
                sellPrice = null,
                order = 1,
                subType = "Melee"
            ),
            quantityList = listOf(3)
        )
    )

    val level1Stats: List<GridLevelInfo> = listOf(
        GridLevelInfo(
            id = 1,
            icon = Icons.Rounded.Star,
            iconColor = Color(0xFFFFC107),
            title = "Quality Level",
            power = 1
        ),
        GridLevelInfo(
            id = 2,
            icon = Lucide.Flame,
            iconColor = Color(0xFFE53935),
            title = "Attack",
            power = 150
        ),
        GridLevelInfo(
            id = 3,
            icon = Lucide.Shield,
            iconColor = Color(0xFF1E88E5),
            title = "Defense",
            power = 50
        ),
        GridLevelInfo(
            id = 4,
            icon = Lucide.Hammer,
            iconColor = Color(0xFF8E44AD),
            title = "Station Level",
            power = 2
        )
    )

    val fakeWeaponList: List<Weapon> = listOf(
        Weapon(
            id = "wpn_001",
            imageUrl = "https://www.unrealengine.com/marketplace/en-US/product/dark-fantasy-weapons-skeletal-mesh-ready",
            category = "Melee",
            subCategory = "Sword",
            name = "Shadowblade of the Silent Night",
            description = "A swift blade crafted from obsidian, said to be quenched in the tears of a fallen star. It whispers secrets to its wielder in the dead of night.",
            order = 1,
            upgradeInfoList = listOf(
                UpgradeInfo(
                    upgradeLevels = 1,
                    slashDamage = 25,
                    pierceDamage = 15,
                    durability = 100,
                    stationLevel = 1
                ),
                UpgradeInfo(
                    upgradeLevels = 2,
                    slashDamage = 35,
                    pierceDamage = 20,
                    spiritDamage = 5,
                    durability = 120,
                    stationLevel = 2
                ),
                UpgradeInfo(
                    upgradeLevels = 3,
                    slashDamage = 45,
                    pierceDamage = 25,
                    spiritDamage = 10,
                    durability = 150,
                    stationLevel = 3,
                    maximumSkeletonsControllable = 1 // Unique stat for a magic-infused sword
                )
            ),
            subType = "One-Handed Sword"
        ),
        Weapon(
            id = "wpn_002",
            imageUrl = "https://example.com/images/runichammer.png",
            category = "Melee",
            subCategory = "Hammer",
            name = "Runic Earthshaker",
            description = "A massive hammer inscribed with ancient runes of power. Each strike feels like the mountain's fury.",
            order = 2,
            upgradeInfoList = listOf(
                UpgradeInfo(
                    upgradeLevels = 1,
                    bluntDamage = 40,
                    durability = 150,
                    stationLevel = 1
                ),
                UpgradeInfo(
                    upgradeLevels = 2,
                    bluntDamage = 55,
                    fireDamage = 10, // Added elemental damage
                    durability = 180,
                    stationLevel = 2
                ),
                UpgradeInfo(
                    upgradeLevels = 3,
                    bluntDamage = 70,
                    fireDamage = 15,
                    lightningDamage = 5,
                    durability = 220,
                    stationLevel = 4 // Higher station level for powerful upgrades
                )
            ),
            subType = "Two-Handed Hammer"
        ),
        Weapon(
            id = "wpn_003",
            imageUrl = "https://example.com/images/spiritbow.png",
            category = "Ranged",
            subCategory = "Bow",
            name = "Whispering Spiritwood Bow",
            description = "Crafted from the ancient Spiritwood, this bow fires arrows guided by ethereal forces. It hums with a gentle energy.",
            order = 3,
            upgradeInfoList = listOf(
                UpgradeInfo(
                    upgradeLevels = 1,
                    pierceDamage = 20,
                    durability = 80,
                    stationLevel = 1
                ),
                UpgradeInfo(
                    upgradeLevels = 2,
                    pierceDamage = 28,
                    frostDamage = 8,
                    durability = 95,
                    stationLevel = 2
                ),
                UpgradeInfo(
                    upgradeLevels = 3,
                    pierceDamage = 35,
                    frostDamage = 12,
                    poisonDamage = 5, // Added poison
                    durability = 110,
                    stationLevel = 3
                ),
                UpgradeInfo( // Added a 4th upgrade level for this bow
                    upgradeLevels = 4,
                    pierceDamage = 42,
                    frostDamage = 18,
                    poisonDamage = 8,
                    pureDamage = 5, // Added pure damage
                    durability = 130,
                    stationLevel = 4
                )
            ),
            subType = "Longbow"
        )
    )

    val pointOfInterest = listOf(
        PointOfInterest(
            id = "poi_001",
            imageUrl = "https://example.com/images/tower_ruins.png",
            category = "Structure",
            subCategory = "Ruins",
            name = "Ancient Tower",
            description = "Crumbling remains of an ancient watchtower, now home to Greydwarfs.",
            order = 1
        ),
        PointOfInterest(
            id = "poi_002",
            imageUrl = "https://example.com/images/magic_circle.png",
            category = "Mystical",
            subCategory = "Circle",
            name = "Mystic Circle",
            description = "An eerie stone circle radiating magical energy. Rumors say a portal opens here during storms.",
            order = 2
        ),
        PointOfInterest(
            id = "poi_003",
            imageUrl = "https://example.com/images/abandoned_village.png",
            category = "Settlement",
            subCategory = "Abandoned",
            name = "Forgotten Village",
            description = "Once a thriving settlement, now overrun by skeletons and hidden treasures.",
            order = 3
        )
    )


    val fakeNpcDetailUiState = NpcDetailUiState(
        npc = NPC(
            id = "npc_blacksmith",
            name = "Bjorn the Blacksmith",
            imageUrl = "https://example.com/images/npcs/blacksmith.png",
            description = "A sturdy dwarf who forges powerful weapons for travellers.",
            order = 1,
            category = "Overworld",
            subCategory = "Overworld",
            biography = "Overworld",
            location = "Overworld",
        ),

        biome = Biome(
            id = "biome_plains",
            category = "Overworld",
            imageUrl = "https://example.com/images/biomes/plains.png",
            name = "Sunny Plains",
            description = "Rolling green fields with gentle hills and the occasional oak tree.",
            order = 0
        ),

        shopItems = listOf(
            Material(
                id = "mat_iron_ingot",
                imageUrl = "https://example.com/images/materials/iron_ingot.png",
                category = "Ore",
                subCategory = "Metal",
                name = "Iron Ingot",
                description = "A bar of smelted iron, still warm from the forge.",
                usage = "Crafting weapons and heavy armor.",
                growthTime = null,
                needCultivatorGround = null,
                price = 15,
                effect = null,
                sellPrice = null,
                order = 0,
                subType = null
            ),
            Material(
                id = "mat_steel_sword",
                imageUrl = "https://example.com/images/items/steel_sword.png",
                category = "Weapon",
                subCategory = "Sword",
                name = "Steel Longsword",
                description = "Reliable blade forged from high-quality steel.",
                usage = "Primary weapon-slot item.",
                growthTime = null,
                needCultivatorGround = null,
                price = 120,
                effect = "+20 Slash Damage",
                sellPrice = null,
                order = 1,
                subType = "Melee"
            )
        ),
    )

    fun generateFakeMaterials(): List<Material> {
        return listOf(
            Material(
                id = "mat001",
                imageUrl = "https://example.com/image1.jpg",
                category = "Plant",
                subCategory = "Herb",
                name = "Withered bone X3",
                description = "A common herb used in cooking.",
                usage = "Culinary",
                growthTime = "30 days",
                needCultivatorGround = "Yes",
                order = 1,
                subType = "Leafy"
            ),
            Material(
                id = "mat002",
                imageUrl = "https://example.com/image2.jpg",
                category = "Plant",
                subCategory = "Vegetable",
                name = "Carrot",
                description = "Root vegetable rich in Vitamin A.",
                usage = "Culinary",
                growthTime = "70 days",
                needCultivatorGround = "No",
                order = 2,
                subType = "Root"
            ),
            Material(
                id = "mat003",
                imageUrl = "https://example.com/image3.jpg",
                category = "Soil",
                subCategory = "Compost",
                name = "Organic Compost",
                description = "Nutrient-rich compost for plants.",
                usage = "Soil Enrichment",
                growthTime = null,
                needCultivatorGround = "No",
                order = 3,
                subType = "Fertilizer"
            ),
            Material(
                id = "mat004",
                imageUrl = "https://example.com/image4.jpg",
                category = "Tool",
                subCategory = "Hand Tool",
                name = "Trowel",
                description = "Hand tool for digging and planting.",
                usage = "Gardening",
                growthTime = null,
                needCultivatorGround = "No",
                order = 4,
                subType = "Digging"
            ),
            Material(
                id = "mat005",
                imageUrl = "https://example.com/image5.jpg",
                category = "Plant",
                subCategory = "Flower",
                name = "Marigold",
                description = "A flowering plant often used for decoration.",
                usage = "Ornamental",
                growthTime = "50 days",
                needCultivatorGround = "Yes",
                order = 5,
                subType = "Bloom"
            )
        )
    }

    fun generateFakeOreDeposits(): List<OreDeposit> {
        return listOf(
            OreDeposit(
                id = "gold_deposit_1",
                category = "Precious Metals",
                name = "Golden Vein Mine",
                description = "A rich deposit of gold ore.",
                order = 1,
                imageUrl = "https://example.com/gold.jpg"
            ),
            OreDeposit(
                id = "iron_deposit_2",
                category = "Base Metals",
                name = "Iron Peak Quarry",
                description = "A large source of high-grade iron ore.",
                order = 2,
                imageUrl = "https://example.com/iron.jpg"
            ),
            OreDeposit(
                id = "copper_deposit_3",
                category = "Base Metals",
                name = "Copper Canyon Lode",
                description = "An extensive deposit containing significant copper reserves.",
                order = 3,
                imageUrl = "https://example.com/copper.jpg"
            ),
            OreDeposit(
                id = "diamond_deposit_4",
                category = "Gemstones",
                name = "Crystal Caves Mine",
                description = "Known for its high-quality diamond crystals.",
                order = 4,
                imageUrl = "https://example.com/diamond.jpg"
            )
        )
    }


    fun generateFakeCreatures(): List<Creature> {
        return listOf(
            Creature(
                id = "creature001",
                category = "Boss",
                subCategory = "Elder",
                imageUrl = "https://example.com/meadows_troll.png",
                name = "Meadows Troll",
                description = "A massive troll that roams the peaceful meadows. Despite its size, it's known for its gentle nature unless provoked.",
                order = 1,
                levels = 3,
                baseHP = 1000,
                weakness = "Fire",
                resistance = "Frost",
                baseDamage = "100-150",
                collapseImmune = "Yes",
                forsakenPower = "Nature's Wrath",
            ),
            Creature(
                id = "creature002",
                category = "Aggressive",
                subCategory = "Undead",
                imageUrl = "https://example.com/frost_draugr.png",
                name = "Frost Draugr",
                description = "An ancient warrior risen from the dead in the frozen mountains. Carries ice-encrusted weapons and armor.",
                order = 2,
                levels = 2,
                baseHP = 150,
                weakness = "Fire, Spirit",
                resistance = "Frost, Poison",
                baseDamage = "35-45",
            ),
            Creature(
                id = "creature003",
                category = "Passive",
                subCategory = "Wildlife",
                imageUrl = "https://example.com/glowing_deer.png",
                name = "Glowing Deer",
                description = "A magical deer with bioluminescent antlers that light up the Black Forest at night. Its hide has alchemical properties.",
                order = 3,
                levels = 1,
                baseHP = 60,
                weakness = "Pierce",
                resistance = "None",
                baseDamage = "5-10",
                abilities = "Night Vision, Swift Movement",
            ),
            Creature(
                id = "creature004",
                category = "MiniBoss",
                subCategory = "Elemental",
                imageUrl = "https://example.com/swamp_guardian.png",
                name = "Swamp Guardian",
                description = "A sentient mass of vines, mud, and ancient bones that protects the heart of the swamp. Can summon lesser creatures to its aid.",
                order = 4,
                levels = 2,
                baseHP = 500,
                weakness = "Fire, Slash",
                resistance = "Blunt, Pierce, Poison",
                baseDamage = "65-80",
                collapseImmune = "Yes",
                forsakenPower = null,
            ),
            Creature(
                id = "creature005",
                category = "Aggressive",
                subCategory = "Insect",
                imageUrl = "https://example.com/plains_mantis.png",
                name = "Plains Mantis",
                description = "A gigantic praying mantis that camouflages in the tall grass of the plains. Known for its lightning-quick strikes and precise hunting.",
                order = 5,
                levels = 3,
                baseHP = 220,
                weakness = "Blunt, Frost",
                resistance = "Pierce, Slash",
                baseDamage = "75-85",

                )
        )

    }

}
