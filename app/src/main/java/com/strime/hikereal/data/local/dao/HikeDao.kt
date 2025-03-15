package com.strime.hikereal.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.strime.hikereal.data.local.entity.HikeEntity
import com.strime.hikereal.data.local.view.UserStatsAggregate
import kotlinx.coroutines.flow.Flow

@Dao
interface HikeDao {
    @Query("SELECT COUNT(id) FROM hikes")
    suspend fun getHikeCount(): Int

    @Query("SELECT * FROM hikes WHERE userId = :userId ORDER BY date DESC LIMIT :limit")
    fun getRecentHikes(userId: String, limit: Int): Flow<List<HikeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHikes(hikes: List<HikeEntity>)

    /**
     * Optimized query that retrieves all statistics in a single request
     * Reduces the number of observed Flows and improves performance
     */
    @Query(
        """
        SELECT 
            COUNT(*) AS totalHikes,
            SUM(distance) AS totalDistance,
            SUM(elevation) AS totalElevation,
            SUM(CASE WHEN timestamp >= :startOfYear THEN 1 ELSE 0 END) AS yearlyHikeCount,
            SUM(CASE WHEN timestamp >= :startOfYear THEN distance ELSE 0 END) AS yearlyDistance,
            SUM(CASE WHEN timestamp >= :startOfYear THEN elevation ELSE 0 END) AS yearlyElevation
        FROM hikes
        WHERE userId = :userId
    """
    )
    fun getUserStatsAggregate(userId: String, startOfYear: Long): Flow<UserStatsAggregate>
}
