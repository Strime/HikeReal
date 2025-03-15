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

fun BadgeEntity.toModel(): Badge {
    return Badge(
        id = id,
        name = name,
        description = description,
        type = BadgeType.fromString(type),
        level = BadgeLevel.fromInt(level),
        dateEarned = dateEarned
    )
}

fun Badge.toEntity(userId: String): BadgeEntity {
    return BadgeEntity(
        id = id,
        name = name,
        description = description,
        type = type.name,
        level = BadgeLevel.toInt(level),
        dateEarned = dateEarned,
        userId = userId
    )
}
