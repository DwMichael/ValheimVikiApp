package com.rabbitv.valheimviki.data.repository.creatures

import android.util.Log
import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService
import com.rabbitv.valheimviki.domain.model.creature.Creature
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
import com.rabbitv.valheimviki.utils.bodyList
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class CreaturesRepositoryImpl @Inject constructor(
    private val apiService: ApiCreatureService,
    private val creatureDao: CreatureDao
) : CreaturesRepository {



    override fun getLocalMainBosses(): Flow<List<MainBoss>>  {
        return creatureDao.getLocalMainBosses()
    }

    override fun getMainBossById(id: String): Flow<MainBoss> {
        return creatureDao.getMainBossById(id)
    }

    override suspend fun fetchMainBosses(lang: String): List<MainBoss> {
        try {
            val response: Response<List<MainBoss>> =  apiService.fetchMainBosses(lang)
            return if (response.isSuccessful) {
                response.bodyList()
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            Log.i("EXEPTION FETCH MAIN BOSSES", exception.message.toString())
            return emptyList()
        }
    }

    override suspend fun insertLocalCreatures(creatures: List<Creature>) {
        if (creatures.isNotEmpty()) {
            creatureDao.insertCreatures(creatures)
        }
    }
}

//override suspend fun fetchBoss(lang: String): ApiResponse<MainBoss> {
//    try {
//
//        val response = apiService.getAllCreatures(lang)
//
//        return if (response.success) {
//            ApiResponse<MainBoss>(
//                success = true,
//                error = response.error,
//                message = response.message,
//                creatures = response.creatures,
//            )
//        } else {
//            ApiResponse<MainBoss>(
//                success = false,
//                error = response.error,
//                message = response.message,
//                creatures = emptyList(),
//            )
//        }
//
//    } catch (e: Exception) {
//        val networkException = NetworkExceptionHandler.handleException(e)
//        return ApiResponse<MainBoss>(
//            success = networkException.success,
//            error = networkException.error,
//            message = networkException.message,
//            creatures = emptyList(),
//        )
//    }
//}
