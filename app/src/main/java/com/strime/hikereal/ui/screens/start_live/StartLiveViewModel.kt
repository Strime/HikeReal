package com.strime.hikereal.ui.screens.start_live

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strime.hikereal.domain.repository.ActiveHikeRepository
import com.strime.hikereal.domain.repository.UserRepository
import com.strime.hikereal.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartLiveViewModel @Inject constructor(
    private val activeHikeRepository: ActiveHikeRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _startHikeState = MutableStateFlow<UiState<String>>(UiState.Initial)
    val startHikeState: StateFlow<UiState<String>> = _startHikeState

    fun startNewHike() {
        _startHikeState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val userId = userRepository.getUserId()

                val hikeId = activeHikeRepository.startNewHike(userId)

                _startHikeState.value = UiState.Success(hikeId)
            } catch (e: Exception) {
                // Gérer les erreurs
                _startHikeState.value = UiState.Error(e.message ?: "")
            }
        }
    }

}