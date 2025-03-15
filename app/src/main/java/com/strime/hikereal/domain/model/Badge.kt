package com.strime.hikereal.domain.model

data class Badge(
    val id: String,
    val name: String,
    val description: String,
    val type: BadgeType,
    val level: BadgeLevel,
    val dateEarned: Long
)

enum class BadgeType(val displayName: String) {
    DISTANCE("Distance"),
    ELEVATION("Elevation"),
    SPEED("Speed"),
    EXPLORE("Exploration"),
    STREAK("Streak"),
    SOCIAL("Social"),
    SPECIAL("Special");

    companion object {
        fun fromString(value: String): BadgeType {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: SPECIAL
        }
    }
}

enum class BadgeLevel(val displayName: String, val colorHex: Long) {
    BRONZE("Bronze", 0xFFCD7F32),
    SILVER("Silver", 0xFFC0C0C0),
    GOLD("Gold", 0xFFFFD700),
    PLATINUM("Platinum", 0xFF43B0F1);

    companion object {
        fun fromInt(value: Int): BadgeLevel = entries.toTypedArray().getOrElse(value - 1) { BRONZE }
        fun toInt(level: BadgeLevel): Int = level.ordinal + 1
    }
}
