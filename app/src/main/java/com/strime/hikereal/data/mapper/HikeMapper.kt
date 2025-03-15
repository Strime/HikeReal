package com.strime.hikereal.data.mapper

import com.strime.hikereal.data.local.entity.HikeEntity
import com.strime.hikereal.domain.model.HikePost
import java.util.concurrent.TimeUnit

fun HikeEntity.toHikePost(
): HikePost {
    val hours = TimeUnit.MILLISECONDS.toHours(duration)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60
    val formattedDuration = if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"

    val now = System.currentTimeMillis()
    val diff = now - timestamp
    val timeAgo = when {
        diff < TimeUnit.MINUTES.toMillis(1) -> "À l'instant"
        diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)}m"
        diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)}h"
        else -> "${TimeUnit.MILLISECONDS.toDays(diff)}j"
    }

    val dualView = HikePost.DualViewImage(
        frontImageUrl = frontCameraUri,
        backImageUrl = backCameraUri,
    )

    return HikePost(
        id = id,
        userId = userId,
        userName = userName,
        userProfilePicture = userProfilePicture,
        timeAgo = timeAgo,
        locationName = locationName,
        dualViewImage = dualView,
        caption = name,
        isLiked = false,
        groupSize = groupSize,
        likeCount = likes,
        metrics = HikePost.Metrics(
            elevation = elevation,
            distance = distance,
            duration = formattedDuration
        )
    )
}
