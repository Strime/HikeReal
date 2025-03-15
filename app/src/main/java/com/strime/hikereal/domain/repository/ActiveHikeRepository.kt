package com.strime.hikereal.domain.repository

import com.strime.hikereal.data.local.entity.ActiveHikeEntity
import com.strime.hikereal.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface ActiveHikeRepository {
    fun getCurrentActiveHike(): Flow<ActiveHikeEntity?>
    suspend fun startNewHike(userProfile: UserProfile): String
    suspend fun completeHike(hikeId: String, userProfile: UserProfile): Boolean
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

    suspend fun getCurrentActiveHikeId(): String?
    suspend fun saveSelectedFrontCameraPhoto(hikeId: String, uri: String): Boolean
    suspend fun saveSelectedBackCameraPhoto(hikeId: String, uri: String): Boolean
}