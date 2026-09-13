package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GharGoLogo
import com.example.ui.components.getTradeIcon
import com.example.ui.theme.CooperativeNavy
import com.example.ui.theme.GharGoBlue
import com.example.ui.theme.SaffronTrust
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Two-way Login experience for GHARgo:
 * 1. Customer Login (Simple name, phone, locality -> lands on Services Marketplace)
 * 2. Artisan / Worker Login (Name, trade selection, e-Shram / NSDC ID, experience, rate -> lands on Worker Portal)
 */
@Composable
fun LoginScreen(
    onLoginSuccess: (name: String, phone: String, role: String) -> Unit,
    onRegisterWorker: (
        name: String,
        phone: String,
        trade: String,
        eShramOrNsdc: String,
        experienceYears: Int,
        hourlyRate: Int,
        locality: String
    ) -> Unit = { _, _, _, _, _, _, _ -> },
    onContinueAsGuest: () -> Unit,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    // Active Tab: "CUSTOMER" or "WORKER"
    var selectedRole by remember { mutableStateOf("WORKER") }

    // Customer Form State
    var customerName by remember { mutableStateOf("Ananya Sen") }
    var customerPhone by remember { mutableStateOf("+91 98765 43210") }
    var customerLocality by remember { mutableStateOf("Connaught Place, New Delhi") }

    // Worker Form State
    var workerName by remember { mutableStateOf("Rameshwar Sharma") }
    var workerPhone by remember { mutableStateOf("+91 98712 34501") }
    var workerTrade by remember { mutableStateOf("Electrician") }
    var workerCertNumber by remember { mutableStateOf("1234-5678-9012") } // e-Shram or NSDC
    var workerExperience by remember { mutableStateOf("14") }
    var workerRate by remember { mutableStateOf("350") }
    var workerLocality by remember { mutableStateOf("Rohini Sector 9, Delhi") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val availableTrades = listOf(
        "Electrician", "Plumber", "Carpenter", "Technician",
        "Painter", "Cleaner", "Caregiver", "Driver"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF1F5F9),
                        Color(0xFFF8FAFC),
                        Color(0xFFFFFFFF)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Navigation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 520.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (onBack != null) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("login_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = CooperativeNavy
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(40.dp))
                }

                TextButton(
                    onClick = onContinueAsGuest,
                    modifier = Modifier.testTag("top_skip_guest_button")
                ) {
                    Text(
                        text = "Explore as Guest →",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Brand Logo Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 480.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GharGoLogo(
                        iconSize = 60.dp,
                        titleSize = 30.sp,
                        showTagline = true,
                        taglineText = "Direct Worker-Owned Home Services",
                        lightText = false
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Step 1: Role Selection Options
                    Text(
                        text = "Select How You Wish to Sign In",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Segmented Two-Role Selector
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF1F5F9))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Customer Tab
                        val isCust = selectedRole == "CUSTOMER"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isCust) GharGoBlue else Color.Transparent)
                                .clickable {
                                    selectedRole = "CUSTOMER"
                                    errorMessage = null
                                }
                                .padding(vertical = 10.dp)
                                .testTag("login_customer_tab"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = if (isCust) Color.White else TextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Customer",
                                    fontSize = 14.sp,
                                    fontWeight = if (isCust) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isCust) Color.White else TextSecondary
                                )
                            }
                        }

                        // Artisan / Worker Tab
                        val isWork = selectedRole == "WORKER"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isWork) SaffronTrust else Color.Transparent)
                                .clickable {
                                    selectedRole = "WORKER"
                                    errorMessage = null
                                }
                                .padding(vertical = 10.dp)
                                .testTag("login_worker_tab"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Engineering,
                                    contentDescription = null,
                                    tint = if (isWork) Color.White else TextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Artisan / Worker",
                                    fontSize = 14.sp,
                                    fontWeight = if (isWork) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isWork) Color.White else TextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Error Message
                    AnimatedVisibility(visible = errorMessage != null) {
                        errorMessage?.let { msg ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFFEF2F2),
                                border = BorderStroke(1.dp, Color(0xFFFECACA)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            ) {
                                Text(
                                    text = "⚠️ $msg",
                                    color = Color(0xFFDC2626),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }

                    // -------------------------------------------------------------
                    // 1. CUSTOMER LOGIN FORM
                    // -------------------------------------------------------------
                    if (selectedRole == "CUSTOMER") {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Info Banner
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFEFF6FF),
                                border = BorderStroke(1.dp, Color(0xFFDBEAFE)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = GharGoBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Direct booking with verified artisans • 0% middleman commission",
                                        fontSize = 11.sp,
                                        color = Color(0xFF1E40AF),
                                        lineHeight = 15.sp
                                    )
                                }
                            }

                            // Customer Name Field
                            OutlinedTextField(
                                value = customerName,
                                onValueChange = {
                                    customerName = it
                                    if (errorMessage != null) errorMessage = null
                                },
                                label = { Text("Your Full Name", fontSize = 13.sp) },
                                placeholder = { Text("e.g. Ananya Sen", fontSize = 13.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = GharGoBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("customer_name_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GharGoBlue,
                                    unfocusedBorderColor = Color(0xFFCBD5E1)
                                )
                            )

                            // Customer Phone Field
                            OutlinedTextField(
                                value = customerPhone,
                                onValueChange = {
                                    customerPhone = it
                                    if (errorMessage != null) errorMessage = null
                                },
                                label = { Text("Mobile Number", fontSize = 13.sp) },
                                placeholder = { Text("+91 98765 43210", fontSize = 13.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = null,
                                        tint = GharGoBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("customer_phone_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GharGoBlue,
                                    unfocusedBorderColor = Color(0xFFCBD5E1)
                                )
                            )

                            // Customer Locality Field
                            OutlinedTextField(
                                value = customerLocality,
                                onValueChange = { customerLocality = it },
                                label = { Text("Service City / Locality", fontSize = 13.sp) },
                                placeholder = { Text("e.g. Connaught Place, New Delhi", fontSize = 13.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = GharGoBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GharGoBlue,
                                    unfocusedBorderColor = Color(0xFFCBD5E1)
                                )
                            )

                            // Submit Button
                            Button(
                                onClick = {
                                    focusManager.clearFocus()
                                    if (customerName.trim().isBlank()) {
                                        errorMessage = "Please enter your name"
                                    } else {
                                        onLoginSuccess(
                                            customerName.trim(),
                                            customerPhone.trim(),
                                            "CUSTOMER"
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("customer_login_submit"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GharGoBlue)
                            ) {
                                Text(
                                    text = "Enter Customer Marketplace →",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // -------------------------------------------------------------
                    // 2. WORKER / ARTISAN LOGIN FORM
                    // -------------------------------------------------------------
                    else {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Worker Benefit Banner
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFFFF7ED),
                                border = BorderStroke(1.dp, Color(0xFFFED7AA)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = null,
                                        tint = SaffronTrust,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Sahakari Guild Member • 100% Direct Payouts + Cooperative Shares",
                                        fontSize = 11.sp,
                                        color = Color(0xFF9A3412),
                                        lineHeight = 15.sp
                                    )
                                }
                            }

                            // Artisan Name
                            OutlinedTextField(
                                value = workerName,
                                onValueChange = {
                                    workerName = it
                                    if (errorMessage != null) errorMessage = null
                                },
                                label = { Text("Artisan Name", fontSize = 13.sp) },
                                placeholder = { Text("e.g. Rameshwar Sharma", fontSize = 13.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = SaffronTrust,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("worker_name_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SaffronTrust,
                                    unfocusedBorderColor = Color(0xFFCBD5E1)
                                )
                            )

                            // Artisan Phone
                            OutlinedTextField(
                                value = workerPhone,
                                onValueChange = {
                                    workerPhone = it
                                    if (errorMessage != null) errorMessage = null
                                },
                                label = { Text("Mobile Number", fontSize = 13.sp) },
                                placeholder = { Text("+91 98712 34501", fontSize = 13.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = null,
                                        tint = SaffronTrust,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("worker_phone_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SaffronTrust,
                                    unfocusedBorderColor = Color(0xFFCBD5E1)
                                )
                            )

                            // Select Primary Trade
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Select Primary Trade",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(availableTrades) { trade ->
                                        val isSelected = workerTrade.equals(trade, ignoreCase = true)
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(if (isSelected) SaffronTrust else Color(0xFFF1F5F9))
                                                .border(
                                                    1.dp,
                                                    if (isSelected) SaffronTrust else Color(0xFFE2E8F0),
                                                    RoundedCornerShape(10.dp)
                                                )
                                                .clickable { workerTrade = trade }
                                                .padding(horizontal = 12.dp, vertical = 7.dp)
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = getTradeIcon(trade),
                                                    contentDescription = trade,
                                                    tint = if (isSelected) Color.White else TextSecondary,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(5.dp))
                                                Text(
                                                    text = trade,
                                                    fontSize = 12.sp,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                    color = if (isSelected) Color.White else TextPrimary
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // e-Shram or NSDC Certification ID
                            OutlinedTextField(
                                value = workerCertNumber,
                                onValueChange = { workerCertNumber = it },
                                label = { Text("e-Shram UAN / NSDC ID (Optional)", fontSize = 13.sp) },
                                placeholder = { Text("e.g. 1234-5678-9012 or NSDC-2024", fontSize = 13.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Badge,
                                        contentDescription = null,
                                        tint = SaffronTrust,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("worker_cert_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SaffronTrust,
                                    unfocusedBorderColor = Color(0xFFCBD5E1)
                                )
                            )

                            // Experience & Hourly Rate row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = workerExperience,
                                    onValueChange = { workerExperience = it },
                                    label = { Text("Experience (Yrs)", fontSize = 12.sp) },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Timeline,
                                            contentDescription = null,
                                            tint = SaffronTrust,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = SaffronTrust,
                                        unfocusedBorderColor = Color(0xFFCBD5E1)
                                    )
                                )

                                OutlinedTextField(
                                    value = workerRate,
                                    onValueChange = { workerRate = it },
                                    label = { Text("Rate (₹/hr)", fontSize = 12.sp) },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.CurrencyRupee,
                                            contentDescription = null,
                                            tint = SaffronTrust,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = SaffronTrust,
                                        unfocusedBorderColor = Color(0xFFCBD5E1)
                                    )
                                )
                            }

                            // Locality Field
                            OutlinedTextField(
                                value = workerLocality,
                                onValueChange = { workerLocality = it },
                                label = { Text("Operating Service Zone / City", fontSize = 13.sp) },
                                placeholder = { Text("e.g. Rohini Sector 9, Delhi", fontSize = 13.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = SaffronTrust,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SaffronTrust,
                                    unfocusedBorderColor = Color(0xFFCBD5E1)
                                )
                            )

                            // Submit Button
                            Button(
                                onClick = {
                                    focusManager.clearFocus()
                                    if (workerName.trim().isBlank()) {
                                        errorMessage = "Please enter artisan name"
                                    } else {
                                        val exp = workerExperience.toIntOrNull() ?: 5
                                        val rate = workerRate.toIntOrNull() ?: 350
                                        onRegisterWorker(
                                            workerName.trim(),
                                            workerPhone.trim(),
                                            workerTrade,
                                            workerCertNumber.trim(),
                                            exp,
                                            rate,
                                            workerLocality.trim()
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("worker_login_submit"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SaffronTrust)
                            ) {
                                Text(
                                    text = "Enter Artisan Portal →",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // One-Click Demo Shortcut
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (selectedRole == "CUSTOMER") {
                                    onLoginSuccess("Ananya Sen", "+91 98765 43210", "CUSTOMER")
                                } else {
                                    onLoginSuccess("Rameshwar Sharma", "+91 98712 34501", "WORKER")
                                }
                            }
                            .padding(10.dp)
                            .testTag("quick_demo_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = if (selectedRole == "CUSTOMER") GharGoBlue else SaffronTrust,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (selectedRole == "CUSTOMER") {
                                    "Quick Demo Sign-In as Ananya Sen (Customer)"
                                } else {
                                    "Quick Demo Sign-In as Rameshwar Sharma (Electrician)"
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (selectedRole == "CUSTOMER") GharGoBlue else SaffronTrust
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Continue as Guest Link
                    TextButton(
                        onClick = onContinueAsGuest,
                        modifier = Modifier.testTag("continue_as_guest_button")
                    ) {
                        Text(
                            text = "Skip & Browse Without Signing In",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Trust Guarantee Badges
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = Color(0xFF059669),
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Govt e-Shram & NSDC Verified • 0% Platform Commission",
                    fontSize = 11.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
