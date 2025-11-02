package com.example.huerto1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto1.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
// --- ESTA ES LA IMPORTACIÓN QUE FALTA ---
import kotlinx.coroutines.flow.stateIn
// ----------------------------------------
import kotlinx.coroutines.launch

class ProductListViewModel : ViewModel() {

    // Simula una lista de productos que vendría de una base de datos o API
    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // Lógica de búsqueda
    val filteredProducts: StateFlow<List<Product>> = _searchQuery
        .combine(_allProducts) { query, products ->
            if (query.isBlank()) {
                products
            } else {
                products.filter {
                    it.name.contains(query, ignoreCase = true) ||
                            it.description.contains(query, ignoreCase = true)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            // Usamos WhileSubscribed para que el Flow solo esté activo si la UI lo observa
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList() // El valor inicial es una lista vacía
        )

    init {
        loadProducts()
    }

    private fun loadProducts() {
        // Simulación de carga
        viewModelScope.launch {
            // --- PRODUCTOS CORREGIDOS PARA QUE COINCIDAN CON EL MODELO ---
            // Product(id, name, description, price, unit, imageUrl)
            _allProducts.value = listOf(
                Product("1", "Tomate", "Tomate fresco de la huerta", 1200.0, "Kg", "https://placehold.co/300x300/F44336/FFFFFF?text=Tomate"),
                Product("2", "Lechuga", "Lechuga costina fresca", 800.0, "Unidad", "https://placehold.co/300x300/4CAF50/FFFFFF?text=Lechuga"),
                Product("3", "Zanahoria", "Zanahorias dulces", 900.0, "Kg", "https://placehold.co/300x300/FF9800/FFFFFF?text=Zanahoria"),
                Product("4", "Cebollín", "Atado de cebollín verde", 1000.0, "Atado", "https://placehold.co/300x300/8BC34A/FFFFFF?text=Cebollín"),
                Product("5", "Zapallo", "Zapallo camote grande", 1500.0, "Unidad", "https://placehold.co/300x300/FFC107/FFFFFF?text=Zapallo"),
                Product("6", "Papa", "Saco de papas (5kg)", 5000.0, "Saco", "https://placehold.co/300x300/795548/FFFFFF?text=Papa")
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}

