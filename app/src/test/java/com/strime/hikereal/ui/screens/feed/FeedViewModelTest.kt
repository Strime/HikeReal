/*
 * Copyright 2023 HikeReal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.strime.hikereal.ui.screens.feed

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.strime.hikereal.R
import com.strime.hikereal.domain.model.HikePost
import com.strime.hikereal.domain.repository.BadgeRepository
import com.strime.hikereal.domain.repository.HikeRepository
import com.strime.hikereal.domain.repository.UserRepository
import com.strime.hikereal.utils.UiState
import com.strime.sharedtestcode.MainCoroutineRule
import com.strime.sharedtestcode.data.HikeEntityFactory
import com.strime.sharedtestcode.data.HikePostFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for the implementation of [FeedViewModel]
 */
@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    // Mocked dependencies
    private lateinit var userRepository: UserRepository
    private lateinit var hikeRepository: HikeRepository
    private lateinit var badgeRepository: BadgeRepository

    // Test subject
    private lateinit var viewModel: FeedViewModel

    // Test data
    private val testUserId = "test-user-id"
    private val testHikeEntityPair = HikeEntityFactory.createTestPair()
    private val testHikePostPair = HikePostFactory.createTestPair()
    private val testHikeEntities = listOf(testHikeEntityPair.first, testHikeEntityPair.second)
    private val testHikePosts = listOf(testHikePostPair.first, testHikePostPair.second)

    @Before
    fun setup() {
        // Initialize mocks
        userRepository = mockk(relaxed = true)
        hikeRepository = mockk(relaxed = true)
        badgeRepository = mockk(relaxed = true)

        // Setup common mock behavior
        every { userRepository.getUserId() } returns testUserId
        coEvery { hikeRepository.initializeDatabase(any()) } returns Unit
        coEvery { badgeRepository.initializeDatabase(any()) } returns Unit
    }

    @Test
    fun `init initializes databases`() = runTest {
        // Given repositories are set up

        // When ViewModel is initialized
        viewModel = FeedViewModel(userRepository, hikeRepository, badgeRepository)

        // Then databases are initialized with the correct user ID
        verify { userRepository.getUserId() }
        coVerify { hikeRepository.initializeDatabase(testUserId) }
        coVerify { badgeRepository.initializeDatabase(testUserId) }
    }

    @Test
    fun `loadFeed emits success state when data is available`() = runTest {
        // Given hikeRepository returns successful data with HikeEntity objects
        coEvery { hikeRepository.getAllHikes() } returns flow { emit(testHikeEntities) }

        // When ViewModel is initialized (which calls loadFeed)
        viewModel = FeedViewModel(userRepository, hikeRepository, badgeRepository)

        // Then uiState should emit Loading followed by Success with the expected data
        viewModel.uiState.test {
            val successState = awaitItem()
            assertThat(successState).isInstanceOf(UiState.Success::class.java)
            val data = (successState as UiState.Success<List<HikePost>>).data
            assertThat(data).isEqualTo(testHikePosts)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadFeed emits error state when exception occurs`() = runTest {
        // Given hikeRepository throws an exception
        coEvery { hikeRepository.getAllHikes() } throws RuntimeException("Test exception")

        // When ViewModel is initialized (which calls loadFeed)
        viewModel = FeedViewModel(userRepository, hikeRepository, badgeRepository)

        // Then uiState should emit Error
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state).isInstanceOf(UiState.Error::class.java)
            val errorMessage = (state as UiState.Error).errorCode
            assertThat(errorMessage).isEqualTo(R.string.error_unknown)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadFeed transforms hike entities to posts`() = runTest {
        // Create a larger list of entities to test
        val entities = HikeEntityFactory.createList(5)

        // Mock the collection from the repository
        coEvery { hikeRepository.getAllHikes() } returns flow { emit(entities) }

        // When ViewModel is initialized
        viewModel = FeedViewModel(userRepository, hikeRepository, badgeRepository)

        // Then the posts should be transformed and emitted as Success
        viewModel.uiState.test {
            // Get Success state
            val successState = awaitItem()
            assertThat(successState).isInstanceOf(UiState.Success::class.java)

            // Verify the size matches what we expect
            val posts = (successState as UiState.Success<List<HikePost>>).data
            assertThat(posts).hasSize(5)
            cancelAndIgnoreRemainingEvents()
        }
    }
}