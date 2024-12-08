package com.example.health.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.health.ui.screens.LoginScreen
import com.example.health.ui.screens.MedicineDetailsScreen
import com.example.health.ui.screens.MedicineListScreen
import com.example.health.ui.viewmodels.AppVM
import com.example.health.utils.Constants
import com.example.health.data.apiHelper.Result


sealed class HealthCareScreen(val route: String, val screenName: String) {
    data object Login : HealthCareScreen(Constants.Routes.LOGIN, Constants.Screens.LOGIN)
    data object MedicineList :
        HealthCareScreen(Constants.Routes.MEDICINE_LISTING, Constants.Screens.MEDICINE_LISTING) {
        fun createRoute(userName: String) =
            Constants.Routes.MEDICINE_LISTING.replace("{${Constants.ArgsName.USER_NAME}}", userName)
    }

    data object MedicineDetails :
        HealthCareScreen(Constants.Routes.MEDICINE_DETAILS, Constants.Screens.MEDICINE_DETAILS) {
        fun createRoute(medicineId: String) = Constants.Routes.MEDICINE_DETAILS.replace(
            "{${Constants.ArgsName.MEDICINE_ID}}", medicineId
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthCareAppBar(
    currentScreen: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    if ((currentScreen != HealthCareScreen.Login.route)) TopAppBar(title = { Text(currentScreen) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = currentScreen
                    )
                }
            }
        })
}

@Composable
fun HealthCareApp(
    modifier: Modifier = Modifier,
    viewModel: AppVM = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = when (backStackEntry?.destination?.route) {
        HealthCareScreen.MedicineList.route -> HealthCareScreen.MedicineList.screenName
        HealthCareScreen.MedicineDetails.route -> HealthCareScreen.MedicineDetails.screenName
        else -> HealthCareScreen.Login.route
    }

    Scaffold(topBar = {
        HealthCareAppBar(
            canNavigateBack = navController.previousBackStackEntry != null,
            navigateUp = { navController.navigateUp() },
            currentScreen = currentScreen
        )
    }) { innerPadding ->
        val uiState by viewModel.appDataState.collectAsState()
        NavHost(
            navController = navController, startDestination = HealthCareScreen.Login.route
        ) {
            composable(HealthCareScreen.Login.route) {
                LoginScreen(modifier = Modifier.padding(innerPadding), onLoginSuccess = {
                    navController.navigate(HealthCareScreen.MedicineList.createRoute(it))
                })
            }

            composable(
                route = HealthCareScreen.MedicineList.route,
                arguments = listOf(navArgument(Constants.ArgsName.USER_NAME) {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                val username =
                    backStackEntry.arguments?.getString(Constants.ArgsName.USER_NAME) ?: "User"
                MedicineListScreen(Modifier.padding(innerPadding),
                    username = username,
                    dataState = uiState,
                    onMedicineClick = { medicineId ->
                        navController.navigate(
                            HealthCareScreen.MedicineDetails.createRoute(
                                medicineId.toString()
                            )
                        )
                    })
            }
            composable(
                route = HealthCareScreen.MedicineDetails.route,
                arguments = listOf(navArgument(Constants.ArgsName.MEDICINE_ID) {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                val medicineID =
                    backStackEntry.arguments?.getString(Constants.ArgsName.MEDICINE_ID) ?: "0"
                // Fetch the medicine details based on ID
                val medicine =
                    (uiState as? Result.Success)?.data?.medicines?.find { it.id == medicineID.toInt() }

                MedicineDetailsScreen(
                    medicineDose = medicine, modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }

}