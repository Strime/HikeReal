package com.strime.hikereal.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.strime.hikereal.data.local.entity.BadgeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BadgeDao {
    @Query("SELECT * FROM badges WHERE userId = :userId ORDER BY dateEarned DESC LIMIT :limit")
    fun getRecentBadges(userId: String, limit: Int): Flow<List<BadgeEntity>>

    @Query("SELECT COUNT(*) FROM badges")
    suspend fun getBadgeCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadges(badges: List<BadgeEntity>)
}