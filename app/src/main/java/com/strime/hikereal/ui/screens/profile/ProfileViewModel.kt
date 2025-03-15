package com.strime.hikereal.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strime.hikereal.domain.model.Badge
import com.strime.hikereal.domain.model.HikeData
import com.strime.hikereal.domain.model.UserProfile
import com.strime.hikereal.domain.model.UserStats
import com.strime.hikereal.domain.repository.BadgeRepository
import com.strime.hikereal.domain.repository.HikeRepository
import com.strime.hikereal.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    userRepository: UserRepository,
    hikeRepository: HikeRepository,
    badgeRepository: BadgeRepository
) : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileUiState())

    private val userId = userRepository.getUserId()
    private val currentUser = userRepository.getUserProfile()

    val uiState: StateFlow<ProfileUiState> = combine(
        hikeRepository.getRecentHikes(userId),
        hikeRepository.getUserStats(userId),
        badgeRepository.getRecentBadges(userId),
        _profileState
    ) { hikes, stats, badges, currentState ->
        currentState.copy(
            isLoading = false,
            userProfile = currentUser,
            userStats = stats,
            recentHikeData = hikes,
            badges = badges
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState(isLoading = true)
    )
}

data class ProfileUiState(
    val isLoading: Boolean = false,
    val userProfile: UserProfile? = null,
    val userStats: UserStats = UserStats(),
    val recentHikeData: List<HikeData> = emptyList(),
    val badges: List<Badge>? = null,
    val error: String? = null
)