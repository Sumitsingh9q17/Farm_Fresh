package com.sumit.farm_fresh

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductsActivity : AppCompatActivity() {

    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var productsRecyclerView: RecyclerView
    private lateinit var categoryAdapter: pcategoryadapter
    private lateinit var productAdapter: pproductadapter
    private lateinit var categoryList: List<Category>
    private var productList: List<Product> = listOf() // Initialize as empty list
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var drawerLayout: DrawerLayout
    private var userEmail: String? = null  // Store the email


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        val categoryName = intent.getStringExtra("CATEGORY_NAME")
        userEmail = intent.getStringExtra("COL_USER_EMAIL") ?: ""


        // Initialize Views
        categoriesRecyclerView = findViewById(R.id.categoryRecyclerView)
        productsRecyclerView = findViewById(R.id.productsRecyclerView)
        bottomNav = findViewById(R.id.bottomNavigationView)
        drawerLayout = findViewById(R.id.drawerLayout)

        // Set up the Category RecyclerView
        setupCategoryRecyclerView()

        // Set up the Product RecyclerView
        setupProductRecyclerView()

        // Set up Bottom Navigation
        setupBottomNavigation()

        // Load products for the default category ("Vegetables") or selected category
        categoryName?.let {
            loadProducts(it)
        } ?: run {
            loadProducts("Vegetables")
        }
    }

    private fun setupCategoryRecyclerView() {
        categoryList = getCategories() // Fetch static categories
        categoryAdapter = pcategoryadapter(categoryList) { category ->
            // Update products for selected category
            loadProducts(category.name)
            drawerLayout.closeDrawers() // Close drawer after category selection
        }

        categoriesRecyclerView.layoutManager = LinearLayoutManager(this)
        categoriesRecyclerView.adapter = categoryAdapter
    }

    private fun setupProductRecyclerView() {
        productAdapter =
            pproductadapter(productList, Cart(), this)// Pass the cart here for adding products
        productsRecyclerView.layoutManager = GridLayoutManager(this, 2) // Display products in grid
        productsRecyclerView.adapter = productAdapter
    }

    private fun setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
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
                    // Stay on the products page
                    true
                }

                R.id.nav_cart -> {
                    if (userEmail != null) {
                        val intent = Intent(this, CartActivity::class.java)
                        intent.putExtra("COL_USER_EMAIL", userEmail)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show()
                    }
                    true
                }

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

    // Function to load products based on selected category
    private fun loadProducts(category: String) {
        val dbHelper = DatabaseHelper(this)
        try {
            productList = dbHelper.getProductsByCategory(category)
            if (productList.isEmpty()) {
                // Show a message if no products are found for the category
                Toast.makeText(this, "No products available in this category", Toast.LENGTH_SHORT)
                    .show()
            }
            productAdapter.updateProductList(productList)
        } catch (e: Exception) {
            Log.e("ProductsActivity", "Error loading products for category $category", e)
            // Show a user-friendly error message
            Toast.makeText(this, "Error loading products. Please try again.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    // Function to fetch the categories (static for now, could be dynamic in the future)
    private fun getCategories(): List<Category> {
        return listOf(
            Category("Vegetables", R.drawable.vegetables),
            Category("Fruits", R.drawable.fruits),
            Category("Dairy", R.drawable.dairy),
            Category("Grains", R.drawable.grains),
            Category("Meat", R.drawable.meat),
            Category("Eggs", R.drawable.eggs)
        )
    }
}
