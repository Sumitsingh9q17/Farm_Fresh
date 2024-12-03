package com.sumit.farm_fresh

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SavedAddressesActivity : AppCompatActivity() {

    private lateinit var emailTextView: TextView
    private lateinit var addressesTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_addresses)

        emailTextView = findViewById(R.id.textViewEmail)
        addressesTextView = findViewById(R.id.textViewAddresses)

        // Get the user email from intent
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: ""

        // Display email
        emailTextView.text = "User Email: $userEmail"

        // Example: Display saved addresses (this should be fetched from database)
        addressesTextView.text = "Saved addresses for $userEmail go here."
    }
}
