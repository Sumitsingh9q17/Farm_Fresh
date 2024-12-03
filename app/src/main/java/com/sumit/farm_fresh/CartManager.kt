package com.sumit.farm_fresh

import android.util.Log

object CartManager {
    private val cartItems = mutableListOf<CartItem>()

    // Add a product to the cart
    fun addToCart(product: Product) {
        val existingItem = cartItems.find { it.product.id == product.id }
        if (existingItem != null) {
            existingItem.quantity += 1
            Log.d(
                "CartManager",
                "Increased quantity for product: ${product.name}, New quantity: ${existingItem.quantity}"
            )
        } else {
            cartItems.add(CartItem(product, 1))
            Log.d("CartManager", "Product added: ${product.name}, Cart size: ${cartItems.size}")
        }
    }

    // Remove a product from the cart
    fun removeProduct(productId: Int) {
        cartItems.removeAll { it.product.id == productId }
        Log.d("CartManager", "Product removed. Cart size: ${cartItems.size}")
    }

    // Update product quantity
    fun updateQuantity(productId: Int, newQuantity: Int) {
        val cartItem = cartItems.find { it.product.id == productId }
        if (cartItem != null) {
            Log.d(
                "CartManager",
                "Updating quantity for product ${cartItem.product.name}: Old quantity = ${cartItem.quantity}, New quantity = $newQuantity"
            )
            if (newQuantity > 0) {
                cartItem.quantity = newQuantity
            } else {
                removeProduct(productId) // Remove product if quantity is zero
            }
        }
    }

    // Clear all items from the cart
    fun clearCart() {
        cartItems.clear()
        Log.d("CartManager", "Cart cleared")
    }

    // Get all cart items
    fun getCartItems(): List<CartItem> {
        Log.d(
            "CartManager",
            "Getting cart items: ${cartItems.map { it.product.name + " x" + it.quantity }}"
        )
        return cartItems.toList() // Return a copy to prevent direct modification
    }

    // Calculate the total price of all items in the cart
    fun getTotalPrice(): Float {
        val totalPrice =
            cartItems.sumByDouble { it.product.price * it.quantity.toDouble() }.toFloat()
        Log.d("CartManager", "Total price: ₹${"%.2f".format(totalPrice)}")
        return totalPrice
    }
}
