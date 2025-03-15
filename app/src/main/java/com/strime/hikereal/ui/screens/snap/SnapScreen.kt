package com.strime.hikereal.ui.screens.snap

import android.Manifest
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SnapScreen(
    navController: NavController,
    viewModel: SnapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    // Demande de permission pour la caméra
    var hasCameraPermission by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    // Lancer la demande de permission au démarrage
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    // Configuration de CameraX
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    val cameraSelector by remember(uiState.cameraFacingFront) {
        mutableStateOf(
            if (uiState.cameraFacingFront) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (hasCameraPermission) {
            // Affichage de la prévisualisation caméra réelle
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val executor = ContextCompat.getMainExecutor(ctx)

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()

                        // Configuration de la prévisualisation
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        // Configuration de la capture d'image
                        imageCapture = ImageCapture.Builder()
                            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                            .build()

                        try {
                            // Unbind des cas d'utilisation avant de rebind
                            cameraProvider.unbindAll()

                            // Bind des cas d'utilisation à la caméra
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageCapture
                            )
                        } catch (e: Exception) {
                            Log.e("CameraX", "Binding failed", e)
                        }
                    }, executor)

                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Afficher message de demande de permission
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Camera permission required",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }
                    ) {
                        Text("Grant Permission")
                    }
                }
            }
        }

        // Afficher l'indicateur de chargement pendant la capture
        if (uiState.isCapturing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Capturing photos...",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        // Afficher le challenge en haut de l'écran
        if (!uiState.challengePrompt.isNullOrEmpty() && !uiState.isCapturing && !uiState.isCaptureComplete) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp)
                    .align(Alignment.TopCenter)
            ) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black.copy(alpha = 0.7f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Today's Challenge",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = uiState.challengePrompt,
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Time remaining: ${uiState.remainingTime}",
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // Contrôles de la caméra
        if (!uiState.isCapturing && !uiState.isCaptureComplete) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Toggle flash
                    IconButton(
                        onClick = { viewModel.toggleFlash() },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = if (uiState.flashEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                            contentDescription = "Flash",
                            tint = Color.White
                        )
                    }

                    // Capture button
                    IconButton(
                        onClick = {
                            // Démarrer la capture de la séquence
                            viewModel.startPhotoCapture()

                            // Dans une vraie implémentation, nous utiliserions CameraX pour capturer
                            // les photos depuis les deux caméras. Voici la logique de comment cela
                            // pourrait être fait (commentée car nécessite des permissions et des
                            // configurations supplémentaires).

                            /*
                            val imgCapture = imageCapture ?: return@IconButton

                            // Créer un répertoire pour stocker les photos
                            val outputDir = File(context.externalCacheDir ?: context.cacheDir, "photos")
                            if (!outputDir.exists()) outputDir.mkdirs()

                            // Fonction auxiliaire pour la capture
                            fun takePhoto(isFront: Boolean) {
                                val photoFile = File(
                                    outputDir,
                                    "${if (isFront) "front" else "back"}_${System.currentTimeMillis()}.jpg"
                                )

                                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                                imgCapture.takePicture(
                                    outputOptions,
                                    ContextCompat.getMainExecutor(context),
                                    object : ImageCapture.OnImageSavedCallback {
                                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                            val savedUri = Uri.fromFile(photoFile)
                                            viewModel.onPhotoTaken(savedUri.toString(), isFront)
                                        }

                                        override fun onError(exception: ImageCaptureException) {
                                            Log.e("CameraX", "Photo capture failed: ${exception.message}", exception)
                                        }
                                    }
                                )
                            }

                            // Logique de capture en séquence
                            // Serait mise en œuvre avec des coroutines et des délais entre les captures
                            */
                        },
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Camera,
                            contentDescription = "Take Photo",
                            tint = Color.Black,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    // Flip camera (pendant la prévisualisation seulement)
                    IconButton(
                        onClick = { viewModel.toggleCamera() },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlipCameraAndroid,
                            contentDescription = "Flip Camera",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        // Afficher écran de sélection des photos quand capture terminée
        if (uiState.isCaptureComplete) {
            PhotoSelectionScreen(
                frontPhotos = uiState.frontCapturedPhotos,
                backPhotos = uiState.backCapturedPhotos,
                selectedFrontIndex = uiState.selectedFrontPhotoIndex,
                selectedBackIndex = uiState.selectedBackPhotoIndex,
                captionText = uiState.captionText,
                hikingMetrics = uiState.hikingMetrics,
                onFrontPhotoSelected = { viewModel.selectFrontPhoto(it) },
                onBackPhotoSelected = { viewModel.selectBackPhoto(it) },
                onCaptionChange = { viewModel.updateCaption(it) },
                onDiscard = { viewModel.discardPhoto() },
                onSave = {
                    viewModel.savePhoto()
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun PhotoSelectionScreen(
    frontPhotos: List<String>,
    backPhotos: List<String>,
    selectedFrontIndex: Int,
    selectedBackIndex: Int,
    captionText: String,
    hikingMetrics: SnapViewModel.HikingMetrics?,
    onFrontPhotoSelected: (Int) -> Unit,
    onBackPhotoSelected: (Int) -> Unit,
    onCaptionChange: (String) -> Unit,
    onDiscard: () -> Unit,
    onSave: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Titre
            Text(
                text = "Choose Your HikeReal Moment",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Photos frontales
            Text(
                text = "Selfie Photos",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(frontPhotos) { index, photo ->
                    // Dans une application réelle, utiliser Coil pour charger l'image
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_camera), // Placeholder
                        contentDescription = "Front photo $index",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                width = 3.dp,
                                color = if (index == selectedFrontIndex) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { onFrontPhotoSelected(index) },
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Photos arrière
            Text(
                text = "View Photos",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(backPhotos) { index, photo ->
                    // Dans une application réelle, utiliser Coil pour charger l'image
                    Image(
                        painter = painterResource(id = android.R.drawable.ic_menu_report_image), // Placeholder
                        contentDescription = "Back photo $index",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                width = 3.dp,
                                color = if (index == selectedBackIndex) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { onBackPhotoSelected(index) },
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Hiking metrics
            if (hikingMetrics != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.DarkGray
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${hikingMetrics.elevation}m",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Elevation",
                                fontSize = 12.sp,
                                color = Color.LightGray
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${hikingMetrics.distance}km",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Distance",
                                fontSize = 12.sp,
                                color = Color.LightGray
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = hikingMetrics.duration,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Duration",
                                fontSize = 12.sp,
                                color = Color.LightGray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Caption input
            OutlinedTextField(
                value = captionText,
                onValueChange = onCaptionChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Add a caption...") },
                maxLines = 3,
            )

            Spacer(modifier = Modifier.weight(1f))

            // Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onDiscard,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray
                    )
                ) {
                    Text("Discard")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = onSave,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Share HikeReal")
                }
            }
        }
    }
}