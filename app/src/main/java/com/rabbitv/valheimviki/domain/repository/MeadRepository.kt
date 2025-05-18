package com.rabbitv.valheimviki.domain.repository

import com.rabbitv.valheimviki.domain.model.mead.Mead
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface MeadRepository {
    suspend fun fetchMeads(language: String): Response<List<Mead>>
    fun getLocalMeads(): Flow<List<Mead>>
    fun getMeadsBySubCategory(subCategory: String): List<Mead>
    suspend fun insertMeads(meads: List<Mead>)
}