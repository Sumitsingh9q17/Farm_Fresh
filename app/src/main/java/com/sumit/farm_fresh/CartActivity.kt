package com.sumit.farm_fresh

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class CartActivity : AppCompatActivity() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var totalPriceTextView: TextView
    private lateinit var checkoutButton: Button
    private lateinit var cartAdapter: CartAdapter
    private var userEmail: String? = null  // Store the email


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Initialize views
        cartRecyclerView = findViewById(R.id.cartRecyclerView)
        totalPriceTextView = findViewById(R.id.totalPriceTextView)
        checkoutButton = findViewById(R.id.checkoutButton)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        userEmail = intent.getStringExtra("COL_USER_EMAIL") ?: ""


        // Set up RecyclerView
        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(CartManager.getCartItems().toMutableList())
        cartRecyclerView.adapter = cartAdapter

        // Update total price
        updateTotalPrice()

        // Handle checkout button click
        checkoutButton.setOnClickListener {
            // Get the total price of the cart
            val totalPrice = CartManager.getTotalPrice()

            // Create an Intent to navigate to PaymentActivity
            val intent = Intent(this, UserDetailsA::class.java)

            // Pass the total price as an extra
            intent.putExtra("TOTAL_PRICE", totalPrice)

            // Start the PaymentActivity
            startActivity(intent)
        }

        // Set selected item in BottomNavigationView
        bottomNav.selectedItemId = R.id.nav_cart

        // Handle navigation bar item clicks
        bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    if (userEmail != null) {
                        val intent = Intent(this, HomepageActivity::class.java)
                        intent.putExtra("COL_USER_EMAIL", userEmail)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show()
                    }
                    true
                }

                R.id.nav_product -> {
                    if (userEmail != null) {
                        val intent = Intent(this, ProductsActivity::class.java)
                        intent.putExtra("COL_USER_EMAIL", userEmail)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show()
                    }
                    true
                }

                R.id.nav_cart -> true // Stay in Cart
                R.id.nav_profile -> {
                    if (userEmail != null) {
                        val intent = Intent(this, UserProfileActivity::class.java)
                        intent.putExtra("COL_USER_EMAIL", userEmail)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show()
                    }
                    true
                }

                else -> false
            }
        }
    }

    // Refresh the cart and update total price
    fun refreshCart() {
        cartAdapter.updateCartItems(CartManager.getCartItems().toMutableList())
        updateTotalPrice()
    }

    // Update the total price of items in the cart
    private fun updateTotalPrice() {
        val totalPrice = CartManager.getTotalPrice()
        totalPriceTextView.text = "Total: ₹${"%.2f".format(totalPrice)}"
    }
}
