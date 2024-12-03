package com.sumit.farm_fresh

data class Order(
    val orderId: String,
    val items: List<String>,  // List of items
    val totalAmount: Double,
    val orderStatus: String,
    val orderDate: String
)


