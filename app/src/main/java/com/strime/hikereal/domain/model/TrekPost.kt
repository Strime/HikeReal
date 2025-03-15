package com.strime.hikereal.domain.model

data class TrekPost(
    val id: String,
    val userId: String,
    val userName: String,
    val userProfilePicture: String,
    val timeAgo: String,
    val locationName: String,
    val imageUrls: List<String>,
    val dualViewImage: DualViewImage?,
    val caption: String,
    val isLiked: Boolean,
    val groupSize: Int,
    val likeCount: Int,
    val commentCount: Int,
    val metrics: Metrics
) {
    data class Metrics(
        val elevation: Int,   //meters
        val distance: Float,  // km
        val duration: String  // e.g. "3h 20m"
    )

    data class DualViewImage(
        val frontImageUrl: String,
        val backImageUrl: String,
        val captureLocation: String,
        val timestamp: Long,
        val captureElevation: Int?
    )
}