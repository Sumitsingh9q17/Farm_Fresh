package com.sumit.farm_fresh

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AdminDashboardActivity : AppCompatActivity() {
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        logoutButton = findViewById(R.id.logoutButton)


        val productManagementButton = findViewById<Button>(R.id.btn_manage_products)
        val userManagementButton = findViewById<Button>(R.id.btn_manage_users)

        // Get the isAdmin flag from the Intent
        val isAdmin = intent.getBooleanExtra("isAdmin", false)

        productManagementButton.setOnClickListener {
            val intent = Intent(this, ProductManagementActivity::class.java)
            intent.putExtra("isAdmin", true) // Pass the isAdmin flag
            startActivity(intent)
        }

        userManagementButton.setOnClickListener {
            val intent = Intent(this, UserManagementActivity::class.java)
            intent.putExtra("isAdmin", isAdmin) // Pass the isAdmin flag
            startActivity(intent)
        }
        logoutButton.setOnClickListener {
            // Clear user session or preferences
            logoutUser()
        }
    }

    private fun logoutUser() {
        // Clear user session, shared preferences, or database as needed
        val sharedPreferences = getSharedPreferences("user_profile", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()  // Clears all user data
        editor.apply()

        // Redirect to login or home screen
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()

    }
}
