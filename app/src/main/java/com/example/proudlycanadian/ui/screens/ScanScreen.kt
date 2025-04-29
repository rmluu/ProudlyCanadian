package com.example.proudlycanadian.ui.screens

import android.Manifest
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proudlycanadian.R
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.proudlycanadian.ScanCode
import com.example.proudlycanadian.api.ProductManager
import com.example.proudlycanadian.api.model.FirestoreProduct
import com.example.proudlycanadian.navigation.Destination
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

/**
 * Purpose: Scan a barcode or manually enter a UPC number to fetch and display product details.
 *          The option to add the scanned product to a list is also available.
 * @param productManager: ProductManager - Responsible for fetching product details from APIs.
 * @param navController: NavHostController - Used for navigating to other screens (AddToList).
 */
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(
    productManager: ProductManager,
    navController: NavHostController
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    var showScanner by remember { mutableStateOf(false) }
    var upcInput by remember { mutableStateOf("") }
    var manualUpcInput by remember { mutableStateOf("") }

    var productName by remember { mutableStateOf("Scan a barcode to get product details") }
    var productImage by remember { mutableStateOf("") }
    var productUPC by remember { mutableStateOf("") }
    var countryCode by remember { mutableStateOf("") }

    val scannedProduct = remember { mutableStateOf<FirestoreProduct?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Barcode scanning
        if (showScanner) {
            ScanCode(
                onCodeDetected = { scannedCode ->
                    Log.e("Scan", "Scanned Code: $scannedCode")
                    upcInput = scannedCode
                    showScanner = false

                    // Fetch product details using ProductManager
                    productManager.getProductDetails(scannedCode) { gtin, country, name, image, upc ->
                        productName = name
                        productImage = image
                        productUPC = upc
                        countryCode = country
                    }
                }
            )
        } else {
            // Barcode Image
            Image(
                painter = painterResource(id = R.mipmap.ic_barcode),
                contentDescription = "Barcode Image",
                modifier = Modifier.size(180.dp)
            )

            // Button to scan a barcode
            Button(
                onClick = {
                    if (cameraPermissionState.status.isGranted) {
                        showScanner = true
                    } else {
                        cameraPermissionState.launchPermissionRequest()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Scan a barcode", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                modifier = Modifier
                    .width(200.dp)
                    .align(Alignment.CenterHorizontally),
                thickness = 1.dp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Manual UPC input
            TextField(
                value = manualUpcInput,
                onValueChange = { manualUpcInput = it },
                label = { Text("Enter a UPC number") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFFFEBEB)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    if (manualUpcInput.isNotBlank()) {
                        val trimmedUpc = manualUpcInput.trim()
                        upcInput = trimmedUpc

                        // Fetch product details for manual input
                        productManager.getProductDetails(trimmedUpc) { gtin, country, name, image, upc ->
                            productName = name
                            productImage = image
                            productUPC = upc
                            countryCode = country
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF212121)
                )
            ) {
                Text("Manual Search", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Display scanned product details
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (productName != "Scan a barcode to get product details") {
                    // Display product image if available
                    if (productImage.isNotEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(productImage),
                            contentDescription = "Product Image",
                            modifier = Modifier.size(180.dp)
                        )
                    }
                    Text(
                        productName,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )
                    Text(
                        "UPC: $upcInput",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )

                    // Row for Country of Origin and Add to List button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ) {
                        Text(
                            text = "Country of Origin: $countryCode",
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f)
                        )

                        // Show Trump image if product is from the US
                        if (countryCode == "US") {
                            Image(
                                painter = painterResource(id = R.drawable.ic_trump),
                                contentDescription = "Image of Trump",
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(end = 8.dp)
                            )
                        }

                        // Show Canadian image if product is from CA
                        if (countryCode == "CA") {
                            Image(
                                painter = painterResource(id = R.drawable.ic_canada),
                                contentDescription = "Made in Canada image",
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(end = 8.dp)
                            )
                        }

                        // Button to add the product to a list
                        IconButton(
                            onClick = {
                                val scannedProduct = FirestoreProduct(
                                    name = productName,
                                    upc = productUPC,
                                    image = productImage,
                                    origin = countryCode
                                )
                                navController.navigate(Destination.AddToList.createRoute(scannedProduct))
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add),
                                contentDescription = "Add to list",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
