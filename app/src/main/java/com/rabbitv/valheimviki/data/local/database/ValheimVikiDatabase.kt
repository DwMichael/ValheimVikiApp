package com.rabbitv.valheimviki.data.local.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.rabbitv.valheimviki.data.local.dao.BiomeDao
import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.domain.model.biome.BiomeDtoX
import com.rabbitv.valheimviki.domain.model.creature.CreatureDtoX

@Database(entities = [BiomeDtoX::class, CreatureDtoX::class], version = 3)
abstract class ValheimVikiDatabase : RoomDatabase() {
    abstract fun biomeDao(): BiomeDao
    abstract fun creatureDao(): CreatureDao
}