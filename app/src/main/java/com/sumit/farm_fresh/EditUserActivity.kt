package com.sumit.farm_fresh

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        val email = intent.getStringExtra("userEmail") ?: ""
        val nameEditText = findViewById<EditText>(R.id.editTextName)
        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val saveButton = findViewById<Button>(R.id.btnSaveChanges)

        // Get user details from the database using the email
        val dbHelper = DatabaseHelper(this)
        val cursor = dbHelper.getUserByEmail(email)


        cursor.use {
            if (it != null) {
                if (it.moveToFirst()) {
                    // Print column names for debugging purposes
                    val columnNames = it.columnNames.joinToString(", ")
                    println("Column names: $columnNames")

                    // Get the indices for the columns by name
                    val nameIndex = it.getColumnIndex(DatabaseHelper.COL_USER_NAME)
                    val emailIndex = it.getColumnIndex(DatabaseHelper.COL_USER_EMAIL)

                    if (nameIndex != -1 && emailIndex != -1) {
                        val currentName = it.getString(nameIndex)
                        val currentEmail = it.getString(emailIndex)

                        nameEditText.setText(currentName)
                        emailEditText.setText(currentEmail)
                    } else {
                        // Log or show an error if column indices are not valid
                        println("Invalid column index. Check your column names.")
                    }
                }
            }
        }


        saveButton.setOnClickListener {
            val newName = nameEditText.text.toString().trim()
            val newEmail = emailEditText.text.toString().trim()

            if (newName.isNotEmpty() && newEmail.isNotEmpty()) {
                // Validate email format
                if (Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                    // Check if email already exists
                    val cursor = dbHelper.getUserByEmail(newEmail)
                    if (cursor != null) {
                        if (cursor.count > 0) {
                            Toast.makeText(this, "Email already in use", Toast.LENGTH_SHORT).show()
                        } else {
                            // Proceed with updating the user
                            val result = dbHelper.updateUser(email, newName, newEmail)
                            if (result > 0) {
                                Toast.makeText(this, "User details updated", Toast.LENGTH_SHORT)
                                    .show()
                                finish()
                            } else {
                                Toast.makeText(this, "Failed to update user", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                    if (cursor != null) {
                        cursor.close()
                    }
                } else {
                    Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
