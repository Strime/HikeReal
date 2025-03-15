package com.strime.hikereal.data.local.view

data class UserStatsAggregate(
    val totalHikes: Int = 0,
    val totalDistance: Float? = 0f,
    val totalElevation: Int? = 0,
    val yearlyHikeCount: Int = 0,
    val yearlyDistance: Float? = 0f,
    val yearlyElevation: Int? = 0
)
