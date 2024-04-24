package com.example.prm_1

import java.io.Serializable

data class Product(
    var name: String,
    var category: String,
    var expirationDate: Long,
    var quantity: Int?,
    var status: String
) : Serializable