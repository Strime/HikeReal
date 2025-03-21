package com.strime.hikereal.domain.model

data class HikeData(
    val id: String,
    val name: String,
    val distance: Float,
    val elevation: Int,
    val views: Int,
    val likes: Int,
    val formattedDate: String
)
