package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.mead.Mead
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface MeadRepository {
    suspend fun fetchMeads(language: String): Response<List<Mead>>
    fun getLocalMeads(): Flow<List<Mead>>
    fun getMeadsByIds(ids: List<String>): Flow<List<Mead>>
    fun getMeadById(id: String): Flow<Mead?>
    fun getMeadsBySubCategory(subCategory: String): Flow<List<Mead>>
    suspend fun insertMeads(meads: List<Mead>)
}