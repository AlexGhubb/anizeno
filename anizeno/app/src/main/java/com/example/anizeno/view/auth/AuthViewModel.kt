package com.example.anizeno.view.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la autenticación de usuarios con Firebase Authentication.
 */
class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    /**
     * Inicia sesión con un correo electrónico y contraseña.
     */
    fun loginWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _uiState.value = AuthUiState.Success
                    } else {
                        _uiState.value = AuthUiState.Error(task.exception?.message ?: "Error desconocido")
                    }
                }
        }
    }

    /**
     * Registra un nuevo usuario con un correo electrónico y contraseña.
     */
    fun registerWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _uiState.value = AuthUiState.Success
                    } else {
                        _uiState.value = AuthUiState.Error(task.exception?.message ?: "Error en registro")
                    }
                }
        }
    }

    /**
     * Inicia sesión con una cuenta de Google.
     */
    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _uiState.value = AuthUiState.Success
                    } else {
                        _uiState.value = AuthUiState.Error(task.exception?.message ?: "Error con Google")
                    }
                }
        }
    }


}

/**
 * Estados de la interfaz de usuario para la autenticación.
 */
sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    object Success : AuthUiState
    data class Error(val message: String) : AuthUiState
}
