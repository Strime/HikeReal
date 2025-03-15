package com.strime.hikereal.ui.screens.snap

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strime.hikereal.domain.repository.ActiveHikeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class SnapUiState(
    val isLoading: Boolean = false,
    val isCapturing: Boolean = false,
    val shouldTakePhoto: Boolean = false,
    val flashEnabled: Boolean = false,
    val frontCapturedPhotos: List<String> = emptyList(),
    val backCapturedPhotos: List<String> = emptyList(),
    val selectedFrontPhotoIndex: Int = -1,
    val selectedBackPhotoIndex: Int = -1,
    val captionText: String = "",
    val photosTakenCount: Int = 0,
    val cameraFacingFront: Boolean = false,
    val isCaptureComplete: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class SnapViewModel @Inject constructor(
    private val activeHikeRepository: ActiveHikeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SnapUiState())
    val uiState: StateFlow<SnapUiState> = _uiState.asStateFlow()

    companion object {
        const val PHOTOS_PER_CAMERA = 3
    }

    private fun toggleCamera() {
        _uiState.update {
            it.copy(
                cameraFacingFront = !it.cameraFacingFront,
                photosTakenCount = 0,
                isCapturing = false,
            )
        }
    }

    fun startPhotoCapture() {
        _uiState.update { it.copy(isCapturing = true, shouldTakePhoto = true) }
    }

    fun createPhotoFile(cacheDir: File?, externalCacheDir: File?): File {
        val outputDir = File(externalCacheDir ?: cacheDir, "photos")
        if (!outputDir.exists()) outputDir.mkdirs()

        return File(
            outputDir,
            "${if (_uiState.value.cameraFacingFront) "front" else "back"}_${System.currentTimeMillis()}.jpg"
        )
    }

    fun onPhotoTaken(photoUri: String, isFrontCamera: Boolean) {
        val currentPhotoCount = _uiState.value.photosTakenCount + 1

        if (isFrontCamera) {
            val updatedPhotos = _uiState.value.frontCapturedPhotos.toMutableList()
            updatedPhotos.add(photoUri)
            _uiState.update {
                it.copy(
                    frontCapturedPhotos = updatedPhotos,
                    shouldTakePhoto = false,
                    photosTakenCount = currentPhotoCount
                )
            }
        } else {
            val updatedPhotos = _uiState.value.backCapturedPhotos.toMutableList()
            updatedPhotos.add(photoUri)
            _uiState.update {
                it.copy(
                    backCapturedPhotos = updatedPhotos,
                    shouldTakePhoto = false,
                    photosTakenCount = currentPhotoCount
                )
            }
        }

        if (currentPhotoCount >= PHOTOS_PER_CAMERA) {
            if (!isFrontCamera) {
                toggleCamera()
            } else if (_uiState.value.backCapturedPhotos.size >= PHOTOS_PER_CAMERA) {
                completeCaptureProcess()
            }
        } else {
            viewModelScope.launch {
                delay(300)
                triggerNextPhoto()
            }
        }
    }

    private fun triggerNextPhoto() {
        _uiState.update { it.copy(shouldTakePhoto = true) }
    }

    fun onCaptureError(errorMessage: String) {
        _uiState.update {
            it.copy(
                isCapturing = false,
                errorMessage = errorMessage
            )
        }
    }

    private fun completeCaptureProcess() {
        _uiState.update {
            it.copy(
                isCapturing = false,
                isCaptureComplete = true,
                selectedFrontPhotoIndex = 0,
                selectedBackPhotoIndex = 0
            )
        }
    }

    fun selectFrontPhoto(index: Int) {
        if (index < 0 || index >= _uiState.value.frontCapturedPhotos.size) return
        _uiState.update { it.copy(selectedFrontPhotoIndex = index) }
    }

    fun selectBackPhoto(index: Int) {
        if (index < 0 || index >= _uiState.value.backCapturedPhotos.size) return
        _uiState.update { it.copy(selectedBackPhotoIndex = index) }
    }

    fun discardPhotos() {
        _uiState.update {
            it.copy(
                frontCapturedPhotos = emptyList(),
                backCapturedPhotos = emptyList(),
                selectedFrontPhotoIndex = -1,
                selectedBackPhotoIndex = -1,
                captionText = "",
                isCaptureComplete = false,
                cameraFacingFront = false,
                photosTakenCount = 0
            )
        }
    }

    fun savePhotos() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val selectedFrontPhoto = if (_uiState.value.selectedFrontPhotoIndex >= 0) {
                    _uiState.value.frontCapturedPhotos[_uiState.value.selectedFrontPhotoIndex]
                } else null

                val selectedBackPhoto = if (_uiState.value.selectedBackPhotoIndex >= 0) {
                    _uiState.value.backCapturedPhotos[_uiState.value.selectedBackPhotoIndex]
                } else null

                if (selectedFrontPhoto != null && selectedBackPhoto != null) {
                    Log.d(
                        "SnapViewModel",
                        "Saving HikeReal - Front: $selectedFrontPhoto, Back: $selectedBackPhoto"
                    )
                    val activeHikeId = activeHikeRepository.getCurrentActiveHikeId()

                    if (activeHikeId != null) {
                        activeHikeRepository.saveSelectedFrontCameraPhoto(
                            activeHikeId,
                            selectedFrontPhoto
                        )
                        activeHikeRepository.saveSelectedBackCameraPhoto(
                            activeHikeId,
                            selectedBackPhoto
                        )

                    } else {
                        Log.w("SnapViewModel", "No active hike found to save photos")
                    }
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        frontCapturedPhotos = emptyList(),
                        backCapturedPhotos = emptyList(),
                        selectedFrontPhotoIndex = -1,
                        selectedBackPhotoIndex = -1,
                        captionText = "",
                        isCaptureComplete = false,
                        errorMessage = null,
                        photosTakenCount = 0
                    )
                }
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
}