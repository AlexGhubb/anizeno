package com.example.anizeno.view.core.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.anizeno.data.remote.dto.AnimeData
import com.example.anizeno.view.profile.ProfileViewModel
import com.example.anizeno.view.auth.AuthUiState
import com.example.anizeno.view.auth.AuthViewModel
import com.example.anizeno.view.profile.ProfileScreen
import com.example.anizeno.view.auth.login.LoginScreen
import com.example.anizeno.view.auth.login.RegisterScreen
import com.example.anizeno.view.home.HomeScreen
import com.example.anizeno.view.search.AnimeDetailScreen

import com.example.anizeno.view.search.SearchScreen
import com.google.firebase.auth.FirebaseAuth

/**
 * Clase que representa la navegación de la aplicación.
 * Cada destino tiene una ruta única.
 *
 */
@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val uiState by authViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val isLoggedIn = FirebaseAuth.getInstance().currentUser != null

    // Reaccionar a cambios de estado de autenticación
    LaunchedEffect(uiState) {
        when (uiState) {
            is AuthUiState.Success -> {
                Toast.makeText(context, "Bienvenido", Toast.LENGTH_SHORT).show()
                navController.navigate(Home) {
                    popUpTo(Login) { inclusive = true }
                }
            }

            is AuthUiState.Error -> {
                Toast.makeText(context, "Error en email o password", Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Home else Login
    ) {
        // Login Screen
        composable<Login> {
            LoginScreen(
                onRegisterClick = { navController.navigate(Register) },
                onLoginClick = { email, password ->
                    authViewModel.loginWithEmail(email, password)
                }
            )
        }

        // Register Screen
        composable<Register> {
            RegisterScreen(
                onRegisterClick = { name, email, password ->
                    authViewModel.registerWithEmail(email, password)
                },
                onBackClick = { navController.popBackStack() },
            )
        }

        // Home Screen
        composable<Home> {
            HomeScreen(
                userName = FirebaseAuth.getInstance().currentUser?.email ?: "Otaku",
                onSearchClick = { navController.navigate(Search) },
                onFavoritesClick = { navController.navigate(Profile) },
                onAnimeClick = { anime ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("anime", anime)
                    navController.navigate(AnimeDetail)
                }
            )
        }

        //Profile Screen
        composable<Profile> {
            val context = LocalContext.current
            val profileViewModel: ProfileViewModel = viewModel()

            ProfileScreen(
                userEmail = FirebaseAuth.getInstance().currentUser?.email ?: "Otaku",
                onBackClick = { navController.popBackStack() },
                onLogoutClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Login) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onAnimeClick = { userAnime ->
                    if (userAnime.malId != null) {
                        // 🔹 Buscar por ID (más preciso)
                        profileViewModel.fetchAnimeById(userAnime.malId) { fullAnime ->
                            fullAnime?.let {
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("anime", it)
                                navController.navigate(AnimeDetail)
                            } ?: run {
                                Toast.makeText(context, "No se encontró el anime (ID ${userAnime.malId})", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // 🔹 Buscar por título como respaldo
                        profileViewModel.fetchAnimeByTitle(userAnime.title) { fullAnime ->
                            fullAnime?.let {
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("anime", it)
                                navController.navigate(AnimeDetail)
                            } ?: run {
                                Toast.makeText(context, "No se encontró información del anime", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            )
        }



        // SearchScreen
        composable<Search> {
            SearchScreen(
                onAnimeClick = { anime ->
                    // Guardamos el anime seleccionado en el back stack
                    navController.currentBackStackEntry?.savedStateHandle?.set("anime", anime)
                    navController.navigate(AnimeDetail)

                },

                onBackClick = { navController.popBackStack() }
            )
        }


        // AnimeDetailScreen
        composable<AnimeDetail> {
            val anime = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<AnimeData>("anime")

            anime?.let {
                AnimeDetailScreen(anime = anime,
                    onBackClick = { navController.popBackStack()},)
                //Text(text = it.title ?: "Sin título")
            }

        }
    }
}


