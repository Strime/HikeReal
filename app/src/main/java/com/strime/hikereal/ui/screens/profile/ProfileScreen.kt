package com.strime.hikereal.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.strime.hikereal.R
import com.strime.hikereal.domain.model.Badge
import com.strime.hikereal.domain.model.BadgeLevel
import com.strime.hikereal.domain.model.BadgeType
import com.strime.hikereal.domain.model.HikeData
import com.strime.hikereal.domain.model.UserProfile
import com.strime.hikereal.domain.model.UserStats
import com.strime.hikereal.ui.theme.Dimens
import com.strime.hikereal.ui.theme.Dimens.paddingLarge
import com.strime.hikereal.ui.theme.Dimens.paddingMedium
import com.strime.hikereal.ui.theme.Dimens.paddingSmall
import com.strime.hikereal.ui.theme.Dimens.spacingLarge
import com.strime.hikereal.ui.theme.Dimens.spacingMedium
import com.strime.hikereal.ui.theme.Dimens.spacingSmall

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Add friends */ }) {
                        Icon(
                            imageVector = Icons.Filled.PersonAdd,
                            contentDescription = "Add friends"
                        )
                    }
                },
                windowInsets = WindowInsets(0, 0, 0, 0),
                actions = {
                    IconButton(onClick = { /* TODO: Open calendar */ }) {
                        Icon(
                            imageVector = Icons.Filled.CalendarMonth,
                            contentDescription = "Calendar"
                        )
                    }
                    IconButton(onClick = { /* TODO: Open settings */ }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings"
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(paddingLarge)
                )
            } else {
                ProfileContent(
                    uiState = uiState,
                    onHikeClick = { hikeId ->
                        //TODO: navController.navigate("hikeDetail/$hikeId")
                    },
                    onAllBadgesClick = {
                        //TODO: navController.navigate("allBadges")
                    }
                )
            }
        }
    }
}

@Composable
fun ProfileHeader(
    userProfile: UserProfile?,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = paddingMedium)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(110.dp)
        ) {
            // Level progress ring
            CircularProgressIndicator(
                progress = { (userProfile?.experiencePoints ?: 0) / 1000f },
                modifier = Modifier.size(110.dp),
                strokeWidth = 4.dp,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                color = MaterialTheme.colorScheme.primary
            )

            // Profile image with border
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(95.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    )
                    .border(
                        width = Dimens.borderWidth,
                        color = MaterialTheme.colorScheme.surface,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            // Level indicator
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.BottomEnd)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.surface,
                        shape = CircleShape
                    )
            ) {
                Text(
                    text = "${userProfile?.level ?: 1}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(spacingMedium))

        Text(
            text = userProfile?.username ?: "",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.EmojiEvents,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${userProfile?.experiencePoints ?: 0}/1000 ${stringResource(R.string.profile_xp)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(spacingMedium))
    }
}

@Composable
fun ThisYearSection(userStats: UserStats) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = paddingMedium, vertical = spacingMedium)
    ) {
        Text(
            text = stringResource(R.string.profile_this_year),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = paddingSmall)
        )

        Column(
            modifier = Modifier.padding(paddingMedium)
        ) {
            YearlyStatRow(
                icon = Icons.AutoMirrored.Filled.DirectionsWalk,
                label = stringResource(R.string.profile_total_hikes),
                value = "${userStats.yearlyHikeCount} ${stringResource(R.string.profile_hikes)}",
            )

            Spacer(modifier = Modifier.height(spacingMedium))

            YearlyStatRow(
                icon = Icons.Filled.Straighten,
                label = stringResource(R.string.profile_total_distance),
                value = "${userStats.yearlyDistance} ${stringResource(R.string.profile_km)}",
            )

            Spacer(modifier = Modifier.height(spacingMedium))

            YearlyStatRow(
                icon = Icons.Filled.Terrain,
                label = stringResource(R.string.profile_total_elevation),
                value = "${userStats.yearlyElevation} ${stringResource(R.string.profile_m)}",
            )
        }
    }
}

@Composable
fun YearlyStatRow(
    icon: ImageVector,
    label: String,
    value: String,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    onHikeClick: (String) -> Unit,
    onAllBadgesClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = paddingMedium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            ProfileHeader(
                userProfile = uiState.userProfile,
            )

            Spacer(modifier = Modifier.height(spacingLarge))

            ThisYearSection(userStats = uiState.userStats)

            Spacer(modifier = Modifier.height(spacingLarge))

            BadgesSection(
                badges = uiState.badges ?: emptyList(),
                onAllBadgesClick = onAllBadgesClick
            )

            Spacer(modifier = Modifier.height(spacingLarge))
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = paddingMedium),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Spacer(modifier = Modifier.height(spacingLarge))

            Row {
                Text(
                    text = stringResource(R.string.profile_recent_hikes),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = paddingMedium),
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (uiState.recentHikeData.isNotEmpty()) {
            items(uiState.recentHikeData) { hike ->
                HikeItem(
                    hikeData = hike,
                    onClick = { onHikeClick(hike.id) }
                )
                Spacer(modifier = Modifier.height(spacingMedium))
            }
        } else {
            item {
                EmptyHikesPlaceholder()
            }
        }

        item {
            Spacer(modifier = Modifier.height(paddingLarge))
        }
    }
}

@Composable
fun BadgesSection(
    badges: List<Badge>,
    onAllBadgesClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = paddingSmall),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.profile_badges_section_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            ViewAllTextButton(onAllBadgesClick)
        }

        if (badges.isEmpty()) {
            EmptyBadgesPlaceholder()
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
            ) {
                items(badges) { badge ->
                    BadgeItem(badge = badge)
                }
            }
        }
    }
}

@Composable
fun BadgeItem(badge: Badge) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(70.dp)
                .shadow(
                    elevation = 2.dp,
                    shape = CircleShape
                )
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = CircleShape
                )
                .border(
                    width = Dimens.borderWidth * 2,
                    color = getBadgeColor(badge.level),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = getBadgeIcon(badge.type),
                contentDescription = stringResource(R.string.profile_badge_content_description),
                modifier = Modifier.size(Dimens.iconSizeExtraLarge),
                tint = getBadgeColor(badge.level)
            )
        }

        Spacer(modifier = Modifier.height(Dimens.spacingTiny * 2))

        Text(
            text = badge.name,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun EmptyBadgesPlaceholder() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Filled.EmojiEvents,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(spacingSmall))

            Text(
                text = stringResource(R.string.profile_empty_badges_message),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun EmptyHikesPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.Terrain,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(spacingMedium))

        Text(
            text = stringResource(R.string.coming_soon),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(spacingMedium))

        Text(
            text = stringResource(R.string.profile_construction_message),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun HikeItem(hikeData: HikeData, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(Dimens.paddingExtraLarge)
        ) {
            Text(
                text = hikeData.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = hikeData.formattedDate,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Straighten,
                        contentDescription = stringResource(R.string.profile_distance_icon_description),
                        modifier = Modifier.size(Dimens.iconSizeMedium),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${hikeData.distance} km",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Icon(
                        imageVector = Icons.Filled.Terrain,
                        contentDescription = stringResource(R.string.profile_elevation_icon_description),
                        modifier = Modifier.size(Dimens.iconSizeMedium),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${hikeData.elevation} m",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(Dimens.iconSizeMedium),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${hikeData.views}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.Filled.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(Dimens.iconSizeMedium),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${hikeData.likes}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}


@Composable
private fun ViewAllTextButton(onAllBadgesClick: () -> Unit) {
    TextButton(
        onClick = onAllBadgesClick,
        contentPadding = PaddingValues(horizontal = paddingSmall)
    ) {
        Text(stringResource(R.string.profile_view_all_badges))
        Spacer(modifier = Modifier.width(paddingSmall))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(Dimens.iconSizeMedium)
        )
    }
}

@Composable
fun getBadgeIcon(type: BadgeType): ImageVector {
    return when (type) {
        BadgeType.DISTANCE -> Icons.AutoMirrored.Filled.DirectionsWalk
        BadgeType.ELEVATION -> Icons.Filled.Terrain
        BadgeType.SPEED -> Icons.Filled.Speed
        BadgeType.EXPLORE -> Icons.Filled.Explore
        BadgeType.STREAK -> Icons.Filled.Whatshot
        BadgeType.SOCIAL -> Icons.Filled.People
        BadgeType.SPECIAL -> Icons.Filled.EmojiEvents
    }
}

@Composable
fun getBadgeColor(level: BadgeLevel): Color {
    return Color(level.colorHex)
}
