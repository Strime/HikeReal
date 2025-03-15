package com.strime.hikereal.data.repository

import com.strime.hikereal.data.local.dao.BadgeDao
import com.strime.hikereal.data.local.entity.BadgeEntity
import com.strime.hikereal.data.local.entity.toModel
import com.strime.hikereal.domain.model.Badge
import com.strime.hikereal.domain.repository.BadgeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BadgeRepositoryImpl @Inject constructor(
    private val badgeDao: BadgeDao
) : BadgeRepository {
    override suspend fun initializeDatabase(userId: String) {
        val count = badgeDao.getBadgeCount();

        if(count == 0) {
            val currentTime = System.currentTimeMillis()
            val dayInMillis = 24 * 60 * 60 * 1000L

            val badges = listOf(
                BadgeEntity(
                    id = "1",
                    name = "Distance Master",
                    description = "Hike over 100km total",
                    type = "Distance",
                    level = 1,
                    dateEarned = currentTime - (10 * dayInMillis),
                    userId = userId
                ),
                BadgeEntity(
                    id = "2",
                    name = "Summit Seeker",
                    description = "Reach 5000m elevation gain total",
                    type = "Elevation",
                    level = 3,
                    dateEarned = currentTime - (5 * dayInMillis),
                    userId = userId
                ),
                BadgeEntity(
                    id = "3",
                    name = "Explorer",
                    description = "Visit 3 different regions",
                    type = "Explore",
                    level = 0,
                    dateEarned = currentTime - (15 * dayInMillis),
                    userId = userId
                ),
                BadgeEntity(
                    id = "4",
                    name = "Weekly Warrior",
                    description = "Hike for 3 consecutive weeks",
                    type = "Streak",
                    level = 0,
                    dateEarned = currentTime - (2 * dayInMillis),
                    userId = userId
                ),
                BadgeEntity(
                    id = "5",
                    name = "Speed Demon",
                    description = "Maintain 5km/h average on a 10km+ hike",
                    type = "Speed",
                    level = 0,
                    dateEarned = currentTime - (7 * dayInMillis),
                    userId = userId
                )
            )

            badgeDao.insertBadges(badges)
        }
    }
    override fun getRecentBadges(userId: String, limit: Int): Flow<List<Badge>> {
        return badgeDao.getRecentBadges(userId, limit).map { entities ->
            entities.map { it.toModel() }
        }
    }
}