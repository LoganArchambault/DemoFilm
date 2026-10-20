package cstjean.mobile2.demofilms.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import cstjean.mobile2.demofilms.DemoFilmsApplication
import cstjean.mobile2.demofilms.ui.navigation.FilmsRoute
import cstjean.mobile2.demofilms.ui.navigation.AboutRoute
import cstjean.mobile2.demofilms.ui.navigation.CreateFilmRoute
import cstjean.mobile2.demofilms.ui.navigation.DetailsRoute
import cstjean.mobile2.demofilms.ui.screens.AboutScreen
import cstjean.mobile2.demofilms.ui.screens.CreateFilmScreen
import cstjean.mobile2.demofilms.ui.screens.MediaDetailsScreen
import cstjean.mobile2.demofilms.ui.screens.MediaScreen
import cstjean.mobile2.demofilms.viewmodel.CreateFilmViewModel
import cstjean.mobile2.demofilms.viewmodel.MediaDetailsViewModel
import cstjean.mobile2.demofilms.viewmodel.MediaViewModel

@Composable
fun DemoFilmsApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    // Vérification typée de la destination active
                    selected = currentDestination?.hasRoute<FilmsRoute>() == true,
                    onClick = {
                        navController.navigate(FilmsRoute) {
                            popUpTo<FilmsRoute> { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Movie, contentDescription = "Films") },
                    label = { Text("Films") }
                )
                NavigationBarItem(
                    selected = currentDestination?.hasRoute<AboutRoute>() == true,
                    onClick = {
                        navController.navigate(AboutRoute) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Info, contentDescription = "À propos") },
                    label = { Text("À propos") }
                )
            }
        }
    ) { innerPadding ->
        DemoFilmsNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}


@Composable
private fun DemoFilmsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val app = context.applicationContext as DemoFilmsApplication
    val repository = app.repository

    NavHost(
        navController = navController,
        startDestination = FilmsRoute,
        modifier = modifier
    ) {
        composable<AboutRoute> {
            AboutScreen()
        }

        composable<FilmsRoute> {
            MediaScreen(
                viewModel = viewModel(
                    factory = viewModelFactory {
                        //Création du ViewModel en passant le repository au constructeur
                        initializer { MediaViewModel(repository) }
                    }),
                onDetailsClick = { film ->
                    navController.navigate(DetailsRoute(idFilm = film.id))
                },
                // Nécessaire seulement si vous avez ajouté l'écran d'ajout de films
                onCreateClick = { navController.navigate(CreateFilmRoute) }
            )
        }

        composable<DetailsRoute> { backStackEntry ->
            val route: DetailsRoute = backStackEntry.toRoute()

            MediaDetailsScreen(
                idFilm = route.idFilm,
                viewModel = viewModel(
                    factory = viewModelFactory {
                        //Création du ViewModel en passant le repository au constructeur
                        initializer { MediaDetailsViewModel(repository, route.idFilm) }
                    }),
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<CreateFilmRoute> {
            CreateFilmScreen(
                viewModel = viewModel(
                    factory = viewModelFactory {
                        //Création du ViewModel en passant le repository au constructeur
                        initializer { CreateFilmViewModel(repository) }
                    }),
                onBackClick = { navController.popBackStack() },
                onFilmCreated = {
                    navController.popBackStack()
                }
            )
        }

    }
}
