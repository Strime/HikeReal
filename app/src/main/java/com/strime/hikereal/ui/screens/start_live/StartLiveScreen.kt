package com.strime.hikereal.ui.screens.start_live

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.strime.hikereal.R
import com.strime.hikereal.ui.components.DebouncedButtons
import com.strime.hikereal.ui.theme.Dimens.paddingLarge
import com.strime.hikereal.ui.theme.Dimens.spacingLarge
import com.strime.hikereal.ui.theme.Dimens.spacingMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartLiveScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = { Text(stringResource(R.string.start_hiking_new_activity)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(paddingLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Hiking,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = Color.White.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(spacingLarge))

                Text(
                    text = stringResource(R.string.start_hiking_ready_to_start),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(spacingMedium))

                Text(
                    text = stringResource(R.string.start_hiking_capture_your_adventure),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(spacingLarge))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.start_hiking_features),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        ListItem(
                            headlineContent = {
                                Text(
                                    text = stringResource(R.string.start_hiking_stats_tracking),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = stringResource(R.string.start_hiking_stats_description),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                )
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.Timeline,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        )

                        ListItem(
                            headlineContent = {
                                Text(
                                    text = stringResource(R.string.start_hiking_hikereel_photo),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = stringResource(R.string.start_hiking_hikereel_description),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                )
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.Photo,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        )

                        ListItem(
                            headlineContent = {
                                Text(
                                    text = stringResource(R.string.start_hiking_share_title),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = stringResource(R.string.start_hiking_share_description),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                )
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.Photo,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        )
                    }
                }
            }

            DebouncedButtons.DebouncedFullWidthButton(
                text = stringResource(R.string.start_hiking_start_activity),
                onClick = {
                    navController.navigate("active_hike_screen")
                },
                modifier = Modifier.padding(paddingLarge),
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.primary
            )


            Spacer(modifier = Modifier.height(spacingMedium))
        }
    }
}