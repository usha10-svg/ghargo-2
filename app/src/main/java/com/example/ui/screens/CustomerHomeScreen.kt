package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WorkerEntity
import com.example.ui.components.WorkerCard
import com.example.ui.components.getTradeIcon
import com.example.ui.theme.CooperativeNavy
import com.example.ui.theme.GharGoBlue
import com.example.ui.theme.GharGoOrange
import com.example.ui.theme.SaffronTrust
import com.example.ui.theme.SaffronTrustLight
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WelfareGreen
import com.example.ui.components.LocationSelectorDialog
import com.example.ui.theme.WelfareGreenLight

fun getWorkerDistanceKm(workerId: Long): Double {
    return when (workerId) {
        1L -> 0.8 // Ramesh Sharma, Saket
        7L -> 0.9 // Amit Verma, Saket
        2L -> 1.1 // Kavita Devi, Mayur Vihar
        8L -> 1.2 // Sunita Mehra, Lajpat Nagar
        3L -> 1.4 // Mohd. Arif, Old Delhi
        11L -> 1.5 // Ram Prasad, Hauz Khas
        9L -> 1.6 // Suman Lata, Vasant Kunj
        4L -> 1.9 // Suresh Mistri, Janakpuri
        10L -> 2.2 // Rajesh Rawat, Karol Bagh
        5L -> 2.3 // Vikram Negi, Dwarka
        6L -> 2.7 // Manoj Yadav, Rohini
        12L -> 3.1 // Vikram Malhotra, Noida Sec 62
        else -> ((workerId * 3 % 20) + 8) / 10.0
    }
}

data class ServiceCategoryItem(
    val key: String,
    val label: String,
    val startingPrice: String,
    val accentColor: Color
)

/**
 * Modern, clean, and attractive Customer Home & Services Screen.
 * Uncluttered and simple, with clear categories, search, and artisan cards.
 */
@Composable
fun CustomerHomeScreen(
    workers: List<WorkerEntity>,
    allWorkers: List<WorkerEntity> = workers,
    selectedTrade: String,
    searchQuery: String,
    onSelectTrade: (String) -> Unit,
    onSearchChange: (String) -> Unit,
    onWorkerClick: (WorkerEntity) -> Unit,
    onBookClick: (WorkerEntity) -> Unit,
    onStartBookingFlow: (trade: String?, worker: WorkerEntity?) -> Unit = { _, worker ->
        if (worker != null) onBookClick(worker)
    },
    onNavigateToEmergency: () -> Unit = {},
    currentLocation: String = "Connaught Place, New Delhi",
    onLocationChange: (String) -> Unit = {}
) {
    var showLocationDialog by remember { mutableStateOf(false) }

    if (showLocationDialog) {
        LocationSelectorDialog(
            currentLocation = currentLocation,
            onLocationSelected = { loc -> onLocationChange(loc) },
            onDismiss = { showLocationDialog = false }
        )
    }

    val serviceCategories = listOf(
        ServiceCategoryItem("ALL", "All", "₹199", CooperativeNavy),
        ServiceCategoryItem("Electrician", "Electrician", "₹249", SaffronTrust),
        ServiceCategoryItem("Plumber", "Plumber", "₹199", Color(0xFF0284C7)),
        ServiceCategoryItem("Carpenter", "Carpenter", "₹299", Color(0xFFB45309)),
        ServiceCategoryItem("Painter", "Painter", "₹350", Color(0xFF7C3AED)),
        ServiceCategoryItem("Cleaner", "Cleaner", "₹299", Color(0xFF0D9488)),
        ServiceCategoryItem("Caregiver", "Caregiver", "₹399", Color(0xFFE11D48)),
        ServiceCategoryItem("Driver", "Driver", "₹249", Color(0xFF4F46E5)),
        ServiceCategoryItem("Gardener", "Gardener", "₹249", Color(0xFF15803D)),
        ServiceCategoryItem("Technician", "Technician", "₹299", Color(0xFF0891B2))
    )

    // Recommended top rated workers (rating >= 4.90)
    val recommendedWorkers = remember(workers) {
        val topRated = workers.filter { it.rating >= 4.85f }.sortedByDescending { it.rating }
        if (topRated.isNotEmpty()) topRated.take(5) else workers.take(4)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Location & Trust Strip
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
                    .clickable { showLocationDialog = true }
                    .testTag("customer_home_location_strip")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEFF6FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = GharGoBlue,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Deliver service to",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "▾",
                                    fontSize = 10.sp,
                                    color = GharGoBlue,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = currentLocation,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFECFDF5))
                            .border(0.5.dp, Color(0xFFA7F3D0), RoundedCornerShape(12.dp))
                            .padding(horizontal = 9.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Handshake,
                                contentDescription = null,
                                tint = Color(0xFF059669),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "0% Middleman Cut",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF059669)
                            )
                        }
                    }
                }
            }
        }

        // Quick 24/7 Emergency Prompt Callout Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F2)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECDD3)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
                    .clickable { onNavigateToEmergency() }
                    .testTag("emergency_banner_callout")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE11D48)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "24/7 Urgent Emergency Dispatch",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF9F1239)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFFE11D48))
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "15m",
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Text(
                                text = "Sparking wires, pipe bursts, lockouts • Standby artisans",
                                fontSize = 11.sp,
                                color = Color(0xFF881337)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = onNavigateToEmergency,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48)),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(
                            text = "SOS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
                    .testTag("marketplace_search_input"),
                placeholder = {
                    Text(
                        text = "Search Electrician, Plumber, Carpenter, localities...",
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = GharGoBlue,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GharGoBlue,
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )
        }

        // Category Filter Chips
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Browse by Skill & Trade",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "Fixed cooperative rate cards",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    serviceCategories.forEach { category ->
                        val isSelected = selectedTrade.equals(category.key, ignoreCase = true)
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) GharGoBlue else Color.White
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) GharGoBlue else Color(0xFFE2E8F0)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp),
                            modifier = Modifier
                                .clickable {
                                    onSelectTrade(if (isSelected && category.key != "ALL") "ALL" else category.key)
                                }
                                .testTag("category_chip_${category.key}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (category.key != "ALL") {
                                    Icon(
                                        imageVector = getTradeIcon(category.key),
                                        contentDescription = null,
                                        tint = if (isSelected) Color.White else category.accentColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Text(
                                    text = category.label,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section Title: Available Artisans
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (selectedTrade == "ALL") "Verified Local Artisans" else "$selectedTrade Specialists",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFEFF6FF))
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${workers.size}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GharGoBlue
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Nearest first",
                        fontSize = 11.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Worker List
        if (workers.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 600.dp)
                        .padding(vertical = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No artisans matching '$searchQuery'",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Try clearing filters or search for another trade",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                onSelectTrade("ALL")
                                onSearchChange("")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GharGoBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Reset Filters", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            items(workers) { worker ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 600.dp)
                ) {
                    WorkerCard(
                        worker = worker,
                        distance = "${getWorkerDistanceKm(worker.id)} km",
                        onBookClick = { onBookClick(worker) },
                        onDetailClick = { onWorkerClick(worker) }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
