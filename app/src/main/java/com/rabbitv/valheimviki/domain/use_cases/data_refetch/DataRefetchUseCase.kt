package com.rabbitv.valheimviki.domain.use_cases.data_refetch

import com.example.domain.entities.tool.Tool
import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.data_refetch_result.DataRefetchResult
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.relation.Relation
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.repository.ArmorRepository
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import com.rabbitv.valheimviki.domain.repository.BuildingMaterialRepository
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import com.rabbitv.valheimviki.domain.repository.FoodRepository
import com.rabbitv.valheimviki.domain.repository.MaterialRepository
import com.rabbitv.valheimviki.domain.repository.MeadRepository
import com.rabbitv.valheimviki.domain.repository.OreDepositRepository
import com.rabbitv.valheimviki.domain.repository.PointOfInterestRepository
import com.rabbitv.valheimviki.domain.repository.RelationRepository
import com.rabbitv.valheimviki.domain.repository.ToolRepository
import com.rabbitv.valheimviki.domain.repository.TreeRepository
import com.rabbitv.valheimviki.domain.repository.WeaponRepository
import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import java.io.IOException
import java.net.UnknownHostException


class DataRefetchUseCase @Inject constructor(
    private val biomeRepository: BiomeRepository,
    private val creatureRepository: CreatureRepository,
    private val oreDepositRepository: OreDepositRepository,
    private val materialsRepository: MaterialRepository,
    private val relationsRepository: RelationRepository,
    private val pointOfInterestRepository: PointOfInterestRepository,
    private val treeRepository: TreeRepository,
    private val foodRepository: FoodRepository,
    private val weaponRepository: WeaponRepository,
    private val armorRepository: ArmorRepository,
    private val meadRepository: MeadRepository,
    private val toolRepository: ToolRepository,
    private val buildingMaterialRepository: BuildingMaterialRepository,
    val dataStoreUseCases: DataStoreUseCases,
) {
    suspend fun refetchAllData(): DataRefetchResult {
        return try {
            val language = dataStoreUseCases.languageProvider().first()

            if (shouldNotRefreshData()) {
                return DataRefetchResult.Success
            }

            val biomeResponse = biomeRepository.fetchBiomes(language)
            val creatureResponse = creatureRepository.fetchCreatures(language)
            val oreDepositResponse = oreDepositRepository.fetchOreDeposits(language)
            val materialsResponse = materialsRepository.fetchMaterials(language)
            val pointOfInterestResponse = pointOfInterestRepository.fetchPointOfInterests(language)
            val treeResponse = treeRepository.fetchTrees(language)
            val foodResponse = foodRepository.fetchFoodList(language)
            val weaponResponse = weaponRepository.fetchWeapons(language)
            val armorResponse = armorRepository.fetchArmor(language)
            val meadResponse = meadRepository.fetchMeads(language)
            val toolResponse = toolRepository.fetchTools(language)
            val buildingMaterialResponse =
                buildingMaterialRepository.fetchBuildingMaterial(language)
            val relationResponse = relationsRepository.fetchRelations()


            if (biomeResponse.isSuccessful &&
                creatureResponse.isSuccessful &&
                oreDepositResponse.isSuccessful &&
                materialsResponse.isSuccessful &&
                pointOfInterestResponse.isSuccessful &&
                treeResponse.isSuccessful &&
                foodResponse.isSuccessful &&
                weaponResponse.isSuccessful &&
                armorResponse.isSuccessful &&
                meadResponse.isSuccessful &&
                toolResponse.isSuccessful &&
                buildingMaterialResponse.isSuccessful &&
                relationResponse.isSuccessful
            ) {

                biomeResponse.body()?.let { biomes ->
                    if (biomes.isNotEmpty()) {
                        biomeRepository.storeBiomes(biomes)
                    } else {
                        return DataRefetchResult.Error("Empty biome data received")
                    }
                } ?: return DataRefetchResult.Error("Null biome data received")

                creatureResponse.body()?.let { creatures ->
                    if (creatures.isNotEmpty()) {
                        creatureRepository.insertCreatures(creatures)
                    } else {
                        return DataRefetchResult.Error("Empty creature data received")
                    }
                } ?: return DataRefetchResult.Error("Null creature data received")

                oreDepositResponse.body()?.let {
                    if (it.isNotEmpty()) {
                        oreDepositRepository.insertOreDeposit(it)
                    } else {
                        return DataRefetchResult.Error("Empty ore deposit data received")
                    }
                } ?: return DataRefetchResult.Error("Null ore deposit data received")

                materialsResponse.body()?.let {
                    if (it.isNotEmpty()) {
                        materialsRepository.insertMaterials(it)
                    } else {
                        return DataRefetchResult.Error("Empty materials data received")
                    }
                } ?: return DataRefetchResult.Error("Null materials data received")

                pointOfInterestResponse.body()?.let {
                    if (it.isNotEmpty()) {
                        pointOfInterestRepository.insertPointOfInterest(it)
                    } else {
                        return DataRefetchResult.Error("Empty pointOfInterest data received")
                    }
                } ?: return DataRefetchResult.Error("Null pointOfInterest data received")

                treeResponse.body()?.let {
                    if (it.isNotEmpty()) {
                        treeRepository.insertTrees(it)
                    } else {
                        return DataRefetchResult.Error("Empty trees data received")
                    }
                } ?: return DataRefetchResult.Error("Null trees data received")

                foodResponse.body()?.let { foodList ->
                    if (foodList.isNotEmpty()) {
                        foodRepository.insertFoodList(foodList)
                    } else {
                        return DataRefetchResult.Error("Empty food data received")
                    }
                } ?: return DataRefetchResult.Error("Null food data received")

                weaponResponse.body()?.let { weaponList ->
                    if (weaponList.isNotEmpty()) {
                        weaponRepository.insertWeapons(weaponList)
                    } else {
                        return DataRefetchResult.Error("Empty weapons data received")
                    }
                } ?: return DataRefetchResult.Error("Null weapons data received")

                armorResponse.body()?.let { armorList ->
                    if (armorList.isNotEmpty()) {
                        armorRepository.insertArmors(armorList)
                    } else {
                        return DataRefetchResult.Error("Empty armors data received")
                    }
                } ?: return DataRefetchResult.Error("Null armors data received")

                meadResponse.body()?.let { meadList ->
                    if (meadList.isNotEmpty()) {
                        meadRepository.insertMeads(meadList)
                    } else {
                        return DataRefetchResult.Error("Empty meads data received")
                    }
                } ?: return DataRefetchResult.Error("Null meads data received")

                toolResponse.body()?.let { tools ->
                    if (tools.isNotEmpty()) {
                        toolRepository.insertTools(tools)
                    } else {
                        return DataRefetchResult.Error("Empty tools data received")
                    }
                } ?: return DataRefetchResult.Error("Null tools data received")

                buildingMaterialResponse.body()?.let { buildingMaterials ->
                    if (buildingMaterials.isNotEmpty()) {
                        buildingMaterialRepository.insertBuildingMaterial(buildingMaterials)
                    } else {
                        return DataRefetchResult.Error("Empty buildingMaterials data received")
                    }
                } ?: return DataRefetchResult.Error("Null buildingMaterials data received")

                relationResponse.body()?.let { relations ->
                    if (relations.isNotEmpty()) {
                        relationsRepository.insertRelations(relations)
                    } else {
                        return DataRefetchResult.Error("Empty relation data received")
                    }
                } ?: return DataRefetchResult.Error("Null relation data received")

                DataRefetchResult.Success
            } else {
                val errorMessage = "API error: " +
                        (biomeResponse.errorBody()?.string() ?: "") +
                        (creatureResponse.errorBody()?.string() ?: "") +
                        (oreDepositResponse.errorBody()?.string() ?: "") +
                        (materialsResponse.errorBody()?.string() ?: "") +
                        (pointOfInterestResponse.errorBody()?.string() ?: "") +
                        (foodResponse.errorBody()?.string() ?: "") +
                        (treeResponse.errorBody()?.string() ?: "") +
                        (weaponResponse.errorBody()?.string() ?: "") +
                        (armorResponse.errorBody()?.string() ?: "") +
                        (meadResponse.errorBody()?.string() ?: "") +
                        (toolResponse.errorBody()?.string() ?: "") +
                        (buildingMaterialResponse.errorBody()?.string() ?: "") +
                        (relationResponse.errorBody()?.string() ?: "")
                DataRefetchResult.NetworkError(errorMessage)
            }
        } catch (e: Exception) {
            if (e is UnknownHostException
                || e is FetchException
                || e is IOException
            ) {
                DataRefetchResult.NetworkError(e.message ?: "Network error")
            } else {
                DataRefetchResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun shouldNotRefreshData(): Boolean = coroutineScope {


        val deferredResults = listOf(
            async { biomeRepository.getLocalBiomes().first() },
            async { creatureRepository.getLocalCreatures().first() },
            async { oreDepositRepository.getLocalOreDeposits().first() },
            async { materialsRepository.getLocalMaterials().first() },
            async { pointOfInterestRepository.getLocalPointOfInterest().first() },
            async { treeRepository.getLocalTrees().first() },
            async { foodRepository.getLocalFoodList().first() },
            async { weaponRepository.getLocalWeapons().first() },
            async { armorRepository.getLocalArmors().first() },
            async { meadRepository.getLocalMeads().first() },
            async { toolRepository.getLocalTools().first() },
            async { buildingMaterialRepository.getLocalBuildingMaterials().first() },
            async { relationsRepository.getLocalRelations().first() }
        )

        val results = deferredResults.awaitAll()

        val biomes = results[0] as List<Biome>
        val creatures = results[1] as List<Creature>
        val oreDeposits = results[2] as List<OreDeposit>
        val materials = results[3] as List<Material>
        val pointsOfInterest = results[4] as List<PointOfInterest>
        val trees = results[5] as List<Tree>
        val food = results[6] as List<Food>
        val weapons = results[7] as List<Weapon>
        val armors = results[8] as List<Armor>
        val meads = results[9] as List<Mead>
        val tools = results[10] as List<Tool>
        val buildingMaterials = results[11] as List<BuildingMaterial>
        val relations = results[12] as List<Relation>

        return@coroutineScope (
                biomes.size == 9 &&
                        creatures.size == 83 &&
                        oreDeposits.size == 9 &&
                        materials.size == 267 &&
                        pointsOfInterest.size == 51 &&
                        trees.size == 8 &&
                        food.size == 76 &&
                        weapons.size == 100 &&
                        armors.size == 50 &&
                        meads.size == 40 &&
                        tools.size == 14 &&
                        buildingMaterials.size == 248 &&
                        relations.size == 464
                )
    }
}