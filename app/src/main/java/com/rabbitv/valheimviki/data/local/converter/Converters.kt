package com.rabbitv.valheimviki.data.local.converter

import androidx.room.TypeConverter
import com.rabbitv.valheimviki.domain.model.creature.aggresive.LevelCreatureData
import com.rabbitv.valheimviki.domain.model.weapon.UpgradeInfo
import kotlinx.serialization.json.Json

object Converters {
    private val json = Json { ignoreUnknownKeys = true }

    //FROM
    //Creature
    @TypeConverter
    @JvmStatic
    fun fromLevelCreatureDataList(list: List<LevelCreatureData>): String {
        return json.encodeToString(list)
    }

    //Weapons
    @TypeConverter
    @JvmStatic
    fun fromUpgradeInfo(list: List<UpgradeInfo>): String {
        return json.encodeToString(list)
    }

    //TO
    //Creature
    @TypeConverter
    @JvmStatic
    fun toLevelCreatureDataList(data: String): List<LevelCreatureData> {
        return if (data.isEmpty()) emptyList()
        else json.decodeFromString(data)
    }

    //Weapon
    @TypeConverter
    @JvmStatic
    fun toUpgradeInfoList(data: String): List<UpgradeInfo> {
        return if (data.isEmpty()) emptyList()
        else json.decodeFromString(data)
    }
}