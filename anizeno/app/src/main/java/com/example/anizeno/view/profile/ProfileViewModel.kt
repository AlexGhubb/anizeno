package com.example.anizeno.view.profile

import android.app.Application
import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.anizeno.data.local.room_db.AppDatabase
import com.example.anizeno.data.local.room_db.UserAnimeData
import com.example.anizeno.data.local.room_db.UserProfile
import com.example.anizeno.data.remote.dto.AnimeData
import com.example.anizeno.data.remote.RetrofitInstance
import com.example.anizeno.data.repository.UserAnimeDataRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.Companion.getInstance(application).userAnimeDataDao()
    private val profileDao = AppDatabase.Companion.getInstance(application).userProfileDao()
    private val reviewDao = AppDatabase.Companion.getInstance(application).animeReviewDao()
    private val repo = UserAnimeDataRepositoryImpl(dao)

    // ---------- ESTADOS ----------
    private val _userAnimes = MutableStateFlow<List<UserAnimeData>>(emptyList())
    val userAnimes = _userAnimes.asStateFlow()

    private val _profileImageUri = MutableStateFlow<String?>(null)
    val profileImageUri = _profileImageUri.asStateFlow()

    private val _userName = MutableStateFlow<String?>(null)
    val userName = _userName.asStateFlow()

    private val _userBio = MutableStateFlow<String?>(null)
    val userBio = _userBio.asStateFlow()

    /**
     * Cargar lista de animes de una categoría
     */
    fun loadCategory(category: String, userEmail: String) {
        viewModelScope.launch {
            _userAnimes.value = repo.getCategory(category, userEmail)
        }
    }
    /**
    * eliminar el anime de una lista
    */
    fun removeAnime(anime: UserAnimeData, userEmail: String, currentCategory: String) {
        viewModelScope.launch {
            repo.removeAnime(anime)
            _userAnimes.value = repo.getCategory(currentCategory, userEmail)
        }
    }

    /**
    * añadir el anime a una lista
    * se comprueba si existe antes de añadirlo
    */
    fun addAnime(anime: UserAnimeData, onResult: (Boolean) -> Unit={}) {
        viewModelScope.launch {
            val malId= anime.malId ?: return@launch onResult(false)
            val exists =  repo.existsInCategory(
                malId = malId,
                category = anime.category,
                userEmail = anime.userEmail
            )
            if (exists) onResult(false)
            else {
                repo.addAnime(anime)
                onResult(true)
            }
        }
    }

    /**
    * 🔹 Cargar perfil del usuario
    * 🔹 Cargar imagen de perfil
    * 🔹 Cargar nombre y bio del perfil
    */
    fun loadUserProfile(userEmail: String) {
        viewModelScope.launch {
            val profile = profileDao.getUserProfile(userEmail)
            _profileImageUri.value = profile?.profileImageUri
            _userName.value = profile?.userName
            _userBio.value = profile?.userBio
        }
    }
    /**
    * 🔹 Actualizar imagen de perfil
    * 🔹 Actualizar nombre y bio del perfil
    */
    fun saveUserProfileImage(userEmail: String, imageUri: String) {
        viewModelScope.launch {
            try {
                val uri = imageUri.toUri()
                val resolver = getApplication<Application>().contentResolver
                resolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                val existing = profileDao.getUserProfile(userEmail)
                val merged = existing?.copy(profileImageUri = imageUri)
                    ?: UserProfile(
                        userEmail = userEmail,
                        userName = "",
                        userBio = "",
                        profileImageUri = imageUri
                    )

                profileDao.insertOrUpdateProfile(merged)
                _profileImageUri.value = merged.profileImageUri

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 🔹 Actualiza nombre y bio del perfil
     * y sincroniza el nombre en todas las reseñas del usuario.
     */
    fun updateUserInfo(userEmail: String, name: String?, bioText: String?) {
        viewModelScope.launch {
            val existing = profileDao.getUserProfile(userEmail)

            val merged = existing?.copy(
                userName = name?.takeIf { it.isNotBlank() } ?: "",
                userBio = bioText ?: "" // permite bio vacía
            ) ?: UserProfile(
                userEmail = userEmail,
                userName = name?.takeIf { it.isNotBlank() } ?: "",
                userBio = bioText ?: "",
                profileImageUri = null
            )

            profileDao.insertOrUpdateProfile(merged)

            _userName.value = merged.userName
            _userBio.value = merged.userBio
            _profileImageUri.value = merged.profileImageUri

            try {
                reviewDao.updateUserNameForEmail(userEmail, merged.userName?: "anon")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun moveAnimeTo(anime: UserAnimeData, newCategory: String, userEmail: String) {
        viewModelScope.launch {
            val updated = anime.copy(category = newCategory)
            repo.updateAnime(updated)
            _userAnimes.value = repo.getCategory(newCategory, userEmail)
        }
    }

    /**
     * 🔹 Busca un anime por su título
     * 🔹 Devuelve el primer resultado de la lista
     */
    fun fetchAnimeByTitle(title: String, onResult: (AnimeData?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiSearch.searchAnime(title)
                val anime = response.data.firstOrNull()
                onResult(anime)
            } catch (_: Exception) {
                onResult(null)
            }
        }
    }

    /**
     * 🔹 Busca un anime por su ID
     * 🔹 Devuelve el resultado
     */
    fun fetchAnimeById(id: Int, onResult: (AnimeData?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiSearch.getAnimeById(id)
                onResult(response.data)
            } catch (_: Exception) {
                onResult(null)
            }
        }
    }
}