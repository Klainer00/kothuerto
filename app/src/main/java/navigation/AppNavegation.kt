package com.example.huerto1.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.huerto1.ui.screens.CartScreen
import com.example.huerto1.ui.screens.LoginScreen
import com.example.huerto1.ui.screens.ProductListScreen
import com.example.huerto1.viewmodel.CartViewModel
import com.example.huerto1.viewmodel.ProductListViewModel
import kotlinx.coroutines.launch

// Definición de las rutas de la aplicación
sealed class AppScreen(val route: String, val title: String) {
    object Login : AppScreen("login", "Login")
    object ProductList : AppScreen("product_list", "Productos")
    object Cart : AppScreen("cart", "Carro de Compras")
    object Profile : AppScreen("profile", "Perfil") // Ruta para el Drawer
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // ViewModels (se pueden proveer a nivel de navegación)
    val productListViewModel: ProductListViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()

    // Observa la ruta actual para saber si mostrar la UI principal
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showMainUI = currentRoute != AppScreen.Login.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showMainUI, // Solo permite abrir el drawer si no estamos en Login
        drawerContent = {
            DrawerContent(navController, onCloseDrawer = {
                scope.launch { drawerState.close() }
            })
        }
    ) {
        Scaffold(
            topBar = {
                if (showMainUI) {
                    MainTopBar(
                        title = AppScreen.values().find { it.route == currentRoute }?.title ?: "KotHuerto",
                        onMenuClick = {
                            scope.launch { drawerState.open() }
                        },
                        onCartClick = {
                            navController.navigate(AppScreen.Cart.route)
                        }
                    )
                }
            },
            bottomBar = {
                if (showMainUI) {
                    MainBottomBar(navController = navController)
                }
            }
        ) { paddingValues ->
            AppNavHost(
                navController = navController,
                paddingValues = paddingValues,
                productListViewModel = productListViewModel,
                cartViewModel = cartViewModel
            )
        }
    }
}

// Host de Navegación (Define las pantallas)
@Composable
fun AppNavHost(
    navController: NavHostController,
    paddingValues: PaddingValues,
    productListViewModel: ProductListViewModel,
    cartViewModel: CartViewModel
) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Login.route, // Inicia en Login
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(AppScreen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    // Navega a la lista de productos y limpia el stack
                    navController.navigate(AppScreen.ProductList.route) {
                        popUpTo(AppScreen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(AppScreen.ProductList.route) {
            ProductListScreen(
                viewModel = productListViewModel,
                cartViewModel = cartViewModel
            )
        }
        composable(AppScreen.Cart.route) {
            CartScreen(
                viewModel = cartViewModel,
                onCheckout = {
                    // Aquí iría la lógica para enviar al backend
                }
            )
        }
        composable(AppScreen.Profile.route) {
            // Placeholder para la pantalla de Perfil
        }
    }
}

// TopAppBar (Acciones: Menu y Carro)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(title: String, onMenuClick: () -> Unit, onCartClick: () -> Unit) {
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Filled.Menu, contentDescription = "Abrir menú")
            }
        },
        actions = {
            // Acción de Búsqueda (requerida en listado)
            IconButton(onClick = { /* TODO: Implementar búsqueda */ }) {
                Icon(Icons.Filled.Search, contentDescription = "Buscar")
            }
            // Acción de Carro
            IconButton(onClick = onCartClick) {
                Icon(Icons.Filled.ShoppingCart, contentDescription = "Carro de compras")
            }
        }
    )
}

// BottomNavBar
@Composable
fun MainBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        // Ítem 1: Productos (Home)
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Productos") },
            label = { Text("Productos") },
            selected = currentRoute == AppScreen.ProductList.route,
            onClick = {
                navController.navigate(AppScreen.ProductList.route) {
                    // Evita apilar la misma pantalla
                    launchSingleTop = true
                }
            }
        )
        // Ítem 2: Carro
        NavigationBarItem(
            icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Carro") },
            label = { Text("Carro") },
            selected = currentRoute == AppScreen.Cart.route,
            onClick = {
                navController.navigate(AppScreen.Cart.route) {
                    launchSingleTop = true
                }
            }
        )
    }
}

// Contenido del Navigation Drawer
@Composable
fun DrawerContent(navController: NavHostController, onCloseDrawer: () -> Unit) {
    ModalDrawerSheet {
        Spacer(Modifier.height(12.dp))
        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.AccountCircle, contentDescription = null) },
            label = { Text("Mi Perfil") },
            selected = false,
            onClick = {
                navController.navigate(AppScreen.Profile.route)
                onCloseDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.Info, contentDescription = null) },
            label = { Text("Acerca de") },
            selected = false,
            onClick = { /* TODO */ onCloseDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        Spacer(Modifier.weight(1.0f)) // Empuja el logout al fondo
        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.ExitToApp, contentDescription = null) },
            label = { Text("Cerrar Sesión") },
            selected = false,
            onClick = {
                navController.navigate(AppScreen.Login.route) {
                    // Limpia todo el stack al cerrar sesión
                    popUpTo(0) { inclusive = true }
                }
                onCloseDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        Spacer(Modifier.height(12.dp))
    }
}

