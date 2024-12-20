package com.example.plannersandmoversapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.plannersandmoversapp.ui.theme.PlannersAndMoversAppTheme
import com.google.firebase.firestore.FirebaseFirestore
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

data class Booking(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val bookingType: String = "",
    val propertyType: String = "",
    val pickupLocation: String = "",
    val pickupDate: String = "",
    val pickupTime: String = "",
    val dropOffLocation: String = "",
    val dropOffDate: String = "",
    val dropOffTime: String = "",
    val inventoryItems: String = "",
    val additionalNotes: String = "",
    val packingService: Boolean = false,
    val disassemblyService: Boolean = false,
    val storageService: Boolean = false,
    val heavyLifting: Boolean = false,
    val preferredVehicleSize: String = "",
    val paymentMethod: String = ""
)


class CalendarManagerActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlannersAndMoversAppTheme {
                HamburgerMenuScreen(title = "Awesome Movers Inc.") { innerModifier ->
                    CalendarManagerScreen(modifier = innerModifier)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarManagerScreen(modifier: Modifier = Modifier) {
   /* val bookings = remember {
        listOf(
            Booking("1", "Alice", "123-456-789", "2024-11-22", "2-bedroom move"),
            Booking("2", "Bob", "987-654-321", "2024-11-22", "Office relocation"),
            Booking("3", "Charlie", "555-555-555", "2024-11-23", "Studio move")
        )
    }*/
    val bookings = remember { mutableStateListOf<Booking>() }
    var selectedDate by remember { mutableStateOf("2024-11-22") }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch data from Firestore
    LaunchedEffect(currentMonth) {
        isLoading = true
        val db = FirebaseFirestore.getInstance()
        db.collection("Bookings")
            .get()
            .addOnSuccessListener { result ->
                bookings.clear()
                for (document in result) {
                    val booking = Booking(
                        id = document.id,
                        name = document.getString("name") ?: "Unknown",
                        phone = document.getString("phone") ?: "N/A",
                        email = document.getString("email") ?: "N/A",
                        bookingType = document.getString("bookingType") ?: "N/A",
                        propertyType = document.getString("propertyType") ?: "N/A",
                        pickupLocation = document.getString("pickupLocation") ?: "N/A",
                        pickupDate = document.getString("pickupDate") ?: "N/A",
                        pickupTime = document.getString("pickupTime") ?: "N/A",
                        dropOffLocation = document.getString("dropOffLocation") ?: "N/A",
                        dropOffDate = document.getString("dropOffDate") ?: "N/A",
                        dropOffTime = document.get("dropOffTime")?.toString() ?: "N/A",
                        inventoryItems = document.getString("inventoryItems") ?: "N/A",
                        additionalNotes = document.getString("additionalNotes") ?: "N/A",
                        packingService = document.getBoolean("packingService") ?: false,
                        disassemblyService = document.getBoolean("disassemblyService") ?: false,
                        storageService = document.getBoolean("storageService") ?: false,
                        heavyLifting = document.getBoolean("heavyLifting") ?: false,
                        preferredVehicleSize = document.getString("preferredVehicleSize") ?: "N/A",
                        paymentMethod = document.getString("paymentMethod") ?: "N/A"
                    )
                    bookings.add(booking)
                }
                isLoading = false
                // Set a default selected date if available
                if (selectedDate.isEmpty() && bookings.isNotEmpty()) {
                    selectedDate = bookings.first().pickupDate
                }
            }
            .addOnFailureListener {
                isLoading = false
            }
    }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Manage Bookings",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Month Navigation
        MonthNavigation(
            currentMonth = currentMonth,
            onMonthChange = { newMonth -> currentMonth = newMonth }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Calendar Section
        BookingCalendar(
            bookings = bookings,
            currentMonth = currentMonth,
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Booking Details Section
        BookingDetails(
            bookings = bookings.filter { it.pickupDate == selectedDate }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthNavigation(
    currentMonth: YearMonth,
    onMonthChange: (YearMonth) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = { onMonthChange(currentMonth.minusMonths(1)) }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month")
        }
        Text(
            text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        IconButton(onClick = { onMonthChange(currentMonth.plusMonths(1)) }) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingCalendar(
    bookings: List<Booking>,
    currentMonth: YearMonth,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val daysInMonth = currentMonth.lengthOfMonth()
    val days = (1..daysInMonth).map { day ->
        "${currentMonth.year}-${currentMonth.monthValue.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
    }

    LazyRow(Modifier.fillMaxWidth()) {
        items(days.size) { index ->
            val day = days[index]
            val hasBooking = bookings.any { it.pickupDate == day }
            val isSelected = day == selectedDate

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(60.dp)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else if (hasBooking) MaterialTheme.colorScheme.secondaryContainer
                        else MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(8.dp)
                    )
                    .clickable { onDateSelected(day) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.takeLast(2),
                    fontSize = 16.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun BookingDetails(bookings: List<Booking>) {
    if (bookings.isEmpty()) {
        Text(
            text = "No bookings for the selected date.",
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 16.dp)
        )
    } else {
        LazyColumn(
            Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(bookings.size) { index ->
                val booking = bookings[index]
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(text = "Name: ${booking.name}", fontSize = 18.sp)
                        Text(text = "Phone: ${booking.phone}", fontSize = 16.sp)
                        Text(text = "Email: ${booking.email}", fontSize = 16.sp)
                        Text(text = "Booking Type: ${booking.bookingType}", fontSize = 16.sp)
                        Text(text = "Property Type: ${booking.propertyType}", fontSize = 16.sp)
                        Text(text = "Pickup Location: ${booking.pickupLocation}", fontSize = 16.sp)
                        Text(text = "Pickup Date: ${booking.pickupDate}", fontSize = 16.sp)
                        Text(text = "Pickup Time: ${booking.pickupTime}", fontSize = 16.sp)
                        Text(text = "Dropoff Location: ${booking.dropOffLocation}", fontSize = 16.sp)
                        Text(text = "Dropoff Date: ${booking.dropOffDate}", fontSize = 16.sp)
                        Text(text = "Dropoff Time: ${booking.dropOffTime}", fontSize = 16.sp)
                        Text(text = "Inventory Items: ${booking.inventoryItems}", fontSize = 16.sp)
                        Text(text = "Additional Notes: ${booking.additionalNotes}", fontSize = 16.sp)
                        Text(text = "Packing Service: ${if (booking.packingService) "Yes" else "No"}", fontSize = 16.sp)
                        Text(text = "Disassembly Service: ${if (booking.disassemblyService) "Yes" else "No"}", fontSize = 16.sp)
                        Text(text = "Storage Service: ${if (booking.storageService) "Yes" else "No"}", fontSize = 16.sp)
                        Text(text = "Heavy Lifting: ${if (booking.heavyLifting) "Yes" else "No"}", fontSize = 16.sp)
                        Text(text = "Preferred Vehicle Size: ${booking.preferredVehicleSize}", fontSize = 16.sp)
                        Text(text = "Payment Method: ${booking.paymentMethod}", fontSize = 16.sp)

                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewCalendarManagerScreen() {
    PlannersAndMoversAppTheme {
        HamburgerMenuScreen(title = "Calendar Manager") { innerModifier ->
            // Pass mocked data for the preview
            CalendarManagerScreen(modifier = innerModifier)
    }
}}
