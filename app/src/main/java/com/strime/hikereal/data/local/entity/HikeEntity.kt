package com.strime.hikereal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hikes")
data class HikeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val distance: Float,
    val elevation: Int,
    val views: Int,
    val likes: Int,
    val date: Long,
    val userId: String,
    val timestamp: Long = System.currentTimeMillis()
)

