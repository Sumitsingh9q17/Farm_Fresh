package com.sumit.farm_fresh

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchResultsActivity : AppCompatActivity() {

    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var productAdapter: FeaturedProductAdapter
    private var searchResults: List<Product> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        // Initialize the RecyclerView
        resultsRecyclerView = findViewById(R.id.resultsRecyclerView)
        resultsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Get search query from intent
        val query = intent.getStringExtra("QUERY") ?: ""

        // Fetch the filtered product list directly from the database based on the search query
        searchResults = getSearcheddProducts(query)

        // Initialize and set the adapter
        productAdapter = FeaturedProductAdapter(searchResults) { product ->
            // When a product is clicked, navigate to ProductDetailActivity
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("PRODUCT", product)
            startActivity(intent)
        }
        resultsRecyclerView.adapter = productAdapter
    }

    // Function to fetch and filter products based on the search query directly from the database
    private fun getSearcheddProducts(query: String): List<Product> {
        val searchedProducts = mutableListOf<Product>()
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase

        // Log the search query to confirm it's correct
        Log.d("SearchQuery", "Searching for products with query: $query")

        // SQL query to filter products by name based on the search query
        val cursor = db.rawQuery(
            "SELECT * FROM Products WHERE name LIKE ? COLLATE NOCASE",
            arrayOf("%$query%") // Using LIKE to match products by name
        )

        // Check if the cursor is not null and contains any results
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex("id")
                val nameIndex = cursor.getColumnIndex("name")
                val priceIndex = cursor.getColumnIndex("price")
                val typeIndex = cursor.getColumnIndex("type")
                val imageUriIndex = cursor.getColumnIndex("image")
                val categoryIndex = cursor.getColumnIndex("category")

                // Check if the necessary columns exist
                if (idIndex >= 0 && nameIndex >= 0 && priceIndex >= 0 && typeIndex >= 0 && imageUriIndex >= 0 && categoryIndex >= 0) {
                    val id = cursor.getInt(idIndex)
                    val name = cursor.getString(nameIndex)
                    val price = cursor.getFloat(priceIndex)
                    val type = cursor.getString(typeIndex)
                    val imageUri = cursor.getString(imageUriIndex)
                    val category = cursor.getString(categoryIndex)

                    // Log the retrieved product name to confirm filtering
                    Log.d("SearchResult", "Found product: $name")

                    // Add the product to the list
                    searchedProducts.add(Product(id, name, price, type, imageUri, category))
                } else {
                    Log.e("DB Error", "One or more columns are missing in the cursor.")
                }
            } while (cursor.moveToNext())
        }

        // Close the cursor after use
        cursor?.close()

        // Log the number of products found
        Log.d("SearchResult", "Total products found: ${searchedProducts.size}")

        return searchedProducts
    }

}
