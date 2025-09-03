@file:Suppress("UNCHECKED_CAST")

package com.rabbitv.valheimviki.domain.use_cases.data_refetch

import com.rabbitv.valheimviki.data.mappers.search.toSearchList
import com.rabbitv.valheimviki.domain.exceptions.FetchException
import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.model.biome.Biome
import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import com.rabbitv.valheimviki.domain.model.crafting_object.CraftingObject
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.data_refetch_result.DataRefetchResult
import com.rabbitv.valheimviki.domain.model.food.Food
import com.rabbitv.valheimviki.domain.model.item_tool.ItemTool
import com.rabbitv.valheimviki.domain.model.material.Material
import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.relation.Relation
import com.rabbitv.valheimviki.domain.model.search.Search
import com.rabbitv.valheimviki.domain.model.tree.Tree
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.repository.ArmorRepository
import com.rabbitv.valheimviki.domain.repository.BiomeRepository
import com.rabbitv.valheimviki.domain.repository.BuildingMaterialRepository
import com.rabbitv.valheimviki.domain.repository.CraftingObjectRepository
import com.rabbitv.valheimviki.domain.repository.CreatureRepository
import com.rabbitv.valheimviki.domain.repository.FoodRepository
import com.rabbitv.valheimviki.domain.repository.MaterialRepository
import com.rabbitv.valheimviki.domain.repository.MeadRepository
import com.rabbitv.valheimviki.domain.repository.OreDepositRepository
import com.rabbitv.valheimviki.domain.repository.PointOfInterestRepository
import com.rabbitv.valheimviki.domain.repository.RelationRepository
import com.rabbitv.valheimviki.domain.repository.SearchRepository
import com.rabbitv.valheimviki.domain.repository.ToolRepository
import com.rabbitv.valheimviki.domain.repository.TreeRepository
import com.rabbitv.valheimviki.domain.repository.WeaponRepository
import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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
	private val craftingObjectRepository: CraftingObjectRepository,
	private val searchRepository: SearchRepository,
	private val dataStoreUseCases: DataStoreUseCases,
) {

	// Data category configuration with expected minimum sizes
	private data class DataCategory(
		val name: String,
		val fetchFunction: suspend (String) -> retrofit2.Response<*>,
		val storeFunction: suspend (List<*>) -> Unit,
		val toSearchList: (List<*>) -> List<Search>,
		val expectedMinSize: Int
	)

	suspend fun refetchAllData(): DataRefetchResult {
		return try {
			val language = dataStoreUseCases.languageProvider().first()

			if (shouldNotRefreshData()) {
				return DataRefetchResult.Success
			}

			val categories = createDataCategories()
			val results = refetchDataCategories(categories, language)

			when {
				results.allSuccessful -> {
					updateSearchIndex(results.successfulData)
					DataRefetchResult.Success
				}

				results.hasSuccessful -> {
					updateSearchIndex(results.successfulData)
					DataRefetchResult.PartialSuccess(
						successfulCategories = results.successfulCategories,
						failedCategories = results.failedCategories,
						totalCategories = categories.size
					)
				}

				else -> {
					DataRefetchResult.NetworkError("All data categories failed to fetch")
				}
			}
		} catch (e: Exception) {
			when (e) {
				is UnknownHostException, is FetchException, is IOException -> {
					DataRefetchResult.NetworkError(e.message ?: "Network error")
				}

				else -> {
					DataRefetchResult.Error(e.message ?: "Unknown error")
				}
			}
		}
	}

	private suspend fun refetchDataCategories(
		categories: List<DataCategory>,
		language: String
	): RefetchResults = coroutineScope {
		val deferredResults = categories.map { category ->
			async {
				retryWithBackoff(
					maxAttempts = DataRefetchConfig.MAX_RETRY_ATTEMPTS,
					initialDelay = 1000L
				) {
					try {
						val response = category.fetchFunction(language)
						if (response.isSuccessful) {
							val data = response.body()
							if (data != null && (data as List<*>).isNotEmpty()) {
								category.storeFunction(data)
								val searchItems = category.toSearchList(data)
								RefetchResult.Success(category.name, searchItems)
							} else {
								throw Exception("Empty or null data received for ${category.name}")
							}
						} else {
							throw Exception(
								"API error for ${category.name}: ${
									response.errorBody()?.string() ?: "Unknown"
								}"
							)
						}
					} catch (e: Exception) {
						throw Exception("Failed to fetch ${category.name}: ${e.message}")
					}
				}
			}
		}

		val results = deferredResults.awaitAll()
		RefetchResults(results)
	}

	private suspend fun <T> retryWithBackoff(
		maxAttempts: Int,
		initialDelay: Long,
		block: suspend () -> T
	): T {
		var lastException: Exception? = null
		var delay = initialDelay

		repeat(maxAttempts) { attempt ->
			try {
				return block()
			} catch (e: Exception) {
				lastException = e
				if (attempt < maxAttempts - 1) {
					kotlinx.coroutines.delay(delay)
					delay *= 2 // Exponential backoff
				}
			}
		}

		throw lastException ?: Exception("Retry failed after $maxAttempts attempts")
	}

	private fun createDataCategories(): List<DataCategory> = listOf(
		DataCategory(
			name = "Biomes",
			fetchFunction = { lang -> biomeRepository.fetchBiomes(lang) },
			storeFunction = { data -> biomeRepository.storeBiomes(data as List<Biome>) },
			toSearchList = { data -> (data as List<Biome>).toSearchList() },
			expectedMinSize = 9
		),
		DataCategory(
			name = "Creatures",
			fetchFunction = { lang -> creatureRepository.fetchCreatures(lang) },
			storeFunction = { data -> creatureRepository.insertCreatures(data as List<Creature>) },
			toSearchList = { data -> (data as List<Creature>).toSearchList() },
			expectedMinSize = 83
		),
		DataCategory(
			name = "Ore Deposits",
			fetchFunction = { lang -> oreDepositRepository.fetchOreDeposits(lang) },
			storeFunction = { data -> oreDepositRepository.insertOreDeposit(data as List<OreDeposit>) },
			toSearchList = { data -> (data as List<OreDeposit>).toSearchList() },
			expectedMinSize = 14
		),
		DataCategory(
			name = "Materials",
			fetchFunction = { lang -> materialsRepository.fetchMaterials(lang) },
			storeFunction = { data -> materialsRepository.insertMaterials(data as List<Material>) },
			toSearchList = { data -> (data as List<Material>).toSearchList() },
			expectedMinSize = 272
		),
		DataCategory(
			name = "Points of Interest",
			fetchFunction = { lang -> pointOfInterestRepository.fetchPointOfInterests(lang) },
			storeFunction = { data -> pointOfInterestRepository.insertPointOfInterest(data as List<PointOfInterest>) },
			toSearchList = { data -> (data as List<PointOfInterest>).toSearchList() },
			expectedMinSize = 49
		),
		DataCategory(
			name = "Trees",
			fetchFunction = { lang -> treeRepository.fetchTrees(lang) },
			storeFunction = { data -> treeRepository.insertTrees(data as List<Tree>) },
			toSearchList = { data -> (data as List<Tree>).toSearchList() },
			expectedMinSize = 8
		),
		DataCategory(
			name = "Food",
			fetchFunction = { lang -> foodRepository.fetchFoodList(lang) },
			storeFunction = { data -> foodRepository.insertFoodList(data as List<Food>) },
			toSearchList = { data -> (data as List<Food>).toSearchList() },
			expectedMinSize = 84
		),
		DataCategory(
			name = "Weapons",
			fetchFunction = { lang -> weaponRepository.fetchWeapons(lang) },
			storeFunction = { data -> weaponRepository.insertWeapons(data as List<Weapon>) },
			toSearchList = { data -> (data as List<Weapon>).toSearchList() },
			expectedMinSize = 99
		),
		DataCategory(
			name = "Armor",
			fetchFunction = { lang -> armorRepository.fetchArmor(lang) },
			storeFunction = { data -> armorRepository.insertArmors(data as List<Armor>) },
			toSearchList = { data -> (data as List<Armor>).toSearchList() },
			expectedMinSize = 51
		),
		DataCategory(
			name = "Meads",
			fetchFunction = { lang -> meadRepository.fetchMeads(lang) },
			storeFunction = { data -> meadRepository.insertMeads(data as List<Mead>) },
			toSearchList = { data -> (data as List<Mead>).toSearchList() },
			expectedMinSize = 41
		),
		DataCategory(
			name = "Tools",
			fetchFunction = { lang -> toolRepository.fetchTools(lang) },
			storeFunction = { data -> toolRepository.insertTools(data as List<ItemTool>) },
			toSearchList = { data -> (data as List<ItemTool>).toSearchList() },
			expectedMinSize = 14
		),
		DataCategory(
			name = "Building Materials",
			fetchFunction = { lang -> buildingMaterialRepository.fetchBuildingMaterial(lang) },
			storeFunction = { data -> buildingMaterialRepository.insertBuildingMaterial(data as List<BuildingMaterial>) },
			toSearchList = { data -> (data as List<BuildingMaterial>).toSearchList() },
			expectedMinSize = 258
		),
		DataCategory(
			name = "Crafting Objects",
			fetchFunction = { lang -> craftingObjectRepository.fetchCraftingObject(lang) },
			storeFunction = { data -> craftingObjectRepository.insertCraftingObjects(data as List<CraftingObject>) },
			toSearchList = { data -> (data as List<CraftingObject>).toSearchList() },
			expectedMinSize = 46
		),
		DataCategory(
			name = "Relations",
			fetchFunction = { _ -> relationsRepository.fetchRelations() },
			storeFunction = { data -> relationsRepository.insertRelations(data as List<Relation>) },
			toSearchList = { _ -> emptyList() }, // Relations don't need search indexing
			expectedMinSize = 2724
		)
	)

	private suspend fun updateSearchIndex(searchableItems: List<Search>) {
		if (searchableItems.isNotEmpty()) {
			try {
				searchRepository.deleteAllAndInsertNew(searchableItems)
			} catch (e: Exception) {
				println("Failed to update search index: ${e.message}")
			}
		}
	}

	@Suppress("UNCHECKED_CAST")
	private suspend fun shouldNotRefreshData(): Boolean = coroutineScope {
		val expectedSizes = DataRefetchConfig.EXPECTED_DATA_SIZES

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
			async { craftingObjectRepository.getLocalCraftingObjects().first() },
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
		val itemTools = results[10] as List<ItemTool>
		val buildingMaterials = results[11] as List<BuildingMaterial>
		val craftingObjects = results[12] as List<CraftingObject>
		val relations = results[13] as List<Relation>

		return@coroutineScope (
				biomes.size >= expectedSizes["biomes"]!! &&
						creatures.size >= expectedSizes["creatures"]!! &&
						oreDeposits.size >= expectedSizes["oreDeposits"]!! &&
						materials.size >= expectedSizes["materials"]!! &&
						pointsOfInterest.size >= expectedSizes["pointsOfInterest"]!! &&
						trees.size >= expectedSizes["trees"]!! &&
						food.size >= expectedSizes["food"]!! &&
						weapons.size >= expectedSizes["weapons"]!! &&
						armors.size >= expectedSizes["armors"]!! &&
						meads.size >= expectedSizes["meads"]!! &&
						itemTools.size >= expectedSizes["tools"]!! &&
						buildingMaterials.size >= expectedSizes["buildingMaterials"]!! &&
						craftingObjects.size >= expectedSizes["craftingObjects"]!! &&
						relations.size >= expectedSizes["relations"]!!
				)
	}

	// Helper classes for better organization
	private sealed class RefetchResult {
		data class Success(val categoryName: String, val searchItems: List<Search>) :
			RefetchResult()

		data class Failure(val categoryName: String, val errorMessage: String) : RefetchResult()
	}

	private data class RefetchResults(
		val results: List<RefetchResult>
	) {
		val successfulCategories: List<String> = results
			.filterIsInstance<RefetchResult.Success>()
			.map { it.categoryName }

		val failedCategories: Map<String, String> = results
			.filterIsInstance<RefetchResult.Failure>()
			.associate { it.categoryName to it.errorMessage }

		val successfulData: List<Search> = results
			.filterIsInstance<RefetchResult.Success>()
			.flatMap { it.searchItems }

		val allSuccessful: Boolean = results.all { it is RefetchResult.Success }
		val hasSuccessful: Boolean = results.any { it is RefetchResult.Success }
	}
}