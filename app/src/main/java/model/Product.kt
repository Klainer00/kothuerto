package model
// Modelo para un Producto
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String, // URL de la imagen
    val description: String
)

// Modelo para un ítem en el carro de compras
data class CartItem(
    val product: Product,
    var quantity: Int
)
