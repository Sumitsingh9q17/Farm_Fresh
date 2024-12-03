package com.sumit.farm_fresh

import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditProductActivity : AppCompatActivity() {

    private lateinit var etProductName: EditText
    private lateinit var etProductPrice: EditText
    private lateinit var spinnerProductType: Spinner
    private lateinit var spinnerProductCategory: Spinner
    private lateinit var ivProductImage: ImageView
    private lateinit var btnSaveProduct: Button

    private var productId: Int = -1
    private var imageUri: Uri? = null

    private val REQUEST_CODE_PICK_IMAGE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        etProductName = findViewById(R.id.et_product_name)
        etProductPrice = findViewById(R.id.et_product_price)
        spinnerProductType = findViewById(R.id.spinner_product_type)
        spinnerProductCategory = findViewById(R.id.spinner_product_category)
        ivProductImage = findViewById(R.id.iv_product_image)
        btnSaveProduct = findViewById(R.id.btn_save_product)



        productId = intent.getIntExtra("product_id", -1)
        val productTypes = listOf("Vegetarian", "Non-Vegetarian")
        val productCategories = listOf("Fruits", "Vegetables", "Dairy", "Grains", "featured")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, productTypes)
        val categoryAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, productCategories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerProductType.adapter = adapter
        spinnerProductCategory.adapter = categoryAdapter

        if (productId != -1) {
            loadProductDetails()
        }

        // Open image picker when ImageView is clicked
        ivProductImage.setOnClickListener {
            openImagePicker()
        }

        // Save the product details when Save button is clicked
        btnSaveProduct.setOnClickListener {
            val productName = etProductName.text.toString().trim()
            val productPrice = etProductPrice.text.toString().trim()
            val productType = spinnerProductType.selectedItem.toString()
            val productCategory = spinnerProductCategory.selectedItem.toString()

            if (productName.isNotEmpty() && productPrice.isNotEmpty()) {
                updateProductInDatabase(productName, productPrice, productType, productCategory)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadProductDetails() {
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Products WHERE id=?", arrayOf(productId.toString()))

        if (cursor != null && cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex("name")
            val priceIndex = cursor.getColumnIndex("price")
            val typeIndex = cursor.getColumnIndex("type")
            val categoryIndex = cursor.getColumnIndex("category")
            val imageUriIndex = cursor.getColumnIndex("image")

            if (nameIndex >= 0 && priceIndex >= 0 && typeIndex >= 0 && imageUriIndex >= 0) {
                val name = cursor.getString(nameIndex)
                val price = cursor.getFloat(priceIndex)
                val type = cursor.getString(typeIndex)
                val category = cursor.getString(categoryIndex)
                val imageUriString = cursor.getString(imageUriIndex)

                etProductName.setText(name)
                etProductPrice.setText(price.toString())
                spinnerProductType.setSelection(if (type == "Vegetarian") 0 else 1)
                val productCategories =
                    listOf("Fruits", "Vegetables", "Dairy", "Grains", "featured")
                spinnerProductCategory.setSelection(productCategories.indexOf(category))

                // Load the image URI properly with ContentResolver
                val imageUri = Uri.parse(imageUriString)
                try {
                    // Use ImageDecoder for Android 10 and above
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                        val source = ImageDecoder.createSource(this.contentResolver, imageUri)
                        val drawable = ImageDecoder.decodeDrawable(source)
                        ivProductImage.setImageDrawable(drawable)
                    } else {
                        // Fallback for older versions using BitmapFactory
                        val inputStream = contentResolver.openInputStream(imageUri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        ivProductImage.setImageBitmap(bitmap)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(
                    this,
                    "Error: Some columns are missing in the database.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(this, "Product not found.", Toast.LENGTH_SHORT).show()
        }

        cursor.close()
    }

    private fun getCategoryPosition(category: String): Int {
        val Categories =
            listOf("Fruits", "Vegetables", "Dairy", "Grains", "Meat", "Eggs", "featured")
        return Categories.indexOf(category)
    }

    private fun updateProductInDatabase(
        name: String,
        price: String,
        type: String,
        category: String
    ) {
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.writableDatabase

        val contentValues = ContentValues().apply {
            put("name", name)
            put("price", price.toFloat())
            put("type", type)
            put("category", category)
            // Save the image URI in the database
            imageUri?.let {
                put("image", it.toString())
            }
        }

        val result = db.update("Products", contentValues, "id=?", arrayOf(productId.toString()))

        if (result > 0) {
            Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show()
            finish() // Close activity after successful update
        } else {
            Toast.makeText(this, "Error updating product", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"  // Allow image selection
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)  // Grant permission to read the image
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            val selectedUri = data?.data
            if (selectedUri != null) {
                imageUri = selectedUri
                contentResolver.takePersistableUriPermission(
                    selectedUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                try {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                        val source = ImageDecoder.createSource(contentResolver, selectedUri)
                        val drawable = ImageDecoder.decodeDrawable(source)
                        ivProductImage.setImageDrawable(drawable)
                    } else {
                        val inputStream = contentResolver.openInputStream(selectedUri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        ivProductImage.setImageBitmap(bitmap)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}
