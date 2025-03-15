package com.strime.hikereal.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.strime.hikereal.R
import com.strime.hikereal.domain.model.ActiveHike
import com.strime.hikereal.domain.model.HikeState
import com.strime.hikereal.ui.theme.Dimens.iconSizeMedium
import com.strime.hikereal.ui.theme.Dimens.paddingExtraLarge
import com.strime.hikereal.ui.theme.Dimens.paddingMedium
import com.strime.hikereal.ui.theme.Dimens.spacingMedium
import com.strime.hikereal.ui.theme.Dimens.spacingSmall

@Composable
fun ActiveHikeBanner(
    activeHikeState: ActiveHike,
    duration: String,
    distance: String,
    onCompleteClick: () -> Unit,
    onCapturePhotoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (activeHikeState.hikeState) {
        HikeState.ACTIVE -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val contentColor = when (activeHikeState.hikeState) {
        HikeState.ACTIVE -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        color = backgroundColor,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = !activeHikeState.alreadyTookPhoto,
                onClick = { onCapturePhotoClick() }
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = paddingExtraLarge, vertical = paddingMedium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = when (activeHikeState.hikeState) {
                        HikeState.ACTIVE -> stringResource(R.string.active_hike_in_progress)
                        else -> stringResource(R.string.active_hike)
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = contentColor
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(iconSizeMedium)
                    )
                    Spacer(modifier = Modifier.width(spacingSmall))
                    Text(
                        text = duration,
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor
                    )

                    Spacer(modifier = Modifier.width(spacingMedium))

                    Icon(
                        imageVector = Icons.Default.Straighten,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(iconSizeMedium)
                    )
                    Spacer(modifier = Modifier.width(spacingSmall))
                    Text(
                        text = distance,
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(spacingMedium)) {
                if (!activeHikeState.alreadyTookPhoto) {
                    IconButton(onClick = onCapturePhotoClick) {
                        Icon(
                            imageVector = Icons.Default.Camera,
                            contentDescription = stringResource(id = R.string.capture_hikereal),
                            tint = contentColor
                        )
                    }
                }

                IconButton(
                    onClick = onCompleteClick,
                    modifier = Modifier.clickable(
                        onClick = onCompleteClick,
                        indication = null,
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() })
                ) {
                    Icon(
                        imageVector = Icons.Default.Stop,
                        contentDescription = stringResource(R.string.complete_hike),
                        tint = contentColor
                    )
                }
            }
        }
    }
}