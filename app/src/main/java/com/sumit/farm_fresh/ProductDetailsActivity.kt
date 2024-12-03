package com.sumit.farm_fresh

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var productImage: ImageView
    private lateinit var productName: TextView
    private lateinit var productPrice: TextView
    private lateinit var productType: TextView
    private lateinit var productCategory: TextView
    private lateinit var addToCartButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        // Initialize Views
        productImage = findViewById(R.id.productImage)
        productName = findViewById(R.id.productName)
        productPrice = findViewById(R.id.productPrice)
        productType = findViewById(R.id.productType)
        productCategory = findViewById(R.id.productCategory)
        addToCartButton = findViewById(R.id.addToCartButton)

        // Get product data from the Intent
        val product = intent.getParcelableExtra<Product>("PRODUCT")

        // If product is not null, display its details
        product?.let {
            // Load image using Glide
            Glide.with(this)
                .load(it.imageUri)
                .into(productImage)

            productName.text = it.name
            productPrice.text = "₹${it.price}"
            productType.text = "Type: ${it.type}"
            productCategory.text = "Category: ${it.category}"
        } ?: run {
            // Handle case where product is null (e.g., show error or go back)
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show()
            finish()  // Finish activity if no product is found
        }

        // Handle "Add to Cart" button click
        addToCartButton.setOnClickListener {
            product?.let { nonNullProduct ->
                // Use the CartManager singleton to add product to the cart
                CartManager.addToCart(nonNullProduct)
                Toast.makeText(this, "${nonNullProduct.name} added to cart", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
