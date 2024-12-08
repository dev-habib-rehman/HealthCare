package com.example.health.ui.screens

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.health.data.apiHelper.Result
import com.example.health.data.remote.models.MedicineDose
import com.example.health.data.remote.models.MedicineResponse
import com.example.health.ui.theme.HealthCareTheme
import java.time.LocalTime

@Composable
fun MedicineListScreen(
    modifier: Modifier = Modifier,
    dataState: Result<MedicineResponse>,
    username: String,
    onMedicineClick: (Int?) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Greeting Section
        Greeting(username)

        Spacer(modifier = Modifier.height(16.dp))
        when (dataState) {
            is Result.Empty -> Unit
            is Result.Failure -> {
                FailureState(
                    errorMessage = dataState.exception?.message ?: "An error occurred.",
                )
            }

            is Result.Loading -> LoadingState()
            is Result.Success -> {
                // Medicine List
                val medicines = dataState.data?.medicines ?: emptyList()
                MedicineList(medicines = medicines, onCardClick = onMedicineClick)
            }
        }
    }
}

@Composable
fun Greeting(username: String) {
    val greetingMessage = when (val hour = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalTime.now().hour
    } else {
        "Greetings"
    }) {
        in 5..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        in 17..20 -> "Good Evening"
        else -> "Good Night"
    }

    Text(
        text = "$greetingMessage, $username!",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
fun MedicineList(medicines: List<MedicineDose>, onCardClick: (Int?) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(medicines, key = { it.id ?: 0 }) { medicines ->
            MedicineCard(medicine = medicines, onClick = { onCardClick(medicines.id) })
        }
    }
}

@Composable
fun MedicineCard(medicine: MedicineDose, onClick: () -> Unit) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Name: ${medicine.name}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Dose: ${medicine.dose}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Strength: ${medicine.strength}", style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun FailureState(
    modifier: Modifier = Modifier, errorMessage: String
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = errorMessage, style = MaterialTheme.typography.bodyMedium, color = Color.Red
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Retry later", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MedicineListScreenPreview() {
    HealthCareTheme {
        MedicineListScreen(
            dataState = Result.Success(
                MedicineResponse(
                    listOf(
                        MedicineDose(
                            id = 10,
                            name = "Salbutamol",
                            dose = "2mg",
                            strength = "Mild",
                            description = "Bronchodilator used for asthma and COPD.",
                            manufacturer = "BreathEasy Inc."
                        ), MedicineDose(
                            id = 10,
                            name = "Salbutamol",
                            dose = "2mg",
                            strength = "Mild",
                            description = "Bronchodilator used for asthma and COPD.",
                            manufacturer = "BreathEasy Inc."
                        ), MedicineDose(
                            id = 10,
                            name = "Salbutamol",
                            dose = "2mg",
                            strength = "Mild",
                            description = "Bronchodilator used for asthma and COPD.",
                            manufacturer = "BreathEasy Inc."
                        ), MedicineDose(
                            id = 10,
                            name = "Salbutamol",
                            dose = "2mg",
                            strength = "Mild",
                            description = "Bronchodilator used for asthma and COPD.",
                            manufacturer = "BreathEasy Inc."
                        )
                    )
                ), 200
            ), username = "User"
        )
    }
}