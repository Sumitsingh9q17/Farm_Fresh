package com.sumit.farm_fresh

class Cart {
    private val items = mutableListOf<Product>()

    // Add a product to the cart
    fun addProduct(product: Product) {
        items.add(product)
    }

    // Get the list of products in the cart
    fun getProducts(): List<Product> {
        return items
    }

    // Get the total price of the products in the cart
    fun getTotalPrice(): Float {
        // Using fold or sumByDouble to handle the sum correctly
        return items.sumByDouble { it.price.toDouble() }.toFloat()
    }
}
