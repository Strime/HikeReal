package com.strime.hikereal.data.repository

import com.strime.hikereal.domain.model.UserProfile
import com.strime.hikereal.domain.repository.UserRepository
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
) : UserRepository {
    private val userId = "current_user"

    override fun getUserId(): String {
        return userId;
    }

    override fun getUserProfile(): UserProfile {
        return UserProfile(
            userId = userId,
            username = "Jon Doe",
            level = 4,
            experiencePoints = 650,
            bio = "Blabla"
        );
    }
}