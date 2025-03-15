package com.strime.hikereal.domain.model

data class UserStats(
    val hikeCount: Int = 0,
    val totalDistance: Double = 0.0,
    val totalElevation: Int = 0,
    val yearlyHikeCount: Int = 0,
    val yearlyDistance: Double = 0.0,
    val yearlyElevation: Int = 0
)
