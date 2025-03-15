package com.strime.hikereal.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.strime.hikereal.data.local.entity.ActiveHikeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActiveHikeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActiveHike(activeHike: ActiveHikeEntity): Long

    @Update
    suspend fun updateActiveHike(activeHike: ActiveHikeEntity)

    @Query("SELECT * FROM active_hikes WHERE id = :hikeId")
    suspend fun getActiveHikeById(hikeId: String): ActiveHikeEntity?

    @Query("SELECT * FROM active_hikes WHERE status = 'ACTIVE' OR status = 'PAUSED' LIMIT 1")
    suspend fun getCurrentActiveHike(): ActiveHikeEntity?

    @Query("SELECT * FROM active_hikes WHERE status = 'ACTIVE' OR status = 'PAUSED' LIMIT 1")
    fun getCurrentActiveHikeAsFlow(): Flow<ActiveHikeEntity?>

    @Query("UPDATE active_hikes SET status = :newStatus, endTime = :endTime, updatedAt = :updatedAt WHERE id = :hikeId")
    suspend fun updateHikeStatus(hikeId: String, newStatus: String, endTime: Long, updatedAt: Long)

    @Query("UPDATE active_hikes SET currentDistance = :distance, updatedAt = :updatedAt WHERE id = :hikeId")
    suspend fun updateHikeDistance(hikeId: String, distance: Float, updatedAt: Long)

    @Query("UPDATE active_hikes SET currentDuration = :duration, updatedAt = :updatedAt WHERE id = :hikeId")
    suspend fun updateHikeDuration(hikeId: String, duration: Long, updatedAt: Long)

    @Query("UPDATE active_hikes SET pauseTime = :pauseTime, status = 'PAUSED', updatedAt = :updatedAt WHERE id = :hikeId")
    suspend fun pauseHike(hikeId: String, pauseTime: Long, updatedAt: Long)

    @Query("UPDATE active_hikes SET pauseTime = NULL, status = 'ACTIVE', updatedAt = :updatedAt WHERE id = :hikeId")
    suspend fun resumeHike(hikeId: String, updatedAt: Long)

    @Query("UPDATE active_hikes SET frontCameraUri = :uri, updatedAt = :updatedAt WHERE id = :hikeId")
    suspend fun updateFrontCameraUri(hikeId: String, uri: String, updatedAt: Long)

    @Query("UPDATE active_hikes SET backCameraUri = :uri, updatedAt = :updatedAt WHERE id = :hikeId")
    suspend fun updateBackCameraUri(hikeId: String, uri: String, updatedAt: Long)

    @Query("DELETE FROM active_hikes WHERE id = :hikeId")
    suspend fun deleteActiveHike(hikeId: String)

    @Query("SELECT COUNT(*) FROM active_hikes WHERE status IN ('ACTIVE', 'PAUSED')")
    suspend fun hasActiveHike(): Int
}