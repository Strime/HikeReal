package com.strime.hikereal.ui.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strime.hikereal.data.mapper.toHikePost
import com.strime.hikereal.domain.model.HikePost
import com.strime.hikereal.domain.repository.HikeRepository
import com.strime.hikereal.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val hikeRepository: HikeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<HikePost>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<HikePost>>> = _uiState.asStateFlow()

    init {
        loadFeed()
    }

    private fun loadFeed() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading

                hikeRepository.getAllHikes().collectLatest { hikeEntities ->
                    val hikePosts = hikeEntities.map { it.toHikePost(

                    ) }
                    _uiState.value = UiState.Success(hikePosts)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Impossible de charger le flux: ${e.message}")
            }
        }
    }
}