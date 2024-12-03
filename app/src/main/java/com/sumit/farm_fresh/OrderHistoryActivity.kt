package com.sumit.farm_fresh

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class OrderHistoryActivity : AppCompatActivity() {

    private lateinit var emailTextView: TextView
    private lateinit var orderHistoryTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var orderHistoryAdapter: OrderHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        emailTextView = findViewById(R.id.textViewEmail)
        recyclerView = findViewById(R.id.orderHistoryRecyclerView)
        orderHistoryAdapter = OrderHistoryAdapter()

        // Set RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = orderHistoryAdapter

        // Get the user email from intent
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: ""

        // Display email
        emailTextView.text = "User Email: $userEmail"

        // Fetch order history from database
        val dbHelper = DatabaseHelper(this)
        val orderHistoryList = dbHelper.getOrderHistory(userEmail)

        // Display order history in RecyclerView
        orderHistoryAdapter.submitList(orderHistoryList)
    }
}
