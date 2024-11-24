package com.example.plannersandmoversapp

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plannersandmoversapp.ui.theme.PlannersAndMoversAppTheme
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import java.util.Calendar


class BookingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlannersAndMoversAppTheme {
                BookingPage()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingPage() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val database = FirebaseFirestore.getInstance()
    // Personal Information
    val name = remember { mutableStateOf("") }
    val phoneno = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val isEmailValid = remember { mutableStateOf(true) }

    // Booking Type
    val bookingType = remember { mutableStateOf("") }
    val propertyType = remember { mutableStateOf("") }

    // Pick-up and Drop-off Details
    val pickupLocation = remember { mutableStateOf("") }
    val pickUpDate = remember { mutableStateOf("") }
    val pickUpTime = remember { mutableStateOf("") }
    val dropOffLocation = remember { mutableStateOf("") }
    val dropOffDate = remember { mutableStateOf("") }
    val dropOffTime = remember { mutableStateOf("") }

    // Inventory Information
    val inventoryItems = remember { mutableStateOf("") }
    val additionalNotes = remember { mutableStateOf("") }

    // Additional Services
    val packingService = remember { mutableStateOf(false) }
    val disassemblyService = remember { mutableStateOf(false) }
    val storageService = remember { mutableStateOf(false) }
    val heavyLifting = remember { mutableStateOf(false) }

    // Payment Information
    val paymentMethod = remember { mutableStateOf("") }


    // User Preferences
    val preferredVehicleSize = remember { mutableStateOf("") }

    val scrollState = rememberScrollState()


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NormalUserDrawerContent { selectedItem ->
                    scope.launch { drawerState.close() } // Close drawer after item selection
                    when (selectedItem) {
                        "Home" -> {
                            val intent = Intent(context, NormalUserHomeActivity::class.java)
                            context.startActivity(intent)
                        }
                        "Bookings" -> {
                            val intent = Intent(context, BookingActivity::class.java)
                            context.startActivity(intent)
                        }
                        "Tracking" -> {
                            // Uncomment and add your TrackingActivity here
                            // val intent = Intent(context, TrackingActivity::class.java)
                            // context.startActivity(intent)
                        }
                        "Contact Page" -> {
                            val intent = Intent(context, ContactUsActivity::class.java)
                            context.startActivity(intent)
                        }
                        "Moving Company" -> {
                            val intent = Intent(context, UserMovingCompanyActivity::class.java)
                            context.startActivity(intent)
                        }
                        "Log Out" -> {
                            logOutNormalUser(context) // Handle logout
                        }
                    }
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Book Your Move") },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menu")
                            }
                        },
                        colors = TopAppBarDefaults.mediumTopAppBarColors(
                            containerColor = Color(0xFF6200EE),
                            titleContentColor = Color.White
                        )
                    )
                },
                content = { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp)
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Personal Information Section
                        Text(
                            text = "Personal Information",
                            style = MaterialTheme.typography.titleMedium
                        )
                        OutlinedTextField(
                            value = name.value,
                            onValueChange = { name.value = it },
                            label = { Text("Full Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = phoneno.value,
                            onValueChange = { phoneno.value = it },
                            label = { Text("Phone Number") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = email.value,
                            onValueChange = {
                                email.value = it
                                isEmailValid.value = android.util.Patterns.EMAIL_ADDRESS.matcher(it)
                                    .matches() // Validate email
                            },
                            label = { Text("Email") },
                            isError = !isEmailValid.value, // Highlight the text field in red if invalid
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (!isEmailValid.value) {
                            Text(
                                text = "Invalid email format",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        // Booking Type Section
                        Text(text = "Booking Type", style = MaterialTheme.typography.titleMedium)
                        DropdownMenuExample(
                            options = listOf(
                                "Local Move",
                                "Long-Distance Move",
                                "Office Relocation"
                            ),
                            selectedOption = bookingType.value,
                            onOptionSelected = { bookingType.value = it }
                        )
                        DropdownMenuExample(
                            options = listOf("Apartment", "House", "Office"),
                            selectedOption = propertyType.value,
                            onOptionSelected = { propertyType.value = it }
                        )

                        // Pick-up and Drop-off Details
                        Text(text = "Pick-up Details", style = MaterialTheme.typography.titleMedium)
                        OutlinedTextField(
                            value = pickupLocation.value,
                            onValueChange = { pickupLocation.value = it },
                            label = { Text("Pickup Location") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = pickUpDate.value,
                            onValueChange = {},
                            label = { Text("Pick-up Date") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    showDatePicker(context) { selectedDate ->
                                        pickUpDate.value = selectedDate
                                    }
                                }) {
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = "Select Date"
                                    )
                                }
                            }
                        )
                        OutlinedTextField(
                            value = pickUpTime.value,
                            onValueChange = {},
                            label = { Text("Pick-up Time") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    showTimePicker(context) { selectedTime ->
                                        pickUpTime.value = selectedTime
                                    }
                                }) {
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = "Select Time"
                                    )
                                }
                            }
                        )


                        Text(
                            text = "Drop-off Details",
                            style = MaterialTheme.typography.titleMedium
                        )
                        OutlinedTextField(
                            value = dropOffLocation.value,
                            onValueChange = { dropOffLocation.value = it },
                            label = { Text("Drop-off Location") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = dropOffDate.value,
                            onValueChange = {},
                            label = { Text("Drop-off Date") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    showDatePicker(context) { selectedDate ->
                                        dropOffDate.value = selectedDate
                                    }
                                }) {
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = "Select Date"
                                    )
                                }
                            }
                        )
                        OutlinedTextField(
                            value = dropOffTime.value,
                            onValueChange = {},
                            label = { Text("Drop-Off Time") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    showTimePicker(context) { selectedTime ->
                                        dropOffTime.value = selectedTime
                                    }
                                }) {
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = "Select Time"
                                    )
                                }
                            }
                        )


                        // Inventory Information
                        Text(
                            text = "Inventory Information",
                            style = MaterialTheme.typography.titleMedium
                        )
                        OutlinedTextField(
                            value = inventoryItems.value,
                            onValueChange = { inventoryItems.value = it },
                            label = { Text("Items to Move") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp), // Set the desired height
                            maxLines = 10, // Allow up to 10 lines of input
                            singleLine = false // Ensure it supports multiline input
                        )
                        OutlinedTextField(
                            value = additionalNotes.value,
                            onValueChange = { additionalNotes.value = it },
                            label = { Text("Additional Notes") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = "Preffered Moving Vehicle",
                            style = MaterialTheme.typography.titleMedium
                        )
                        DropdownMenuExample(
                            options = listOf("Small", "Medium", "Large"),
                            selectedOption = preferredVehicleSize.value,
                            onOptionSelected = { preferredVehicleSize.value = it }
                        )

                        // Additional Services
                        Text(
                            text = "Additional Services",
                            style = MaterialTheme.typography.titleMedium
                        )
                        CheckboxWithLabel(
                            checked = packingService.value,
                            onCheckedChange = { packingService.value = it },
                            label = "Packing Services"
                        )
                        CheckboxWithLabel(
                            checked = disassemblyService.value,
                            onCheckedChange = { disassemblyService.value = it },
                            label = "Disassembly/Assembly Services"
                        )
                        CheckboxWithLabel(
                            checked = storageService.value,
                            onCheckedChange = { storageService.value = it },
                            label = "Storage Services"
                        )
                        CheckboxWithLabel(
                            checked = heavyLifting.value,
                            onCheckedChange = { heavyLifting.value = it },
                            label = "Heavy Lifting Equipment"
                        )

                        // Payment Information
                        Text(
                            text = "Payment Information",
                            style = MaterialTheme.typography.titleMedium
                        )
                        DropdownMenuExample(
                            options = listOf("Credit Card", "PayPal", "Cash on Delivery"),
                            selectedOption = paymentMethod.value,
                            onOptionSelected = { paymentMethod.value = it }
                        )


                        Button(
                            onClick = {
                                // Validation for required fields
                                if (name.value.isBlank() ||
                                    phoneno.value.isBlank() ||
                                    email.value.isBlank() ||
                                    !isEmailValid.value || // Ensure email format is valid
                                    bookingType.value.isBlank() ||
                                    propertyType.value.isBlank() ||
                                    pickupLocation.value.isBlank() ||
                                    pickUpDate.value.isBlank() ||
                                    pickUpTime.value.isBlank() ||
                                    dropOffLocation.value.isBlank() ||
                                    dropOffDate.value.isBlank() ||
                                    dropOffTime.value.isBlank() ||
                                    inventoryItems.value.isBlank() ||
                                    preferredVehicleSize.value.isBlank() ||
                                    paymentMethod.value.isBlank()
                                ) {
                                    Toast.makeText(
                                        context,
                                        "Please fill in all required fields correctly",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    return@Button
                                }

                                // Collect booking data
                                val bookingData = mapOf(
                                    "name" to name.value,
                                    "phone" to phoneno.value,
                                    "email" to email.value,
                                    "bookingType" to bookingType.value,
                                    "propertyType" to propertyType.value,
                                    "pickupLocation" to pickupLocation.value,
                                    "pickupDate" to pickUpDate.value,
                                    "pickupTime" to pickUpTime.value,
                                    "dropOffLocation" to dropOffLocation.value,
                                    "dropOffDate" to dropOffDate.value,
                                    "dropOffTime" to dropOffTime.value,
                                    "inventoryItems" to inventoryItems.value,
                                    "additionalNotes" to additionalNotes.value,
                                    "packingService" to packingService.value,
                                    "disassemblyService" to disassemblyService.value,
                                    "storageService" to storageService.value,
                                    "heavyLifting" to heavyLifting.value,
                                    "preferredVehicleSize" to preferredVehicleSize.value,
                                    "paymentMethod" to paymentMethod.value
                                )

                                // Save booking to Firestore
                                saveBookingToFirestore(
                                    db = database,
                                    bookingData = bookingData,
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "Booking saved successfully!",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        // Clear form after success
                                        name.value = ""
                                        phoneno.value = ""
                                        email.value = ""
                                        bookingType.value = ""
                                        propertyType.value = ""
                                        pickupLocation.value = ""
                                        pickUpDate.value = ""
                                        pickUpTime.value = ""
                                        dropOffLocation.value = ""
                                        dropOffDate.value = ""
                                        dropOffTime.value = ""
                                        inventoryItems.value = ""
                                        additionalNotes.value = ""
                                        packingService.value = false
                                        disassemblyService.value = false
                                        storageService.value = false
                                        heavyLifting.value = false
                                        preferredVehicleSize.value = ""
                                        paymentMethod.value = ""
                                    },
                                    onFailure = { exception ->
                                        Toast.makeText(
                                            context,
                                            "Failed to save booking: ${exception.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Confirm Booking")
                        }
                    }
                })
        })
}

@Composable
fun CheckboxWithLabel(checked: Boolean, onCheckedChange: (Boolean) -> Unit, label: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}


fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            onDateSelected(selectedDate)
        },
        year,
        month,
        day
    )

    // Set the minimum date to today's date
    datePickerDialog.datePicker.minDate = calendar.timeInMillis

    datePickerDialog.show()
}


fun showTimePicker(context: Context, onTimeSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            onTimeSelected(formattedTime)
        },
        hour,
        minute,
        true // Use 24-hour format
    ).show()
}


@Composable
fun DropdownMenuExample(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var currentSelection by remember { mutableStateOf(selectedOption) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = currentSelection,
            onValueChange = {},
            label = { Text("Select Option") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                }
            }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        currentSelection = option
                        onOptionSelected(option)
                        expanded = false
                    })
            }
        }
    }
}
fun saveBookingToFirestore(
    db: FirebaseFirestore,
    bookingData: Map<String, Any>,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    db.collection("Bookings")
        .add(bookingData)
        .addOnSuccessListener {
            onSuccess()
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}

@Preview(showBackground = true)
@Composable
fun BookingPagePreview() {
    PlannersAndMoversAppTheme {
        BookingPage()
    }
}
