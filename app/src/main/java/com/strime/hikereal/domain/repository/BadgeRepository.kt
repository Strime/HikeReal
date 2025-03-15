package com.strime.hikereal.domain.repository

import com.strime.hikereal.domain.model.Badge
import kotlinx.coroutines.flow.Flow

interface BadgeRepository {
    fun getRecentBadges(userId: String, limit: Int = 10): Flow<List<Badge>>
    suspend fun initializeDatabase(userId: String)
}