package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BookingEntity
import com.example.data.BookingTrackingStatus
import com.example.data.WorkerEntity
import com.example.ui.theme.CooperativeNavy
import com.example.ui.theme.CooperativeNavyDark
import com.example.ui.theme.SaffronTrust
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VerifiedBadgeBg
import com.example.ui.theme.VerifiedBadgeBlue
import com.example.ui.theme.WelfareGreen
import com.example.ui.theme.WelfareGreenLight
import kotlinx.coroutines.delay

data class ServiceOption(
    val trade: String,
    val title: String,
    val description: String,
    val basePrice: Int,
    val durationText: String
)

val defaultServicesCatalog = listOf(
    ServiceOption(
        trade = "Electrician",
        title = "Electrical Inspection & Wiring Fix",
        description = "Circuit fault tracing, switchboard repairs, and MCB protection check.",
        basePrice = 249,
        durationText = "60-90 min"
    ),
    ServiceOption(
        trade = "Electrician",
        title = "Ceiling Fan / Lighting Installation",
        description = "Safe installation and regulator testing with earth fault inspection.",
        basePrice = 199,
        durationText = "45 min"
    ),
    ServiceOption(
        trade = "Plumber",
        title = "Sanitary Fixture & Tap Leak Repair",
        description = "Precision washers, tap replacement, and pipe joint sealing.",
        basePrice = 199,
        durationText = "45-60 min"
    ),
    ServiceOption(
        trade = "Plumber",
        title = "Water Tank & Main Line Cleansing",
        description = "High-pressure hygiene flush and valve inspection.",
        basePrice = 399,
        durationText = "90 min"
    ),
    ServiceOption(
        trade = "Carpenter",
        title = "Furniture & Door Lock Restoration",
        description = "Hinge adjustment, cylindrical lock fitting, and hardwood smoothing.",
        basePrice = 299,
        durationText = "60 min"
    ),
    ServiceOption(
        trade = "Painter",
        title = "Wall Damp Proofing & Patch Painting",
        description = "Waterproofing primer application and color matching finish.",
        basePrice = 349,
        durationText = "2-3 hrs"
    ),
    ServiceOption(
        trade = "Cleaner",
        title = "Deep Kitchen & Bathroom Sanitization",
        description = "Tile de-scaling, chrome polish, and floor scrubbing with eco-solvents.",
        basePrice = 299,
        durationText = "2 hrs"
    ),
    ServiceOption(
        trade = "Caregiver",
        title = "Senior Home Assistance & Vitals",
        description = "Compassionate vital monitoring, mobility help, and medicine tracking.",
        basePrice = 399,
        durationText = "Half Day"
    ),
    ServiceOption(
        trade = "Technician",
        title = "AC / Refrigerator General Service",
        description = "Cooling coil cleanse, gas pressure check, and amperage tuning.",
        basePrice = 349,
        durationText = "60 min"
    )
)

/**
 * Complete Customer Booking Flow:
 * Customer/Location -> Service Request -> Date/Time -> AI Matching ->
 * Cooperative Approval & Worker -> Digital Payment -> Live Tracking -> Rating
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingFlowSheet(
    allWorkers: List<WorkerEntity>,
    initialWorker: WorkerEntity? = null,
    initialServiceTitle: String? = null,
    initialTrade: String? = null,
    onDismiss: () -> Unit,
    onConfirmBooking: (
        worker: WorkerEntity,
        serviceTitle: String,
        description: String,
        date: String,
        timeSlot: String,
        customerName: String,
        customerPhone: String,
        customerAddress: String,
        totalAmount: Int,
        paymentMethod: String
    ) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Current Step:
    // 1 = Account & Location (GPS / Address)
    // 2 = Select Service
    // 3 = Choose Date & Time
    // 4 = AI Matching (Skill, Availability, Distance, Workload & Fair Allocation)
    // 5 = Cooperative Approval & Worker Notification (Accept / Auto-Reassign)
    // 6 = Digital Payment Gateway
    // 7 = Live Service Lifecycle Tracking
    // 8 = Rate & Review Worker
    var currentStep by remember {
        mutableIntStateOf(if (initialWorker != null) 3 else 1)
    }

    // Step 1: Customer & Location State
    var customerName by remember { mutableStateOf("Kapoor Usha") }
    var customerPhone by remember { mutableStateOf("+91 98102 34567") }
    var customerAddress by remember { mutableStateOf("Flat 402, Lotus Heights, Saket, New Delhi") }
    var isGpsActive by remember { mutableStateOf(false) }
    var isLocatingGps by remember { mutableStateOf(false) }
    var gpsCoordinates by remember { mutableStateOf("28.5244° N, 77.2066° E (Saket, Delhi)") }

    // Step 2: Service State
    var selectedTrade by remember {
        mutableStateOf(initialWorker?.trade ?: initialTrade ?: "Electrician")
    }
    var selectedService by remember {
        mutableStateOf(
            defaultServicesCatalog.firstOrNull { it.trade.equals(selectedTrade, ignoreCase = true) }
                ?: defaultServicesCatalog.first()
        )
    }
    var customServiceNotes by remember {
        mutableStateOf("Please bring standard diagnostic tools and insulated safety gear.")
    }

    // Step 3: Date & Time State
    var selectedDate by remember { mutableStateOf("Today, Express (Within 60 min)") }
    var selectedTimeSlot by remember { mutableStateOf("11:30 AM - 01:30 PM") }

    // Step 4: AI Matching State
    val availableWorkersForTrade = remember(selectedTrade, allWorkers) {
        val filtered = allWorkers.filter { it.trade.equals(selectedTrade, ignoreCase = true) }
        if (filtered.isNotEmpty()) filtered else allWorkers
    }

    // AI Best Worker based on skill, availability, proximity, workload fair allocation
    val aiMatchedWorker = remember(availableWorkersForTrade, initialWorker) {
        initialWorker ?: availableWorkersForTrade.maxByOrNull {
            it.rating * 10 + (if (it.isAvailable) 20 else 0) - (it.completedJobs % 15)
        } ?: availableWorkersForTrade.first()
    }

    var selectedWorker by remember {
        mutableStateOf(initialWorker ?: aiMatchedWorker)
    }

    // Step 5: Cooperative Approval & Worker Response Simulation
    var workerResponseStatus by remember { mutableStateOf("ACCEPTED") } // "ACCEPTED", "REJECTED_REPLACED"
    var replacementWorkerNotice by remember { mutableStateOf<String?>(null) }

    // Step 6: Digital Payment State
    var paymentMethod by remember { mutableStateOf("UPI (GPay / PhonePe)") }
    var isPaymentProcessing by remember { mutableStateOf(false) }
    var isPaymentComplete by remember { mutableStateOf(false) }

    // Step 7: Live Service Tracking Simulation State
    var activeTrackingStatus by remember { mutableStateOf(BookingTrackingStatus.WORKER_ASSIGNED) }
    var otpCode by remember { mutableStateOf("5821") }

    // Step 8: Rate & Review State
    var ratingScore by remember { mutableFloatStateOf(5f) }
    var reviewText by remember { mutableStateOf("Master craftsmanship, punctual arrival, and transparent zero-commission billing!") }
    var selectedCompliments by remember { mutableStateOf(setOf("Punctual", "Master Craftsmanship", "Transparent Price")) }
    var isReviewSubmitted by remember { mutableStateOf(false) }

    val baseLaborRate = selectedWorker.hourlyRate
    val welfareFund = (baseLaborRate * 0.03).toInt().coerceAtLeast(10)
    val totalAmount = baseLaborRate

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .widthIn(max = 600.dp)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 32.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (currentStep > 1 && currentStep < 7) {
                        IconButton(
                            onClick = { currentStep-- },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = CooperativeNavy
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    Column {
                        Text(
                            text = when (currentStep) {
                                1 -> "1. Account & GPS Location"
                                2 -> "2. Select Service"
                                3 -> "3. Choose Date & Time"
                                4 -> "4. AI Matching Engine"
                                5 -> "5. Cooperative Dispatch"
                                6 -> "6. Digital Payment"
                                7 -> "7. Live Service Tracking"
                                8 -> "8. Rate & Review Artisan"
                                else -> "Customer Booking Flow"
                            },
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = CooperativeNavy
                        )
                        Text(
                            text = "Delhi Shramik Sahakari • 100% Cooperative Owned",
                            fontSize = 11.sp,
                            color = SaffronTrust,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 8-Step Flow Stepper
            FlowStepProgressBar(currentStep = currentStep)

            Spacer(modifier = Modifier.height(14.dp))

            // Step Content
            AnimatedContent(
                targetState = currentStep,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "BookingStepAnimation"
            ) { step ->
                when (step) {
                    1 -> Step1AccountAndLocation(
                        customerName = customerName,
                        customerPhone = customerPhone,
                        customerAddress = customerAddress,
                        isGpsActive = isGpsActive,
                        isLocatingGps = isLocatingGps,
                        gpsCoordinates = gpsCoordinates,
                        onNameChange = { customerName = it },
                        onPhoneChange = { customerPhone = it },
                        onAddressChange = { customerAddress = it },
                        onTriggerGps = {
                            isLocatingGps = true
                            // Simulated fast GPS fix
                            isGpsActive = true
                            customerAddress = "Saket District Centre, Sector 6, South Delhi"
                            gpsCoordinates = "28.5244° N, 77.2066° E (Accuracy: ±6m)"
                            isLocatingGps = false
                        },
                        onSelectPresetLocality = { locality ->
                            customerAddress = "$locality, Delhi NCR"
                        },
                        onNext = { currentStep = 2 }
                    )

                    2 -> Step2ServiceSelection(
                        selectedTrade = selectedTrade,
                        selectedService = selectedService,
                        customNotes = customServiceNotes,
                        onTradeSelected = { trade ->
                            selectedTrade = trade
                            val match = defaultServicesCatalog.firstOrNull { it.trade.equals(trade, ignoreCase = true) }
                            if (match != null) selectedService = match
                            val workers = allWorkers.filter { it.trade.equals(trade, ignoreCase = true) }
                            if (workers.isNotEmpty()) selectedWorker = workers.first()
                        },
                        onServiceSelected = { service -> selectedService = service },
                        onNotesChanged = { customServiceNotes = it },
                        onNext = { currentStep = 3 }
                    )

                    3 -> Step3DateTimeSelection(
                        selectedDate = selectedDate,
                        selectedTimeSlot = selectedTimeSlot,
                        onDateSelected = { selectedDate = it },
                        onTimeSlotSelected = { selectedTimeSlot = it },
                        onNext = { currentStep = 4 }
                    )

                    4 -> Step4AiMatchingEngine(
                        selectedTrade = selectedTrade,
                        allAvailableWorkers = availableWorkersForTrade,
                        matchedWorker = selectedWorker,
                        onSelectWorker = { worker -> selectedWorker = worker },
                        onNext = { currentStep = 5 }
                    )

                    5 -> Step5CooperativeApprovalAndWorker(
                        worker = selectedWorker,
                        serviceTitle = selectedService.title,
                        scheduledDate = selectedDate,
                        scheduledTime = selectedTimeSlot,
                        customerAddress = customerAddress,
                        workerResponseStatus = workerResponseStatus,
                        replacementNotice = replacementWorkerNotice,
                        onSimulateWorkerAccept = {
                            workerResponseStatus = "ACCEPTED"
                            currentStep = 6
                        },
                        onSimulateWorkerDeclineAndReplace = {
                            // Find backup worker from same trade
                            val backup = availableWorkersForTrade.firstOrNull { it.id != selectedWorker.id }
                                ?: allWorkers.firstOrNull { it.id != selectedWorker.id }
                            if (backup != null) {
                                selectedWorker = backup
                                workerResponseStatus = "REJECTED_REPLACED"
                                replacementWorkerNotice = "Original worker was in another AGM meeting. Cooperative Auto-Replacement dispatched verified artisan ${backup.name} (${backup.trade}, ${backup.rating}★) immediately!"
                            }
                        },
                        onProceedToPayment = { currentStep = 6 }
                    )

                    6 -> Step6DigitalPaymentGateway(
                        worker = selectedWorker,
                        service = selectedService,
                        totalAmount = totalAmount,
                        baseRate = baseLaborRate,
                        welfareFee = welfareFund,
                        paymentMethod = paymentMethod,
                        isProcessing = isPaymentProcessing,
                        onPaymentMethodChanged = { paymentMethod = it },
                        onPayAndConfirm = {
                            isPaymentProcessing = true
                            isPaymentComplete = true
                            isPaymentProcessing = false

                            // Persist into Room Database & Active Tracking
                            onConfirmBooking(
                                selectedWorker,
                                selectedService.title,
                                customServiceNotes,
                                selectedDate,
                                selectedTimeSlot,
                                customerName,
                                customerPhone,
                                customerAddress,
                                totalAmount,
                                paymentMethod
                            )

                            // Move to Live Service Tracking Step
                            currentStep = 7
                        }
                    )

                    7 -> Step7LiveServiceTracking(
                        worker = selectedWorker,
                        serviceTitle = selectedService.title,
                        customerAddress = customerAddress,
                        otpCode = otpCode,
                        currentStatus = activeTrackingStatus,
                        onAdvanceStatus = { nextStatus ->
                            activeTrackingStatus = nextStatus
                            if (nextStatus == BookingTrackingStatus.COMPLETED) {
                                currentStep = 8
                            }
                        },
                        onGoToRating = { currentStep = 8 },
                        onDismiss = onDismiss
                    )

                    8 -> Step8RateAndReviewWorker(
                        worker = selectedWorker,
                        serviceTitle = selectedService.title,
                        rating = ratingScore,
                        reviewText = reviewText,
                        selectedCompliments = selectedCompliments,
                        isSubmitted = isReviewSubmitted,
                        onRatingChange = { ratingScore = it },
                        onReviewTextChange = { reviewText = it },
                        onToggleCompliment = { comp ->
                            selectedCompliments = if (selectedCompliments.contains(comp)) {
                                selectedCompliments - comp
                            } else {
                                selectedCompliments + comp
                            }
                        },
                        onSubmitReview = {
                            isReviewSubmitted = true
                        },
                        onFinish = onDismiss
                    )
                }
            }
        }
    }
}

/**
 * Scannable 8-Step Header Indicator
 */
@Composable
fun FlowStepProgressBar(currentStep: Int) {
    val stepLabels = listOf(
        "Location", "Service", "Schedule", "AI Match",
        "Worker", "Payment", "Track", "Rating"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            stepLabels.forEachIndexed { index, label ->
                val stepNum = index + 1
                val isDone = currentStep > stepNum
                val isCurrent = currentStep == stepNum

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isDone -> WelfareGreen
                                    isCurrent -> SaffronTrust
                                    else -> Color(0xFFE2E8F0)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isDone) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                        } else {
                            Text(
                                text = "$stepNum",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCurrent) Color.White else TextMuted
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                        color = if (isCurrent) CooperativeNavy else if (isDone) WelfareGreen else TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Progress line
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(0xFFE2E8F0))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(currentStep / 8f)
                    .height(3.dp)
                    .background(SaffronTrust)
            )
        }
    }
}

// ----------------------------------------------------------------------
// STEP 1: ACCOUNT & LOCATION (GPS + RADAR GEOLOCATION)
// ----------------------------------------------------------------------
@Composable
fun Step1AccountAndLocation(
    customerName: String,
    customerPhone: String,
    customerAddress: String,
    isGpsActive: Boolean,
    isLocatingGps: Boolean,
    gpsCoordinates: String,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onTriggerGps: () -> Unit,
    onSelectPresetLocality: (String) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        // Customer Profile Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Customer Account",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = CooperativeNavy
                    )
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(WelfareGreenLight)
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = WelfareGreen, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("Verified Citizen", fontSize = 10.sp, color = WelfareGreen, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = customerName,
                        onValueChange = onNameChange,
                        label = { Text("Full Name", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = customerPhone,
                        onValueChange = onPhoneChange,
                        label = { Text("Mobile Phone", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Location & Geolocation Section
        Text(
            text = "Service Location & Geolocation",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = CooperativeNavy
        )
        Text(
            text = "Allow GPS or choose your locality to match nearby cooperative artisans.",
            fontSize = 11.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        // GPS Auto-detect Button
        Button(
            onClick = onTriggerGps,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isGpsActive) WelfareGreen else CooperativeNavy
            )
        ) {
            Icon(Icons.Default.MyLocation, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isGpsActive) "GPS Location Locked: $gpsCoordinates" else "Allow GPS Location (Auto-Detect)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Manual Address Input
        OutlinedTextField(
            value = customerAddress,
            onValueChange = onAddressChange,
            label = { Text("Service Street Address / Flat No.", fontSize = 12.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Preset Locality Chips for Delhi NCR
        Text("Quick Localities:", fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("Saket", "Indirapuram", "Rohini Sec 9", "Dwarka Mor", "Connaught Place", "Mayur Vihar", "Noida Sec 62").forEach { loc ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF1F5F9))
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(8.dp))
                        .clickable { onSelectPresetLocality(loc) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(loc, fontSize = 11.sp, color = CooperativeNavy, fontWeight = FontWeight.Medium)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Nearby Workers Geolocation Radar Preview
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(WelfareGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Radar, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Geolocation Radar: 8 Verified Artisans Nearby",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF166534)
                    )
                    Text(
                        text = "Cooperative cluster active within 3.2 km radius of your location.",
                        fontSize = 11.sp,
                        color = Color(0xFF15803D)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SaffronTrust)
        ) {
            Text("Proceed to Service Selection", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(6.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
        }
    }
}

// ----------------------------------------------------------------------
// STEP 2: SELECT SERVICE
// ----------------------------------------------------------------------
@Composable
fun Step2ServiceSelection(
    selectedTrade: String,
    selectedService: ServiceOption,
    customNotes: String,
    onTradeSelected: (String) -> Unit,
    onServiceSelected: (ServiceOption) -> Unit,
    onNotesChanged: (String) -> Unit,
    onNext: () -> Unit
) {
    val trades = listOf("Electrician", "Plumber", "Carpenter", "Painter", "Cleaner", "Caregiver", "Technician")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Select Service Trade",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = CooperativeNavy
        )
        Text(
            text = "All services backed by cooperative transparent standardized rate card.",
            fontSize = 11.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Trade selector horizontal chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            trades.forEach { trade ->
                val isSelected = selectedTrade.equals(trade, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) CooperativeNavy else Color(0xFFF1F5F9))
                        .border(1.dp, if (isSelected) CooperativeNavy else SurfaceBorder, RoundedCornerShape(12.dp))
                        .clickable { onTradeSelected(trade) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = getTradeIcon(trade),
                            contentDescription = trade,
                            tint = if (isSelected) SaffronTrust else TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
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

        Spacer(modifier = Modifier.height(14.dp))

        Text("Select Specific Task / Repair:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(8.dp))

        val tradeServices = defaultServicesCatalog.filter { it.trade.equals(selectedTrade, ignoreCase = true) }
        val displayServices = if (tradeServices.isNotEmpty()) tradeServices else listOf(selectedService)

        displayServices.forEach { service ->
            val isSelected = selectedService.title == service.title
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFFFFFBEB) else Color.White
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isSelected) SaffronTrust else SurfaceBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onServiceSelected(service) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(service.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                        Text(service.description, fontSize = 11.sp, color = TextSecondary, maxLines = 2)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Est: ${service.durationText}", fontSize = 10.sp, color = TextMuted)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("0% Platform Cut", fontSize = 10.sp, color = WelfareGreen, fontWeight = FontWeight.Bold)
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("₹${service.basePrice}", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = CooperativeNavy)
                        RadioButton(
                            selected = isSelected,
                            onClick = { onServiceSelected(service) },
                            colors = RadioButtonDefaults.colors(selectedColor = SaffronTrust)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = customNotes,
            onValueChange = onNotesChanged,
            label = { Text("Describe specific problem / instructions", fontSize = 12.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SaffronTrust)
        ) {
            Text("Choose Date & Time Slot", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(6.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
        }
    }
}

// ----------------------------------------------------------------------
// STEP 3: CHOOSE DATE & TIME
// ----------------------------------------------------------------------
@Composable
fun Step3DateTimeSelection(
    selectedDate: String,
    selectedTimeSlot: String,
    onDateSelected: (String) -> Unit,
    onTimeSlotSelected: (String) -> Unit,
    onNext: () -> Unit
) {
    val dateOptions = listOf(
        "Today, Express (Within 60 min)",
        "Today, Evening",
        "Tomorrow, Morning",
        "Tomorrow, Afternoon"
    )

    val timeSlots = listOf(
        "09:00 AM - 11:30 AM",
        "11:30 AM - 01:30 PM",
        "02:00 PM - 04:30 PM",
        "05:00 PM - 07:30 PM"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text("Select Service Date", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CooperativeNavy)
        Text("Express cooperative dispatch available within 60 minutes.", fontSize = 11.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(10.dp))

        dateOptions.forEach { date ->
            val isSelected = selectedDate == date
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFFEFF6FF) else Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) CooperativeNavy else SurfaceBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onDateSelected(date) }
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = if (isSelected) CooperativeNavy else TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(date, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium, fontSize = 13.sp, color = TextPrimary)
                    Spacer(modifier = Modifier.weight(1f))
                    if (isSelected) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CooperativeNavy, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text("Select Preferred Time Slot", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CooperativeNavy)
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            timeSlots.take(2).forEach { slot ->
                val isSelected = selectedTimeSlot == slot
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) CooperativeNavy else Color(0xFFF1F5F9))
                        .border(1.dp, if (isSelected) CooperativeNavy else SurfaceBorder, RoundedCornerShape(10.dp))
                        .clickable { onTimeSlotSelected(slot) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(slot, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else TextPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            timeSlots.drop(2).forEach { slot ->
                val isSelected = selectedTimeSlot == slot
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) CooperativeNavy else Color(0xFFF1F5F9))
                        .border(1.dp, if (isSelected) CooperativeNavy else SurfaceBorder, RoundedCornerShape(10.dp))
                        .clickable { onTimeSlotSelected(slot) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(slot, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else TextPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SaffronTrust)
        ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Trigger AI Worker Matching", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// ----------------------------------------------------------------------
// STEP 4: AI MATCHING ENGINE (Skill, Availability, Distance, Workload & Fair Allocation)
// ----------------------------------------------------------------------
@Composable
fun Step4AiMatchingEngine(
    selectedTrade: String,
    allAvailableWorkers: List<WorkerEntity>,
    matchedWorker: WorkerEntity,
    onSelectWorker: (WorkerEntity) -> Unit,
    onNext: () -> Unit
) {
    var isAnalyzing by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(600)
        isAnalyzing = false
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        // AI Matching Banner
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CooperativeNavy, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Shram AI Cooperative Matching Engine",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = CooperativeNavy
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Balancing 4 Key Pillars: Skill Verification, GPS Proximity, Live Availability, and Democratic Fair Workload Allocation (no middleman monopoly).",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 4 Pillars Breakdown
                AiPillarMetricRow(title = "Skill & NSDC Certification", score = "99% Match", progress = 0.99f)
                AiPillarMetricRow(title = "Distance & Proximity (1.2 km)", score = "96% Match", progress = 0.96f)
                AiPillarMetricRow(title = "Duty Availability (On Duty)", score = "100% Ready", progress = 1.0f)
                AiPillarMetricRow(title = "Workload & Fair Rotation Index", score = "Democratically Balanced", progress = 0.94f)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "AI Recommended Cooperative Artisan",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = CooperativeNavy
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Selected Worker Highlight Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, SaffronTrust),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WorkerPhotoAvatar(worker = matchedWorker, size = 56.dp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(matchedWorker.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.Default.Verified, contentDescription = "Verified", tint = VerifiedBadgeBlue, modifier = Modifier.size(16.dp))
                        }
                        Text(
                            text = "${matchedWorker.trade} • ${matchedWorker.experienceYears} Yrs Experience",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = SaffronTrust, modifier = Modifier.size(14.dp))
                            Text(" ${matchedWorker.rating} (${matchedWorker.reviewCount} reviews)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("ID: ${matchedWorker.memberId}", fontSize = 10.sp, color = TextMuted)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(WelfareGreenLight)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = WelfareGreen, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Cooperative Fair Rotation Pick", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = WelfareGreen)
                    }

                    Text("₹${matchedWorker.hourlyRate}/hr", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = CooperativeNavy)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Or pick another worker from the cooperative
        if (allAvailableWorkers.size > 1) {
            Text("Other Certified Artisans in Trade:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
            Spacer(modifier = Modifier.height(6.dp))
            allAvailableWorkers.filter { it.id != matchedWorker.id }.take(2).forEach { other ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .clickable { onSelectWorker(other) }
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            WorkerPhotoAvatar(worker = other, size = 32.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(other.name, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("${other.trade} • ${other.rating}★", fontSize = 10.sp, color = TextSecondary)
                            }
                        }
                        Text("Select", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CooperativeNavy)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SaffronTrust)
        ) {
            Text("Approve & Dispatch to Worker", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(6.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun AiPillarMetricRow(title: String, score: String, progress: Float) {
    Column(modifier = Modifier.padding(vertical = 3.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, fontSize = 10.sp, color = TextSecondary)
            Text(score, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CooperativeNavy)
        }
        Spacer(modifier = Modifier.height(2.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
            color = WelfareGreen,
            trackColor = Color(0xFFE2E8F0)
        )
    }
}

// ----------------------------------------------------------------------
// STEP 5: COOPERATIVE APPROVAL & WORKER NOTIFICATION (ACCEPT / AUTO-REASSIGN)
// ----------------------------------------------------------------------
@Composable
fun Step5CooperativeApprovalAndWorker(
    worker: WorkerEntity,
    serviceTitle: String,
    scheduledDate: String,
    scheduledTime: String,
    customerAddress: String,
    workerResponseStatus: String,
    replacementNotice: String?,
    onSimulateWorkerAccept: () -> Unit,
    onSimulateWorkerDeclineAndReplace: () -> Unit,
    onProceedToPayment: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        // Cooperative Approval Banner
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC))
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = WelfareGreen, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Cooperative Order Dispatch Approved",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF166534)
                    )
                    Text(
                        text = "Registered in Cooperative Registry. Sent to artisan's Shram Worker App.",
                        fontSize = 11.sp,
                        color = Color(0xFF15803D)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Worker Response Status Box
        if (replacementNotice != null) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = Color(0xFFB45309), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = replacementNotice,
                        fontSize = 11.sp,
                        color = Color(0xFF92400E),
                        lineHeight = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Assigned Worker Details Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    WorkerPhotoAvatar(worker = worker, size = 48.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(worker.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Verified, contentDescription = null, tint = VerifiedBadgeBlue, modifier = Modifier.size(14.dp))
                        }
                        Text("${worker.trade} • Member #${worker.memberId}", fontSize = 11.sp, color = TextSecondary)
                        Text("★ ${worker.rating} • ${worker.completedJobs} jobs completed", fontSize = 11.sp, color = SaffronTrust, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(8.dp))

                Text("Booking Details:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = TextMuted)
                Text("• Service: $serviceTitle", fontSize = 12.sp, color = TextPrimary)
                Text("• Date/Time: $scheduledDate, $scheduledTime", fontSize = 12.sp, color = TextPrimary)
                Text("• Destination: $customerAddress", fontSize = 12.sp, color = TextPrimary)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Worker App Interaction Simulator Panel
        Text(
            text = "Worker App Response Simulator",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = CooperativeNavy
        )
        Text(
            text = "Test worker accept or auto-replacement if artisan declines/unavailable:",
            fontSize = 10.sp,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = onSimulateWorkerDeclineAndReplace,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = Color.Red, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Artisan Unavailable (Auto-Replace)", fontSize = 10.sp, color = Color.Red, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onSimulateWorkerAccept,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = WelfareGreen)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Artisan Accepts", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = onProceedToPayment,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SaffronTrust)
        ) {
            Text("Proceed to Digital Payment", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(6.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
        }
    }
}

// ----------------------------------------------------------------------
// STEP 6: DIGITAL PAYMENT GATEWAY
// ----------------------------------------------------------------------
@Composable
fun Step6DigitalPaymentGateway(
    worker: WorkerEntity,
    service: ServiceOption,
    totalAmount: Int,
    baseRate: Int,
    welfareFee: Int,
    paymentMethod: String,
    isProcessing: Boolean,
    onPaymentMethodChanged: (String) -> Unit,
    onPayAndConfirm: () -> Unit
) {
    val paymentOptions = listOf(
        "UPI (Google Pay / PhonePe / Paytm)",
        "Credit / Debit Card (Zero Surcharge)",
        "Net Banking (All Indian Banks)",
        "Sahakari Escrow / Pay on Service Completion"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        // Transparent Bill Breakdown
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Transparent Cooperative Bill", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CooperativeNavy)
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Artisan Direct Labor (97%)", fontSize = 12.sp, color = TextSecondary)
                    Text("₹${baseRate - welfareFee}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Worker Medical & Pension Pool (3%)", fontSize = 12.sp, color = TextSecondary)
                    Text("₹$welfareFee", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WelfareGreen)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Platform Commission Cut", fontSize = 12.sp, color = TextSecondary)
                    Text("₹0 (0% Middleman)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WelfareGreen)
                }
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9))
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Payable", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CooperativeNavy)
                    Text("₹$totalAmount", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = SaffronTrust)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text("Select Digital Payment Method", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CooperativeNavy)
        Text("Protected by Cooperative Escrow Guarantee.", fontSize = 11.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(8.dp))

        paymentOptions.forEach { method ->
            val isSelected = paymentMethod == method
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFFFFFBEB) else Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) SaffronTrust else SurfaceBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onPaymentMethodChanged(method) }
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when {
                            method.startsWith("UPI") -> Icons.Default.QrCode
                            method.startsWith("Credit") -> Icons.Default.CreditCard
                            method.startsWith("Net") -> Icons.Default.AccountBalance
                            else -> Icons.Default.Shield
                        },
                        contentDescription = null,
                        tint = if (isSelected) SaffronTrust else CooperativeNavy,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = method,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    RadioButton(
                        selected = isSelected,
                        onClick = { onPaymentMethodChanged(method) },
                        colors = RadioButtonDefaults.colors(selectedColor = SaffronTrust)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = onPayAndConfirm,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = WelfareGreen)
        ) {
            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Pay ₹$totalAmount & Confirm Booking", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// ----------------------------------------------------------------------
// STEP 7: LIVE SERVICE TRACKING (Assigned -> Accepted -> On the Way -> Started -> Completed)
// ----------------------------------------------------------------------
@Composable
fun Step7LiveServiceTracking(
    worker: WorkerEntity,
    serviceTitle: String,
    customerAddress: String,
    otpCode: String,
    currentStatus: BookingTrackingStatus,
    onAdvanceStatus: (BookingTrackingStatus) -> Unit,
    onGoToRating: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        // Live Status Banner
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
        ) {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.NearMe, contentDescription = null, tint = CooperativeNavy, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Live Order Tracking • Stage ${currentStatus.stepNumber} of 5",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = CooperativeNavy
                    )
                    Text(
                        text = "Assigned → Accepted → On the Way → Service Started → Completed",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 5-Stage Stepper
        BookingTrackingTimeline(currentStatus = currentStatus)

        Spacer(modifier = Modifier.height(12.dp))

        // OTP Banner
        if (currentStatus != BookingTrackingStatus.COMPLETED) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Start-Job Security OTP", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF92400E))
                        Text("Share with ${worker.name} on arrival", fontSize = 10.sp, color = Color(0xFFB45309))
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(CooperativeNavy)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(otpCode, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, letterSpacing = 2.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Assigned Artisan Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                WorkerPhotoAvatar(worker = worker, size = 48.dp)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(worker.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("${worker.trade} • ${worker.phone}", fontSize = 11.sp, color = TextSecondary)
                    Text("Destination: $customerAddress", fontSize = 11.sp, color = TextMuted, maxLines = 1)
                }
                IconButton(onClick = { /* Call worker */ }) {
                    Icon(Icons.Default.Phone, contentDescription = "Call", tint = WelfareGreen)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Progress Controls to advance through the 5 stages
        Text("Advance Tracking Stage (Simulate Job Lifecycle):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
        Spacer(modifier = Modifier.height(6.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            when (currentStatus) {
                BookingTrackingStatus.CONFIRMED -> {
                    Button(
                        onClick = { onAdvanceStatus(BookingTrackingStatus.WORKER_ASSIGNED) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = CooperativeNavy)
                    ) {
                        Text("1. Worker Accepts Job", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                BookingTrackingStatus.WORKER_ASSIGNED -> {
                    Button(
                        onClick = { onAdvanceStatus(BookingTrackingStatus.WORKER_ARRIVING) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronTrust)
                    ) {
                        Icon(Icons.Default.NearMe, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("2. Worker En-Route (On the Way)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                BookingTrackingStatus.WORKER_ARRIVING -> {
                    Button(
                        onClick = { onAdvanceStatus(BookingTrackingStatus.SERVICE_STARTED) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = CooperativeNavy)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("3. Verify OTP & Start Service", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                BookingTrackingStatus.SERVICE_STARTED -> {
                    Button(
                        onClick = {
                            onAdvanceStatus(BookingTrackingStatus.COMPLETED)
                            onGoToRating()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = WelfareGreen)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("4. Complete Job & Rate Artisan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                BookingTrackingStatus.COMPLETED -> {
                    Button(
                        onClick = onGoToRating,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronTrust)
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Leave Rating & Review", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Close & Track in 'My Bookings'", fontSize = 12.sp, color = CooperativeNavy)
        }
    }
}

// ----------------------------------------------------------------------
// STEP 8: RATE & REVIEW WORKER
// ----------------------------------------------------------------------
@Composable
fun Step8RateAndReviewWorker(
    worker: WorkerEntity,
    serviceTitle: String,
    rating: Float,
    reviewText: String,
    selectedCompliments: Set<String>,
    isSubmitted: Boolean,
    onRatingChange: (Float) -> Unit,
    onReviewTextChange: (String) -> Unit,
    onToggleCompliment: (String) -> Unit,
    onSubmitReview: () -> Unit,
    onFinish: () -> Unit
) {
    val complimentsList = listOf(
        "Punctual & Fast",
        "Transparent Price",
        "Master Craftsmanship",
        "Clean Worksite",
        "Polite & Respectful",
        "Brought Right Tools"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isSubmitted) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(WelfareGreenLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = WelfareGreen, modifier = Modifier.size(36.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Review Submitted!", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CooperativeNavy)
            Text(
                "Thank you for rating ${worker.name}. Your feedback directly strengthens our democratic cooperative merit shares.",
                fontSize = 12.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onFinish,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CooperativeNavy)
            ) {
                Text("Return to Marketplace", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            return
        }

        WorkerPhotoAvatar(worker = worker, size = 64.dp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(worker.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Service Completed: $serviceTitle", fontSize = 11.sp, color = TextSecondary)

        Spacer(modifier = Modifier.height(14.dp))

        // Interactive 5-Star Rating
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            (1..5).forEach { starIndex ->
                IconButton(
                    onClick = { onRatingChange(starIndex.toFloat()) },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star $starIndex",
                        tint = if (starIndex <= rating) SaffronTrust else Color(0xFFE2E8F0),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
        Text(
            text = when (rating.toInt()) {
                5 -> "⭐⭐⭐⭐⭐ Outstanding Craftsmanship!"
                4 -> "⭐⭐⭐⭐ Very Good Work!"
                3 -> "⭐⭐⭐ Satisfactory"
                else -> "⭐⭐ Needs Improvement"
            },
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = CooperativeNavy
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Compliments Chips
        Text("Add Quality Badges:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            complimentsList.forEach { comp ->
                val isSelected = selectedCompliments.contains(comp)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) SaffronTrust else Color(0xFFF1F5F9))
                        .clickable { onToggleCompliment(comp) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        comp,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.White else TextPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = reviewText,
            onValueChange = onReviewTextChange,
            label = { Text("Write your feedback for the cooperative registry", fontSize = 12.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onSubmitReview,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = WelfareGreen)
        ) {
            Icon(Icons.Default.ThumbUp, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Submit Cooperative Review", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}
