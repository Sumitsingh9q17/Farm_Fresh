package com.sumit.farm_fresh

data class OrderHistory(
    val orderId: Int,
    val orderDate: String,
    val orderStatus: String,
    val orderAmount: Float,
    val productName: String,
    val productPrice: Float,
    val productQuantity: Int,
    val productImage: String
)
