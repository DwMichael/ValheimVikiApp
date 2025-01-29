package com.rabbitv.valheimviki.data.local.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.rabbitv.valheimviki.data.local.dao.BiomeDao
import com.rabbitv.valheimviki.data.local.dao.CreatureDao
import com.rabbitv.valheimviki.domain.model.BiomeDtoX
import com.rabbitv.valheimviki.domain.model.CreatureDtoX

@Database(entities = [BiomeDtoX::class, CreatureDtoX::class], version = 2)
abstract class ValheimVikiDatabase :RoomDatabase() {
    abstract fun biomeDao(): BiomeDao
    abstract fun creatureDao(): CreatureDao
}