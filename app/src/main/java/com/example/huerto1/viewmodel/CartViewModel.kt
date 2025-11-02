package com.example.huerto1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto1.model.CartItem
import com.example.huerto1.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    // Estado del carro de compras (persistencia en memoria por ahora)
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Estado para el total del carro
    val total: StateFlow<Double> = _cartItems.map { items ->
        items.sumOf { it.product.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    // Añadir producto (o incrementar cantidad)
    fun addProduct(product: Product) {
        _cartItems.update { currentList ->
            val existingItem = currentList.find { it.product.id == product.id }
            if (existingItem != null) {
                // Si existe, incrementa la cantidad
                currentList.map {
                    if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                // Si no existe, lo añade
                currentList + CartItem(product = product, quantity = 1)
            }
        }
    }

    // Actualizar cantidad (Modificación)
    fun updateQuantity(item: CartItem, newQuantity: Int) {
        if (newQuantity <= 0) {
            // Si la cantidad es 0 o menos, elimina el ítem
            removeItem(item)
        } else {
            _cartItems.update { currentList ->
                currentList.map {
                    if (it.product.id == item.product.id) it.copy(quantity = newQuantity) else it
                }
            }
        }
    }

    // Eliminar producto
    fun removeItem(item: CartItem) {
        _cartItems.update { currentList ->
            currentList.filterNot { it.product.id == item.product.id }
        }
    }

    // Enviar venta al backend
    fun checkout() {
        viewModelScope.launch {
            // Aquí iría la lógica de API (Retrofit, Ktor, etc.)
            println("Enviando venta al backend...")
            println("Total: ${total.value}")
            println("Items: ${cartItems.value.joinToString()}")

            // Limpiar carro después de la "venta"
            _cartItems.value = emptyList()
        }
    }
}

