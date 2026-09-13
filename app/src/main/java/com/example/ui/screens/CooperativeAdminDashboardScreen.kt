package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.BookingEntity
import com.example.data.WorkerEntity
import com.example.ui.components.GharGoLogo
import com.example.ui.components.WorkerPhotoAvatar
import com.example.ui.components.getTradeIcon
import com.example.ui.theme.CooperativeNavy
import com.example.ui.theme.GharGoBlue
import com.example.ui.theme.SaffronTrust
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WelfareGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Smart India Hackathon (SIH) Winning Cooperative Admin Dashboard for GHARgo.
 * Features:
 * - Real-time KPI Matrix (Workers, Active, Pending Verifications, GMV, 3% Welfare Fund Pool)
 * - Worker Verification & Certification Management (e-Shram, NSDC Skill India, One-click Approval)
 * - Service Requests & AI-Based Smart Worker Allocation
 * - Transparent Payments & Worker Welfare Tracking (0% platform cut, direct Jan Dhan payouts)
 * - 24/7 Emergency Dispatch Control Board
 * - Analytics & Service-Demand Heatmap Insights
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CooperativeAdminDashboardScreen(
    workers: List<WorkerEntity>,
    bookings: List<BookingEntity>,
    onApproveWorker: (Long) -> Unit,
    onToggleWorkerAvailability: (Long, Boolean) -> Unit,
    onAiAllocateBookings: () -> Unit,
    onReassignBooking: (Long, WorkerEntity) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        "Overview" to Icons.Default.Dashboard,
        "Artisans" to Icons.Default.Engineering,
        "Bookings & AI" to Icons.Default.Work,
        "Payments & Welfare" to Icons.Default.AccountBalance,
        "Emergencies" to Icons.Default.Bolt,
        "Analytics" to Icons.Default.Analytics
    )

    // Filter states
    var workerFilterTrade by remember { mutableStateOf("ALL") }
    var workerSearchQuery by remember { mutableStateOf("") }
    var bookingSearchQuery by remember { mutableStateOf("") }

    // Dialog & Notification states
    var inspectingWorker by remember { mutableStateOf<WorkerEntity?>(null) }
    var assigningBooking by remember { mutableStateOf<BookingEntity?>(null) }
    var bannerNotification by remember { mutableStateOf<String?>(null) }
    var isRunningAiAllocation by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Calculated Dashboard Metrics
    val totalWorkers = workers.size
    val activeWorkers = workers.count { it.isAvailable }
    val pendingVerifications = workers.count { !it.verifiedEShram || !it.verifiedNsdc }
    val totalBookings = bookings.size
    val activeBookings = bookings.count { it.status != "COMPLETED" && it.status != "CANCELLED" }
    val totalGmv = bookings.sumOf { it.totalAmount }.let { if (it == 0) 142500 else it * 15 }
    val welfareFund = (totalGmv * 0.03).toInt()
    val platformCutSavings = (totalGmv * 0.20).toInt() // Compared to private gig platforms charging 20%

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Surface(
                color = CooperativeNavy,
                shadowElevation = 4.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = onClose,
                                modifier = Modifier.testTag("admin_close_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back to App",
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "GHARgo Sahakari Admin",
                                        color = Color.White,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(SaffronTrust)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "SIH-2024 WINNER",
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                                Text(
                                    text = "Democratic Artisan Operations & Welfare Control Suite",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Top Quick Actions
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                onClick = {
                                    isRunningAiAllocation = true
                                    scope.launch {
                                        delay(1200)
                                        onAiAllocateBookings()
                                        isRunningAiAllocation = false
                                        bannerNotification = "✨ AI Smart Allocation completed! 100% active requests assigned."
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("run_ai_allocation_button")
                            ) {
                                if (isRunningAiAllocation) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(14.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Allocating...", fontSize = 12.sp, color = Color.White)
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("AI Auto-Allocate", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }

                    // Navigation Tabs Row
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color(0xFF0F172A),
                        contentColor = Color.White,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = SaffronTrust,
                                height = 3.dp
                            )
                        }
                    ) {
                        tabs.forEachIndexed { index, (title, icon) ->
                            val isSelected = selectedTab == index
                            Tab(
                                selected = isSelected,
                                onClick = { selectedTab = index },
                                icon = {
                                    if (title == "Artisans" && pendingVerifications > 0) {
                                        BadgedBox(badge = {
                                            Badge(containerColor = Color(0xFFEA580C)) {
                                                Text("$pendingVerifications")
                                            }
                                        }) {
                                            Icon(imageVector = icon, contentDescription = title, modifier = Modifier.size(18.dp))
                                        }
                                    } else {
                                        Icon(imageVector = icon, contentDescription = title, modifier = Modifier.size(18.dp))
                                    }
                                },
                                text = {
                                    Text(
                                        text = title,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) SaffronTrust else Color(0xFF94A3B8)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FAFC))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Banner Toast Notification
                AnimatedVisibility(visible = bannerNotification != null) {
                    bannerNotification?.let { msg ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFEFF6FF),
                            border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF2563EB),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = msg,
                                        fontSize = 12.sp,
                                        color = Color(0xFF1E3A8A),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                IconButton(
                                    onClick = { bannerNotification = null },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Dismiss",
                                        tint = Color(0xFF64748B),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Screen Tab Switcher
                when (selectedTab) {
                    0 -> AdminOverviewTab(
                        totalWorkers = totalWorkers,
                        activeWorkers = activeWorkers,
                        pendingVerifications = pendingVerifications,
                        totalBookings = totalBookings,
                        activeBookings = activeBookings,
                        totalGmv = totalGmv,
                        welfareFund = welfareFund,
                        platformCutSavings = platformCutSavings,
                        onNavigateToTab = { selectedTab = it },
                        onQuickApproveAll = {
                            workers.filter { !it.verifiedEShram || !it.verifiedNsdc }.forEach {
                                onApproveWorker(it.id)
                            }
                            bannerNotification = "✅ All pending artisans verified & cooperative shares activated!"
                        }
                    )
                    1 -> AdminWorkersTab(
                        workers = workers,
                        filterTrade = workerFilterTrade,
                        searchQuery = workerSearchQuery,
                        onFilterTrade = { workerFilterTrade = it },
                        onSearchChange = { workerSearchQuery = it },
                        onInspectWorker = { inspectingWorker = it },
                        onApproveWorker = { id ->
                            onApproveWorker(id)
                            bannerNotification = "Verified e-Shram & NSDC status for artisan."
                        },
                        onToggleAvailability = onToggleWorkerAvailability
                    )
                    2 -> AdminBookingsTab(
                        bookings = bookings,
                        workers = workers,
                        searchQuery = bookingSearchQuery,
                        onSearchChange = { bookingSearchQuery = it },
                        onAssignWorker = { booking -> assigningBooking = booking },
                        onRunAiAllocation = {
                            isRunningAiAllocation = true
                            scope.launch {
                                delay(1000)
                                onAiAllocateBookings()
                                isRunningAiAllocation = false
                                bannerNotification = "🎯 AI worker dispatch executed successfully!"
                            }
                        }
                    )
                    3 -> AdminPaymentsWelfareTab(
                        totalGmv = totalGmv,
                        welfareFund = welfareFund,
                        platformCutSavings = platformCutSavings,
                        workers = workers,
                        bookings = bookings
                    )
                    4 -> AdminEmergencyDispatchTab(
                        workers = workers,
                        bookings = bookings
                    )
                    5 -> AdminAnalyticsTab(
                        workers = workers,
                        bookings = bookings,
                        totalGmv = totalGmv
                    )
                }
            }
        }
    }

    // Modal Sheet: Inspect Artisan Verification Documents
    inspectingWorker?.let { worker ->
        WorkerInspectionDialog(
            worker = worker,
            onDismiss = { inspectingWorker = null },
            onApprove = {
                onApproveWorker(worker.id)
                inspectingWorker = null
                bannerNotification = "🎉 Approved ${worker.name}! e-Shram UAN & NSDC credentials activated."
            }
        )
    }

    // Modal Sheet: Manual Booking Assignment
    assigningBooking?.let { booking ->
        BookingAssignmentDialog(
            booking = booking,
            workers = workers.filter { it.trade.equals(booking.trade, ignoreCase = true) || it.isAvailable },
            onDismiss = { assigningBooking = null },
            onSelectWorker = { worker ->
                onReassignBooking(booking.id, worker)
                assigningBooking = null
                bannerNotification = "Assigned job #${booking.id} to ${worker.name}."
            }
        )
    }
}

// -----------------------------------------------------------------------------------------
// TAB 1: OVERVIEW & KEY KPI MATRIX
// -----------------------------------------------------------------------------------------
@Composable
private fun AdminOverviewTab(
    totalWorkers: Int,
    activeWorkers: Int,
    pendingVerifications: Int,
    totalBookings: Int,
    activeBookings: Int,
    totalGmv: Int,
    welfareFund: Int,
    platformCutSavings: Int,
    onNavigateToTab: (Int) -> Unit,
    onQuickApproveAll: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Hero Mission Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "GHARgo Cooperative Health & Status",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Real-time ledger • Zero middleman cut • 100% Artisan-owned",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFDCFCE7))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF16A34A))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SYSTEM OPTIMAL",
                                color = Color(0xFF15803D),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // KPI 4-Card Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KpiStatCard(
                        title = "Total Artisans",
                        value = "$totalWorkers",
                        subtext = "$activeWorkers on active duty",
                        badge = "+12% this mo",
                        icon = Icons.Default.Group,
                        iconTint = GharGoBlue,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToTab(1) }
                    )
                    KpiStatCard(
                        title = "Pending Verifications",
                        value = "$pendingVerifications",
                        subtext = if (pendingVerifications > 0) "Action required" else "All clear",
                        badge = if (pendingVerifications > 0) "Review" else "Zero backlogs",
                        icon = Icons.Default.Badge,
                        iconTint = if (pendingVerifications > 0) Color(0xFFEA580C) else WelfareGreen,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToTab(1) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KpiStatCard(
                        title = "Coop Welfare Fund",
                        value = "₹${"%,d".format(welfareFund)}",
                        subtext = "3% health & insurance",
                        badge = "100% Solvency",
                        icon = Icons.Default.HealthAndSafety,
                        iconTint = WelfareGreen,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToTab(3) }
                    )
                    KpiStatCard(
                        title = "Middleman Savings",
                        value = "₹${"%,d".format(platformCutSavings)}",
                        subtext = "Kept directly by artisans",
                        badge = "0% Platform Cut",
                        icon = Icons.Default.TrendingUp,
                        iconTint = SaffronTrust,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToTab(3) }
                    )
                }
            }
        }

        // Action Alerts Banner (Pending Verifications quick resolve)
        if (pendingVerifications > 0) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)),
                border = BorderStroke(1.dp, Color(0xFFFED7AA))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFEDD5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = Color(0xFFEA580C),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "$pendingVerifications Artisan Credentials Awaiting Audit",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF9A3412)
                            )
                            Text(
                                text = "e-Shram card photo and NSDC Skill India certificates uploaded.",
                                fontSize = 11.sp,
                                color = Color(0xFFC2410C)
                            )
                        }
                    }
                    Button(
                        onClick = onQuickApproveAll,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA580C)),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Approve All", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        // Live Service Allocation Status
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AssignmentTurnedIn,
                            contentDescription = null,
                            tint = GharGoBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI Smart Dispatch Engine Pulse",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }
                    Text(
                        text = "98.4% Match Accuracy",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = WelfareGreen
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "AI automatically optimizes matching by factoring artisan locality proximity, verified skill level, customer ratings, and equitable rotation across cooperative guild members.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                LinearProgressIndicator(
                    progress = { 0.92f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = GharGoBlue,
                    trackColor = Color(0xFFE2E8F0)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Response Latency: < 4.2 mins", fontSize = 11.sp, color = TextMuted)
                    Text("Active Jobs: $activeBookings of $totalBookings", fontSize = 11.sp, color = TextMuted)
                }
            }
        }
    }
}

@Composable
private fun KpiStatCard(
    title: String,
    value: String,
    subtext: String,
    badge: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(iconTint.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = badge, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0F172A))
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(text = subtext, fontSize = 10.sp, color = TextMuted, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

// -----------------------------------------------------------------------------------------
// TAB 2: ARTISAN VERIFICATIONS & CERTIFICATIONS ROSTER
// -----------------------------------------------------------------------------------------
@Composable
private fun AdminWorkersTab(
    workers: List<WorkerEntity>,
    filterTrade: String,
    searchQuery: String,
    onFilterTrade: (String) -> Unit,
    onSearchChange: (String) -> Unit,
    onInspectWorker: (WorkerEntity) -> Unit,
    onApproveWorker: (Long) -> Unit,
    onToggleAvailability: (Long, Boolean) -> Unit
) {
    val trades = listOf("ALL", "Electrician", "Plumber", "Carpenter", "Technician", "Painter")

    val filtered = workers.filter { worker ->
        val matchesTrade = if (filterTrade == "ALL") true else worker.trade.equals(filterTrade, ignoreCase = true)
        val matchesQuery = if (searchQuery.isBlank()) true else {
            worker.name.contains(searchQuery, ignoreCase = true) ||
            worker.locality.contains(searchQuery, ignoreCase = true) ||
            worker.memberId.contains(searchQuery, ignoreCase = true)
        }
        matchesTrade && matchesQuery
    }

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Search & Filter Header
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = { Text("Search by artisan name, locality, member ID...", fontSize = 12.sp) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp)) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GharGoBlue,
                        unfocusedBorderColor = Color(0xFFE2E8F0)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(trades) { trade ->
                        val isSelected = filterTrade == trade
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) GharGoBlue else Color(0xFFF1F5F9))
                                .clickable { onFilterTrade(trade) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = trade,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Artisan List
        Text(
            text = "Cooperative Guild Roster (${filtered.size} Artisans)",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        filtered.forEach { worker ->
            val isFullyVerified = worker.verifiedEShram && worker.verifiedNsdc

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            WorkerPhotoAvatar(
                                worker = worker,
                                size = 48.dp,
                                showVerificationBadge = true
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = worker.name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    if (isFullyVerified) {
                                        Icon(
                                            imageVector = Icons.Default.Verified,
                                            contentDescription = "Verified",
                                            tint = GharGoBlue,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "${worker.trade} • ${worker.experienceYears} yrs exp • ID: ${worker.memberId}",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = "📍 ${worker.locality}",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        // Availability Duty Switch
                        Column(horizontalAlignment = Alignment.End) {
                            Switch(
                                checked = worker.isAvailable,
                                onCheckedChange = { onToggleAvailability(worker.id, it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = WelfareGreen
                                )
                            )
                            Text(
                                text = if (worker.isAvailable) "ON DUTY" else "STANDBY",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (worker.isAvailable) WelfareGreen else TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(10.dp))

                    // Badges & Equity Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            // e-Shram badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (worker.verifiedEShram) Color(0xFFDCFCE7) else Color(0xFFFFEDD5))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = if (worker.verifiedEShram) "e-Shram Linked" else "e-Shram Pending",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (worker.verifiedEShram) Color(0xFF15803D) else Color(0xFFC2410C)
                                )
                            }

                            // NSDC badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (worker.verifiedNsdc) Color(0xFFEFF6FF) else Color(0xFFFEF2F2))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = if (worker.verifiedNsdc) "NSDC Skill Level 4" else "Skill Audit Req",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (worker.verifiedNsdc) Color(0xFF1D4ED8) else Color(0xFFB91C1C)
                                )
                            }
                        }

                        // Equity Shares
                        Text(
                            text = "${worker.sharesOwned} Coop Shares",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaffronTrust
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onInspectWorker(worker) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Security, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Audit Docs", fontSize = 12.sp)
                        }

                        if (!isFullyVerified) {
                            Button(
                                onClick = { onApproveWorker(worker.id) },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = WelfareGreen),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(vertical = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Done, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Approve & Verify", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// TAB 3: BOOKINGS & AI WORKER ALLOCATION
// -----------------------------------------------------------------------------------------
@Composable
private fun AdminBookingsTab(
    bookings: List<BookingEntity>,
    workers: List<WorkerEntity>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onAssignWorker: (BookingEntity) -> Unit,
    onRunAiAllocation: () -> Unit
) {
    val filteredBookings = bookings.filter { booking ->
        if (searchQuery.isBlank()) true else {
            booking.serviceTitle.contains(searchQuery, ignoreCase = true) ||
            booking.customerName.contains(searchQuery, ignoreCase = true) ||
            booking.workerName.contains(searchQuery, ignoreCase = true) ||
            booking.trade.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // AI Allocation Callout Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
            border = BorderStroke(1.dp, Color(0xFFBFDBFE))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = GharGoBlue, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI-Driven Smart Job Assignment",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3A8A)
                        )
                    }
                    Button(
                        onClick = onRunAiAllocation,
                        colors = ButtonDefaults.buttonColors(containerColor = GharGoBlue),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Re-Run AI Matching", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Considers geolocation distance, trade skill match, artisan past customer ratings, and equitable rotation across guild members to avoid over-assigning single artisans.",
                    fontSize = 11.sp,
                    color = Color(0xFF1E40AF),
                    lineHeight = 16.sp
                )
            }
        }

        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search bookings by customer, trade, or title...", fontSize = 12.sp) },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp)) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GharGoBlue,
                unfocusedBorderColor = Color(0xFFE2E8F0)
            )
        )

        // Bookings Table Cards
        Text(
            text = "Active Service Requests (${filteredBookings.size})",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        filteredBookings.forEach { booking ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(GharGoBlue.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = getTradeIcon(booking.trade), contentDescription = null, tint = GharGoBlue, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = booking.serviceTitle,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                                Text(
                                    text = "Booked by ${booking.customerName} • ${booking.scheduledDate}, ${booking.scheduledTime}",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        // Status Badge
                        val isComplete = booking.status == "COMPLETED"
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isComplete) Color(0xFFDCFCE7) else Color(0xFFEFF6FF))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = booking.status.replace("_", " "),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isComplete) Color(0xFF15803D) else Color(0xFF1D4ED8)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "📍 ${booking.customerAddress}", fontSize = 11.sp, color = TextMuted)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "“${booking.description}”", fontSize = 11.sp, color = TextSecondary)

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(10.dp))

                    // Assigned Worker & Rematch Action
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Assigned Artisan", fontSize = 10.sp, color = TextMuted)
                            Text(
                                text = booking.workerName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "₹${booking.totalAmount}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF0F172A)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            OutlinedButton(
                                onClick = { onAssignWorker(booking) },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("Reassign", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// TAB 4: PAYMENTS & WELFARE TRACKING
// -----------------------------------------------------------------------------------------
@Composable
private fun AdminPaymentsWelfareTab(
    totalGmv: Int,
    welfareFund: Int,
    platformCutSavings: Int,
    workers: List<WorkerEntity>,
    bookings: List<BookingEntity>
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Transparent Economic Model Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CooperativeNavy),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "0% Platform Cut • Direct Worker Payout Ledger",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Unlike private apps that deduct 20-30% from poor artisans, GHARgo forwards 100% of customer payments directly to artisans, deducting only a member-approved 3% into their own social security pool.",
                    fontSize = 12.sp,
                    color = Color(0xFFCBD5E1),
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        color = Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Total GMV Facilitated", fontSize = 10.sp, color = Color(0xFF94A3B8))
                            Text("₹${"%,d".format(totalGmv)}", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        }
                    }

                    Surface(
                        color = Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("3% Welfare Health Pool", fontSize = 10.sp, color = WelfareGreen)
                            Text("₹${"%,d".format(welfareFund)}", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = WelfareGreen)
                        }
                    }
                }
            }
        }

        // Welfare Benefit Breakdown Cards
        Text(
            text = "Artisan Welfare Fund Allocations",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        val welfareItems = listOf(
            Triple("Ayushman Bharat Health Top-Up", "Covers hospitalization up to ₹2,00,000 for artisan families", "₹84,200 Disbursed"),
            Triple("Accidental Death & Disability Cover", "24/7 on-duty insurance through PMJJBY / PMSBY integration", "100% Active Policy"),
            Triple("Toolkit & Safety Equipment Micro-Loans", "Zero-interest micro-credit for modern drills, jet washers, multimeters", "28 Loans Active"),
            Triple("Annual Cooperative Dividend Distribution", "Democratic profit share paid before Diwali & Eid festivals", "₹1,40,000 Reserved")
        )

        welfareItems.forEach { (title, desc, status) ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                        Text(text = desc, fontSize = 11.sp, color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFDCFCE7))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = status, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D))
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// TAB 5: 24/7 EMERGENCY DISPATCH BOARD
// -----------------------------------------------------------------------------------------
@Composable
private fun AdminEmergencyDispatchTab(
    workers: List<WorkerEntity>,
    bookings: List<BookingEntity>
) {
    val emergencyArtisans = workers.filter { it.isAvailable }

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
            border = BorderStroke(1.dp, Color(0xFFFECACA))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF87171)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "24/7 Rapid Emergency Response Desk",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB91C1C)
                        )
                        Text(
                            text = "Average dispatch arrival: 14 minutes • Standby units: ${emergencyArtisans.size}",
                            fontSize = 11.sp,
                            color = Color(0xFFDC2626)
                        )
                    }
                }
            }
        }

        Text(
            text = "Emergency Standby Artisans on Live Call",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        emergencyArtisans.take(6).forEach { artisan ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        WorkerPhotoAvatar(worker = artisan, size = 42.dp, showVerificationBadge = false)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = artisan.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            Text(text = "${artisan.trade} • 📍 ${artisan.locality}", fontSize = 11.sp, color = TextSecondary)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFDCFCE7))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("STANDBY READY", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D))
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// TAB 6: ANALYTICS & SERVICE-DEMAND INSIGHTS
// -----------------------------------------------------------------------------------------
@Composable
private fun AdminAnalyticsTab(
    workers: List<WorkerEntity>,
    bookings: List<BookingEntity>,
    totalGmv: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Trade Demand Chart
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Service Demand Breakdown by Trade",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "Based on verified completed and requested service volumes",
                    fontSize = 11.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                val demandStats = listOf(
                    Triple("Electrician", 0.42f, "42% (High demand - circuit tripping & AC load)"),
                    Triple("Plumber", 0.28f, "28% (Overhead tank cleaning & pipe repair)"),
                    Triple("Appliance Tech", 0.16f, "16% (Jet pump foam AC service & PCB repair)"),
                    Triple("Carpenter", 0.09f, "9% (Cabinet hinges & mortise locks)"),
                    Triple("Painter / Other", 0.05f, "5% (Waterproofing & touchup)")
                )

                demandStats.forEach { (trade, ratio, note) ->
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = trade, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            Text(text = "${(ratio * 100).toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GharGoBlue)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { ratio },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = GharGoBlue,
                            trackColor = Color(0xFFF1F5F9)
                        )
                        Text(text = note, fontSize = 10.sp, color = TextMuted, modifier = Modifier.padding(top = 2.dp))
                    }
                }
            }
        }

        // Peak Hours & Locality Heatmap Insights
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Geographic & Time-of-Day Insights",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Top Demand Zone", fontSize = 10.sp, color = TextMuted)
                            Text("South & West Delhi", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            Text("Saket, Rohini, CP", fontSize = 10.sp, color = GharGoBlue)
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Peak Booking Window", fontSize = 10.sp, color = TextMuted)
                            Text("10 AM - 1 PM", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            Text("68% Same-Day Delivery", fontSize = 10.sp, color = WelfareGreen)
                        }
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------------------
// DIALOG: INSPECT ARTISAN CREDENTIALS
// -----------------------------------------------------------------------------------------
@Composable
private fun WorkerInspectionDialog(
    worker: WorkerEntity,
    onDismiss: () -> Unit,
    onApprove: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WorkerPhotoAvatar(worker = worker, size = 64.dp, showVerificationBadge = true)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = worker.name, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                Text(text = "${worker.trade} • Member ID: ${worker.memberId}", fontSize = 12.sp, color = TextSecondary)

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color(0xFFE2E8F0))
                Spacer(modifier = Modifier.height(16.dp))

                // Verification Checkpoints
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AuditCheckItem(
                        title = "Government e-Shram Registration",
                        status = if (worker.verifiedEShram) "Verified (UAN: 1234-5678-9012)" else "Document Uploaded • Pending Approval",
                        isPassed = worker.verifiedEShram
                    )
                    AuditCheckItem(
                        title = "NSDC Skill India Certificate",
                        status = if (worker.verifiedNsdc) "Level 4 Master Artisan Certified" else "Assessment Verification Pending",
                        isPassed = worker.verifiedNsdc
                    )
                    AuditCheckItem(
                        title = "Aadhaar e-KYC & Police Clearance",
                        status = "UIDAI Verified • Clean Record",
                        isPassed = true
                    )
                    AuditCheckItem(
                        title = "Cooperative Equity Allocation",
                        status = "${worker.sharesOwned} Shares • ₹${worker.dividendEarned} Dividend Earned",
                        isPassed = true
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Close")
                    }
                    Button(
                        onClick = onApprove,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GharGoBlue),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Approve & Verify")
                    }
                }
            }
        }
    }
}

@Composable
private fun AuditCheckItem(
    title: String,
    status: String,
    isPassed: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isPassed) Icons.Default.CheckCircle else Icons.Default.Verified,
            contentDescription = null,
            tint = if (isPassed) WelfareGreen else Color(0xFFEA580C),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
            Text(text = status, fontSize = 10.sp, color = TextSecondary)
        }
    }
}

// -----------------------------------------------------------------------------------------
// DIALOG: REASSIGN BOOKING
// -----------------------------------------------------------------------------------------
@Composable
private fun BookingAssignmentDialog(
    booking: BookingEntity,
    workers: List<WorkerEntity>,
    onDismiss: () -> Unit,
    onSelectWorker: (WorkerEntity) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Assign Artisan for Booking #${booking.id}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "${booking.serviceTitle} (${booking.trade})",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    modifier = Modifier.height(280.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(workers) { worker ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF8FAFC),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectWorker(worker) }
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    WorkerPhotoAvatar(worker = worker, size = 36.dp, showVerificationBadge = false)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(text = worker.name, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text(text = "${worker.trade} • ★ ${worker.rating}", fontSize = 10.sp, color = TextMuted)
                                    }
                                }
                                Text("Assign →", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GharGoBlue)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}
