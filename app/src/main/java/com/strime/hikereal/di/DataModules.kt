/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.strime.hikereal.di

import android.content.Context
import androidx.room.Room
import com.strime.hikereal.data.local.dao.BadgeDao
import com.strime.hikereal.data.local.dao.HikeDao
import com.strime.hikereal.data.local.database.HikeRealDatabase
import com.strime.hikereal.data.repository.BadgeRepositoryImpl
import com.strime.hikereal.data.repository.HikeRepositoryImpl
import com.strime.hikereal.domain.repository.BadgeRepository
import com.strime.hikereal.domain.repository.HikeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindHikeRepository(repository: HikeRepositoryImpl): HikeRepository
    @Singleton
    @Binds
    abstract fun bindBadgeRepository(repository: BadgeRepositoryImpl): BadgeRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): HikeRealDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            HikeRealDatabase::class.java,
            "HikeReal.db"
        ).build()
    }

    @Provides
    fun provideHikeDao(database: HikeRealDatabase): HikeDao = database.hikeDao()

    @Provides
    fun provideBadgeDao(database: HikeRealDatabase): BadgeDao = database.badgeDao()
}
