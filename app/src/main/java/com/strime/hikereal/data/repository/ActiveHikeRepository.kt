package com.strime.hikereal.data.repository

import com.strime.hikereal.data.local.dao.ActiveHikeDao
import com.strime.hikereal.data.local.entity.ActiveHikeEntity
import com.strime.hikereal.domain.repository.ActiveHikeRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActiveHikeRepositoryImpl @Inject constructor(
    private val activeHikeDao: ActiveHikeDao
): ActiveHikeRepository {

    override fun getCurrentActiveHike(): Flow<ActiveHikeEntity?> {
        return activeHikeDao.getCurrentActiveHikeAsFlow()
    }

    private suspend fun hasActiveHike(): Boolean {
        return activeHikeDao.hasActiveHike() > 0
    }

    override suspend fun startNewHike(userId: String): String {
        if (hasActiveHike()) {
            throw IllegalStateException("Une activité est déjà en cours. Impossible d'en démarrer une nouvelle.")
        }

        val hikeId = UUID.randomUUID().toString()
        val now = System.currentTimeMillis()

        val newHike = ActiveHikeEntity(
            id = hikeId,
            startTime = now,
            status = "ACTIVE",
            userId = userId,
            createdAt = now,
            updatedAt = now
        )

        activeHikeDao.insertActiveHike(newHike)
        return hikeId
    }

    override suspend fun completeHike(hikeId: String): Boolean {
        val hike = activeHikeDao.getActiveHikeById(hikeId) ?: return false

        if (hike.status != "ACTIVE" && hike.status != "PAUSED") {
            return false
        }

        val now = System.currentTimeMillis()
        activeHikeDao.updateHikeStatus(hikeId, "COMPLETED", now, now)

        return true
    }

    override suspend fun cancelHike(hikeId: String): Boolean {
        val now = System.currentTimeMillis()
        activeHikeDao.updateHikeStatus(hikeId, "CANCELLED", now, now)
        return true
    }

    override suspend fun updateDistance(hikeId: String, distance: Float): Boolean {
        val now = System.currentTimeMillis()
        activeHikeDao.updateHikeDistance(hikeId, distance, now)
        return true
    }

    override suspend fun saveFrontCameraPhoto(hikeId: String, uri: String): Boolean {
        val now = System.currentTimeMillis()
        activeHikeDao.updateFrontCameraUri(hikeId, uri, now)
        return true
    }

    override suspend fun saveBackCameraPhoto(hikeId: String, uri: String): Boolean {
        val now = System.currentTimeMillis()
        activeHikeDao.updateBackCameraUri(hikeId, uri, now)
        return true
    }

    override suspend fun updateHikeStats(
        hikeId: String,
        distance: Float?,
        duration: Long?,
        elevation: Int?,
        maxSpeed: Float?,
        averageSpeed: Float?,
        totalAscent: Int?,
        totalDescent: Int?,
        caloriesBurned: Int?
    ): Boolean {
        val hike = activeHikeDao.getActiveHikeById(hikeId) ?: return false

        val updatedHike = hike.copy(
            currentDistance = distance ?: hike.currentDistance,
            currentDuration = duration ?: hike.currentDuration,
            currentElevation = elevation ?: hike.currentElevation,
            maxSpeed = maxSpeed ?: hike.maxSpeed,
            averageSpeed = averageSpeed ?: hike.averageSpeed,
            totalAscent = totalAscent ?: hike.totalAscent,
            totalDescent = totalDescent ?: hike.totalDescent,
            caloriesBurned = caloriesBurned ?: hike.caloriesBurned,
            updatedAt = System.currentTimeMillis()
        )

        activeHikeDao.updateActiveHike(updatedHike)
        return true
    }
}