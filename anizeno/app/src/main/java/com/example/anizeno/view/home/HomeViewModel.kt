package com.example.anizeno.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anizeno.data.remote.dto.AnimeData
import com.example.anizeno.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val seasonNowList: List<AnimeData>? = null,
    val topAnimeList: List<AnimeData>? = null,
    val upcomingList: List<AnimeData>? = null,
    val error: String? = null
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    fun seasonNow() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = RetrofitInstance.apiSearch.seasonList()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    seasonNowList = response.data,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun topAnimes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = RetrofitInstance.apiSearch.topList()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    topAnimeList = response.data,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun upcomingList() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = RetrofitInstance.apiSearch.upcomingList()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    upcomingList = response.data,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun resetState() {
        _uiState.value = HomeUiState()
    }

    fun clearAnimeList() {
        _uiState.value = _uiState.value.copy(
            seasonNowList = emptyList(),
            topAnimeList = emptyList(),
            upcomingList = emptyList(),
            isLoading = false,
            error = null
        )
    }
}