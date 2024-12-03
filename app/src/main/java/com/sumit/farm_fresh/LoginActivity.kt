package com.sumit.farm_fresh

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DatabaseHelper(this)

        val emailField = findViewById<EditText>(R.id.et_email)
        val passwordField = findViewById<EditText>(R.id.et_password)
        val loginButton = findViewById<Button>(R.id.btn_login)
        val signupTextView = findViewById<TextView>(R.id.tv_signup)

        // Login button logic
        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            when {
                email.isEmpty() || password.isEmpty() -> {
                    Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
                }

                isAdminLogin(email, password) -> {
                    // Navigate to Admin Dashboard
                    val intent = Intent(this, AdminDashboardActivity::class.java)
                    intent.putExtra("isAdmin", true)
                    startActivity(intent)
                    finish()
                }

                isValidUserLogin(email, password) -> {
                    // Navigate to HomepageActivity and pass the email
                    val intent = Intent(this, HomepageActivity::class.java)
                    intent.putExtra("COL_USER_EMAIL", email)  // Pass email to HomepageActivity
                    startActivity(intent)
                    finish()
                }

                else -> {
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Signup navigation
        signupTextView.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isAdminLogin(email: String, password: String): Boolean {
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM Admin WHERE email = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))
        val isAdmin = cursor.count > 0
        cursor.close()
        return isAdmin
    }

    private fun isValidUserLogin(email: String, password: String): Boolean {
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM Users WHERE email = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))
        val isValidUser = cursor.count > 0
        cursor.close()
        return isValidUser
    }

    // Optional: Use this for signup navigation via XML onClick
    fun goToSignup(view: View) {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }
}
