package com.sumit.farm_fresh

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddProductActivity : AppCompatActivity() {
    private lateinit var etProductName: EditText
    private lateinit var etProductPrice: EditText
    private lateinit var spinnerProductType: Spinner
    private lateinit var spinnerProductCategory: Spinner
    private lateinit var btnChooseImage: Button
    private lateinit var ivProductImage: ImageView
    private lateinit var btnSaveProduct: Button
    private var productImageUri: Uri? = null
    private var isAdmin: Boolean = false  // Variable to store isAdmin flag

    private val IMAGE_PICK_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        // Retrieve the isAdmin flag passed from ProductManagementActivity
        isAdmin = intent.getBooleanExtra("isAdmin", false)

        etProductName = findViewById(R.id.et_product_name)
        etProductPrice = findViewById(R.id.et_product_price)
        spinnerProductType = findViewById(R.id.spinner_product_type)
        spinnerProductCategory = findViewById(R.id.spinner_product_category)
        btnChooseImage = findViewById(R.id.btn_choose_image)
        ivProductImage = findViewById(R.id.iv_product_image)
        btnSaveProduct = findViewById(R.id.btn_save_product)

        // If not an admin, disable the save button or show a message
        if (!isAdmin) {
            btnSaveProduct.isEnabled = false
            Toast.makeText(
                this,
                "You do not have admin privileges to add a product",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Set up product type spinner
        val types = arrayOf("Vegetarian", "Non-Vegetarian")
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProductType.adapter = typeAdapter

        // Set up product category spinner
        val categories = arrayOf(
            "Fruits",
            "Vegetables",
            "Dairy",
            "Grains",
            "Meat",
            "Eggs",
            "featured"
        )  // Sample categories
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProductCategory.adapter = categoryAdapter

        btnChooseImage.setOnClickListener {
            // Open image gallery to choose product image
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        btnSaveProduct.setOnClickListener {
            val productName = etProductName.text.toString().trim()
            val productPrice = etProductPrice.text.toString().trim()
            val productType = spinnerProductType.selectedItem.toString()
            val productCategory =
                spinnerProductCategory.selectedItem.toString()  // Get the selected category

            if (productName.isNotEmpty() && productPrice.isNotEmpty() && productImageUri != null) {
                saveProductToDatabase(
                    productName,
                    productPrice,
                    productType,
                    productCategory,
                    productImageUri!!
                )
            } else {
                Toast.makeText(
                    this,
                    "Please fill all fields and choose an image",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun saveProductToDatabase(
        name: String,
        price: String,
        type: String,
        category: String,
        imageUri: Uri
    ) {
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase

        val contentValues = ContentValues().apply {
            put("name", name)
            put("price", price.toFloat())
            put("type", type)
            put("category", category)  // Save category field
            put(
                "image",
                imageUri.toString()
            ) // Save the image URI or base64 string, depending on preference
        }

        val result = db.insert("Products", null, contentValues)
        if (result != -1L) {
            Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error adding product", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            productImageUri = data?.data
            ivProductImage.setImageURI(productImageUri) // Display the selected image
        }
    }
}
