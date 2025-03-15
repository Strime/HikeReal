package com.strime.hikereal.data.repository

import com.google.common.truth.Truth.assertThat
import com.strime.hikereal.data.local.dao.ActiveHikeDao
import com.strime.hikereal.data.local.dao.HikeDao
import com.strime.hikereal.data.local.entity.ActiveHikeEntity
import com.strime.hikereal.domain.model.UserProfile
import com.strime.sharedtestcode.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ActiveHikeRepositoryTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    // Mocked dependencies
    private lateinit var activeHikeDao: ActiveHikeDao
    private lateinit var hikeDao: HikeDao

    // System under test
    private lateinit var repository: ActiveHikeRepositoryImpl

    // Test data
    private val testUserId = "user123"
    private val testUserProfile = UserProfile(
        userId = testUserId,
        username = "Test User",
        profilePicture = "profile-url",
    )

    private val testActiveHike = ActiveHikeEntity(
        id = "hike123",
        startTime = System.currentTimeMillis() - 3600000, // 1 hour ago
        status = "ACTIVE",
        userId = testUserId,
        frontCameraUri = null,
        backCameraUri = null,
        currentDistance = 5.5f,
        currentDuration = 3600L, // 1 hour
        currentElevation = 350,
        maxSpeed = 12.5f,
        averageSpeed = 8.2f,
        totalAscent = 420,
        totalDescent = 180,
        caloriesBurned = 450,
        startLocationName = "Mountain Trail",
        createdAt = System.currentTimeMillis() - 3600000,
        updatedAt = System.currentTimeMillis() - 1800000 // 30 min ago
    )

    private val testActiveHikeWithPhotos = testActiveHike.copy(
        frontCameraUri = "front-photo-uri",
        backCameraUri = "back-photo-uri"
    )

    @Before
    fun setup() {
        activeHikeDao = mockk(relaxed = true)
        hikeDao = mockk(relaxed = true)

        repository = ActiveHikeRepositoryImpl(activeHikeDao, hikeDao)
    }

    @Test
    fun `getCurrentActiveHike returns Flow from DAO`() = runTest {
        // Given
        every { activeHikeDao.getCurrentActiveHikeAsFlow() } returns flowOf(testActiveHike)

        // When
        val result = repository.getCurrentActiveHike()

        // Then
        result.collect { activeHike ->
            assertNotNull(activeHike)
            assertEquals(testActiveHike.id, activeHike!!.id)
            assertEquals("ACTIVE", activeHike.status)
        }
    }

    @Test
    fun `startNewHike throws exception when active hike exists`() = runTest {
        // Given
        coEvery { activeHikeDao.hasActiveHike() } returns 1

        // When/Then
        try {
            repository.startNewHike(testUserProfile)
            fail("Expected HikeException.ActiveHikeAlreadyExists")
        } catch (e: HikeException) {
            assertThat(e).isInstanceOf(HikeException.ActiveHikeAlreadyExists::class.java)
        }
    }

    @Test
    fun `startNewHike creates and stores new hike when no active hike exists`() = runTest {
        // Given
        coEvery { activeHikeDao.hasActiveHike() } returns 0
        val slot = slot<ActiveHikeEntity>()
        coEvery { activeHikeDao.insertActiveHike(capture(slot)) } returns 1

        // When
        val hikeId = repository.startNewHike(testUserProfile)

        // Then
        assertThat(hikeId).isNotEmpty()
        with(slot.captured) {
            assertThat(id).isEqualTo(hikeId)
            assertThat(status).isEqualTo("ACTIVE")
            assertThat(userId).isEqualTo(testUserId)
            assertThat(startTime).isGreaterThan(0L)
        }
    }

    @Test
    fun `completeHike throws NoActiveHike when hike is not active`() = runTest {
        // Given
        val inactiveHike = testActiveHike.copy(status = "COMPLETED")
        coEvery { activeHikeDao.getActiveHikeById("hike123") } returns inactiveHike

        // When/Then
        try {
            repository.completeHike("hike123", testUserProfile)
            fail("Expected HikeException.NoActiveHike")
        } catch (e: HikeException) {
            assertThat(e).isInstanceOf(HikeException.NoActiveHike::class.java)
        }
    }

    @Test
    fun `completeHike throws ActiveHikeCanBeCompleted when photos are missing`() = runTest {
        // Given - hike without photos
        coEvery { activeHikeDao.getActiveHikeById("hike123") } returns testActiveHike

        // When/Then
        try {
            repository.completeHike("hike123", testUserProfile)
            fail("Expected HikeException.ActiveHikeCanBeCompleted")
        } catch (e: HikeException) {
            assertThat(e).isInstanceOf(HikeException.ActiveHikeCanBeCompleted::class.java)
        }
    }

    @Test
    fun `completeHike completes hike and creates HikeEntity when all conditions are met`() = runTest {
        // Given - hike with photos
        coEvery { activeHikeDao.getActiveHikeById("hike123") } returns testActiveHikeWithPhotos

        // When
        val result = repository.completeHike("hike123", testUserProfile)

        // Then
        assertTrue(result)
        coVerify {
            activeHikeDao.updateHikeStatus("hike123", "COMPLETED", any(), any())
            hikeDao.insertHike(any())
        }
    }

    @Test
    fun `saveBackCameraPhoto updates backend camera URI`() = runTest {
        // Given
        val hikeId = "hike123"
        val photoUri = "back-photo-uri"

        // When
        val result = repository.saveBackCameraPhoto(hikeId, photoUri)

        // Then
        assertTrue(result)
        coVerify { activeHikeDao.updateBackCameraUri(hikeId, photoUri, any()) }
    }

    @Test
    fun `saveFrontCameraPhoto updates frontend camera URI`() = runTest {
        // Given
        val hikeId = "hike123"
        val photoUri = "front-photo-uri"

        // When
        val result = repository.saveFrontCameraPhoto(hikeId, photoUri)

        // Then
        assertTrue(result)
        coVerify { activeHikeDao.updateFrontCameraUri(hikeId, photoUri, any()) }
    }

    @Test
    fun `updateHikeStats updates all stats correctly`() = runTest {
        // Given
        val hikeId = "hike123"
        coEvery { activeHikeDao.getActiveHikeById(hikeId) } returns testActiveHike
        val slot = slot<ActiveHikeEntity>()
        coEvery { activeHikeDao.updateActiveHike(capture(slot)) } returns Unit

        // When
        val result = repository.updateHikeStats(
            hikeId = hikeId,
            distance = 7.5f,
            duration = 5400L,
            elevation = 500,
            maxSpeed = 15.0f,
            averageSpeed = 10.0f,
            totalAscent = 600,
            totalDescent = 300,
            caloriesBurned = 700
        )

        // Then
        assertTrue(result)
        with(slot.captured) {
            assertEquals(7.5f, currentDistance)
            assertEquals(5400L, currentDuration)
            assertEquals(500, currentElevation)
            assertEquals(15.0f, maxSpeed)
            assertEquals(10.0f, averageSpeed)
            assertEquals(600, totalAscent)
            assertEquals(300, totalDescent)
            assertEquals(700, caloriesBurned)
        }
    }

    @Test
    fun `updateHikeStats returns false when hike not found`() = runTest {
        // Given
        val nonExistentHikeId = "nonexistent"
        coEvery { activeHikeDao.getActiveHikeById(nonExistentHikeId) } returns null

        // When
        val result = repository.updateHikeStats(
            hikeId = nonExistentHikeId,
            distance = 7.5f,
            duration = 5400L,
            elevation = 500,
            maxSpeed = null,
            averageSpeed = null,
            totalAscent = null,
            totalDescent = null,
            caloriesBurned = null
        )

        // Then
        assertFalse(result)
    }

    @Test
    fun `updateHikeStats only updates provided values`() = runTest {
        // Given
        val hikeId = "hike123"
        coEvery { activeHikeDao.getActiveHikeById(hikeId) } returns testActiveHike
        val slot = slot<ActiveHikeEntity>()
        coEvery { activeHikeDao.updateActiveHike(capture(slot)) } returns Unit

        // When - only update distance
        val result = repository.updateHikeStats(
            hikeId = hikeId,
            distance = 7.5f,
            duration = null,
            elevation = null,
            maxSpeed = null,
            averageSpeed = null,
            totalAscent = null,
            totalDescent = null,
            caloriesBurned = null
        )

        // Then
        assertTrue(result)
        with(slot.captured) {
            assertEquals(7.5f, currentDistance)
            assertEquals(testActiveHike.currentDuration, currentDuration) // unchanged
            assertEquals(testActiveHike.currentElevation, currentElevation) // unchanged
            assertEquals(testActiveHike.maxSpeed, maxSpeed) // unchanged
            assertEquals(testActiveHike.averageSpeed, averageSpeed) // unchanged
            assertEquals(testActiveHike.totalAscent, totalAscent) // unchanged
            assertEquals(testActiveHike.totalDescent, totalDescent) // unchanged
            assertEquals(testActiveHike.caloriesBurned, caloriesBurned) // unchanged
        }
    }
}