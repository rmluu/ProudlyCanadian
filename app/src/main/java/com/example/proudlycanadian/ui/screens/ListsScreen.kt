package com.example.proudlycanadian.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.proudlycanadian.ui.navigation.BottomNav

// Temporary classes to hold test data
data class Product(val name: String, val company: String, val origin: String, val upc: Long)
data class Collection(val name: String, val products: List<Product>)

/**
 * ListsScreen - Allows users to view and manage their custom product lists.
 * Users can create new lists and view products stored within them.
 */
@Composable
fun ListsScreen() {
    // Placeholder test data
    val collections = listOf(
        Collection(
            "Liquids", listOf(
                Product("Water", "Loblaws Inc.", "Canada", 111111111111),
                Product("Ketchup", "Kraft Heinz Canada ULC", "Canada", 222222222222),
                Product("Mustard", "Kraft Heinz Canada ULC", "Canada", 333333333333)
            )
        ),
        Collection(
            "Morning Essentials", listOf(
                Product("Starbucks Coffee", "Tropicana Products, Inc.", "US", 444444444444),
                Product("Hashbrowns", "McCain Foods USA, Inc.", "US", 555555555555)
            )
        )
    )

    val expandedCollection = remember { mutableStateOf<Collection?>(null) }

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 18.dp)
            ) {
                // Display collections
                collections.forEach { collection ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFe7f3ff))
                            .padding(6.dp)
                            .clickable {
                                // Allows one collection to be expanded at a time. Clicking an expanded collection collapses it.
                                expandedCollection.value =
                                    if (expandedCollection.value == collection) null else collection
                            },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Collection info
                        Text(text = collection.name, fontSize = 18.sp)
                        Text(text = "(${collection.products.size})", fontSize = 18.sp)
                    }

                    // Show products only if the current collection is expanded
                    if (expandedCollection.value == collection) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            collection.products.forEach { product ->
                                ProductCard(product)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.weight(1f))

                // Button for creating new list
                Button(
                    onClick = { /* Handle new list creation */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Create a new list", fontSize = 18.sp)
                }
            }
        }
    )
}

@Composable
fun ProductCard(product: Product) {
    // Display each product in a card
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = "Product: ${product.name}", fontSize = 16.sp)
            Text(text = "Company: ${product.company}", fontSize = 16.sp)
            Text(text = "Origin: ${product.origin}", fontSize = 16.sp)
            Text(text = "UPC: ${product.upc}", fontSize = 16.sp)
        }
    }
}