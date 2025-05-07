package com.example.proudlycanadian

import android.graphics.Rect
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.asPaddingValues

/**
 * Purpose: Displays a live camera preview and detects barcodes in real-time using ML Kit.
 * @param onCodeDetected: (String) -> Unit - Callback invoked when a barcode is detected.
 * @param modifier: Modifier - Optional modifier for layout customization.
 */
@Composable
fun ScanCode(
    onCodeDetected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var barcode by remember { mutableStateOf<String?>(null) }

    // Get current context and lifecycle owner to bind the camera lifecycle
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // State to track whether a barcode has been detected
    var codeDetected by remember { mutableStateOf(false) }

    // State to store the bounding rectangle of the detected barcode
    var boundingRect by remember { mutableStateOf<Rect?>(null) }

    // Initialize CameraX's LifecycleCameraController for managing camera operations
    val cameraController = remember {
        LifecycleCameraController(context)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Integrates camera preview and barcode scanning functionality
        AndroidView(
            modifier = modifier.fillMaxSize(),
            factory = { ctx ->
                PreviewView(ctx).apply {
                    // Specify barcode formats so the scanner only looks for relevant barcodes.
                    // Improves efficiency and reduces false positives.
                    val options = BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                            Barcode.FORMAT_UPC_A,
                            Barcode.FORMAT_UPC_E,
                            // Barcode.FORMAT_QR_CODE,       // QR Codes - marketing, contactless payments, URLs
                            // Barcode.FORMAT_CODABAR,       // Codabar - numeric encoding
                            // Barcode.FORMAT_CODE_93,       // Code 93 - alphanumeric encoding, low density
                            // Barcode.FORMAT_CODE_39,       // Code 39 - alphanumeric encoding, higher density
                            // Barcode.FORMAT_CODE_128,      // Code 128 - alphanumeric encoding, highest density
                            Barcode.FORMAT_EAN_8,         // EAN-8 - shorter EAN code - small products
                            Barcode.FORMAT_EAN_13,        // EAN-13 - product identification
                            // Barcode.FORMAT_AZTEC          // Aztec - large amount of data i.e. airline tickets
                        )
                        .build()

                    // Create the barcode scanner client with the defined options
                    val barcodeScanner = BarcodeScanning.getClient(options)

                    // Use CameraX's Image Analysis API to analyze frames from the camera
                    cameraController.setImageAnalysisAnalyzer(
                        ContextCompat.getMainExecutor(ctx), // Runs the analysis on the main thread
                        MlKitAnalyzer(
                            listOf(barcodeScanner),
                            ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED, // Aligns barcode coords with camera preview
                            ContextCompat.getMainExecutor(ctx)
                        ) { result: MlKitAnalyzer.Result? ->
                            // Process barcode detection results
                            val barcodeResults = result?.getValue(barcodeScanner)
                            if (!barcodeResults.isNullOrEmpty()) {
                                barcode = barcodeResults.first().rawValue
                                codeDetected = true
                                boundingRect = barcodeResults.first().boundingBox // Stores bounding box coords for drawing
                            }
                        }
                    )

                    // Bind the camera controller to the lifecycle of the activity/fragment
                    cameraController.bindToLifecycle(lifecycleOwner)

                    // Set the camera controller for the PreviewView to display the camera feed
                    this.controller = cameraController
                }
            }
        )

        // If a barcode has been detected, trigger callback and draw a rectangle
        if (codeDetected) {
            LaunchedEffect(Unit) {
                // Ensure the UI has time to recompose before invoking the callback
                delay(100)

                // Trigger the callback with the detected barcode value
                onCodeDetected(barcode ?: "")
            }

            // Draw a rectangle around the detected barcode for visual feedback if boundingRect != null
            boundingRect?.let { DrawRectangle(it) }
        }
    }
}

/**
 * Purpose: Draws a red rectangle overlay on the camera preview around the detected barcode.
 * @param rect: Rect - Bounding box of the detected barcode (from ML Kit).
 */
@Composable
fun DrawRectangle(rect: Rect) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Convert the Android Rect to a Compose Rect for rendering
        val composeRect = rect.toComposeRect()

        // Draw the rectangle around the barcode
        drawRect(
            color = Color.Red,
            topLeft = Offset(composeRect.left, composeRect.top), // Offset represents xy coords in canvas
            size = Size(composeRect.width, composeRect.height),
            style = Stroke(width = 5f)
        )
    }
}