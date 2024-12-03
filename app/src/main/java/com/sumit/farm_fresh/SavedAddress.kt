package com.sumit.farm_fresh

data class SavedAddress(
    val id: Int,
    val addressType: String,
    val addressLine1: String,
    val addressLine2: String?,
    val city: String,
    val state: String,
    val zip: String
)
