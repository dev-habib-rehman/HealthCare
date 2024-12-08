package com.example.health.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.health.data.remote.models.MedicineDose
import com.example.health.utils.Constants
import com.google.gson.annotations.SerializedName

@Entity(tableName = Constants.Entities.TABLE_NAME)
data class MedicineEntity(
    @PrimaryKey @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("dose") val dose: String? = null,
    @SerializedName("strength") val strength: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("manufacturer") val manufacturer: String? = null
)

fun MedicineEntity.toDomain(): MedicineDose {
    return MedicineDose(
        id = this.id,
        name = this.name,
        dose = this.dose,
        strength = this.strength,
        description = this.description,
        manufacturer = this.manufacturer
    )
}

