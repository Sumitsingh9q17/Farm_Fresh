package com.sumit.farm_fresh

import android.os.Parcel
import android.os.Parcelable

data class CartItem(
    val product: Product, // The product being added to the cart
    var quantity: Int = 1 // The quantity of the product in the cart
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Product::class.java.classLoader) ?: Product(0, "", 0.0f, "", "", ""),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(product, flags)  // Write the product object
        parcel.writeInt(quantity)  // Write quantity
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartItem> {
        override fun createFromParcel(parcel: Parcel): CartItem {
            return CartItem(parcel)
        }

        override fun newArray(size: Int): Array<CartItem?> {
            return arrayOfNulls(size)
        }
    }
}
