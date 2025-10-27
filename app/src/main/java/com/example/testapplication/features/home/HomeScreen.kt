package com.example.testapplication.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Travel Itinerary", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)

        CurrentTripCard()

        RecommendedDestinationsSection()

        PastTripsSection()

        Button(onClick = onBrowseClick) {
            Text("Browse Destinations")
        }
    }
}

@Composable
fun CurrentTripCard() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text("Current Trip", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Trip to New York", style = androidx.compose.material3.MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text("December 15-22, 2024", style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { /* TODO: Navigate to trip details */ }, modifier = Modifier.align(Alignment.End)) {
                Text("View Details")
            }
        }
    }
}

@Composable
fun RecommendedDestinationsSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Recommended Destinations", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(5) { index ->
                RecommendedDestinationCard(index)
            }
        }
    }
}

@Composable
fun RecommendedDestinationCard(index: Int) {
    val destinations = listOf("Tokyo", "Paris", "Rome", "Bali", "Sydney")
    Card(modifier = Modifier.size(150.dp, 200.dp)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(destinations[index], style = androidx.compose.material3.MaterialTheme.typography.headlineSmall)
        }
    }
}

@Composable
fun PastTripsSection() {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Past Trips", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
        PastTripCard("Trip to Paris", "September 2023")
        PastTripCard("Trip to Rome", "May 2023")
        PastTripCard("Trip to Tokyo", "January 2023")
    }
}

@Composable
fun PastTripCard(name: String, date: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(name, style = androidx.compose.material3.MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(date, style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(onBrowseClick = {})
}
