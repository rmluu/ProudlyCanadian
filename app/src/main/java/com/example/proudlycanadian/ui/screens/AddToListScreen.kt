package com.example.proudlycanadian.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proudlycanadian.api.model.FirestoreProduct
import com.google.firebase.firestore.FirebaseFirestore
import com.example.proudlycanadian.viewmodel.ListViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * Purpose: Displays a screen for adding a scanned product to a Firestore collection.
 * @param navController: NavController - used for navigating back after adding the product.
 * @param upc: String? - UPC code of the scanned product.
 * @param name: String? - Name of the scanned product.
 * @param image: String? - Image URL of the scanned product.
 * @param origin: String? - Origin country of the scanned product.
 * @param viewModel: ListViewModel - ViewModel to manage Firestore collections.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToListScreen(
    navController: NavController,
    upc: String?,
    name: String?,
    image: String?,
    origin: String?,
    viewModel: ListViewModel = viewModel()
) {
    val initialName = name ?: ""
    val upcValue = upc ?: "N/A"
    val originValue = origin ?: "Unknown"
    val imageUrl = image ?: ""

    var productName by remember { mutableStateOf(initialName) }
    var selectedCollection by remember { mutableStateOf("") }
    var newCollectionName by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var isCreatingNew by remember { mutableStateOf(false) }
    val collections = viewModel.collections
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Add Product to a Collection", fontSize = 18.sp)

        Spacer(Modifier.height(16.dp))

        // Product Image
        AsyncImage(
            model = imageUrl,
            contentDescription = "Product Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(16.dp))

        // Editable product name field
        OutlinedTextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Product Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // Product info
        Text("UPC: $upcValue",
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 16.dp))
        Text("Origin: $originValue",
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 16.dp))

        Spacer(Modifier.height(16.dp))

        // Toggle between existing or new list
        Text("Choose List Option:", fontSize = 16.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = !isCreatingNew,
                onClick = { isCreatingNew = false }
            )
            Text("Select Existing", modifier = Modifier.padding(end = 16.dp))

            RadioButton(
                selected = isCreatingNew,
                onClick = { isCreatingNew = true }
            )
            Text("Create New")
        }

        Spacer(Modifier.height(8.dp))

        if (isCreatingNew) {
            // Input new collection name
            OutlinedTextField(
                value = newCollectionName,
                onValueChange = { newCollectionName = it },
                label = { Text("New Collection Name") },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            // Select existing collection
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = selectedCollection,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Collection") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .clickable { expanded = !expanded }
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    collections.forEach { collection ->
                        DropdownMenuItem(
                            text = { Text(collection.name) },
                            onClick = {
                                selectedCollection = collection.name
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Add product to selected or new collection in Firestore
        Button(
            onClick = {
                val collectionName = if (isCreatingNew) newCollectionName else selectedCollection
                if (collectionName.isBlank()) return@Button

                val product = FirestoreProduct(
                    name = productName,
                    upc = upcValue,
                    image = imageUrl,
                    origin = originValue
                )

                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId == null) return@Button

                val db = FirebaseFirestore.getInstance()
                val collectionRef = db.collection("users")
                    .document(userId)
                    .collection("collections")
                    .document(collectionName)

                // Update existing collection
                collectionRef.get().addOnSuccessListener { document ->
                    val currentProducts = (document["products"] as? List<Map<String, Any>>)?.toMutableList() ?: mutableListOf()

                    // Check if UPC already exists in the collection
                    val duplicate = currentProducts.any { it["upc"] == product.upc }
                    if (duplicate) {
                        Toast.makeText(context, "Product already exists in the collection!", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener  // Stop further processing
                    }

                    // Otherwise, add new product
                    val newProductMap = mapOf(
                        "name" to product.name,
                        "image" to product.image,
                        "upc" to product.upc,
                        "origin" to product.origin
                    )
                    currentProducts.add(newProductMap)

                    collectionRef.set(
                        mapOf("name" to collectionName, "products" to currentProducts)
                    ).addOnSuccessListener {
                        viewModel.fetchCollectionsFromFirestore()
                        navController.popBackStack()

                        Toast.makeText(context, "Successfully added to collection", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    // Create new collection if it doesn't exist
                    val newList = mapOf(
                        "name" to collectionName,
                        "products" to listOf(
                            mapOf(
                                "name" to product.name,
                                "image" to product.image,
                                "upc" to product.upc,
                                "origin" to product.origin
                            )
                        )
                    )
                    collectionRef.set(newList).addOnSuccessListener {
                        navController.popBackStack()
                    }
                }
            },
            enabled = if (isCreatingNew) newCollectionName.isNotBlank() else selectedCollection.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                disabledContainerColor = Color.LightGray,
                disabledContentColor = Color.DarkGray
            )
        ) {
            Text("Add to ${if (isCreatingNew) newCollectionName else selectedCollection}",  fontSize = 18.sp)
        }
    }
}