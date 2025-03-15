package com.strime.hikereal.domain.model

data class UserProfile(
    val userId: String,
    val username: String,
    val profilePicture: String,
    val level: Int = 1,
    val experiencePoints: Int = 0,
    val joinDate: String = "",
    val bio: String = ""
)
