package com.example.proudlycanadian.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import coil.compose.AsyncImage
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proudlycanadian.viewmodel.ListViewModel
import com.example.proudlycanadian.model.Collection
import com.example.proudlycanadian.model.FirestoreProduct
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext

/**
 * Purpose: Displays a list of user-created product collections and their respective products.
 *          Users can expand a collection to view the products and delete products as needed.
 * @param viewModel: ListViewModel - Provides collection data and handles deletion.
 */
@Composable
fun ListsScreen(viewModel: ListViewModel = viewModel()) {
    var collections by remember { mutableStateOf<List<Collection>>(emptyList()) }
    val expandedCollection = remember { mutableStateOf<Collection?>(null) }
    val context = LocalContext.current

    // Update collections when the ViewModel's state changes.
    LaunchedEffect(Unit) {
        viewModel.fetchCollectionsFromFirestore()
    }

    // Whenever collections are fetched, update the local state
    LaunchedEffect(viewModel.collections) {
        collections = viewModel.collections
    }

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 18.dp)
            ) {
                // Display collections
                if (collections.isEmpty()) {
                    Text("No collections found or still loading...")
                } else {
                    collections.forEach { collection ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFB23D3D), shape = RoundedCornerShape(8.dp))
                                .padding(8.dp)
                                .clickable {
                                    // Allows one collection to be expanded at a time. Clicking an expanded collection collapses it.
                                    expandedCollection.value =
                                        if (expandedCollection.value == collection) null else collection
                                },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Collection info
                            Text(text = collection.name, fontSize = 18.sp, color = Color.White)
                            Text(text = "(${collection.products.size})", fontSize = 18.sp, color = Color.White)
                        }

                        // Show products only if the current collection is expanded
                        if (expandedCollection.value == collection) {
                            Column(modifier = Modifier.padding(4.dp)) {
                                collection.products.forEach { product ->
                                    ProductCard(
                                        product = product,
                                        collectionName = collection.name,
                                        onDelete = { collectionName, productToDelete ->
                                            viewModel.deleteProductFromCollection(
                                                collectionName,
                                                productToDelete,
                                                context)
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    )
}

/**
 * Purpose: Displays detailed information about a product inside a card.
 * @param product: FirestoreProduct - The product to display.
 * @param collectionName: String - The name of the collection the product belongs to.
 * @param onDelete: (String, FirestoreProduct) -> Unit - Callback to delete the product from the collection.
 */
@Composable
fun ProductCard(
    product: FirestoreProduct,
    collectionName: String,
    onDelete: (String, FirestoreProduct) -> Unit
) {
    val context = LocalContext.current

    // Display each product in a card
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFE4E4)
        ),
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = product.image,
                contentDescription = "Image of ${product.name}",
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .padding(end = 16.dp)
            )
            Column {
                Text(text = "Product: ${product.name}", fontSize = 16.sp)
                Text(text = "UPC: ${product.upc}", fontSize = 16.sp)

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Origin: ${product.origin}",
                        fontSize = 16.sp,
                        color = if (product.origin == "US") Color(0xFFC62828) else Color.Unspecified
                    )
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete ${product.name}",
                        modifier = Modifier.clickable {
                            onDelete(collectionName, product)
                        },
                    )
                }
            }
        }
    }
}