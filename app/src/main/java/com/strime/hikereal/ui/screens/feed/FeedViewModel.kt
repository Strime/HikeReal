package com.strime.hikereal.ui.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strime.hikereal.domain.model.TrekPost
import com.strime.hikereal.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<TrekPost>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<TrekPost>>> = _uiState.asStateFlow()

    init {
        loadFeed()
    }

    private fun loadFeed() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                val posts = getMockPosts()
                _uiState.value = UiState.Success(posts)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to load feed: ${e.message}")
            }
        }
    }

    private fun getMockPosts(): List<TrekPost> {
        return listOf(
            TrekPost(
                id = "1",
                userId = "user1",
                userName = "MountainExplorer",
                userProfilePicture = "https://i.pravatar.cc/150?img=1",
                timeAgo = "2h ago",
                locationName = "Mont Blanc Trail",
                caption = "Incredible views on today's hike! The DualView caught both the summit ahead and the valley behind me.",
                isLiked = false,
                likeCount = 42,
                commentCount = 5,
                groupSize = 2,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1551632811-561732d1e306",
                    "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b"
                ),
                dualViewImage = TrekPost.DualViewImage(
                    frontImageUrl = "https://images.unsplash.com/photo-1464278533981-50e3d9aae1e2",
                    backImageUrl = "https://images.unsplash.com/photo-1458442310124-dde6edb43d10",
                    captureLocation = "Col des Montets",
                    timestamp = System.currentTimeMillis() - 7200000, // 2 hours ago
                    captureElevation = 2450
                ),
                metrics = TrekPost.Metrics(
                    elevation = 2800,
                    distance = 8.5f,
                    duration = "3h 20m"
                )
            ),
            TrekPost(
                id = "2",
                userId = "user2",
                userName = "AlpineAdventurer",
                userProfilePicture = "https://i.pravatar.cc/150?img=2",
                timeAgo = "5h ago",
                locationName = "Chamonix Valley",
                caption = "Today's DualView really captures the essence of this trail - snow ahead, forest behind!",
                isLiked = true,
                likeCount = 78,
                commentCount = 12,
                groupSize = 1,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1519681393784-d120267933ba",
                    "https://images.unsplash.com/photo-1520962880247-cfaf541c8724"
                ),
                dualViewImage = TrekPost.DualViewImage(
                    frontImageUrl = "https://images.unsplash.com/photo-1476900543704-4312b78632f8",
                    backImageUrl = "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05",
                    captureLocation = "Mer de Glace Viewpoint",
                    timestamp = System.currentTimeMillis() - 18000000, // 5 hours ago
                    captureElevation = 1800
                ),
                metrics = TrekPost.Metrics(
                    elevation = 1200,
                    distance = 12.3f,
                    duration = "4h 15m"
                )
            ),
            TrekPost(
                id = "3",
                userId = "user3",
                userName = "TrailRunner",
                userProfilePicture = "https://i.pravatar.cc/150?img=3",
                timeAgo = "Yesterday",
                locationName = "Alpine Lakes Trail",
                caption = "The DualView moment caught the changing weather perfectly - sunshine ahead, storm clouds behind!",
                isLiked = false,
                likeCount = 36,
                commentCount = 3,
                groupSize = 3,
                imageUrls = listOf(
                    "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b",
                    "https://images.unsplash.com/photo-1544198365-f5d60b6d8190"
                ),
                dualViewImage = TrekPost.DualViewImage(
                    frontImageUrl = "https://images.unsplash.com/photo-1465919292275-c60ba49da6ae",
                    backImageUrl = "https://images.unsplash.com/photo-1503614472-8c93d56e92ce",
                    captureLocation = "Lake Summit",
                    timestamp = System.currentTimeMillis() - 86400000, // Yesterday
                    captureElevation = 1650
                ),
                metrics = TrekPost.Metrics(
                    elevation = 920,
                    distance = 15.7f,
                    duration = "2h 45m"
                )
            )
        )
    }
}