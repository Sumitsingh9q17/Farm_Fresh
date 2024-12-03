package com.sumit.farm_fresh

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class PaymentActivity : AppCompatActivity() {

    private lateinit var totalPriceTextView: TextView
    private lateinit var cashOnDeliveryButton: CardView
    private lateinit var proceedButton: Button
    private lateinit var paymentProgressBar: ProgressBar

    private val orderPlacedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Handle order placed notification
            val message = intent?.getStringExtra("orderMessage")
            sendNotification(message)  // Send notification instead of showing a Toast
            finish()  // Close the payment activity
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Initialize views
        totalPriceTextView = findViewById(R.id.totalPriceTextView)
        cashOnDeliveryButton = findViewById(R.id.cashOnDeliveryCard)
        proceedButton = findViewById(R.id.proceedButton)
        paymentProgressBar = findViewById(R.id.paymentProgressBar)

        // Get the total price from the Intent
        val totalPrice = intent.getFloatExtra("TOTAL_PRICE", 0f)

        // Update the TextView with the total price
        totalPriceTextView.text = "Total: ₹${"%.2f".format(totalPrice)}"

        // Initially disable proceed button
        proceedButton.isEnabled = false

        // Handle Cash on Delivery selection
        cashOnDeliveryButton.setOnClickListener {
            proceedButton.isEnabled = true  // Enable the Proceed button
        }

        // Handle Proceed button click
        proceedButton.setOnClickListener {
            // Show the progress bar while payment is processing
            paymentProgressBar.visibility = ProgressBar.VISIBLE

            // Simulate the payment process (e.g., Cash on Delivery)
            processPayment()

            // Send broadcast that the order is placed
            val intent = Intent("orderPlaced")
            intent.putExtra("orderMessage", "Your order has been placed successfully!")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

            // Finish the activity
            finish()
        }

        // Credit Card Payment
        val creditCardCard = findViewById<CardView>(R.id.creditCardPaymentCard)
        creditCardCard.setOnClickListener {
            // Show Credit Card details dialog
            showCreditCardDialog()
        }

        // Net Banking Payment
        val netBankingCard = findViewById<CardView>(R.id.netBankingCard)
        netBankingCard.setOnClickListener {
            // Show Net Banking details dialog
            showNetBankingDialog()
        }

        // UPI Payment
        val upiCard = findViewById<CardView>(R.id.upiPaymentCard)
        upiCard.setOnClickListener {
            // Show UPI details dialog
            showUPIDialog()
        }
    }

    private fun processPayment() {
        // Simulate a payment process. You can extend this with actual payment gateway logic later.

        // Simulate a delay for payment processing, and hide the progress bar
        Handler(Looper.getMainLooper()).postDelayed({
            paymentProgressBar.visibility = ProgressBar.INVISIBLE
        }, 2000)  // Simulate a 2-second payment delay
    }

    private fun showCreditCardDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_credit_card, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Enter Credit Card Details")
            .setPositiveButton("Submit") { _, _ ->
                // Handle Credit Card submission logic
                Toast.makeText(this, "Credit Card payment processed", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)

        builder.create().show()
    }

    private fun showNetBankingDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_net_banking, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Enter Net Banking Details")
            .setPositiveButton("Submit") { _, _ ->
                // Handle Net Banking submission logic
                Toast.makeText(this, "Net Banking payment processed", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)

        builder.create().show()
    }

    private fun showUPIDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_upi, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Enter UPI Details")
            .setPositiveButton("Submit") { _, _ ->
                // Handle UPI submission logic
                Toast.makeText(this, "UPI payment processed", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)

        builder.create().show()
    }

    private fun sendNotification(message: String?) {
        // Create notification channel for Android 8.0+ (Oreo and above)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channelId = "order_notification_channel"
            val channelName = "Order Notifications"
            val channelDescription = "Notifications for order status"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(channelId, channelName, importance)
            notificationChannel.description = channelDescription

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // Build the notification
        val notification = NotificationCompat.Builder(this, "order_notification_channel")
            .setContentTitle("Farm Fresh")
            .setContentText(message)
            .setSmallIcon(R.drawable.farmfresh) // Use your app's icon here
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        // Send the notification
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

    override fun onStart() {
        super.onStart()

        // Register the BroadcastReceiver to listen for the orderPlaced broadcast
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(orderPlacedReceiver, IntentFilter("orderPlaced"))
    }

    override fun onStop() {
        super.onStop()

        // Unregister the BroadcastReceiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(orderPlacedReceiver)
    }
}
