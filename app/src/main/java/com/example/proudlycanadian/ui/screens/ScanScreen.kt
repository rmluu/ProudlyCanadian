package com.example.proudlycanadian.ui.screens

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

/**
 * ScanScreen - Allows users to scan a barcode or manually enter a UPC number.
 * API integration (to be added later) will fetch product details and determine if the
 * product is Canadian in origin.
 */
@Composable
fun ScanScreen()
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Barcode Image
        Image(
            painter = painterResource(id = R.mipmap.ic_barcode),
            contentDescription = "Barcode Image",
            modifier = Modifier.size(200.dp)
        )

        // Scan UPC button
        Button(
            onClick = { /* Handle UPC scan */ },
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