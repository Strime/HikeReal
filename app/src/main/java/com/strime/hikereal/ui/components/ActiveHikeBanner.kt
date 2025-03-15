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
import androidx.compose.ui.unit.dp
import com.strime.hikereal.R
import com.strime.hikereal.domain.model.HikeState

@Composable
fun ActiveHikeBanner(
    hikeState: HikeState,
    duration: String,
    distance: String,
    onCompleteClick: () -> Unit,
    onCapturePhotoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (hikeState) {
        HikeState.ACTIVE -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val contentColor = when (hikeState) {
        HikeState.ACTIVE -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        color = backgroundColor,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCapturePhotoClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = when (hikeState) {
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
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = duration,
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Icon(
                        imageVector = Icons.Default.Straighten,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = distance,
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onCapturePhotoClick) {
                    Icon(
                        imageVector = Icons.Default.Camera,
                        contentDescription = stringResource(id = R.string.capture_hikereal),
                        tint = contentColor
                    )
                }

                IconButton(
                    onClick = onCompleteClick,
                    modifier = Modifier.clickable(onClick = onCompleteClick, indication = null, interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() })
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