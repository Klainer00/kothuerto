package com.example.huerto1.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huerto1.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class ProductListViewModel : ViewModel() {

    // Simula una lista de productos (esto vendría de un repositorio o API)
    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())

    // Estado para el término de búsqueda
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Productos filtrados (los que ve la UI)
    val filteredProducts: StateFlow<List<Product>> =
        _allProducts.combine(_searchQuery) { products, query ->
            if (query.isBlank()) {
                products
            } else {
                products.filter {
                    it.name.contains(query, ignoreCase = true) ||
                            it.description.contains(query, ignoreCase = true)
                }
            }
        }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Lazily, emptyList())

    init {
        // Carga los productos al iniciar
        loadProducts()
    }

    private fun loadProducts() {
        // Simulación de carga
        viewModelScope.launch {
            _allProducts.value = listOf(
                Product("1", "Tomate Cherry", 3500.0, "https://placehold.co/300x300/F44336/FFFFFF?text=Tomate", "Tomates cherry frescos, 1kg"),
                Product("2", "Lechuga Costina", 1500.0, "https://placehold.co/300x300/8BC34A/FFFFFF?text=Lechuga", "Lechuga fresca, unidad"),
                Product("3", "Zanahorias", 2000.0, "https://placehold.co/300x300/FF9800/FFFFFF?text=Zanahoria", "Zanahorias orgánicas, 1kg"),
                Product("4", "Papas", 2500.0, "https://placehold.co/300x300/795548/FFFFFF?text=Papa", "Papas premium, saco 5kg"),
                Product("5", "Albahaca", 1000.0, "https://placehold.co/300x300/4CAF50/FFFFFF?text=Albahaca", "Albahaca fresca, manojo")
            )
        }
    }

    // Función llamada por la UI para actualizar la búsqueda
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}

