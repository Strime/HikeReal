package com.strime.hikereal.ui.screens.feed

import android.graphics.drawable.BitmapDrawable
import androidx.annotation.StringRes
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.strime.hikereal.R
import com.strime.hikereal.domain.model.HikePost
import com.strime.hikereal.ui.components.EnhancedAsyncImage
import com.strime.hikereal.ui.theme.Dimens
import com.strime.hikereal.ui.theme.Dimens.borderRadiusLarge
import com.strime.hikereal.ui.util.extractDominantColorFromBitmap
import com.strime.hikereal.ui.util.getAccessibleTextColor
import com.strime.hikereal.utils.UiState
import kotlin.math.roundToInt

@Composable
fun FeedScreen(
    navController: NavController,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val state = uiState) {
            UiState.Initial,
            UiState.Loading -> {
                LoadingState()
            }

            is UiState.Success -> {
                SuccessState(posts = state.data)
            }

            is UiState.Error -> {
                ErrorState(message = state.errorCode)
            }

        }
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorState(@StringRes message: Int) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(message),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun SuccessState(posts: List<HikePost>) {
    val pagerState = rememberPagerState(pageCount = { posts.size })

    VerticalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        SingleTrekPostView(post = posts[page])
    }
}

@Composable
fun SingleTrekPostView(post: HikePost) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        TrekPostCard(post = post)
    }
}

@Composable
fun TrekPostCard(post: HikePost) {
    var dominantColor by remember { mutableStateOf(Color.White) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
            .padding(Dimens.paddingMedium)
    ) {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.paddingMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(Dimens.profilePictureSize)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable { }
                    ) {
                        EnhancedAsyncImage(
                            model = post.userProfilePicture,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(Dimens.spacingMedium))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = post.userName,
                            style = MaterialTheme.typography.titleSmall,
                            color = dominantColor.getAccessibleTextColor()
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = post.locationName,
                                style = MaterialTheme.typography.bodySmall,
                                color = dominantColor.getAccessibleTextColor().copy(alpha = 0.8f)
                            )

                            Spacer(modifier = Modifier.width(Dimens.spacingMedium))

                            GroupSizeIndicator(
                                groupSize = post.groupSize
                            )
                        }
                    }

                    Text(
                        text = post.timeAgo,
                        style = MaterialTheme.typography.bodySmall,
                        color = dominantColor.getAccessibleTextColor().copy(alpha = 0.7f)
                    )
                }
            }
            Box(modifier = Modifier.fillMaxSize()) {
                DualViewPhotoDisplay(
                    dualViewImage = post.dualViewImage,
                    onDominantColorExtracted = { color ->
                        dominantColor = color
                    }
                )


                // Metrics en bas de l'image
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    color = Color.Black.copy(alpha = Dimens.overlayOpacity)
                ) {
                    Column {
                        MetricsRow(
                            metrics = post.metrics,
                            modifier = Modifier.padding(Dimens.paddingSmall)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DualViewPhotoDisplay(
    dualViewImage: HikePost.DualViewImage,
    onDominantColorExtracted: (Color) -> Unit
) {
    val density = LocalDensity.current

    var imageContainerWidth by remember { mutableIntStateOf(0) }
    var imageContainerHeight by remember { mutableIntStateOf(0) }

    val pipSize = with(density) { Dimens.pipImageSize.toPx() }

    val initialOffset =
        Offset(Dimens.paddingExtraLarge.value * 3, Dimens.paddingExtraLarge.value * 3)
    var currentPosition by remember { mutableStateOf(initialOffset) }

    val animatedPosition by animateOffsetAsState(
        targetValue = currentPosition,
        label = "dual_photo"
    )

    var isBackImageAsBackground by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(borderRadiusLarge))
            .onGloballyPositioned { coordinates ->
                imageContainerWidth = coordinates.size.width
                imageContainerHeight = coordinates.size.height
            }
    ) {
        EnhancedAsyncImage(
            model = if (isBackImageAsBackground) dualViewImage.backImageUrl else dualViewImage.frontImageUrl,            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            onSuccess = { result ->
                val drawable = result.result.drawable
                val bitmap = (drawable as? BitmapDrawable)?.bitmap
                bitmap?.let {
                    onDominantColorExtracted(it.extractDominantColorFromBitmap())
                }
            }
        )

        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        animatedPosition.x.roundToInt(),
                        animatedPosition.y.roundToInt()
                    )
                }
                .size(Dimens.pipImageSize)
                .clip(RoundedCornerShape(Dimens.borderRadiusMedium))
                .border(
                    Dimens.borderWidth,
                    Color.White,
                    RoundedCornerShape(Dimens.borderRadiusMedium)
                )
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            val constrainedX =
                                currentPosition.x.coerceIn(0f, imageContainerWidth - pipSize)
                            val constrainedY =
                                currentPosition.y.coerceIn(0f, imageContainerHeight - pipSize)

                            if (constrainedX != currentPosition.x || constrainedY != currentPosition.y) {
                                currentPosition = Offset(constrainedX, constrainedY)
                            }
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        val newX = (currentPosition.x + dragAmount.x).coerceIn(
                            0f,
                            imageContainerWidth - pipSize
                        )
                        val newY = (currentPosition.y + dragAmount.y).coerceIn(
                            0f,
                            imageContainerHeight - pipSize
                        )
                        currentPosition = Offset(newX, newY)
                    }
                }
                .clickable {
                    isBackImageAsBackground = !isBackImageAsBackground
                    val constrainedX = currentPosition.x.coerceIn(0f, imageContainerWidth - pipSize)
                    val constrainedY =
                        currentPosition.y.coerceIn(0f, imageContainerHeight - pipSize)
                    currentPosition = Offset(constrainedX, constrainedY)
                }
        ) {
            EnhancedAsyncImage(
                model = if (isBackImageAsBackground) dualViewImage.frontImageUrl else dualViewImage.backImageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun GroupSizeIndicator(groupSize: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(Dimens.borderRadiusSmall)
            )
            .padding(
                horizontal = Dimens.paddingSmall,
                vertical = Dimens.spacingTiny
            )
    ) {
        Icon(
            imageVector = if (groupSize <= 1) Icons.Default.Person else Icons.Default.Group,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.size(Dimens.iconSizeSmall)
        )

        Spacer(modifier = Modifier.width(Dimens.spacingTiny))

        Text(
            text = if (groupSize <= 1)
                stringResource(R.string.feed_trek_solo)
            else stringResource(R.string.feed_trek_group_size, groupSize),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun MetricsRow(
    metrics: HikePost.Metrics,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MetricItem(
            icon = Icons.Default.ArrowUpward,
            value = stringResource(R.string.feed_metric_elevation_value, metrics.elevation),
        )

        MetricItem(
            icon = Icons.AutoMirrored.Default.DirectionsWalk,
            value = stringResource(R.string.feed_metric_distance_value, metrics.distance),
        )

        MetricItem(
            icon = Icons.Default.Timer,
            value = metrics.duration,
        )
    }
}

@Composable
fun MetricItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(Dimens.spacingTiny)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White.copy(alpha = Dimens.secondaryIconOpacity),
            modifier = Modifier.size(Dimens.iconSizeMedium)
        )

        Spacer(modifier = Modifier.width(Dimens.spacingSmall))

        Text(
            text = value,
            color = Color.White.copy(alpha = Dimens.primaryIconOpacity),
            style = MaterialTheme.typography.labelMedium
        )
    }
}