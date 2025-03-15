package com.strime.hikereal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strime.hikereal.R
import com.strime.hikereal.data.local.entity.ActiveHikeEntity
import com.strime.hikereal.data.repository.HikeException
import com.strime.hikereal.domain.model.ActiveHike
import com.strime.hikereal.domain.repository.ActiveHikeRepository
import com.strime.hikereal.domain.repository.UserRepository
import com.strime.hikereal.ui.util.toFormattedDistance
import com.strime.hikereal.ui.util.toFormattedDuration
import com.strime.hikereal.utils.UiState
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

    private val _activeHikeState = MutableStateFlow(ActiveHike())
    val activeHikeState: StateFlow<ActiveHike> = _activeHikeState.asStateFlow()

    private val _operationState = MutableStateFlow<UiState<Unit>>(UiState.Initial)
    val operationState: StateFlow<UiState<Unit>> = _operationState.asStateFlow()

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
                formattedDistance = activeHike.currentDistance.toFormattedDistance(),
                alreadyTookPhoto = activeHike.backCameraUri != null && activeHike.frontCameraUri != null
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
            try {
                _operationState.value = UiState.Loading
                val userProfile = userRepository.getUserProfile()
                _activeHikeState.value.hikeId?.let { hikeId ->
                    activeHikeRepository.completeHike(hikeId, userProfile = userProfile)
                }
                _operationState.value = UiState.Success(Unit)
            } catch (e: HikeException) {
                _operationState.value = UiState.Error(e.errorCode)
            } catch (e: Exception) {
                _operationState.value = UiState.Error(R.string.error_unknown)
            }

        }
    }

    fun resetOperationState() {
        _operationState.value = UiState.Initial
    }
}