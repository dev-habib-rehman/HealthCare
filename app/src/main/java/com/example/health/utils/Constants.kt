package com.example.health.utils

object Constants {

    object ApiError {
        const val API_NO_BODY = "Response body is null"
    }

    object ApiEndpoints {
        const val MOCKY_ENDPOINT = "v3/cd4291d3-1ff3-4a38-8fe2-dbf36008ca27"
    }

    object Database {
        const val DB_NAME = "App_database"
        const val DB_CORRUPTED_DATA = "Corrupted data in database"
        const val DB_ERROR = "Unknown database error occurred."
        const val DB_NO_DATA = "No data found in the database."
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