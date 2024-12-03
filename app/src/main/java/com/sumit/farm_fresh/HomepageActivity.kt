package com.sumit.farm_fresh

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Timer
import java.util.TimerTask

class HomepageActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var bannerViewPager: ViewPager2
    private lateinit var featuredRecyclerView: RecyclerView
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var voiceSearchButton: ImageView

    private val REQUEST_CODE_VOICE_SEARCH = 1001
    private var userEmail: String? = null  // Store the email


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        userEmail = intent.getStringExtra("COL_USER_EMAIL") ?: ""


        // Initialize Views
        searchView = findViewById(R.id.searchView)
        bannerViewPager = findViewById(R.id.bannerViewPager)
        featuredRecyclerView = findViewById(R.id.featuredRecyclerView)
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView)
        bottomNav = findViewById(R.id.bottomNav)
        voiceSearchButton = findViewById(R.id.voiceSearchBtn)

        // Set up Bottom Navigation
        setupBottomNavigation()

        setupBannerViewPager()
        setupFeaturedProductsRecyclerView()
        setupCategoryRecyclerView()

        setupSearchView()

        voiceSearchButton.setOnClickListener {
            if (isSpeechRecognitionAvailable()) {
                startVoiceSearch()
            } else {
                Toast.makeText(
                    this,
                    "Voice search is not supported on this device.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Handle text search query submission

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotBlank()) {
                        Toast.makeText(applicationContext, "Searching for: $it", Toast.LENGTH_SHORT)
                            .show()
                        val intent =
                            Intent(this@HomepageActivity, SearchResultsActivity::class.java)
                        intent.putExtra("QUERY", it)
                        startActivity(intent)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                updateMicVisibility(newText) // Optionally update mic button visibility
                return true
            }
        })

    }

    private fun setupSearchView() {
        // Collapse the search view when it loses focus (including when back is pressed)
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // When search bar loses focus, hide mic and collapse the search view
                voiceSearchButton.visibility = View.GONE
                searchView.setIconified(true)  // Collapse the search view back to the magnifying glass icon
            } else {
                // When search bar gains focus, show mic if query is empty
                val queryText = searchView.query.toString()
                if (queryText.isEmpty()) {
                    voiceSearchButton.visibility = View.VISIBLE
                }
            }
        }

        // Hide the mic button when the search view is collapsed (query text cleared or closed)
        searchView.setOnCloseListener {
            voiceSearchButton.visibility = View.GONE
            true
        }
    }

    // Ensure the search view collapses when back button is pressed
    override fun onBackPressed() {
        if (searchView.isFocused) {
            // If the SearchView is focused, collapse it
            searchView.setIconified(true)
        } else {
            super.onBackPressed()  // If not focused, proceed as usual
        }
    }

    private fun updateMicVisibility(queryText: String?) {
        // Show mic if query is empty and hide it when typing starts
        if (queryText.isNullOrEmpty()) {
            voiceSearchButton.visibility = View.VISIBLE
        } else {
            voiceSearchButton.visibility = View.GONE
        }
    }

    private fun isSpeechRecognitionAvailable(): Boolean {
        val packageManager = packageManager
        val pm = packageManager
        val packageList =
            pm.queryIntentActivities(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0)
        return packageList.isNotEmpty()
    }

    private fun startVoiceSearch() {
        val voiceIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        voiceIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something to search")
        startActivityForResult(voiceIntent, REQUEST_CODE_VOICE_SEARCH)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_VOICE_SEARCH && resultCode == RESULT_OK && data != null) {
            val results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = results?.get(0) // Most accurate voice result
            if (spokenText.isNullOrEmpty()) {
                Toast.makeText(this, "No speech input detected.", Toast.LENGTH_SHORT).show()
            } else {
                searchView.setQuery(spokenText, false)
            }
        }
    }

    // Set up the banner view pager with auto-scrolling
    private fun setupBannerViewPager() {
        val bannerImages = listOf(
            R.drawable.banner1, // Replace with your own drawable resource
            R.drawable.banner2,
            R.drawable.banner3,
            R.drawable.banner4,
            R.drawable.banner5
        )

        val bannerAdapter = BannerAdapter(bannerImages)
        bannerViewPager.adapter = bannerAdapter

        // Auto-scroll the banners
        val handler = Handler(mainLooper)
        val update = Runnable {
            val currentItem = bannerViewPager.currentItem
            val nextItem = (currentItem + 1) % bannerImages.size
            bannerViewPager.setCurrentItem(nextItem, true)
        }

        val delay = 3000L
        handler.postDelayed(update, delay)

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, delay, delay)
    }

    // Set up the featured products recycler view
    private fun setupFeaturedProductsRecyclerView() {
        featuredRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val products = getFeaturedProducts()

        val adapter = FAdapter(products)
        featuredRecyclerView.adapter = adapter
    }

    // Fetch featured products from the database
    private fun getFeaturedProducts(): List<Product> {
        val featuredProducts = mutableListOf<Product>()
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase

        // Query to get featured products
        val cursor = db.rawQuery("SELECT * FROM Products WHERE category = 'featured'", null)

        // Check if cursor is not null
        if (cursor != null) {
            // Log the column names to verify the schema
            val columnCount = cursor.columnCount
            for (i in 0 until columnCount) {
                Log.d("DBColumns", "Column ${i}: ${cursor.getColumnName(i)}")
            }

            // Check if cursor has data
            if (cursor.moveToFirst()) {
                do {
                    // Get column indexes
                    val idIndex = cursor.getColumnIndex("id")
                    val nameIndex = cursor.getColumnIndex("name")
                    val priceIndex = cursor.getColumnIndex("price")
                    val typeIndex = cursor.getColumnIndex("type")
                    val imageIndex =
                        cursor.getColumnIndex("image") // Change this from imageUri to image
                    val categoryIndex = cursor.getColumnIndex("category")

                    // Check if the columns exist
                    if (idIndex >= 0 && nameIndex >= 0 && priceIndex >= 0 && typeIndex >= 0 && imageIndex >= 0 && categoryIndex >= 0) {
                        val id = cursor.getInt(idIndex)
                        val name = cursor.getString(nameIndex)
                        val price = cursor.getFloat(priceIndex)
                        val type = cursor.getString(typeIndex)
                        val image = cursor.getString(imageIndex) // Use image instead of imageUri
                        val category = cursor.getString(categoryIndex)

                        // Add the product to the list
                        featuredProducts.add(Product(id, name, price, type, image, category))
                    } else {
                        Log.e("DB Error", "One or more columns are missing in the cursor.")
                    }
                } while (cursor.moveToNext())
            } else {
                Log.e("FeaturedProducts", "No featured products found in the database.")
            }
        } else {
            Log.e("FeaturedProducts", "Failed to query the database. Cursor is null.")
        }

        cursor.close()  // Always close the cursor
        db.close()  // Close the database
        return featuredProducts
    }


    // Set up the category recycler view
    private fun setupCategoryRecyclerView() {
        val categories = getCategories()

        categoryRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = CategoryAdapter(categories) { selectedCategory ->
            // When a category is clicked, start the ProductsActivity and pass the category name
            val intent = Intent(this, ProductsActivity::class.java).apply {
                putExtra("CATEGORY_NAME", selectedCategory.name)  // Pass the selected category name
            }
            startActivity(intent)
        }
        categoryRecyclerView.adapter = adapter
    }

    // Get the list of categories
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

    // Set up the bottom navigation
    private fun setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Already on the homepage
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
                    // Open profile activity if email is available
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
}
