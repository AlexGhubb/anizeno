package com.example.anizeno.view.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anizeno.data.remote.dto.AnimeData
import com.example.anizeno.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AnimeUiState(
    val isLoading: Boolean = false,
    var animeList: List<AnimeData>? = null,
    val error: String? = null
)

class SearchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AnimeUiState())
    val uiState: StateFlow<AnimeUiState> = _uiState

    fun searchAnime(title: String) {
        viewModelScope.launch {
            _uiState.value = AnimeUiState(isLoading = true)
            try {
                val response = RetrofitInstance.apiSearch.searchAnime(title)
                _uiState.value = AnimeUiState(animeList = response.data)
            } catch (e: Exception) {
                _uiState.value = AnimeUiState(error = e.message)
            }
        }
    }

    fun resetState() {
        _uiState.value = AnimeUiState()
    }

    fun clearAnimeList() {
        _uiState.value = _uiState.value.copy(
            animeList = emptyList(),
            isLoading = false,
            error = null
        )
    }

}
