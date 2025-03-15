package com.strime.sharedtestcode.data

import com.strime.hikereal.data.local.entity.HikeEntity
import com.strime.hikereal.domain.model.HikePost
import kotlin.random.Random

object HikeEntityFactory {

    private val defaultUserId = "test-user-id"

    private fun create(
        id: String = "hike_${Random.nextInt(1000)}",
        name: String = "Test Hike",
        locationName: String = "Test Location",
        distance: Float = 5.5f,
        elevation: Int = 350,
        duration: Long = 7200L, // 2 heures en secondes
        views: Int = 100,
        likes: Int = 10,
        date: Long = System.currentTimeMillis() - 7200000, // 2 heures ago
        userId: String = defaultUserId,
        userName: String = "Test User",
        userProfilePicture: String = "profile-url",
        frontCameraUri: String = "front-image-url",
        backCameraUri: String = "back-image-url",
        groupSize: Int = 3,
        timestamp: Long = System.currentTimeMillis() - 7200000
    ): HikeEntity {
        return HikeEntity(
            id = id,
            name = name,
            locationName = locationName,
            distance = distance,
            elevation = elevation,
            duration = duration,
            views = views,
            likes = likes,
            date = date,
            userId = userId,
            userName = userName,
            userProfilePicture = userProfilePicture,
            frontCameraUri = frontCameraUri,
            backCameraUri = backCameraUri,
            groupSize = groupSize,
            timestamp = timestamp
        )
    }

    fun createList(size: Int): List<HikeEntity> {
        return List(size) { index ->
            create(
                id = "hike_$index",
                name = "Test Hike $index",
                locationName = "Location $index",
                distance = 5.0f + (index * 0.5f),
                elevation = 300 + (index * 50),
                likes = 10 + index,
                groupSize = 2 + (index % 3)
            )
        }
    }

    /**
     * Crée une paire de HikeEntity spécifique pour les tests du feed
     */
    fun createTestPair(): Pair<HikeEntity, HikeEntity> {
        val testHikeEntity1 = create(
            id = "hike1",
            name = "Mountain View Trail",
            locationName = "Mountain View",
            distance = 5.5f,
            elevation = 350,
            duration = 9000L,
            views = 150,
            likes = 10,
            date = System.currentTimeMillis() - 7200000, // 2h
            userId = defaultUserId,
            userName = "Test User",
            userProfilePicture = "profile-url",
            frontCameraUri = "front-image-url-1",
            backCameraUri = "back-image-url-1",
            groupSize = 3,
            timestamp = System.currentTimeMillis() - 7200000
        )

        val testHikeEntity2 = create(
            id = "hike2",
            name = "Sunset Ridge",
            locationName = "Sunset Valley",
            distance = 3.2f,
            elevation = 200,
            duration = 6300L,
            views = 100,
            likes = 15,
            date = System.currentTimeMillis() - 18000000, // 5h
            userId = defaultUserId,
            userName = "Test User",
            userProfilePicture = "profile-url",
            frontCameraUri = "front-image-url-2",
            backCameraUri = "back-image-url-2",
            groupSize = 2,
            timestamp = System.currentTimeMillis() - 18000000
        )

        return Pair(testHikeEntity1, testHikeEntity2)
    }
}

object HikePostFactory {

    private val defaultUserId = "test-user-id"

    private fun create(
        id: String = "hike_${Random.nextInt(1000)}",
        userId: String = defaultUserId,
        userName: String = "Test User",
        userProfilePicture: String = "profile-url",
        timeAgo: String = "2h",
        locationName: String = "Test Location",
        frontImageUrl: String = "front-image-url",
        backImageUrl: String = "back-image-url",
        caption: String = "Test Hike",
        isLiked: Boolean = false,
        groupSize: Int = 3,
        likeCount: Int = 10,
        elevation: Int = 350,
        distance: Float = 5.5f,
        duration: String = "0m"
    ): HikePost {
        return HikePost(
            id = id,
            userId = userId,
            userName = userName,
            userProfilePicture = userProfilePicture,
            timeAgo = timeAgo,
            locationName = locationName,
            dualViewImage = HikePost.DualViewImage(
                frontImageUrl = frontImageUrl,
                backImageUrl = backImageUrl
            ),
            caption = caption,
            isLiked = isLiked,
            groupSize = groupSize,
            likeCount = likeCount,
            metrics = HikePost.Metrics(
                elevation = elevation,
                distance = distance,
                duration = duration
            )
        )
    }

    fun createList(size: Int): List<HikePost> {
        return List(size) { index ->
            create(
                id = "hike_$index",
                caption = "Test Hike $index",
                locationName = "Location $index",
                distance = 5.0f + (index * 0.5f),
                elevation = 300 + (index * 50),
                likeCount = 10 + index,
                groupSize = 2 + (index % 3)
            )
        }
    }

    fun createTestPair(): Pair<HikePost, HikePost> {
        val testHikePost1 = create(
            id = "hike1",
            userId = defaultUserId,
            userName = "Test User",
            userProfilePicture = "profile-url",
            timeAgo = "2h",
            locationName = "Mountain View",
            frontImageUrl = "front-image-url-1",
            backImageUrl = "back-image-url-1",
            caption = "Mountain View Trail",
            isLiked = false,
            groupSize = 3,
            likeCount = 10,
            elevation = 350,
            distance = 5.5f,
            duration = "0m"
        )

        val testHikePost2 = create(
            id = "hike2",
            userId = defaultUserId,
            userName = "Test User",
            userProfilePicture = "profile-url",
            timeAgo = "5h",
            locationName = "Sunset Valley",
            frontImageUrl = "front-image-url-2",
            backImageUrl = "back-image-url-2",
            caption = "Sunset Ridge",
            isLiked = false,
            groupSize = 2,
            likeCount = 15,
            elevation = 200,
            distance = 3.2f,
            duration = "0m"
        )

        return Pair(testHikePost1, testHikePost2)
    }
}