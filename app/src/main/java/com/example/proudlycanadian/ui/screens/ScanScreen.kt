package com.example.proudlycanadian.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.proudlycanadian.ScanCode
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

/**
 * ScanScreen - Allows users to scan a barcode or manually enter a UPC number.
 * API integration (to be added later) will fetch product details and determine if the
 * product is Canadian in origin.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScanScreen()
{
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    var showScanner by remember { mutableStateOf(false) }
    var upcInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        if (showScanner) {
            ScanCode(
                onCodeDetected = { scannedCode ->
                    upcInput = scannedCode
                    showScanner = false
                }
            )
        } else {
            // Barcode Image
            Image(
                painter = painterResource(id = R.mipmap.ic_barcode),
                contentDescription = "Barcode Image",
                modifier = Modifier.size(200.dp)
            )

            // Scan UPC button
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

            Spacer(modifier = Modifier.height(24.dp))

            // Manual entry button
            OutlinedButton(
                onClick = { /* Handle manual UPC entry */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Enter a UPC number", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.weight(2f))
        }
    }
}