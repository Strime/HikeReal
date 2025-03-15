package com.strime.hikereal.ui.screens.snap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SnapViewModel @Inject constructor() : ViewModel() {

    // Define UI state for snap screen
    data class SnapUiState(
        val isLoading: Boolean = false,
        val isCapturing: Boolean = false,
        val challengePrompt: String = "",
        val remainingTime: String = "",
        val flashEnabled: Boolean = false,
        val frontCapturedPhotos: List<String> = emptyList(),
        val backCapturedPhotos: List<String> = emptyList(),
        val selectedFrontPhotoIndex: Int = -1,
        val selectedBackPhotoIndex: Int = -1,
        val hikingMetrics: HikingMetrics? = null,
        val captionText: String = "",
        val cameraFacingFront: Boolean = false,
        val isCaptureComplete: Boolean = false,
        val errorMessage: String? = null
    )

    data class HikingMetrics(
        val elevation: Int, // in meters
        val distance: Float, // in kilometers
        val duration: String // formatted string
    )

    private val _uiState = MutableStateFlow(SnapUiState())
    val uiState: StateFlow<SnapUiState> = _uiState.asStateFlow()

    // Timer for challenge countdown
    private var remainingSeconds = 0
    private var timerJob: kotlinx.coroutines.Job? = null

    init {
        loadChallengeData()
        startCountdownTimer()
    }

    private fun loadChallengeData() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                // Simulate network call to get today's challenge
                delay(500)

                // Mock data for today's challenge
                val challenge = getRandomChallenge()
                val hikingMetrics = HikingMetrics(
                    elevation = 450,
                    distance = 3.2f,
                    duration = "1h 15m"
                )

                // Set initial timer (10 minutes)
                remainingSeconds = 10 * 60

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        challengePrompt = challenge,
                        hikingMetrics = hikingMetrics,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to load challenge: ${e.message}"
                    )
                }
            }
        }
    }

    private fun startCountdownTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (remainingSeconds > 0) {
                val minutes = remainingSeconds / 60
                val seconds = remainingSeconds % 60
                val timeString = String.format("%d:%02d", minutes, seconds)

                _uiState.update { it.copy(remainingTime = timeString) }

                delay(1000)
                remainingSeconds--
            }

            // Time's up
            _uiState.update {
                it.copy(
                    remainingTime = "0:00",
                    errorMessage = "Time's up! You missed today's HikeReal challenge."
                )
            }
        }
    }

    fun toggleFlash() {
        _uiState.update { it.copy(flashEnabled = !it.flashEnabled) }
    }

    fun toggleCamera() {
        _uiState.update { it.copy(cameraFacingFront = !it.cameraFacingFront) }
    }

    // Méthode pour démarrer la capture de photos en séquence
    fun startPhotoCapture() {
        if (_uiState.value.isCapturing) return

        _uiState.update { it.copy(
            isCapturing = true,
            frontCapturedPhotos = emptyList(),
            backCapturedPhotos = emptyList(),
            selectedFrontPhotoIndex = -1,
            selectedBackPhotoIndex = -1
        )}

        capturePhotoSequence()
    }

    // Capture 3 photos des deux caméras, avec un délai entre chaque photo
    private fun capturePhotoSequence() {
        viewModelScope.launch {
            try {
                val frontPhotos = mutableListOf<String>()
                val backPhotos = mutableListOf<String>()

                // Prendre 3 photos avec la caméra frontale
                for (i in 0 until 3) {
                    // Simule la capture avec la caméra frontale
                    val frontPhotoUri = "file:///data/user/0/com.hikereal.app/cache/front_photo_${UUID.randomUUID()}.jpg"
                    frontPhotos.add(frontPhotoUri)

                    // Mise à jour de l'UI pour montrer la progression
                    _uiState.update { it.copy(
                        frontCapturedPhotos = frontPhotos.toList()
                    )}

                    // Délai entre les photos
                    delay(700)
                }

                // Prendre 3 photos avec la caméra arrière
                for (i in 0 until 3) {
                    // Simule la capture avec la caméra arrière
                    val backPhotoUri = "file:///data/user/0/com.hikereal.app/cache/back_photo_${UUID.randomUUID()}.jpg"
                    backPhotos.add(backPhotoUri)

                    // Mise à jour de l'UI pour montrer la progression
                    _uiState.update { it.copy(
                        backCapturedPhotos = backPhotos.toList()
                    )}

                    // Délai entre les photos
                    delay(700)
                }

                // Mettre à jour l'état avec toutes les photos capturées
                _uiState.update { it.copy(
                    isCapturing = false,
                    frontCapturedPhotos = frontPhotos,
                    backCapturedPhotos = backPhotos,
                    selectedFrontPhotoIndex = 0, // Sélectionner la première photo par défaut
                    selectedBackPhotoIndex = 0,  // Sélectionner la première photo par défaut
                    isCaptureComplete = true
                )}

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isCapturing = false,
                        errorMessage = "Failed to capture photos: ${e.message}"
                    )
                }
            }
        }
    }

    // Méthode pour capturer une photo individuelle (peut être utilisée par CameraX)
    fun onPhotoTaken(photoUri: String, isFrontCamera: Boolean) {
        viewModelScope.launch {
            if (isFrontCamera) {
                val currentPhotos = _uiState.value.frontCapturedPhotos.toMutableList()
                currentPhotos.add(photoUri)
                _uiState.update { it.copy(frontCapturedPhotos = currentPhotos) }
            } else {
                val currentPhotos = _uiState.value.backCapturedPhotos.toMutableList()
                currentPhotos.add(photoUri)
                _uiState.update { it.copy(backCapturedPhotos = currentPhotos) }
            }
        }
    }

    // Sélectionner une photo front
    fun selectFrontPhoto(index: Int) {
        if (index < 0 || index >= _uiState.value.frontCapturedPhotos.size) return
        _uiState.update { it.copy(selectedFrontPhotoIndex = index) }
    }

    // Sélectionner une photo back
    fun selectBackPhoto(index: Int) {
        if (index < 0 || index >= _uiState.value.backCapturedPhotos.size) return
        _uiState.update { it.copy(selectedBackPhotoIndex = index) }
    }

    fun updateCaption(caption: String) {
        _uiState.update { it.copy(captionText = caption) }
    }

    fun discardPhoto() {
        _uiState.update { it.copy(
            frontCapturedPhotos = emptyList(),
            backCapturedPhotos = emptyList(),
            selectedFrontPhotoIndex = -1,
            selectedBackPhotoIndex = -1,
            captionText = "",
            isCaptureComplete = false
        )}
    }

    fun savePhoto() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                // Les photos sélectionnées
                val selectedFrontPhoto = if (_uiState.value.selectedFrontPhotoIndex >= 0) {
                    _uiState.value.frontCapturedPhotos[_uiState.value.selectedFrontPhotoIndex]
                } else null

                val selectedBackPhoto = if (_uiState.value.selectedBackPhotoIndex >= 0) {
                    _uiState.value.backCapturedPhotos[_uiState.value.selectedBackPhotoIndex]
                } else null

                // Simulate saving photos to backend
                delay(1000)

                // In a real app, this would make an API call to save the photos with metadata

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        frontCapturedPhotos = emptyList(),
                        backCapturedPhotos = emptyList(),
                        selectedFrontPhotoIndex = -1,
                        selectedBackPhotoIndex = -1,
                        captionText = "",
                        isCaptureComplete = false,
                        errorMessage = null
                    )
                }

                // Reset the timer after successful submission
                timerJob?.cancel()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to save photos: ${e.message}"
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    // Helper function to get a random challenge prompt
    private fun getRandomChallenge(): String {
        val challenges = listOf(
            "Capture the path ahead of you",
            "Show us your current view",
            "Take a photo of something blue on your hike",
            "Capture a natural detail that caught your eye",
            "Show us your hiking buddy",
            "Photograph what's beneath your feet",
            "Capture the furthest point you can see",
            "Show us the weather you're experiencing",
            "Take a photo of something that made you smile",
            "Capture a trail marker or sign"
        )
        return challenges.random()
    }
}