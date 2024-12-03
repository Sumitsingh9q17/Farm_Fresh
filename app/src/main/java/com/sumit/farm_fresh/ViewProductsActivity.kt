package com.sumit.farm_fresh

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewProductsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productList: MutableList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_products)

        // Initialize the RecyclerView and set the LayoutManager
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the product list
        productList = ArrayList()

        // Get the isAdmin flag passed from the previous activity (e.g., ProductManagementActivity)
        val isAdmin = intent.getBooleanExtra("isAdmin", false) // Default is false if not passed
        Log.d("ViewProductsActivity", "isAdmin flag received: $isAdmin")


        // Initialize the product adapter and pass the isAdmin flag
        productAdapter = ProductAdapter(productList, object : ProductAdapter.OnItemClickListener {
            override fun onEditClick(position: Int) {
                val product = productList[position]
                val intent = Intent(this@ViewProductsActivity, EditProductActivity::class.java)
                intent.putExtra("product_id", product.id)
                startActivity(intent)
            }

            override fun onDeleteClick(position: Int) {
                val product = productList[position]
                deleteProduct(product.id, position)
            }
        }, isAdmin) // Pass isAdmin to the adapter

        // Set the adapter to the RecyclerView
        recyclerView.adapter = productAdapter

        // Load products from the database
        loadProducts()
    }

    private fun loadProducts() {
        // Initialize the DatabaseHelper and get the readable database
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Products", null)

        // Check if the cursor is valid and contains data
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Get column indices for product details
                val idIndex = cursor.getColumnIndex("id")
                val nameIndex = cursor.getColumnIndex("name")
                val priceIndex = cursor.getColumnIndex("price")
                val typeIndex = cursor.getColumnIndex("type")
                val categoryIndex = cursor.getColumnIndex("category")
                val imageUriIndex = cursor.getColumnIndex("image")

                // Check if the column indices are valid
                if (idIndex >= 0 && nameIndex >= 0 && priceIndex >= 0 && typeIndex >= 0 && imageUriIndex >= 0) {
                    // Get product details from the cursor
                    val id = cursor.getInt(idIndex)
                    val name = cursor.getString(nameIndex)
                    val price = cursor.getFloat(priceIndex)
                    val type = cursor.getString(typeIndex)
                    val category = cursor.getString(categoryIndex)
                    val imageUri = cursor.getString(imageUriIndex)

                    // Add the product to the product list
                    productList.add(Product(id, name, price, type, imageUri, category))
                } else {
                    // Log and display an error if columns are missing
                    Log.e("DB Error", "One or more columns are missing in the cursor.")
                    Toast.makeText(this, "Error loading product data.", Toast.LENGTH_SHORT).show()
                }
            } while (cursor.moveToNext())
        }

        // Close the cursor
        cursor?.close()

        // Notify the adapter that data has changed
        if (productList.isNotEmpty()) {
            productAdapter.notifyDataSetChanged()
        } else {
            Log.e("ViewProductsActivity", "Product list is empty.")
            Toast.makeText(this, "No products found.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteProduct(productId: Int, position: Int) {
        // Initialize the DatabaseHelper and get the writable database
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase
        val result = db.delete("Products", "id=?", arrayOf(productId.toString()))

        // Check if the product deletion was successful
        if (result > 0) {
            // Remove the product from the list and notify the adapter
            productList.removeAt(position)
            productAdapter.notifyItemRemoved(position)
            Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error deleting product", Toast.LENGTH_SHORT).show()
        }
    }
}
