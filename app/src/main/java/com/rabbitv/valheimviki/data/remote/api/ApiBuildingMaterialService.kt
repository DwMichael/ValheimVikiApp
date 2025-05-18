package com.rabbitv.valheimviki.data.remote.api

import com.rabbitv.valheimviki.domain.model.building_material.BuildingMaterial
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiBuildingMaterialService {
    @GET("/BuildingMaterials")
    suspend fun fetchBuildingMaterial(@Query("lang") lang: String): Response<List<BuildingMaterial>>
}