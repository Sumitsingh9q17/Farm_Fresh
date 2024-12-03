package com.sumit.farm_fresh

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class PaymentMethodsActivity : AppCompatActivity() {

    private lateinit var emailTextView: TextView
    private lateinit var paymentMethodsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_methods)

        emailTextView = findViewById(R.id.textViewEmail)
        paymentMethodsTextView = findViewById(R.id.textViewPaymentMethods)

        // Get the user email from intent
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: ""

        // Display email
        emailTextView.text = "User Email: $userEmail"

        // Example: Display payment methods (this should be fetched from database or API)
        paymentMethodsTextView.text = "Payment methods for $userEmail go here."
    }
}
