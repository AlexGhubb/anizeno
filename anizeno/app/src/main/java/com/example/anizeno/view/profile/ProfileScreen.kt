package com.example.anizeno.view.profile

import android.app.Activity
import android.widget.Toast
import com.example.anizeno.R
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.anizeno.data.local.room_db.UserAnimeData
import com.example.anizeno.view.profile.ProfileViewModel

/**
 * Muestra la pantalla de perfil del usuario.
 */
@Suppress("UNUSED_VALUE")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ProfileScreen(
    userEmail: String,
    viewModel: ProfileViewModel = viewModel(),
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onAnimeClick: (UserAnimeData) -> Unit
) {
//    val windowSize= calculateWindowSizeClass(LocalContext.current as Activity)
//    val isTablet= windowSize.widthSizeClass == WindowWidthSizeClass.Expanded
//    val isHorizontal= windowSize.widthSizeClass == WindowWidthSizeClass.Medium
//    val isVertical= windowSize.widthSizeClass == WindowWidthSizeClass.Compact


    val context = LocalContext.current

    val userAnimes by viewModel.userAnimes.collectAsState()
    val profileImageUri by viewModel.profileImageUri.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val userBio by viewModel.userBio.collectAsState()

    var selectedCategory by remember { mutableStateOf("fav") }
    var showEditDialog by remember { mutableStateOf(false) }


    val categories = listOf(
        "fav" to stringResource(R.string.fav_list),
        "vista" to stringResource(R.string.watched_list),
        "me_interesa" to stringResource(R.string.interested_list)
    )


    LaunchedEffect(Unit) {
        viewModel.loadUserProfile(userEmail)
        viewModel.loadCategory(selectedCategory, userEmail)
    }

    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            viewModel.saveUserProfileImage(userEmail, it.toString())
            Toast.makeText(context, context.getString(R.string.photo_updated), Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("") }, navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }, actions = {
                    IconButton(onClick = onLogoutClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = stringResource(R.string.logout)
                        )
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                ))
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondary,
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f),
                            Color.Black
                        )
                    )
                )
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier.size(130.dp),
                contentAlignment = Alignment.BottomEnd
            ) {

                Image(
                    painter = rememberAsyncImagePainter(
                        profileImageUri ?: "https://cdn.myanimelist.net/img/sp/icon/apple-touch-icon-256.png"
                    ),
                    contentDescription = stringResource(R.string.profile_photo),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable {
                            pickMedia.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                )


                IconButton(
                    onClick = { showEditDialog = true },
                    modifier = Modifier
                        .size(36.dp)
                        .offset(x = (-6).dp, y = (-6).dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceBright)
                        .shadow(6.dp, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit_profile_button),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))



            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = userName.takeUnless { it.isNullOrBlank() }
                        ?: userEmail.substringBefore("@"),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }


            Spacer(Modifier.height(8.dp))


            if (!userBio.isNullOrBlank()) {
                Text(
                    text = userBio ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            } else {
                Text(
                    text = stringResource(R.string.tap_to_edit),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                    modifier = Modifier.clickable { showEditDialog = true }
                )
            }

            Spacer(Modifier.height(20.dp))


            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                categories.forEach { (key, label) ->
                    FilterChip(
                        selected = selectedCategory == key,
                        onClick = {
                            selectedCategory = key
                            viewModel.loadCategory(key, userEmail)
                        },
                        label = { Text(label) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))


            if (userAnimes.isEmpty()) {
                Text(
                    text = stringResource(R.string.category_empty),
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(userAnimes) { anime ->
                        UserAnimeCard(
                            anime = anime,
                            onDelete = {
                                viewModel.removeAnime(
                                    anime = anime,
                                    userEmail = userEmail,
                                    currentCategory = selectedCategory
                                )
                            },
                            onMove = { newCategory ->
                                viewModel.moveAnimeTo(
                                    anime = anime,
                                    newCategory = newCategory,
                                    userEmail = userEmail
                                )
                            },
                            onClick = { onAnimeClick(anime) }
                        )
                    }
                }
            }
        }


        if (showEditDialog) {
            EditProfileDialog(
                currentName = userName ?: userEmail,
                currentBio = userBio ?: "",
                onDismiss = { showEditDialog = false },
                onSave = { newName, newBio ->
                    viewModel.updateUserInfo(userEmail, newName, newBio)
                    showEditDialog = false
                }
            )
        }
    }
}

/**
 * Muestra una tarjeta para mostrar un anime del usuario.
 */
@Composable
fun UserAnimeCard(
    anime: UserAnimeData,
    onDelete: () -> Unit,
    onMove: (String) -> Unit,
    onClick: () -> Unit
) {
    var showMoveDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(anime.imageUrl),
                contentDescription = anime.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    anime.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            IconButton(onClick = { showMoveDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.move_anime_btn),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    if (showMoveDialog) {
        MoveAnimeDialog(
            currentCategory = anime.category,
            onMove = {
                onMove(it)
                showMoveDialog = false
            },
            onDismiss = { showMoveDialog = false }
        )
    }
}


/**
 * Muestra un diálogo para editar el perfil del usuario.
 */
@Composable
fun EditProfileDialog(
    currentName: String,
    currentBio: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var bio by remember { mutableStateOf(currentBio) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.edit_profile)) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.username)) },
                    singleLine = true
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text(stringResource(R.string.bio)) },
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(name, bio) }) { Text(stringResource(R.string.save)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

@Composable
fun MoveAnimeDialog(
    currentCategory: String,
    onMove: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.move_anime_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    "fav" to stringResource(R.string.fav_list),
                    "vista" to stringResource(R.string.watched_list),
                    "me_interesa" to stringResource(R.string.interested_list)
                ).filter { it.first != currentCategory }
                    .forEach { (category, label) ->
                        Button(
                            onClick = { onMove(category) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Text(label)
                        }
                    }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}


