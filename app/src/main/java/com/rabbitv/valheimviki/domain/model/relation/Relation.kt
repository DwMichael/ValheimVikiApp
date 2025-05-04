package com.rabbitv.valheimviki.domain.model.relation

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Entity(tableName = "relations")
@Serializable
data class Relation(

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id") val id: String,
    @SerializedName("mainItemId") val mainItemId: String,
    @SerializedName("relationItemId") val relatedItemId: String,
    @SerializedName("quantity") val quantity: Int? = null,
    @SerializedName("quantity2star") val quantity2star: Int? = null,
    @SerializedName("quantity3star") val quantity3star: Int? = null,
    @SerializedName("chance1star") val chance1star: Int? = null,
    @SerializedName("chance2star") val chance2star: Int? = null,
    @SerializedName("chance3star") val chance3star: Int? = null

)
