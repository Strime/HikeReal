package com.strime.hikereal.data.repository

import com.strime.hikereal.data.local.dao.HikeDao
import com.strime.hikereal.data.local.entity.HikeEntity
import com.strime.hikereal.domain.model.Hike
import com.strime.hikereal.domain.model.UserStats
import com.strime.hikereal.domain.repository.HikeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class HikeRepositoryImpl @Inject constructor(
    private val hikeDao: HikeDao
) : HikeRepository {

    override suspend fun initializeDatabase(userId: String) {
        val count = hikeDao.getHikeCount()
        if (count == 0) {
            val hikes = listOf(
                HikeEntity(
                    id = "1",
                    name = "Mont Blanc Circuit",
                    distance = 24.5f,
                    elevation = 1850,
                    views = 328,
                    likes = 45,
                    date = 1709766000000L,
                    userId = userId
                ),
                HikeEntity(
                    id = "2",
                    name = "Alpine Lake Loop",
                    distance = 16.2f,
                    elevation = 760,
                    views = 156,
                    likes = 32,
                    date = 1709161200000L,
                    userId = userId
                ),
                HikeEntity(
                    id = "3",
                    name = "Valley Trail",
                    distance = 12.8f,
                    elevation = 520,
                    views = 203,
                    likes = 28,
                    date = 1708556400000L,
                    userId = userId
                )
            )
            hikeDao.insertHikes(hikes)
        }
    }

    override fun getRecentHikes(userId: String, limit: Int): Flow<List<Hike>> {
        return hikeDao.getRecentHikes(userId, limit).map { entities ->
            entities.map { it.toHike() }
        }
    }

    override fun getUserStats(userId: String): Flow<UserStats> {
        val currentYear = LocalDate.now().year
        val startOfYear = LocalDate.of(currentYear, 1, 1).toEpochDay() * 24 * 60 * 60 * 1000

        return hikeDao.getUserStatsAggregate(userId, startOfYear)
            .map { aggregate ->
                UserStats(
                    hikeCount = aggregate.totalHikes,
                    totalDistance = aggregate.totalDistance?.toDouble() ?: 0.0,
                    totalElevation = aggregate.totalElevation ?: 0,
                    yearlyHikeCount = aggregate.yearlyHikeCount,
                    yearlyDistance = aggregate.yearlyDistance?.toDouble() ?: 0.0,
                    yearlyElevation = aggregate.yearlyElevation ?: 0
                )
            }
    }

    private fun HikeEntity.toHike(): Hike {
        val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        return Hike(
            id = id,
            name = name,
            distance = distance,
            elevation = elevation,
            views = views,
            likes = likes,
            formattedDate = dateFormat.format(Date(date))
        )
    }
}
