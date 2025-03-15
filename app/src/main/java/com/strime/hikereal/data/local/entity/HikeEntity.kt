package com.strime.hikereal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hikes")
data class HikeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val locationName: String,
    val distance: Float,
    val elevation: Int,
    val duration: Long,
    val views: Int = 0,
    val likes: Int = 0,
    val date: Long,
    val userId: String,
    val userName: String,
    val userProfilePicture: String,
    val frontCameraUri: String,
    val backCameraUri: String,
    val groupSize: Int = 1,
    val timestamp: Long = System.currentTimeMillis()
)


