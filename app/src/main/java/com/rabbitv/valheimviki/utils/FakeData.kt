package com.rabbitv.valheimviki.utils

import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit

object FakeData{
    fun generateFakeMaterials(): List<Material> {
        return listOf(
            Material(
                id = "mat001",
                imageUrl = "https://example.com/image1.jpg",
                category = "Plant",
                subCategory = "Herb",
                name = "Basil",
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
                imageStarOne = "https://example.com/meadows_troll_star1.png",
                imageStarTwo = "https://example.com/meadows_troll_star2.png"
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
                imageStarOne = "https://example.com/frost_draugr_star1.png",
                imageStarTwo = "https://example.com/frost_draugr_star2.png"
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
                imageStarOne = null,
                imageStarTwo = null
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
                imageStarOne = "https://example.com/swamp_guardian_star1.png",
                imageStarTwo = "https://example.com/swamp_guardian_star2.png"
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
                imageStarOne = "https://example.com/plains_mantis_star1.png",
                imageStarTwo = "https://example.com/plains_mantis_star2.png"
            )
        )

    }
}