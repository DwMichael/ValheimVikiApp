package com.rabbitv.valheimviki.domain.exceptions

open class FetchLocalException(message: String) : Exception(message)

class BossesFetchLocalException(message: String) : FetchLocalException(message)
class MiniBossesFetchLocalException(message: String) : FetchLocalException(message)

//Creatures
class CreaturesFetchLocalException(message: String) : FetchLocalException(message)
class CreaturesByIdFetchLocalException(message: String) : FetchLocalException(message)
class CreaturesByIdsFetchLocalException(message: String) : FetchLocalException(message)

//Relations
class RelationsFetchLocalException(message: String) : FetchLocalException(message)

//Biomes
class BiomesFetchLocalException(message: String) : FetchLocalException(message)

//OreDeposit
class OreDepositFetchLocalException(message: String) : FetchLocalException(message)
class OreDepositByIdFetchLocalException(message: String) : FetchLocalException(message)
class OreDepositsByIdsFetchLocalException(message: String) : FetchLocalException(message)

//Materials
class MaterialsFetchLocalException(message: String) : FetchLocalException(message)
class MaterialsByIdFetchLocalException(message: String) : FetchLocalException(message)
class MaterialsByIdsFetchLocalException(message: String) : FetchLocalException(message)

//PointOfInterest
class PointOfInterestFetchLocalException(message: String) : FetchLocalException(message)
class PointOfInterestByIdFetchLocalException(message: String) : FetchLocalException(message)
class PointOfInterestBySubCategoryFetchLocalException(message: String) :
    FetchLocalException(message)

class PointOfInterestByIdsFetchLocalException(message: String) : FetchLocalException(message)
class PointOfInterestBySubCategoryAndIdFetchLocalException(message: String) :
    FetchLocalException(message)

//Trees
class TreesFetchLocalException(message: String) : FetchLocalException(message)
class TreesByIdFetchLocalException(message: String) : FetchLocalException(message)
class TreesByIdsFetchLocalException(message: String) : FetchLocalException(message)

//Food
class FoodFetchLocalException(message: String) : FetchLocalException(message)
class FoodByIdsFetchLocalException(message: String) : FetchLocalException(message)

