package com.strime.hikereal.data.repository

import com.strime.hikereal.data.local.dao.HikeDao
import com.strime.hikereal.data.local.entity.HikeEntity
import com.strime.hikereal.domain.model.HikeData
import com.strime.hikereal.domain.model.UserStats
import com.strime.hikereal.domain.repository.HikeRepository
import kotlinx.coroutines.flow.Flow
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
                    name = "UT4M",
                    locationName = "Mont Blanc Circuit",
                    distance = 24.5f,
                    elevation = 1850,
                    views = 328,
                    likes = 45,
                    date = 1709766000000L,
                    userId = "123ABC",
                    userName = "Kilian J.",
                    userProfilePicture = "https://i.pravatar.cc/150?img=1",
                    groupSize = 3,
                    timestamp = System.currentTimeMillis() - 7200000,
                    duration = 6490,
                    frontCameraUri = "https://images.unsplash.com/photo-1503614472-8c93d56e92ce",
                    backCameraUri = "https://images.unsplash.com/photo-1465919292275-c60ba49da6ae",
                ),
                HikeEntity(
                    id = "2",
                    name = "UTMB",
                    locationName = "Grenoble",
                    distance = 16.2f,
                    elevation = 760,
                    views = 156,
                    likes = 32,
                    date = 1709161200000L,
                    userId = "345DEF",
                    userName = "Mathieu B.",
                    userProfilePicture = "https://i.pravatar.cc/150?img=2",
                    groupSize = 1,
                    timestamp = System.currentTimeMillis() - 72000000,
                    duration = 2490,
                    frontCameraUri = "https://images.unsplash.com/photo-1476900543704-4312b78632f8",
                    backCameraUri = "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05",
                ),
                HikeEntity(
                    id = "3",
                    name = "La Diag",
                    locationName = "La Réunion",
                    distance = 12.8f,
                    elevation = 520,
                    views = 203,
                    likes = 28,
                    date = 1708556400000L,
                    userId = "678GHI",
                    userName = "François D.",
                    userProfilePicture = "https://i.pravatar.cc/150?img=3",
                    groupSize = 2,
                    timestamp = System.currentTimeMillis() - 1200000,
                    duration = 21490,
                    frontCameraUri = "https://images.unsplash.com/photo-1464278533981-50e3d9aae1e2",
                    backCameraUri = "https://images.unsplash.com/photo-1458442310124-dde6edb43d10",
                )
            )
            hikeDao.insertHikes(hikes)
        }
    }

    override suspend fun getAllHikes(): Flow<List<HikeEntity>> {
        return hikeDao.getAllHikes()
    }

    override fun getRecentHikes(userId: String, limit: Int): Flow<List<HikeData>> {
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

    private fun HikeEntity.toHike(): HikeData {
        val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        return HikeData(
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
