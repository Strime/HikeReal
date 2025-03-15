package com.strime.hikereal.ui.screens.live

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stream
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.strime.hikereal.R
import com.strime.hikereal.navigation.AppRoutes
import com.strime.hikereal.ui.components.DebouncedButtons
import com.strime.hikereal.ui.theme.Dimens.iconSizeMedium
import com.strime.hikereal.ui.theme.Dimens.paddingLarge
import com.strime.hikereal.ui.theme.Dimens.spacingMedium

@Composable
fun LiveScreen(
    navController: NavController,
    viewModel: LiveViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingLarge)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Stream,
                contentDescription = null,
                modifier = Modifier.size(iconSizeMedium * 2.5f),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(spacingMedium))

            Text(
                text = stringResource(R.string.live_no_lives_available),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(spacingMedium))

            Text(
                text = stringResource(R.string.live_start_your_own_live),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        DebouncedButtons.DebouncedPulseButton(
            onClick = { navController.navigate(AppRoutes.START_LIVE) },
            text = stringResource(R.string.live_start_live_now),
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = paddingLarge * 3)
        )
    }
}
