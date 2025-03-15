package com.strime.hikereal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strime.hikereal.data.local.entity.ActiveHikeEntity
import com.strime.hikereal.domain.model.ActiveHikeUiState
import com.strime.hikereal.domain.repository.ActiveHikeRepository
import com.strime.hikereal.domain.repository.UserRepository
import com.strime.hikereal.ui.util.toFormattedDistance
import com.strime.hikereal.ui.util.toFormattedDuration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val activeHikeRepository: ActiveHikeRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _activeHikeState = MutableStateFlow(ActiveHikeUiState())
    val activeHikeState: StateFlow<ActiveHikeUiState> = _activeHikeState.asStateFlow()

    private var durationUpdateJob: Job? = null

    init {
        observeActiveHike()
    }

    private fun observeActiveHike() {
        viewModelScope.launch {
            activeHikeRepository.getCurrentActiveHike().collect { activeHike ->
                if (activeHike != null) {
                    updateActiveHikeState(activeHike)
                    startDurationUpdates(activeHike)
                } else {
                    _activeHikeState.update { it.copy(hikeId = null) }
                    durationUpdateJob?.cancel()
                }
            }
        }
    }

    private fun updateActiveHikeState(activeHike: ActiveHikeEntity) {
        _activeHikeState.update { currentState ->
            currentState.copy(
                hikeId = activeHike.id,
                formattedDistance = activeHike.currentDistance.toFormattedDistance()
            )
        }
    }

    private fun startDurationUpdates(activeHike: ActiveHikeEntity) {
        durationUpdateJob?.cancel()
        durationUpdateJob = viewModelScope.launch {
            while (isActive) {
                val totalDurationMs = System.currentTimeMillis() - activeHike.startTime

                _activeHikeState.update { currentState ->
                    currentState.copy(formattedDuration = totalDurationMs.toFormattedDuration())
                }

                delay(1000)
            }
        }
    }


    fun completeHike() {
        viewModelScope.launch {
            val userProfile = userRepository.getUserProfile()
            _activeHikeState.value.hikeId?.let { hikeId ->
                activeHikeRepository.completeHike(hikeId, userProfile = userProfile)
            }
        }
    }
}