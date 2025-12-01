package com.example.anizeno.view.search

import android.content.Intent
import android.widget.Toast
import com.example.anizeno.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.anizeno.data.remote.dto.AnimeData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.anizeno.data.local.room_db.AnimeReview
import com.example.anizeno.data.local.room_db.UserAnimeData
import com.example.anizeno.view.profile.ProfileViewModel
import com.example.anizeno.view.review.ReviewViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

/**
 * AnimeDetailScreen muestra la información detallada de un anime.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailScreen(
    anime: AnimeData,
    onBackClick: () -> Unit,
    profileViewModel: ProfileViewModel = viewModel(),
    reviewViewModel: ReviewViewModel = viewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var showReviewDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val reviews by reviewViewModel.reviews.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val currentEmail = FirebaseAuth.getInstance().currentUser?.email ?: "anon"

    // --- Reactive username from profile VM
    val profileUserName by profileViewModel.userName.collectAsState()

    // Load reviews and profile once when the screen shows (and when anime.malId changes)
    LaunchedEffect(anime.malId) {
        anime.malId.let { reviewViewModel.loadReviewsForAnime(it) }
        // load profile now so collectAsState has initial value (non-blocking)
        profileViewModel.loadUserProfile(currentEmail)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(anime.title ?: stringResource(R.string.anime_detail_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    // Action left empty intentionally (you can add icons here)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondary,
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f),
                            Color.Black.copy(alpha = 0.9f)
                        )
                    )
                )
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Imagen + títulos
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = anime.images?.jpg?.image_url
                        ?: "https://cdn.myanimelist.net/img/sp/icon/apple-touch-icon-256.png",
                    contentDescription = anime.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )

                Spacer(Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = anime.title ?: stringResource(R.string.no_title),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = MaterialTheme.colorScheme.primary
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(8.dp))

                    Text("🇯🇵 ${anime.getTitleJapanese()}", color = MaterialTheme.colorScheme.onPrimary)
                    Text("🇬🇧 ${anime.getTitleEnglish()}", color = MaterialTheme.colorScheme.onPrimary)
                    Text("🇪🇸 ${anime.getTitleSpanish()}", color = MaterialTheme.colorScheme.onPrimary)
                }
            }

            Spacer(Modifier.height(20.dp))

            // Botón para agregar a lista
            Button(
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.add_to_list))
            }

            Spacer(Modifier.height(20.dp))

            // Información general
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoRow("🎬 ${stringResource(R.string.studio)}", anime.studios?.firstOrNull()?.name ?: stringResource(R.string.unknown))
                    InfoRow("📺 ${stringResource(R.string.episodes)}", anime.episodes?.toString() ?: "?")
                    InfoRow("⭐ ${stringResource(R.string.score)}", anime.score?.toString() ?: "?")
                    InfoRow("⏳ ${stringResource(R.string.status)}", anime.status ?: stringResource(R.string.unknown))
                }
            }

            Spacer(Modifier.height(20.dp))

            // Sinopsis
            Text(
                text = stringResource(R.string.synopsis),
                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = anime.synopsis ?: stringResource(R.string.no_synopsis),
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onPrimary),
                textAlign = TextAlign.Justify
            )

            Spacer(Modifier.height(20.dp))

            // Trailer
            val embedUrl = anime.trailer?.embedUrl
            val youtubeId = embedUrl?.substringAfter("embed/")?.substringBefore("?")

            if (!youtubeId.isNullOrBlank()) {
                val thumbnailUrl = "https://img.youtube.com/vi/$youtubeId/0.jpg"

                Text(
                    text = stringResource(R.string.trailer),
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
                )
                Spacer(Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                "https://www.youtube.com/watch?v=$youtubeId".toUri()
                            )
                            context.startActivity(intent)
                        }
                ) {
                    AsyncImage(
                        model = thumbnailUrl,
                        contentDescription = stringResource(R.string.trailer),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = stringResource(R.string.play),
                        tint = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.Center)
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            // Header de críticas con botón a la derecha (compacto con fondo)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.reviews_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                // Botón compacto con background
                Button(
                    onClick = {
                        // comprueba si ya comentó (coroutine)
                        anime.malId.let { id ->
                            coroutineScope.launch {
                                val alreadyCommented = reviewViewModel.hasUserReviewed(id, currentEmail)
                                if (alreadyCommented) {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.already_reviewed),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    // Ensure latest profile loaded (non-blocking)
                                    profileViewModel.loadUserProfile(currentEmail)
                                    showReviewDialog = true
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddComment,
                        contentDescription = stringResource(R.string.comment),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.comment),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Listado de reseñas
            if (reviews.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_reviews),
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.heightIn(max = 400.dp)
                ) {
                    items(reviews) { review ->
                        ReviewCard(
                            review = review,
                            reviewViewModel = reviewViewModel,
                            currentUserEmail = currentEmail
                        )
                    }
                }
            }
        }
    }

    // Dialogo de "Agregar a lista"
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.add_to_list)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        stringResource(R.string.fav_list) to "fav",
                        stringResource(R.string.watched_list) to "vista",
                        stringResource(R.string.interested_list) to "me_interesa"
                    ).forEach { (label, category) ->

                        Button(
                            onClick = {
                                profileViewModel.addAnime(
                                    UserAnimeData(
                                        malId = anime.malId,
                                        title = anime.title ?: "",
                                        imageUrl = anime.images?.jpg?.image_url ?: "",
                                        category = category,
                                        userEmail = currentEmail
                                    )
                                ) { added ->
                                    if (added) {
                                        Toast.makeText(context, context.getString(R.string.added_success), Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, context.getString(R.string.already_in_list), Toast.LENGTH_SHORT).show()
                                    }
                                }
                                showDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) { Text(label) }
                    }
                }
            },
            confirmButton = {},
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }

    // Dialogo añadir crítica (usa profileViewModel.userName reactivo)
    if (showReviewDialog) {
        AddReviewDialog(
            animeTitle = anime.title ?: "",
            onSave = { comment ->
                coroutineScope.launch {
                    val alreadyCommented = reviewViewModel.hasUserReviewed(anime.malId ?: 0, currentEmail)
                    if (alreadyCommented) {
                        Toast.makeText(context, context.getString(R.string.duplicate_review), Toast.LENGTH_SHORT).show()
                    } else {
                        // finalUserName toma valor reactivo del profileViewModel
                        val finalUserName = profileUserName
                            ?.takeIf { it.isNotBlank() }
                            ?: currentEmail.substringBefore("@")

                        val review = AnimeReview(
                            malId = anime.malId ?: 0,
                            title = anime.title ?: "",
                            userEmail = currentEmail,
                            userName = finalUserName,
                            comment = comment
                        )
                        reviewViewModel.addReview(review)
                        Toast.makeText(context, context.getString(R.string.review_added), Toast.LENGTH_SHORT).show()
                    }
                    showReviewDialog = false
                }
            },
            onDismiss = { showReviewDialog = false }
        )
    }
}

/**
 * ReviewCard muestra: "userName: comentario" en la misma línea si cabe,
 * y el botón eliminar a la derecha (visible solo para el autor).
 */
@Composable
fun ReviewCard(
    review: AnimeReview,
    reviewViewModel: ReviewViewModel,
    currentUserEmail: String
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Texto (userName: comment) que ocupa todo el espacio posible
            Text(
                text = buildString {
                    append(review.userName)
                    append(": ")
                    append(review.comment)
                },
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                maxLines = Int.MAX_VALUE,
                overflow = TextOverflow.Visible
            )

            if (review.userEmail == currentUserEmail) {
                IconButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_review),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    // Confirmación de eliminación
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_review)) },
            text = { Text(stringResource(R.string.delete_review_confirm)) },
            confirmButton = {
                TextButton(onClick = {
                    reviewViewModel.deleteReview(review)
                    showDeleteDialog = false
                }) { Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text(stringResource(R.string.cancel)) }
            }
        )
    }
}

/**
 * AddReviewDialog muestra un diálogo para añadir una crítica a un anime.
 *
 * @param animeTitle Título del anime.
 * @param onSave Callback que se llama al guardar la crítica.
 * @param onDismiss Callback que se llama al cerrar el diálogo.
 */
@Composable
fun AddReviewDialog(
    animeTitle: String,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var reviewText by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("${stringResource(R.string.add_review_to)} $animeTitle") },
        text = {
            OutlinedTextField(
                value = reviewText,
                onValueChange = { reviewText = it },
                label = { Text(stringResource(R.string.your_opinion)) },
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = {
                if (reviewText.isNotBlank()) onSave(reviewText)
            }) { Text(stringResource(R.string.save)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

/**
 * InfoRow muestra una fila con un label y un valor.
 */
@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = FontWeight.Bold)
        Text(value)
    }
}



