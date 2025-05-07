package com.example.proudlycanadian.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proudlycanadian.model.Collection
import com.example.proudlycanadian.model.FirestoreProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Purpose: ViewModel to manage Firestore collections of products for the current user.
 */
class ListViewModel : ViewModel() {
    var collections by mutableStateOf<List<Collection>>(emptyList())
        private set

    // Firebase instance
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    init {
        fetchCollectionsFromFirestore()
    }

    /**
     * Purpose: Fetch collections belonging to the currently authenticated user.
     */
    fun fetchCollectionsFromFirestore() {
        val userId = auth.currentUser?.uid ?: return // Ensure the user is authenticated

        val collectionsRef = db.collection("users") // Root collection is "users"
            .document(userId)
            .collection("collections")

        collectionsRef.get().addOnSuccessListener { querySnapshot ->
            val fetchedCollections = querySnapshot.documents.map { document ->
                val name = document.getString("name") ?: "Unnamed Collection"

                val productList = (document["products"] as? List<Map<String, Any>>)?.map { productMap ->
                    FirestoreProduct(
                        name = productMap["name"] as? String ?: "",
                        image = productMap["image"] as? String ?: "",
                        upc = productMap["upc"] as? String ?: "",
                        origin = productMap["origin"] as? String ?: ""
                    )
                } ?: listOf()

                Collection(name = name, products = productList)
            }
            collections = fetchedCollections
        }
    }

    /**
     * Purpose: Add a new product to an existing collection for the current user.
     * @param collectionName: String - The name of the collection to add the product to.
     * @param product: FirestoreProduct - The product to be added.
     */
    fun addProductToCollection(collectionName: String, product: FirestoreProduct) {
        val userId = auth.currentUser?.uid ?: return // Ensure the user is authenticated

        val collectionRef = db.collection("users")
            .document(userId) // User document
            .collection("collections")
            .document(collectionName)

        collectionRef.get().addOnSuccessListener { document ->
            val currentProducts = (document["products"] as? List<Map<String, Any>>)?.toMutableList()
                ?: mutableListOf()

            val newProductMap = mapOf(
                "name" to product.name,
                "image" to product.image,
                "upc" to product.upc,
                "origin" to product.origin
            )

            currentProducts.add(newProductMap)

            collectionRef.update("products", currentProducts)
                .addOnSuccessListener {
                    // Re-fetch collections to update the UI after adding the product
                    fetchCollectionsFromFirestore()
                }
        }
    }

    /**
     * Purpose: Delete a product from a specific user's collection.
     * @param collectionName: String - The name of the collection to delete from.
     * @param product: FirestoreProduct - The product to be removed.
     */
    fun deleteProductFromCollection(collectionName: String, product: FirestoreProduct, context: Context) {
        val userId = auth.currentUser?.uid ?: return // Ensure the user is authenticated

        val collectionRef = db.collection("users")
            .document(userId)
            .collection("collections")
            .document(collectionName)

        collectionRef.get().addOnSuccessListener { document ->
            // Get the current products list
            val currentProducts = (document["products"] as? List<Map<String, Any>>)?.toMutableList() ?: mutableListOf()

            // Find and remove the product
            val productToRemove = currentProducts.find {
                it["upc"] == product.upc // Matching by UPC, could be other unique identifier
            }

            if (productToRemove != null) {
                currentProducts.remove(productToRemove)

                if (currentProducts.isEmpty()) {
                    // Delete the collection from Firestore if empty
                    collectionRef.delete()
                        .addOnSuccessListener {
                            // Re-fetch collections to update the UI after deletion
                            fetchCollectionsFromFirestore()

                            // Show confirmation Toast for deleting the collection
                            Toast.makeText(context, "Collection deleted as it became empty.", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            // Handle failure if necessary (e.g., show a Toast with error message)
                            Toast.makeText(context, "Failed to delete collection: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Update the collection with the new list
                    collectionRef.update("products", currentProducts)
                        .addOnSuccessListener {
                            // Re-fetch collections to update the UI after deletion
                            fetchCollectionsFromFirestore()
                            Toast.makeText(
                                context,
                                "Product deleted successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                context,
                                "Failed to delete product: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
        }
    }
}