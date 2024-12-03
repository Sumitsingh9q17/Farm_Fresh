package com.sumit.farm_fresh

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewUsersActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private var userList = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_users)

        // Initialize the database helper
        dbHelper = DatabaseHelper(this)

        // Get the isAdmin flag from the Intent
        val isAdmin = intent.getBooleanExtra("isAdmin", false)

        // Show a message if the user is not an admin
        if (!isAdmin) {
            Toast.makeText(this, "Access Denied: Admins Only", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerViewUsers)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch all users from the database
        userList = getAllUsersFromDb().toMutableList() // Convert List to MutableList

        // Pass userList to the adapter
        userAdapter = UserAdapter(userList, object : UserAdapter.OnUserClickListener {
            override fun onEdit(user: User) {
                // Handle edit click, you can fetch the user by email for further editing
                val cursor = dbHelper.getUserByEmail(user.email)
                val intent = Intent(this@ViewUsersActivity, EditUserActivity::class.java)
                intent.putExtra("userEmail", user.email)
                startActivity(intent)
                cursor?.use {
                    val nameIndex = it.getColumnIndex(DatabaseHelper.COL_USER_NAME)
                    val emailIndex = it.getColumnIndex(DatabaseHelper.COL_USER_EMAIL)

                    // Check if the column indices are valid
                    if (nameIndex >= 0 && emailIndex >= 0) {
                        if (it.moveToFirst()) {
                            val name = it.getString(nameIndex)
                            val email = it.getString(emailIndex)
                            // Use this data to pre-populate edit fields
                        }
                    }
                }
            }


            override fun onDelete(user: User) {
                // Handle delete click
                deleteUser(user)
            }
        })
        recyclerView.adapter = userAdapter
    }

    private fun getAllUsersFromDb(): List<User> {
        val userList = mutableListOf<User>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_USERS}", null)

        cursor.use {
            val nameIndex = it.getColumnIndex(DatabaseHelper.COL_USER_NAME)
            val emailIndex = it.getColumnIndex(DatabaseHelper.COL_USER_EMAIL)

            if (nameIndex >= 0 && emailIndex >= 0) {
                while (it.moveToNext()) {
                    val name = it.getString(nameIndex)
                    val email = it.getString(emailIndex)
                    userList.add(User(name, email, ""))
                }
            }
        }

        db.close()
        return userList
    }

    private fun deleteUser(user: User) {
        val db = dbHelper.writableDatabase
        val rowsDeleted = db.delete(
            DatabaseHelper.TABLE_USERS,
            "${DatabaseHelper.COL_USER_EMAIL} = ?",
            arrayOf(user.email)
        )

        if (rowsDeleted > 0) {
            Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show()
            // Refresh the list without recreating the activity
            userList = getAllUsersFromDb().toMutableList()  // Convert the List to MutableList
            userAdapter.updateList(userList)
        } else {
            Toast.makeText(this, "Failed to delete user", Toast.LENGTH_SHORT).show()
        }

        db.close()
    }
}
