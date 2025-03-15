package com.strime.hikereal.domain.model

data class ActiveHikeUiState(
    val hikeId: String? = null,
    val hikeState: HikeState = HikeState.ACTIVE,
    val formattedDuration: String = "00:00:00",
    val formattedDistance: String = "0.0 km"
)
enum class HikeState {
    ACTIVE, COMPLETED
}