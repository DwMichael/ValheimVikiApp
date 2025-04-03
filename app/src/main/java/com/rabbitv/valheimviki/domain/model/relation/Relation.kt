package com.rabbitv.valheimviki.domain.model.relation

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Entity(tableName = "relations")
@Serializable
data class Relation(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    val id:String,
    @SerializedName("main_item_id") val mainItemId:String,
    @SerializedName("related_item_id") val relatedItemId:String,
    @SerializedName("quantity") val quantity:Int? = null
)
