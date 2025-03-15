package com.strime.hikereal.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strime.hikereal.domain.model.Badge
import com.strime.hikereal.domain.model.Hike
import com.strime.hikereal.domain.model.UserProfile
import com.strime.hikereal.domain.model.UserStats
import com.strime.hikereal.domain.repository.BadgeRepository
import com.strime.hikereal.domain.repository.HikeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val hikeRepository: HikeRepository,
    private val badgeRepository: BadgeRepository
) : ViewModel() {

    val userId = "current_user"

    // In a real app, this would come from a user repository or auth service
    private val currentUser = UserProfile(
        userId = userId,
        username = "Jon Doe",
        level = 4,
        experiencePoints = 650,
        bio = "Blabla"
    )

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = combine(
        hikeRepository.getRecentHikes(currentUser.userId),
        hikeRepository.getUserStats(currentUser.userId),
        badgeRepository.getRecentBadges(currentUser.userId),
        _uiState
    ) { hikes, stats, badges, currentState ->
        currentState.copy(
            isLoading = false,
            userProfile = currentUser,
            userStats = stats,
            recentHikes = hikes,
            badges = badges
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState(isLoading = true)
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            hikeRepository.initializeDatabase(userId)
            badgeRepository.initializeDatabase(userId)
        }
    }
}

data class ProfileUiState(
    val isLoading: Boolean = false,
    val userProfile: UserProfile? = null,
    val userStats: UserStats = UserStats(),
    val recentHikes: List<Hike> = emptyList(),
    val badges: List<Badge>? = null,
    val error: String? = null
)
