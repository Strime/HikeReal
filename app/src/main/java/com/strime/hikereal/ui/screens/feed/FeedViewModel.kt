package com.strime.hikereal.ui.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strime.hikereal.R
import com.strime.hikereal.data.mapper.toHikePost
import com.strime.hikereal.domain.model.HikePost
import com.strime.hikereal.domain.repository.BadgeRepository
import com.strime.hikereal.domain.repository.HikeRepository
import com.strime.hikereal.domain.repository.UserRepository
import com.strime.hikereal.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val hikeRepository: HikeRepository,
    private val badgeRepository: BadgeRepository,
) : ViewModel() {
    private val userId = userRepository.getUserId()

    private val _uiState = MutableStateFlow<UiState<List<HikePost>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<HikePost>>> = _uiState.asStateFlow()


    init {
        loadFeed()
        viewModelScope.launch(Dispatchers.IO) {
            hikeRepository.initializeDatabase(userId)
            badgeRepository.initializeDatabase(userId)
        }
    }

    private fun loadFeed() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading

                hikeRepository.getAllHikes().collectLatest { hikeEntities ->
                    val hikePosts = hikeEntities.map {
                        it.toHikePost()
                    }
                    _uiState.value = UiState.Success(hikePosts)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(R.string.error_unknown)
            }
        }
    }
}