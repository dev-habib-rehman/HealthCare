package com.example.health.data.remote.models

import com.example.health.data.local.entities.MedicineEntity
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MedicineResponse(
    @SerializedName("medicines") val medicines: List<MedicineDose>? = null
) : Serializable

data class MedicineDose(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("dose") val dose: String? = null,
    @SerializedName("strength") val strength: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("manufacturer") val manufacturer: String? = null
) : Serializable

fun MedicineDose.toEntity(): MedicineEntity {
    return MedicineEntity(
        id = this.id,
        name = this.name,
        dose = this.dose,
        strength = this.strength,
        description = this.description,
        manufacturer = this.manufacturer
    )
}

fun MedicineResponse.toEntities(): List<MedicineEntity> {
    return medicines?.map { medicine ->
        MedicineEntity(
            id = medicine.id,
            name = medicine.name,
            dose = medicine.dose,
            strength = medicine.strength,
            description = medicine.description,
            manufacturer = medicine.manufacturer
        )
    } ?: emptyList()
}