package com.strime.hikereal.domain.repository

import com.strime.hikereal.data.local.entity.ActiveHikeEntity
import kotlinx.coroutines.flow.Flow

interface ActiveHikeRepository {
    fun getCurrentActiveHike(): Flow<ActiveHikeEntity?>
    suspend fun startNewHike(userId: String): String
    suspend fun completeHike(hikeId: String): Boolean
    suspend fun cancelHike(hikeId: String): Boolean
    suspend fun updateDistance(hikeId: String, distance: Float): Boolean
    suspend fun saveFrontCameraPhoto(hikeId: String, uri: String): Boolean
    suspend fun saveBackCameraPhoto(hikeId: String, uri: String): Boolean
    suspend fun updateHikeStats(
        hikeId: String,
        distance: Float? = null,
        duration: Long? = null,
        elevation: Int? = null,
        maxSpeed: Float? = null,
        averageSpeed: Float? = null,
        totalAscent: Int? = null,
        totalDescent: Int? = null,
        caloriesBurned: Int? = null
    ): Boolean
}