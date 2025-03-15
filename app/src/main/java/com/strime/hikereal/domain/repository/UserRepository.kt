package com.strime.hikereal.domain.repository

import com.strime.hikereal.domain.model.UserProfile

interface UserRepository {
    fun getUserId(): String
    fun getUserProfile(): UserProfile
}