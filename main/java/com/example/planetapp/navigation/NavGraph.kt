package com.example.planetapp.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.planetapp.ui.components.BottomNavigationBar
import com.example.planetapp.ui.screens.DetailsScreen
import com.example.planetapp.ui.screens.FavoritesScreen
import com.example.planetapp.ui.screens.HomeScreen
import com.example.planetapp.models.planetList
import androidx.compose.foundation.layout.padding

// Classe representando os itens da Bottom Bar
sealed class BottomBarScreen(
    val route: String, val icon:
    @Composable () -> Unit, val label: String
) {
    object Home : BottomBarScreen(
        route = "home",
        icon = {
            androidx.compose.material3.Icon(
                Icons.Default.Home,
                contentDescription = "Home"
            )
        },
        label = "Home"
    )

    object Favorites : BottomBarScreen(
        route = "favorites",
        icon = {
            androidx.compose.material3.Icon(
                Icons.Default.Favorite,
                contentDescription = "Favorites"
            )
        },
        label = "Favoritos"
    )
}

@ExperimentalMaterial3Api
@Composable
fun NavGraph(
    onSettingsClick: () -> Unit,
    onHelpClick: () -> Unit
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomBarScreen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
// Tela Home
            composable(BottomBarScreen.Home.route) {
                HomeScreen(
                    onPlanetSelected = { planet ->
                        navController.navigate("details/${planet.name}")
                    },
                    onSettingsClick = onSettingsClick,
                    onHelpClick = onHelpClick
                )
            }
            // Tela de Favoritos
            composable(BottomBarScreen.Favorites.route) {
                FavoritesScreen(
                    planetList = planetList, // <- aqui passa a lista
                    onPlanetSelected = { planet ->
                        navController.navigate("details/${planet.name}")
                    },
                    onFavoriteToggle = { planet ->
                        planet.isFavorite = !planet.isFavorite
                    }
                )
            }

            // Tela de Detalhes
            composable("details/{planetName}") { backStackEntry ->
                val planetName =
                    backStackEntry.arguments?.getString(
                        "planetName")
                        val selectedPlanet =
                planetList.first { it.name == planetName }
                DetailsScreen(selectedPlanet)
            }
        }
    }
}