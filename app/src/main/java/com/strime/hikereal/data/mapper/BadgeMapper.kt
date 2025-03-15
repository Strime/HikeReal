package com.strime.hikereal.data.mapper

import com.strime.hikereal.data.local.entity.BadgeEntity
import com.strime.hikereal.domain.model.Badge
import com.strime.hikereal.domain.model.BadgeLevel
import com.strime.hikereal.domain.model.BadgeType


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
