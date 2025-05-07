package com.example.proudlycanadian.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Purpose: Represents a product in Firestore.
 */
data class FirestoreProduct(
    val name: String,
    val upc: String,
    val image: String,
    val origin: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(upc)
        parcel.writeString(image)
        parcel.writeString(origin)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<FirestoreProduct> {
            override fun createFromParcel(parcel: Parcel): FirestoreProduct {
                return FirestoreProduct(parcel)
            }

            override fun newArray(size: Int): Array<FirestoreProduct?> {
                return arrayOfNulls(size)
            }
        }
    }
}