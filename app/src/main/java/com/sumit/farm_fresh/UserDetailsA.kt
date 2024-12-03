package com.sumit.farm_fresh

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UserDetailsA : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var cityEditText: EditText
    private lateinit var postalCodeEditText: EditText
    private lateinit var termsCheckBox: CheckBox
    private lateinit var proceedToPaymentButton: Button
    private lateinit var summaryTextView: TextView
    private lateinit var totalPriceTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        // Initialize the views
        nameEditText = findViewById(R.id.nameEditText)
        addressEditText = findViewById(R.id.addressEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        emailEditText = findViewById(R.id.emailEditText)
        cityEditText = findViewById(R.id.cityEditText)
        postalCodeEditText = findViewById(R.id.postalCodeEditText)
        termsCheckBox = findViewById(R.id.termsCheckBox)
        proceedToPaymentButton = findViewById(R.id.proceedToPaymentButton)
        summaryTextView = findViewById(R.id.summaryTextView)
        totalPriceTextView = findViewById(R.id.totalPriceTextView)


        // Pre-populate the details for testing (optional)
        nameEditText.setText("John Doe")
        addressEditText.setText("123 Farm Street")
        phoneEditText.setText("9876543210")
        emailEditText.setText("johndoe@example.com")
        cityEditText.setText("Farmville")
        postalCodeEditText.setText("123456")
        termsCheckBox.isChecked = false

        // Update summary whenever any field is changed
        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateSummary()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        val totalPrice = intent.getFloatExtra("TOTAL_PRICE", 0f)
        totalPriceTextView.text = "Total: ₹${"%.2f".format(totalPrice)}"


        // Validate and proceed to payment on button click
        proceedToPaymentButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val address = addressEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val email = emailEditText.text.toString()
            val city = cityEditText.text.toString()
            val postalCode = postalCodeEditText.text.toString()


            if (isValidInput(name, address, phone, email, city, postalCode)) {
                // Proceed to the payment activity
                val intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra("name", name)
                intent.putExtra("address", address)
                intent.putExtra("phone", phone)
                intent.putExtra("email", email)
                intent.putExtra("city", city)
                intent.putExtra("postalCode", postalCode)
                intent.putExtra("TOTAL_PRICE", totalPrice)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please fill all fields correctly.", Toast.LENGTH_SHORT).show()
            }
        }

        // Enable/Disable proceed button based on checkbox state
        termsCheckBox.setOnCheckedChangeListener { _, isChecked ->
            proceedToPaymentButton.isEnabled = isChecked
        }
    }

    private fun updateSummary() {
        val name = nameEditText.text.toString()
        val address = addressEditText.text.toString()
        val phone = phoneEditText.text.toString()
        val email = emailEditText.text.toString()
        val city = cityEditText.text.toString()
        val postalCode = postalCodeEditText.text.toString()

        val summary =
            "Name: $name\nAddress: $address\nPhone: $phone\nEmail: $email\nCity: $city\nPostal Code: $postalCode"
        summaryTextView.text = summary
    }

    private fun isValidInput(
        name: String,
        address: String,
        phone: String,
        email: String,
        city: String,
        postalCode: String
    ): Boolean {
        val emailRegex = Regex("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
        return name.isNotEmpty() && address.isNotEmpty() && phone.isNotEmpty() && email.matches(
            emailRegex
        ) &&
                city.isNotEmpty() && postalCode.isNotEmpty() && phone.matches(Regex("^[0-9]{10}$"))
    }
}
