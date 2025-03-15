package com.strime.hikereal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "active_hikes")
data class ActiveHikeEntity(
    @PrimaryKey val id: String,
    val startTime: Long = System.currentTimeMillis(),
    val pauseTime: Long? = null,
    val endTime: Long? = null,
    val status: String = "ACTIVE",

    val frontCameraUri: String? = null,
    val backCameraUri: String? = null,

    val currentDistance: Float = 0f,
    val currentElevation: Int = 0,
    val currentDuration: Long = 0,
    val maxSpeed: Float = 0f,
    val averageSpeed: Float = 0f,
    val totalAscent: Int = 0,
    val totalDescent: Int = 0,
    val caloriesBurned: Int = 0,

    val startLocationName: String? = null,
    val currentLocationName: String? = null,
    val lastLocationTimestamp: Long? = null,
    val trackedLocationsCount: Int = 0,

    val weatherCondition: String? = null,
    val temperature: Float? = null,

    val userId: String,

    val batteryStartLevel: Int? = null,
    val batteryCurrentLevel: Int? = null,

    val liveShareCode: String? = null,

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)