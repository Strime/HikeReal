package com.strime.hikereal.ui.screens.live

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strime.hikereal.domain.repository.ActiveHikeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LiveUiState(
    val hasActiveHike: Boolean
)

@HiltViewModel
class LiveViewModel @Inject constructor(
    private val activeHikeRepository: ActiveHikeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LiveUiState(false))
    val uiState: StateFlow<LiveUiState> = _uiState.asStateFlow()

    init {
        checkActiveHike()
    }

    private fun checkActiveHike() {
        viewModelScope.launch {
            activeHikeRepository.getCurrentActiveHike().collect { activeHike ->
                _uiState.update { currentState ->
                    currentState.copy(hasActiveHike = activeHike != null)
                }
            }
        }
    }
}