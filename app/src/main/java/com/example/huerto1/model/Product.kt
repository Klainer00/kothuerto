package com.example.huerto1.model
// Modelo para un Producto

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val unit: String, // "Kg", "Unidad", "Atado", etc.
    val imageUrl: String
)

data class CartItem(
    val product: Product,
    val quantity: Int
)

