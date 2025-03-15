package com.strime.hikereal.domain.model

data class ActiveHike(
    val hikeId: String? = null,
    val hikeState: HikeState = HikeState.ACTIVE,
    val formattedDuration: String = "00:00:00",
    val formattedDistance: String = "0.0 km",
    val alreadyTookPhoto: Boolean = false,
)
enum class HikeState {
    ACTIVE, COMPLETED
}