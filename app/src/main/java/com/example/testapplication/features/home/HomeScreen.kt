package com.example.testapplication.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(onBrowseClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Travel Itinerary", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)

        CurrentTripCard()

        PastTripsSection()

        Button(onClick = onBrowseClick) {
            Text("Browse Destinations")
        }
    }
}

@Composable
fun CurrentTripCard() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Current Trip", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Your next adventure awaits!")
        }
    }
}

@Composable
fun PastTripsSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Past Trips", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        PastTripCard()
        Spacer(modifier = Modifier.height(8.dp))
        PastTripCard()
    }
}

@Composable
fun PastTripCard() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Trip to Paris", style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(onBrowseClick = {})
}
