package com.rabbitv.valheimviki.data.repository

import com.rabbitv.valheimviki.data.remote.api.ApiBiomeService
import com.rabbitv.valheimviki.domain.model.BiomeDtoX
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ApiBiomeRepository @Inject constructor(private val biomeService: ApiBiomeService) {

        fun getAllBiomes(lang:String): Flow<List<BiomeDtoX>>{
            return flow {
                emit(biomeService.getAllBiomes(lang))
            }.map {
                biomeDto ->
                biomeDto.biomes.filter {
                    it.biomeId != "00000000-0000-0000-0000-000000000000"
                }
            }
        }
}