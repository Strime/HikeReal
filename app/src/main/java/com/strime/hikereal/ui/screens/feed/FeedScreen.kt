package com.strime.hikereal.ui.screens.feed

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.strime.hikereal.R
import com.strime.hikereal.domain.model.TrekPost
import com.strime.hikereal.ui.theme.Dimens
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
            is UiState.Loading -> {
                LoadingState()
            }
            is UiState.Success -> {
                SuccessState(posts = state.data, navController = navController)
            }
            is UiState.Error -> {
                ErrorState(message = state.message)
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
fun ErrorState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message.ifEmpty { stringResource(R.string.feed_error_loading) },
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun SuccessState(posts: List<TrekPost>, navController: NavController) {
    val pagerState = rememberPagerState(pageCount = { posts.size })

    VerticalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        SingleTrekPostView(post = posts[page], navController = navController)
    }
}

@Composable
fun SingleTrekPostView(post: TrekPost, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = Dimens.paddingLarge,
                vertical = Dimens.paddingSmall
            ),
        contentAlignment = Alignment.Center
    ) {
        TrekPostCard(post = post, navController = navController)
    }
}

@Composable
fun TrekPostCard(post: TrekPost, navController: NavController) {
    var isLiked by remember { mutableStateOf(post.isLiked) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        elevation = CardDefaults.cardElevation(Dimens.cardElevation),
        shape = RoundedCornerShape(Dimens.borderRadiusMedium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = Dimens.surfaceOpacity)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.paddingMedium),
            verticalArrangement = Arrangement.SpaceBetween
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
                        .clickable {
                            navController.navigate("profile/${post.userId}")
                        }
                ) {
                    AsyncImage(
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
                        style = MaterialTheme.typography.titleSmall
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = post.locationName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (post.dualViewImage != null) {
                    DualViewPhotoDisplay(dualViewImage = post.dualViewImage)
                } else {
                    RegularPhotoCarousel(imageUrls = post.imageUrls)
                }

                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth(),
                    color = Color.Black.copy(alpha = Dimens.overlayOpacity)
                ) {
                    MetricsRow(
                        metrics = post.metrics,
                        modifier = Modifier.padding(Dimens.paddingSmall)
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (post.caption.isNotEmpty()) {
                    Text(
                        text = post.caption,
                        modifier = Modifier.padding(Dimens.paddingLarge),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(horizontal = Dimens.paddingLarge))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.paddingMedium),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { isLiked = !isLiked }
                            .padding(Dimens.paddingMedium)
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = null,
                            tint = if (isLiked) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(Dimens.spacingSmall))
                        Text(
                            text = post.likeCount.toString(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { navController.navigate("post_detail/${post.id}") }
                            .padding(Dimens.paddingMedium)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.Message,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(Dimens.spacingSmall))
                        Text(
                            text = post.commentCount.toString(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DualViewPhotoDisplay(dualViewImage: TrekPost.DualViewImage) {
    val density = LocalDensity.current

    var imageContainerWidth by remember { mutableIntStateOf(0) }
    var imageContainerHeight by remember { mutableIntStateOf(0) }

    val pipSize = with(density) { Dimens.pipImageSize.toPx() }

    val initialOffset = Offset(Dimens.paddingMedium.value, Dimens.paddingMedium.value)
    var currentPosition by remember { mutableStateOf(initialOffset) }

    val animatedPosition by animateOffsetAsState(targetValue = currentPosition, label = "dual_photo")

    var isBackImageAsBackground by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                imageContainerWidth = coordinates.size.width
                imageContainerHeight = coordinates.size.height
            }
    ) {
        AsyncImage(
            model = if (isBackImageAsBackground) dualViewImage.backImageUrl else dualViewImage.frontImageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .offset { IntOffset(animatedPosition.x.roundToInt(), animatedPosition.y.roundToInt()) }
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
                            val constrainedX = currentPosition.x.coerceIn(0f, imageContainerWidth - pipSize)
                            val constrainedY = currentPosition.y.coerceIn(0f, imageContainerHeight - pipSize)

                            if (constrainedX != currentPosition.x || constrainedY != currentPosition.y) {
                                currentPosition = Offset(constrainedX, constrainedY)
                            }
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        val newX = (currentPosition.x + dragAmount.x).coerceIn(0f, imageContainerWidth - pipSize)
                        val newY = (currentPosition.y + dragAmount.y).coerceIn(0f, imageContainerHeight - pipSize)
                        currentPosition = Offset(newX, newY)
                    }
                }
                .clickable {
                    isBackImageAsBackground = !isBackImageAsBackground
                    val constrainedX = currentPosition.x.coerceIn(0f, imageContainerWidth - pipSize)
                    val constrainedY = currentPosition.y.coerceIn(0f, imageContainerHeight - pipSize)
                    currentPosition = Offset(constrainedX, constrainedY)
                }
        ) {
            AsyncImage(
                model = if (isBackImageAsBackground) dualViewImage.frontImageUrl else dualViewImage.backImageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun RegularPhotoCarousel(imageUrls: List<String>) {
    val pagerState = rememberPagerState(pageCount = { imageUrls.size })

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            AsyncImage(
                model = imageUrls[page],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        if (imageUrls.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = Dimens.paddingMedium),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(imageUrls.size) { index ->
                    val color = if (pagerState.currentPage == index)
                        Color.White else Color.White.copy(alpha = Dimens.indicatorOpacity)
                    Box(
                        modifier = Modifier
                            .padding(horizontal = Dimens.spacingSmall)
                            .size(Dimens.photoIndicatorSize)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }
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
    metrics: TrekPost.Metrics,
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
            label = stringResource(R.string.feed_metric_elevation)
        )

        MetricItem(
            icon = Icons.AutoMirrored.Default.DirectionsWalk,
            value = stringResource(R.string.feed_metric_distance_value, metrics.distance),
            label = stringResource(R.string.feed_metric_distance)
        )

        MetricItem(
            icon = Icons.Default.Timer,
            value = metrics.duration,
            label = stringResource(R.string.feed_metric_duration)
        )
    }
}

@Composable
fun MetricItem(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, label: String) {
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