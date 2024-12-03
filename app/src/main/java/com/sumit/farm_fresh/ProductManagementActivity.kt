package com.sumit.farm_fresh

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ProductManagementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_management)

        // Get the isAdmin flag from the Intent (passed from AdminDashboardActivity)

        val isAdmin = intent.getBooleanExtra("isAdmin", false)
        Log.d("ProductManagementActivity", "Passing isAdmin flag: $isAdmin")


        val addButton = findViewById<Button>(R.id.btn_add)
        val viewButton = findViewById<Button>(R.id.btn_view)

        // Passing isAdmin flag to AddProductActivity
        addButton.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            intent.putExtra("isAdmin", isAdmin) // Pass the isAdmin flag
            startActivity(intent)
        }

        // Passing isAdmin flag to ViewProductsActivity
        viewButton.setOnClickListener {
            val intent = Intent(this, ViewProductsActivity::class.java)
            intent.putExtra("isAdmin", isAdmin) // Pass the isAdmin flag
            startActivity(intent)
        }
    }
}
