package com.strime.hikereal.ui.screens.start_live

import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.strime.hikereal.R
import com.strime.hikereal.navigation.AppRoutes
import com.strime.hikereal.ui.components.DebouncedButtons
import com.strime.hikereal.ui.theme.Dimens.paddingLarge
import com.strime.hikereal.ui.theme.Dimens.spacingLarge
import com.strime.hikereal.ui.theme.Dimens.spacingMedium
import com.strime.hikereal.utils.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartLiveScreen(
    navController: NavController,
    viewModel: StartLiveViewModel = hiltViewModel()
) {
    val startHikeState by viewModel.startHikeState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(startHikeState) {
        when (startHikeState) {
            is UiState.Success -> {
                navController.popBackStack()
            }
            is UiState.Error -> {
                val errorMsg = (startHikeState as UiState.Error).message
                snackbarHostState.showSnackbar(
                    message = errorMsg.ifEmpty { "Une erreur est survenue" }
                )
            }
            else -> { /* Ne rien faire pour les autres états */ }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
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
                                        text = stringResource(R.string.start_hiking_hikereal_photo),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                },
                                supportingContent = {
                                    Text(
                                        text = stringResource(R.string.start_hiking_hikereal_description),
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
                                        imageVector = Icons.Default.Share,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            )
                        }
                    }
                }

                // Bouton de démarrage qui appelle viewModel.startNewHike()
                DebouncedButtons.DebouncedFullWidthButton(
                    text = stringResource(R.string.start_hiking_start_activity),
                    onClick = { viewModel.startNewHike() },
                    modifier = Modifier.padding(paddingLarge),
                    containerColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.primary,
                    enabled = startHikeState !is UiState.Loading
                )

                Spacer(modifier = Modifier.height(spacingMedium))
            }

            // Afficher un indicateur de chargement lorsque la randonnée est en cours de démarrage
            if (startHikeState is UiState.Loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            // Affichage des messages d'erreur
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter),
            ) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}