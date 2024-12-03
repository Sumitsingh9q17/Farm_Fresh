package com.sumit.farm_fresh

data class Payment(
    val name: String,
    val address: String,
    val phone: String,
    val email: String,
    val city: String,
    val postalCode: String,
    val totalPrice: Float,
    val paymentMethod: String,
    val orderDate: Long
)
