package com.strime.hikereal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.strime.hikereal.domain.model.Badge
import com.strime.hikereal.domain.model.BadgeLevel
import com.strime.hikereal.domain.model.BadgeType

@Entity(tableName = "badges")
data class BadgeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val type: String,
    val level: Int,
    val dateEarned: Long,
    val userId: String
)