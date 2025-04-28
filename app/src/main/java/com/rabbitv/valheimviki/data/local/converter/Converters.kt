package com.rabbitv.valheimviki.data.local.converter

import androidx.room.TypeConverter
import com.rabbitv.valheimviki.domain.model.creature.aggresive.LevelCreatureData
import kotlinx.serialization.json.Json

object Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    @JvmStatic
    fun fromLevelCreatureDataList(list: List<LevelCreatureData>): String {
        return json.encodeToString(list)
    }

    @TypeConverter
    @JvmStatic
    fun toLevelCreatureDataList(data: String): List<LevelCreatureData> {
        return if (data.isEmpty()) emptyList()
        else json.decodeFromString(data)
    }
}