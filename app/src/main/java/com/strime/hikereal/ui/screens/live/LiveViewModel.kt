package com.strime.hikereal.ui.screens.live

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strime.hikereal.domain.model.Hiker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiveViewModel @Inject constructor() : ViewModel() {

    // Définition de l'état UI pour l'écran Live
    data class LiveUiState(
        val isLoading: Boolean = false,
        val nearbyHikers: List<Hiker> = emptyList(),
        val selectedHiker: Hiker? = null,
        val userTrekCount: Int = 0,
        val canStartLive: Boolean = false,
        val errorMessage: String? = null,
        val isRefreshing: Boolean = false
    )

    private val _uiState = MutableStateFlow(LiveUiState())
    val uiState: StateFlow<LiveUiState> = _uiState.asStateFlow()

    init {
        loadNearbyHikers()
        loadUserTrekCount()
    }

    private fun loadNearbyHikers() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                // Simuler un appel réseau
                delay(1000)

                val hikers = listOf(
                    Hiker(
                        id = "1",
                        name = "Sarah Connor",
                        location = "Alpes du Mont-Blanc",
                        distance = 2.3,
                        activity = "Ascension du Mont Blanc",
                        elevation = 2841,
                        duration = "3h 42min",
                        profileImageRes = android.R.drawable.ic_menu_camera,
                        trekCount = 24
                    ),
                    Hiker(
                        id = "2",
                        name = "John Muir",
                        location = "Yosemite Valley",
                        distance = 5.7,
                        activity = "Randonnée: Panorama Trail",
                        elevation = 1280,
                        duration = "2h 15min",
                        profileImageRes = android.R.drawable.ic_menu_gallery,
                        trekCount = 18
                    ),
                    Hiker(
                        id = "3",
                        name = "Alex Honnold",
                        location = "Parc national des Écrins",
                        distance = 3.1,
                        activity = "Escalade: Face Nord",
                        elevation = 1560,
                        duration = "4h 10min",
                        profileImageRes = android.R.drawable.ic_menu_camera,
                        trekCount = 43
                    ),
                    Hiker(
                        id = "4",
                        name = "Kilian Jornet",
                        location = "Pyrénées",
                        distance = 0.8,
                        activity = "Trail running: GR10",
                        elevation = 930,
                        duration = "1h 25min",
                        profileImageRes = android.R.drawable.ic_menu_gallery,
                        trekCount = 56
                    ),
                    Hiker(
                        id = "5",
                        name = "Reinhold Messner",
                        location = "Mont Ventoux",
                        distance = 10.5,
                        activity = "Découverte des sentiers provençaux",
                        elevation = 754,
                        duration = "5h 30min",
                        profileImageRes = android.R.drawable.ic_menu_camera,
                        trekCount = 12
                    )
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        nearbyHikers = hikers.sortedBy { hiker -> hiker.distance },
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Erreur lors du chargement des randonneurs: ${e.message}"
                    )
                }
            }
        }
    }

    private fun loadUserTrekCount() {
        viewModelScope.launch {
            // Simuler un chargement depuis une base de données ou des préférences
            delay(300)

            val trekCount = 8

            _uiState.update {
                it.copy(
                    userTrekCount = trekCount,
                    canStartLive = trekCount >= 10
                )
            }
        }
    }
}