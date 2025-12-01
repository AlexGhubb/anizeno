package com.example.anizeno.view.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import com.example.anizeno.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.anizeno.data.remote.dto.AnimeData
import com.example.anizeno.view.profile.ProfileViewModel

/**
 * Composable que representa la pantalla principal de la aplicación.
 *
 * @param viewModel El ViewModel asociado a la pantalla.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel(),
    userName: String = "Otaku",
    onSearchClick: () -> Unit = {},
    onFavoritesClick: () -> Unit = {},
    onAnimeClick: (AnimeData) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf(
        stringResource(R.string.nav_home),
        stringResource(R.string.nav_profile),
        stringResource(R.string.nav_explore)
    )
    val icons = listOf(Icons.Default.Home, Icons.Default.Person, Icons.Default.Explore)

    var showSeasonList by remember { mutableStateOf(false) }
    var showTopList by remember { mutableStateOf(false) }
    var showUpcomingList by remember { mutableStateOf(false) }

    val profileName by profileViewModel.userName.collectAsState()

    LaunchedEffect(Unit) {
        if (uiState.seasonNowList.isNullOrEmpty() ||
            uiState.topAnimeList.isNullOrEmpty() ||
            uiState.upcomingList.isNullOrEmpty()
        ) {
            viewModel.seasonNow()
            viewModel.topAnimes()
            viewModel.upcomingList()
        }
        profileViewModel.loadUserProfile(userName)
    }

    val userName = when {
        !profileName.isNullOrBlank() -> profileName!!
        else -> userName.substringBefore("@")
    }

    Box(
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
    ) {
        Scaffold(
            containerColor = Color.Unspecified,
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                ) {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                                when (index) {
                                    1 -> onFavoritesClick()
                                    2 -> onSearchClick()
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = icons[index],
                                    contentDescription = item
                                )
                            },
                            label = {
                                Text(
                                    text = item,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                unselectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                            )
                        )
                    }
                }
            },
            contentColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.home_welcome, userName),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Text(
                    text = stringResource(R.string.home_subtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
                )

                ExpandableAnimeList(
                    title = stringResource(R.string.home_upcoming),
                    animes = uiState.upcomingList.orEmpty(),
                    isExpanded = showUpcomingList,
                    onExpandClick = { showUpcomingList = !showUpcomingList },
                    onAnimeClick = onAnimeClick
                )
                Spacer(modifier = Modifier.height(24.dp))

                ExpandableAnimeList(
                    title = stringResource(R.string.home_current),
                    animes = uiState.seasonNowList.orEmpty(),
                    isExpanded = showSeasonList,
                    onExpandClick = { showSeasonList = !showSeasonList },
                    onAnimeClick = onAnimeClick
                )

                Spacer(modifier = Modifier.height(24.dp))

                ExpandableAnimeList(
                    title = stringResource(R.string.home_top),
                    animes = uiState.topAnimeList.orEmpty(),
                    isExpanded = showTopList,
                    onExpandClick = { showTopList = !showTopList },
                    onAnimeClick = onAnimeClick
                )
            }
        }
    }
}



/**
 * Composable que muestra la lista de animes expandida o colapsada.
 */
@Composable
fun ExpandableAnimeList(
    title: String,
    animes: List<AnimeData>,
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    onAnimeClick: (AnimeData) -> Unit
) {
    val displayList = if (isExpanded) animes else animes.take(3)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Button(
            onClick = onExpandClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isExpanded)
                    MaterialTheme.colorScheme.secondary
                else
                    MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = if (isExpanded)
                        Icons.Default.KeyboardArrowUp
                    else
                        Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded)
                        stringResource(R.string.hide_list)
                    else
                        stringResource(R.string.show_list),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        AnimatedVisibility(visible = displayList.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                displayList.forEach { anime ->
                    AnimeItem(
                        anime = anime,
                        onClick = { onAnimeClick(anime) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (!isExpanded && animes.size > 4) {
                    Text(
                        text = stringResource(R.string.see_more),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable { onExpandClick() }
                            .padding(top = 4.dp)
                    )
                }
            }
        }
    }
}



/**
 * Composable que muestra un ítem de anime en la lista.
 */
@Composable
fun AnimeItem(anime: AnimeData, onClick: () -> Unit) {
    val imageUrl = anime.images?.jpg?.image_url ?: ""
    val title = anime.title ?: stringResource(R.string.no_title)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imageUrl.ifBlank { "https://cdn.myanimelist.net/img/sp/icon/apple-touch-icon-256.png" },
                contentDescription = title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}