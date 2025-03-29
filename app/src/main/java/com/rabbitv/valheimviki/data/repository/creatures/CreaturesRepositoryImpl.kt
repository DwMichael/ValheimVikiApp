package com.rabbitv.valheimviki.data.repository.creatures

import android.util.Log
import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.data.remote.api.ApiCreatureService
import com.rabbitv.valheimviki.domain.model.api_response.ApiResponseSecond
import com.rabbitv.valheimviki.domain.model.creature.main_boss.MainBoss
import com.rabbitv.valheimviki.domain.repository.CreaturesRepository
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
        TODO("Not yet implemented")
    }

    override suspend fun fetchMainBosses(lang: String): List<MainBoss> {

        try {
            val response: Response<ApiResponseSecond<MainBoss>> =  apiService.fetchMainBosses(lang)

            if (response.isSuccessful) {
                val mainBossResponse = response.body()
                val mainBossList = mainBossResponse?.data ?: emptyList()

                return mainBossList
            } else {
                println("FETCH: Unsuccessful response, code=${response.code()}")
                return emptyList()
            }
        } catch (exception: Exception) {
            Log.i("EXEPTION FETCH MAIN BOSSES", exception.message.toString())
            return emptyList()
        }
    }

    override suspend fun storeMainBosses(mainBosses: List<MainBoss>) {
        println(mainBosses)
        if (mainBosses.isNotEmpty()) {
            creatureDao.insertMainBosses(mainBosses)
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
