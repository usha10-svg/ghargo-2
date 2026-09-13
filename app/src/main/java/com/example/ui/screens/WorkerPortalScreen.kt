package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BookingEntity
import com.example.data.BookingTrackingStatus
import com.example.data.CooperativeVoteEntity
import com.example.data.WorkerEntity
import com.example.ui.components.BookingTrackingTimeline
import com.example.ui.components.StatusBadgeChip
import com.example.ui.components.WorkerPhotoAvatar
import com.example.ui.theme.CooperativeNavy
import com.example.ui.theme.CooperativeNavyDark
import com.example.ui.theme.GharGoBlue
import com.example.ui.theme.SaffronTrust
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VerifiedBadgeBlue
import com.example.ui.theme.WelfareGreen
import com.example.ui.theme.WelfareGreenLight

/**
 * Neat, clean, and highly functional Worker Portal for cooperative artisans.
 * Clean artisan identity header with integrated duty switch, key metrics,
 * and clear interactive job dispatch management.
 */
@Composable
fun WorkerPortalScreen(
    worker: WorkerEntity,
    bookings: List<BookingEntity>,
    votes: List<CooperativeVoteEntity>,
    activeSubTab: String = "JOB_REQUESTS",
    onSubTabChange: (String) -> Unit = {},
    onToggleAvailability: (Boolean) -> Unit,
    onAdvanceBookingStatus: (Long, String) -> Unit,
    onVerifyOtp: (BookingEntity, String) -> Boolean,
    onVote: (CooperativeVoteEntity, String) -> Unit,
    onRejectJob: ((BookingEntity) -> Unit)? = null,
    onOpenCalendar: (() -> Unit)? = null,
    onOpenAdminDashboard: (() -> Unit)? = null
) {
    // Filter bookings assigned to this worker (or demo jobs)
    val workerJobs = remember(bookings, worker.id) {
        bookings.filter { it.workerId == worker.id || it.workerId == 0L || it.workerId == 1L }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Back Header if on non-job subtabs (Wallet, Voting, ID Card)
        if (activeSubTab != "JOB_REQUESTS") {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 600.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onSubTabChange("JOB_REQUESTS") },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Jobs",
                            tint = CooperativeNavy
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = when (activeSubTab) {
                            "DIVIDEND_WALLET" -> "Cooperative Dividend Wallet"
                            "COOP_VOTING" -> "Democratic AGM Resolutions"
                            "ID_CARD" -> "Digital Worker Identity Card"
                            else -> "Worker Portal"
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CooperativeNavy
                    )
                }
            }
        }

        // 1. Artisan Identity & Duty Header Card (Shown on Home / JOB_REQUESTS)
        if (activeSubTab == "JOB_REQUESTS") {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 600.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Profile row with Duty Switch
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                WorkerPhotoAvatar(
                                    worker = worker,
                                    size = 52.dp,
                                    showVerificationBadge = true
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = worker.name,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CooperativeNavy
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(WelfareGreenLight)
                                                .padding(horizontal = 5.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                "CO-OP",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = WelfareGreen
                                            )
                                        }
                                    }
                                    Text(
                                        text = "${worker.trade} • Member #${worker.memberId}",
                                        fontSize = 11.sp,
                                        color = TextSecondary,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(top = 2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = SaffronTrust,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(
                                            text = "${worker.rating}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = " (${worker.completedJobs} jobs)",
                                            fontSize = 10.sp,
                                            color = TextMuted
                                        )
                                    }
                                }
                            }

                            // Duty Status Toggle Box
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (worker.isAvailable) Color(0xFFF0FDF4) else Color(0xFFF1F5F9))
                                    .border(
                                        1.dp,
                                        if (worker.isAvailable) Color(0xFFBBF7D0) else Color(0xFFE2E8F0),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(7.dp)
                                                .clip(CircleShape)
                                                .background(if (worker.isAvailable) WelfareGreen else Color.Gray)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (worker.isAvailable) "ONLINE" else "OFF DUTY",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (worker.isAvailable) WelfareGreen else TextSecondary
                                        )
                                    }
                                    Switch(
                                        checked = worker.isAvailable,
                                        onCheckedChange = { onToggleAvailability(it) },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = WelfareGreen,
                                            uncheckedThumbColor = Color.White,
                                            uncheckedTrackColor = Color.LightGray
                                        ),
                                        modifier = Modifier.size(width = 38.dp, height = 24.dp).testTag("worker_duty_switch")
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = Color(0xFFF1F5F9))

                        // Quick Navigation Shortcuts (Schedule, Digital ID, Admin)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (onOpenCalendar != null) {
                                OutlinedButton(
                                    onClick = onOpenCalendar,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f).height(34.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                                ) {
                                    Icon(Icons.Default.Event, contentDescription = null, modifier = Modifier.size(14.dp), tint = CooperativeNavy)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Schedule", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CooperativeNavy)
                                }
                            }

                            OutlinedButton(
                                onClick = { onSubTabChange("ID_CARD") },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).height(34.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                            ) {
                                Icon(Icons.Default.Badge, contentDescription = null, modifier = Modifier.size(14.dp), tint = SaffronTrust)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Worker ID", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }

                            if (onOpenAdminDashboard != null) {
                                Button(
                                    onClick = onOpenAdminDashboard,
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = CooperativeNavy),
                                    modifier = Modifier.weight(1f).height(34.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                                ) {
                                    Icon(Icons.Default.Dashboard, contentDescription = null, modifier = Modifier.size(13.dp), tint = Color.White)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Admin Hub", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }

            // 2. Key Performance Metrics Row (3 Clean Cards)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 600.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Metric 1: Today's Earnings
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onSubTabChange("DIVIDEND_WALLET") }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Direct Earnings", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                "₹${worker.completedJobs * 380 + 1280}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = WelfareGreen
                            )
                            Text("97% to Jan Dhan", fontSize = 9.sp, color = WelfareGreen, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    // Metric 2: Active Orders
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Assigned Tasks", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                "${workerJobs.size} Active",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CooperativeNavy
                            )
                            Text("Delhi NCR Queue", fontSize = 9.sp, color = TextMuted)
                        }
                    }

                    // Metric 3: Welfare & Dividend
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onSubTabChange("DIVIDEND_WALLET") }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Co-op Dividend", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                "₹${worker.dividendEarned}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SaffronTrust
                            )
                            Text("${worker.sharesOwned} Shares", fontSize = 9.sp, color = SaffronTrust, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // 3. Section Title: Active Dispatches
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
                            text = "Live Dispatches & Jobs",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFEFF6FF))
                                .padding(horizontal = 6.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "${workerJobs.size}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = GharGoBlue
                            )
                        }
                    }

                    Text(
                        text = "100% Transparency",
                        fontSize = 10.sp,
                        color = WelfareGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 4. Job List / Empty State
            if (workerJobs.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 600.dp)
                            .padding(vertical = 12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Work,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Standby: All Caught Up!",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "You are currently Online and will receive alerts when new requests are booked in Saket & South Delhi.",
                                fontSize = 11.sp,
                                color = TextMuted,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(workerJobs) { job ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 600.dp)
                    ) {
                        CleanWorkerJobCard(
                            job = job,
                            onAdvanceStatus = { newStatus -> onAdvanceBookingStatus(job.id, newStatus) },
                            onVerifyOtp = { otp -> onVerifyOtp(job, otp) },
                            onRejectJob = onRejectJob?.let { cb -> { cb(job) } }
                        )
                    }
                }
            }
        }

        // Subtab: DIVIDEND_WALLET
        if (activeSubTab == "DIVIDEND_WALLET") {
            item {
                Box(modifier = Modifier.fillMaxWidth().widthIn(max = 600.dp)) {
                    WorkerWalletSection(worker = worker)
                }
            }
        }

        // Subtab: COOP_VOTING
        if (activeSubTab == "COOP_VOTING") {
            item {
                Column(modifier = Modifier.fillMaxWidth().widthIn(max = 600.dp)) {
                    Text(
                        text = "Cooperative Democratic Governance",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = CooperativeNavy
                    )
                    Text(
                        text = "1 Worker = 1 Vote • Democratic control of wage floors and welfare funds",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            items(votes) { vote ->
                Box(modifier = Modifier.fillMaxWidth().widthIn(max = 600.dp)) {
                    VotingCard(vote = vote, onVote = { choice -> onVote(vote, choice) })
                }
            }
        }

        // Subtab: ID_CARD
        if (activeSubTab == "ID_CARD") {
            item {
                Box(modifier = Modifier.fillMaxWidth().widthIn(max = 600.dp)) {
                    WorkerDigitalIdSection(worker = worker)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Neat, clean, and interactive Job Card for the worker portal.
 * Features OTP verification, travel start, completion, and earnings split.
 */
@Composable
private fun CleanWorkerJobCard(
    job: BookingEntity,
    onAdvanceStatus: (String) -> Unit,
    onVerifyOtp: (String) -> Boolean,
    onRejectJob: (() -> Unit)? = null
) {
    var enteredOtp by remember { mutableStateOf("") }
    var otpError by remember { mutableStateOf(false) }

    val currentStatus = BookingTrackingStatus.from(job.status)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Top Row: Title, Scheduled Time & Status Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = job.serviceTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "📅 ${job.scheduledDate}, ${job.scheduledTime}",
                        fontSize = 11.sp,
                        color = GharGoBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                StatusBadgeChip(status = currentStatus)
            }

            // Customer Details Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Customer: ${job.customerName}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = job.customerAddress,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                // Call Phone Chip
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEFF6FF))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call",
                        tint = GharGoBlue,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Call",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GharGoBlue
                    )
                }
            }

            // Transparent Earnings Split Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF0FDF4))
                    .border(0.5.dp, Color(0xFFBBF7D0), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Your Net Earnings (97%): ", fontSize = 11.sp, color = Color(0xFF166534))
                    Text(text = "₹${job.workerShare}", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = WelfareGreen)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Co-op Fund (3%): ", fontSize = 10.sp, color = TextMuted)
                    Text(text = "₹${job.welfareContribution}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CooperativeNavy)
                }
            }

            // Interactive Workflow Action based on Booking Status
            when (currentStatus) {
                BookingTrackingStatus.CONFIRMED -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                if (onRejectJob != null) {
                                    onRejectJob()
                                } else {
                                    onAdvanceStatus("CANCELLED")
                                }
                            },
                            modifier = Modifier.weight(1f).height(38.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Decline", color = Color.Red, fontSize = 11.sp)
                        }
                        Button(
                            onClick = { onAdvanceStatus(BookingTrackingStatus.WORKER_ASSIGNED.code) },
                            modifier = Modifier.weight(1.5f).height(38.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = WelfareGreen)
                        ) {
                            Text("Accept Dispatch", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                BookingTrackingStatus.WORKER_ASSIGNED -> {
                    Button(
                        onClick = { onAdvanceStatus(BookingTrackingStatus.WORKER_ARRIVING.code) },
                        modifier = Modifier.fillMaxWidth().height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CooperativeNavy)
                    ) {
                        Icon(Icons.Default.NearMe, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Start Journey to Customer", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                BookingTrackingStatus.WORKER_ARRIVING -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFFFFBEB))
                            .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(10.dp))
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Arrived! Enter Customer OTP:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = Color(0xFF92400E)
                            )
                            Text(
                                text = "Demo Code: ${job.otpCode}",
                                fontSize = 10.sp,
                                color = SaffronTrust,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = enteredOtp,
                                onValueChange = {
                                    if (it.length <= 4) {
                                        enteredOtp = it
                                        otpError = false
                                    }
                                },
                                placeholder = { Text("${job.otpCode}") },
                                singleLine = true,
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(8.dp)
                            )
                            Button(
                                onClick = {
                                    val success = onVerifyOtp(enteredOtp)
                                    if (!success) {
                                        otpError = true
                                    }
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SaffronTrust)
                            ) {
                                Text("Verify & Start", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        if (otpError) {
                            Text(
                                text = "Incorrect OTP code. Ask the customer for their 4-digit security PIN.",
                                color = Color.Red,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
                BookingTrackingStatus.SERVICE_STARTED -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF0FDF4))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = WelfareGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Work in progress on site",
                                color = Color(0xFF166534),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Button(
                            onClick = { onAdvanceStatus(BookingTrackingStatus.COMPLETED.code) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(34.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = WelfareGreen)
                        ) {
                            Text("Finish & Collect", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                BookingTrackingStatus.COMPLETED -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF0FDF4))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✓ Job Completed • Paid via Direct UPI",
                            color = WelfareGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "₹${job.workerShare} in Jan Dhan",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF166534)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Worker Cooperative Dividend Wallet Section
 */
@Composable
fun WorkerWalletSection(worker: WorkerEntity) {
    var showWithdrawSuccess by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Main Cooperative Balance Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CooperativeNavyDark),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Cooperative Earnings & Dividend",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = "₹${worker.completedJobs * 380 + worker.dividendEarned}",
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(SaffronTrust),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Divider(color = Color.White.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Direct Job Pay (97%)", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                        Text(text = "₹${worker.completedJobs * 380}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Column {
                        Text(text = "Lābhānsh (Dividend)", color = SaffronTrust, fontSize = 10.sp)
                        Text(text = "₹${worker.dividendEarned}", color = SaffronTrust, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Equity Shares", color = WelfareGreen, fontSize = 10.sp)
                        Text(text = "${worker.sharesOwned} Shares", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }

        // Social Security & Welfare Fund status
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Worker Cooperative Welfare Protection",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = CooperativeNavy
                )

                // Item 1: Health
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = WelfareGreen, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Ayushman PM-JAY & ESIC Medical Cover", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Text(text = "₹5,00,000 family cashless hospitalization (Active)", fontSize = 10.sp, color = WelfareGreen)
                    }
                }

                // Item 2: Tool insurance
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = SaffronTrust, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Cooperative Tool Kit Protection", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Text(text = "Up to ₹15,000 zero-deductible tool theft & drop cover", fontSize = 10.sp, color = TextSecondary)
                    }
                }

                // Item 3: Pension
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccountBalance, contentDescription = null, tint = CooperativeNavy, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "PMSYM Old-Age Pension Auto-Matching", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Text(text = "Cooperative matches ₹100/mo pension deposit directly", fontSize = 10.sp, color = TextSecondary)
                    }
                }
            }
        }

        // Instant Payout Button
        Button(
            onClick = { showWithdrawSuccess = true },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = WelfareGreen),
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .testTag("withdraw_to_jandhan_button")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Payments, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Instant Payout to Jan Dhan Bank Account", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        AnimatedVisibility(visible = showWithdrawSuccess) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF0FDF4))
                    .border(1.dp, Color(0xFFBBF7D0), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "Success! Transfer initiated via NPCI Jan Dhan UPI to Bank A/C ending in 4112.",
                    color = Color(0xFF166534),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Democratic AGM Voting Card
 */
@Composable
fun VotingCard(
    vote: CooperativeVoteEntity,
    onVote: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(CooperativeNavy.copy(alpha = 0.1f))
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = vote.resolutionNumber,
                        color = CooperativeNavy,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
                Text(
                    text = "Closes in ${vote.closesInDays} days",
                    fontSize = 10.sp,
                    color = SaffronTrust,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = vote.title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF0F172A)
            )
            Text(
                text = vote.description,
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 15.sp
            )

            val totalVotes = vote.yesVotes + vote.noVotes
            val yesPercent = if (totalVotes > 0) (vote.yesVotes.toFloat() / totalVotes * 100).toInt() else 0

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Yes: ${vote.yesVotes} ($yesPercent%)", fontSize = 10.sp, color = WelfareGreen, fontWeight = FontWeight.Bold)
                Text(text = "No: ${vote.noVotes}", fontSize = 10.sp, color = Color.Red, fontWeight = FontWeight.Bold)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFFF1F5F9))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(yesPercent / 100f)
                        .height(6.dp)
                        .background(WelfareGreen)
                )
            }

            if (vote.userVoted != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEFF6FF))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "You voted: ${vote.userVoted} • Recorded on Cooperative Ledger",
                        color = Color(0xFF1E40AF),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onVote("NO") },
                        modifier = Modifier.weight(1f).height(36.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Vote NO", color = Color.Red, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { onVote("YES") },
                        modifier = Modifier.weight(1f).height(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = WelfareGreen)
                    ) {
                        Text("Vote YES", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/**
 * Worker Digital Identity Card Section
 */
@Composable
fun WorkerDigitalIdSection(worker: WorkerEntity) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "SHRAMIK SAHAKARI SAMITI",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        color = CooperativeNavy
                    )
                    Text(
                        text = "Delhi NCR Skilled Guild Chapter #104",
                        fontSize = 9.sp,
                        color = SaffronTrust,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(WelfareGreenLight)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "ACTIVE MEMBER",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = WelfareGreen
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFF1F5F9))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                WorkerPhotoAvatar(
                    worker = worker,
                    size = 64.dp,
                    showVerificationBadge = true
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = worker.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "${worker.trade} (NSDC Certified)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GharGoBlue
                    )
                    Text(
                        text = "Member ID: ${worker.memberId}",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Text(
                        text = if (worker.verifiedEShram) "e-Shram UAN: 1288-4902-9412" else "e-Shram: Unlinked",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }
            }

            // QR Code Placeholder Card
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = "Verification QR Code",
                    tint = CooperativeNavy,
                    modifier = Modifier.size(80.dp)
                )
            }

            Text(
                text = "Scan with Any Citizen or Police App for Instant Govt & Guild Verification",
                fontSize = 10.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}
