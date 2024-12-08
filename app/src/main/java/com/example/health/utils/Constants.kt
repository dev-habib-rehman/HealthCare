package com.example.health.utils

object Constants {

    object ApiEndpoints {
        const val MOCKY_ENDPOINT = "v3/85b65ddf-b237-4b65-8a31-e9a8586ba746"
    }

    object Database {
        const val DB_NAME = "App_database"
    }

    object ArgsName {
        const val USER_NAME ="username"
        const val MEDICINE_ID = "medicineId"
    }

    object Routes {
        const val LOGIN = "login"
        const val MEDICINE_LISTING = "medicine_listing/{username}"
        const val MEDICINE_DETAILS = "medicine_details/{medicineId}"
    }

    object Screens {
        const val LOGIN = "Login"
        const val MEDICINE_LISTING = "Medicine Listing"
        const val MEDICINE_DETAILS = "Medicine Details"
    }

    object Entities {
        const val TABLE_NAME = "medicine_table"
    }
}