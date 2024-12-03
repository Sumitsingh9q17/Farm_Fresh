package com.sumit.farm_fresh

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UserProfileActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var orderHistoryButton: Button
    private lateinit var savedAddressesButton: Button
    private lateinit var paymentMethodsButton: Button
    private lateinit var helpButton: Button
    private lateinit var logOutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        // Initialize database and UI elements
        databaseHelper = DatabaseHelper(this)
        nameTextView = findViewById(R.id.textViewName)
        emailTextView = findViewById(R.id.textViewEmail)
        orderHistoryButton = findViewById(R.id.buttonOrderHistory)
        savedAddressesButton = findViewById(R.id.buttonSavedAddresses)
        paymentMethodsButton = findViewById(R.id.buttonPaymentMethods)
        helpButton = findViewById(R.id.buttonHelp)
        logOutButton = findViewById(R.id.buttonLogOut)

        // Get the logged-in user's email from Intent
        val userEmail = intent.getStringExtra("COL_USER_EMAIL") ?: ""

        // Load user data and display on screen
        loadUserData(userEmail)

        // Set onClick listeners for buttons
        orderHistoryButton.setOnClickListener {
            navigateToOrderHistory(userEmail)
        }

        savedAddressesButton.setOnClickListener {
            navigateToSavedAddresses(userEmail)
        }

        paymentMethodsButton.setOnClickListener {
            navigateToPaymentMethods(userEmail)
        }

        helpButton.setOnClickListener {
            navigateToHelp()
        }

        logOutButton.setOnClickListener {
            logOut()
        }
    }

    private fun loadUserData(email: String) {
        val db = DatabaseHelper(this)
        val cursor = db.getUserByEmail(email)

        // Log the query parameters to help debug
        Log.d("Database", "Attempting to load user with email: $email")

        if (cursor != null && cursor.moveToFirst()) {
            // Fetch user data
            val name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_NAME))
            val userEmail =
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_EMAIL))
            val password =
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_PASSWORD))

            // Create User object
            val user = User(name, userEmail, password)

            // Populate the views with the user data
            nameTextView.text = user.name
            emailTextView.text = user.email
        } else {
            // If no user is found, show a toast message
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
        }

        // Close the cursor after use
        cursor?.close()
    }


    private fun navigateToOrderHistory(email: String) {
        // Navigate to Order History Activity
        val intent = Intent(this, OrderHistoryActivity::class.java)
        intent.putExtra("USER_EMAIL", email)
        startActivity(intent)
    }

    private fun navigateToSavedAddresses(email: String) {
        // Navigate to Saved Addresses Activity
        val intent = Intent(this, SavedAddressesActivity::class.java)
        intent.putExtra("USER_EMAIL", email)
        startActivity(intent)
    }

    private fun navigateToPaymentMethods(email: String) {
        // Navigate to Payment Methods Activity
        val intent = Intent(this, PaymentMethodsActivity::class.java)
        intent.putExtra("USER_EMAIL", email)
        startActivity(intent)
    }

    private fun navigateToHelp() {
        // Navigate to Help or Support Activity
        val intent = Intent(this, HelpActivity::class.java)
        startActivity(intent)
    }

    private fun logOut() {
        // Log out the user and return to login screen
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()  // Close the UserProfileActivity
    }
}
