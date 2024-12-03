package com.sumit.farm_fresh

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UserManagementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_management)

        // Get the isAdmin flag from the Intent (passed from AdminDashboardActivity)
        val isAdmin = intent.getBooleanExtra("isAdmin", false)
        Log.d("UserManagementActivity", "Passing isAdmin flag: $isAdmin")

        // Disable or show a message for non-admin users
        if (!isAdmin) {
            Toast.makeText(this, "Access Denied: Admins Only", Toast.LENGTH_SHORT).show()
            finish() // Close the activity and return to the previous screen
            return
        }

        val addUserButton = findViewById<Button>(R.id.btnAddUser)
        val viewUsersButton = findViewById<Button>(R.id.btnViewUsers)

        // Passing isAdmin flag to AddUserActivity
        addUserButton.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            intent.putExtra("isAdmin", isAdmin) // Pass the isAdmin flag
            startActivity(intent)
        }

        // Passing isAdmin flag to ViewUsersActivity
        viewUsersButton.setOnClickListener {
            val intent = Intent(this, ViewUsersActivity::class.java)
            intent.putExtra("isAdmin", isAdmin) // Pass the isAdmin flag
            startActivity(intent)
        }
    }
}
