package com.example.anizeno.view.auth.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.anizeno.R
import com.google.firebase.auth.FirebaseAuth

/**
 * LoginScreen muestra la pantalla de inicio de sesión.
 * Permite al usuario ingresar su correo electrónico y contraseña para iniciar sesión.
 *
 * @param onLoginClick Callback que se llama cuando el usuario hace clic en el botón de inicio de sesión.
 * @param onRegisterClick Callback que se llama cuando el usuario hace clic en el botón de registro.
 * @param onForgotPasswordClick Callback que se llama cuando el usuario hace clic en el enlace de restablecimiento de contraseña.
 * @param onBackClick Callback que se llama cuando el usuario hace clic en el botón de retroceso.
 * 
 */
//@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun LoginScreen(
    onLoginClick: (email: String, password: String) -> Unit,
    onRegisterClick: () -> Unit
) {
    val context = LocalContext.current

    val windowSize = calculateWindowSizeClass(LocalContext.current as Activity)
    val isTablet = windowSize.widthSizeClass == WindowWidthSizeClass.Expanded
    val isHorizontal = windowSize.widthSizeClass == WindowWidthSizeClass.Medium

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val emailValid = email.contains("@") && email.contains(".")

    fun sendPasswordReset() {
        if (!emailValid) {
            Toast.makeText(
                context,
                context.getString(R.string.invalid_email),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        FirebaseAuth.getInstance()
            .sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Toast.makeText(
                    context,
                    context.getString(R.string.check_email),
                    Toast.LENGTH_LONG
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    context,
                    "Error: ${it.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f),
                        Color.Black
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        val cardWidth = when {
            isTablet -> 420.dp
            isHorizontal -> 360.dp
            else -> Modifier.fillMaxWidth()
        }

        Card(
            modifier = if (isTablet || isHorizontal)
                Modifier.width(cardWidth as Dp)
            else Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {

            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(R.string.login_title),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )

                Spacer(Modifier.height(24.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.email)) },
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                if (!emailValid && email.isNotBlank()) {
                    Text(
                        text = stringResource(R.string.invalid_email),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(R.string.password)) },
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null)
                    },
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                if (passwordVisible) Icons.Default.VisibilityOff
                                else Icons.Default.Visibility,
                                contentDescription = null
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = { onLoginClick(email, password) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = email.isNotBlank() && password.isNotBlank()
                ) {
                    Text(stringResource(R.string.login_button))
                }

                TextButton(onClick = { sendPasswordReset() }) {
                    Text(
                        text = stringResource(R.string.forgot_password),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                TextButton(onClick = onRegisterClick) {
                    Text(
                        stringResource(R.string.go_register),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}


