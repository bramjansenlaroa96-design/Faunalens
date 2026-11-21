package com.example.ui.scanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Cameraswitch
import androidx.compose.material.icons.rounded.CenterFocusStrong
import androidx.compose.material.icons.rounded.FlashOff
import androidx.compose.material.icons.rounded.FlashOn
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.components.CameraPreviewSurface
import com.example.ui.components.FirebaseAiNoticeBanner
import com.example.ui.components.SampleModePill
import com.example.ui.components.rememberCameraControllerHandle
import java.io.File

@Composable
fun ScannerScreen(
    viewModel: ScannerViewModel,
    onNavigateToDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var cameraAvailable by remember { mutableStateOf(true) }
    var hasFlashUnit by remember { mutableStateOf(false) }
    var torchEnabled by remember { mutableStateOf(false) }
    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }

    val cameraController = rememberCameraControllerHandle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.processPickedUri(context, uri)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.scanCompletedEvents.collect { scanId ->
            onNavigateToDetail(scanId)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A140E))
    ) {
        if (hasCameraPermission && cameraAvailable) {
            CameraPreviewSurface(
                controllerHandle = cameraController,
                lensFacing = lensFacing,
                torchEnabled = torchEnabled,
                onCameraAvailabilityChanged = { available, flash ->
                    cameraAvailable = available
                    hasFlashUnit = flash
                    if (!flash) torchEnabled = false
                },
                modifier = Modifier.fillMaxSize()
            )
            // Subtle top & bottom scrim for high-contrast HUD controls
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.58f),
                                Color.Transparent,
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.72f)
                            )
                        )
                    )
            )
        } else {
            // Atmospheric background fallback when permission not granted or no hardware camera
            Image(
                painter = painterResource(id = R.drawable.img_field_banner),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0B1811).copy(alpha = 0.72f))
            )
        }

        // Top HUD Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.scanner_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                    Text(
                        text = stringResource(R.string.scanner_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.82f)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!viewModel.isLiveAiEnabled) {
                        SampleModePill()
                    }
                    if (hasCameraPermission && cameraAvailable) {
                        if (hasFlashUnit) {
                            FilledTonalIconButton(
                                onClick = { torchEnabled = !torchEnabled },
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("flash_toggle_button"),
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = Color.Black.copy(alpha = 0.45f),
                                    contentColor = if (torchEnabled) MaterialTheme.colorScheme.secondary else Color.White
                                )
                            ) {
                                Icon(
                                    imageVector = if (torchEnabled) Icons.Rounded.FlashOn else Icons.Rounded.FlashOff,
                                    contentDescription = stringResource(R.string.action_toggle_flash)
                                )
                            }
                        }
                        FilledTonalIconButton(
                            onClick = {
                                torchEnabled = false
                                lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                                    CameraSelector.LENS_FACING_FRONT
                                } else {
                                    CameraSelector.LENS_FACING_BACK
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("switch_camera_button"),
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = Color.Black.copy(alpha = 0.45f),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Cameraswitch,
                                contentDescription = stringResource(R.string.action_switch_camera)
                            )
                        }
                    }
                }
            }

            if (!viewModel.isLiveAiEnabled) {
                FirebaseAiNoticeBanner(compact = true)
            }
        }

        // Center Viewfinder Reticle OR Permission / Fallback Card
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (!hasCameraPermission || !cameraAvailable) {
                CameraFallbackCard(
                    hasPermission = hasCameraPermission,
                    cameraAvailable = cameraAvailable,
                    onRequestPermission = {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    onPickPhoto = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onTrySample = { viewModel.runSampleScan() }
                )
            } else {
                ViewfinderReticle(
                    isAnalyzing = uiState is ScannerUiState.Analyzing,
                    modifier = Modifier.size(260.dp)
                )
            }
        }

        // Bottom Controls Dock (when camera is active and ready)
        if (hasCameraPermission && cameraAvailable && uiState is ScannerUiState.Ready) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilledTonalButton(
                    onClick = { viewModel.runSampleScan() },
                    modifier = Modifier.testTag("sample_scan_chip"),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color.Black.copy(alpha = 0.55f),
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = stringResource(R.string.action_sample_scan),
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 460.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pick Photo from Gallery (Android Photo Picker - zero permissions)
                    FilledTonalIconButton(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier
                            .size(56.dp)
                            .testTag("gallery_picker_button"),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = Color.White.copy(alpha = 0.18f),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PhotoLibrary,
                            contentDescription = stringResource(R.string.action_pick_photo),
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    // Primary Camera Shutter Button
                    Surface(
                        onClick = {
                            cameraController.capturePhoto(
                                onCaptured = { file -> viewModel.processCapturedFile(context, file) },
                                onError = { msg -> viewModel.reportError(msg) }
                            )
                        },
                        modifier = Modifier
                            .size(80.dp)
                            .testTag("capture_scan_button"),
                        shape = CircleShape,
                        color = Color.White,
                        border = BorderStroke(4.dp, MaterialTheme.colorScheme.secondary)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.CenterFocusStrong,
                                contentDescription = stringResource(R.string.action_capture_scan),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }

                    // Spacer / Lens quick action symmetry
                    FilledTonalIconButton(
                        onClick = { viewModel.runSampleScan() },
                        modifier = Modifier
                            .size(56.dp)
                            .testTag("quick_sample_button"),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = Color.White.copy(alpha = 0.18f),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PhotoCamera,
                            contentDescription = stringResource(R.string.action_sample_scan),
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        }

        // Analyzing Overlay
        AnimatedVisibility(
            visible = uiState is ScannerUiState.Analyzing,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            val analyzing = uiState as? ScannerUiState.Analyzing
            AnalyzingOverlay(previewImagePath = analyzing?.previewImagePath)
        }

        // Error Dialog Overlay
        val errorState = uiState as? ScannerUiState.Error
        if (errorState != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.55f))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.widthIn(max = 420.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Scan Interrupted",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = errorState.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { viewModel.dismissError() },
                                modifier = Modifier.testTag("dismiss_scan_error_button")
                            ) {
                                Text("OK")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ViewfinderReticle(
    isAnalyzing: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "reticle_scan")
    val scanProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scan_line"
    )

    val bracketColor = if (isAnalyzing) Color(0xFFF59E0B) else Color.White.copy(alpha = 0.9f)
    Canvas(modifier = modifier) {
        val stroke = 4.dp.toPx()
        val cornerLen = 34.dp.toPx()
        val w = size.width
        val h = size.height

        // Top-left
        drawLine(bracketColor, Offset(0f, 0f), Offset(cornerLen, 0f), stroke, StrokeCap.Round)
        drawLine(bracketColor, Offset(0f, 0f), Offset(0f, cornerLen), stroke, StrokeCap.Round)
        // Top-right
        drawLine(bracketColor, Offset(w - cornerLen, 0f), Offset(w, 0f), stroke, StrokeCap.Round)
        drawLine(bracketColor, Offset(w, 0f), Offset(w, cornerLen), stroke, StrokeCap.Round)
        // Bottom-left
        drawLine(bracketColor, Offset(0f, h), Offset(cornerLen, h), stroke, StrokeCap.Round)
        drawLine(bracketColor, Offset(0f, h - cornerLen), Offset(0f, h), stroke, StrokeCap.Round)
        // Bottom-right
        drawLine(bracketColor, Offset(w - cornerLen, h), Offset(w, h), stroke, StrokeCap.Round)
        drawLine(bracketColor, Offset(w, h - cornerLen), Offset(w, h), stroke, StrokeCap.Round)

        if (isAnalyzing) {
            val y = scanProgress * h
            drawLine(
                color = Color(0xFFF59E0B).copy(alpha = 0.85f),
                start = Offset(12.dp.toPx(), y),
                end = Offset(w - 12.dp.toPx(), y),
                strokeWidth = 2.5.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun CameraFallbackCard(
    hasPermission: Boolean,
    cameraAvailable: Boolean,
    onRequestPermission: () -> Unit,
    onPickPhoto: () -> Unit,
    onTrySample: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 460.dp),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        tonalElevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Rounded.PhotoCamera,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Text(
                text = if (!hasPermission) {
                    stringResource(R.string.scanner_permission_title)
                } else {
                    "Camera Preview Unavailable"
                },
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Text(
                text = if (!hasPermission) {
                    stringResource(R.string.scanner_permission_body)
                } else {
                    stringResource(R.string.scanner_camera_unavailable)
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            if (!hasPermission) {
                Button(
                    onClick = onRequestPermission,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("enable_camera_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PhotoCamera,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(stringResource(R.string.action_enable_camera))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onPickPhoto,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("fallback_gallery_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PhotoLibrary,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(stringResource(R.string.action_pick_photo))
                }

                FilledTonalButton(
                    onClick = onTrySample,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("fallback_sample_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text("Sample")
                }
            }
        }
    }
}

@Composable
private fun AnalyzingOverlay(previewImagePath: String?) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A140E).copy(alpha = 0.88f))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 380.dp)
                .testTag("analyzing_overlay"),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (!previewImagePath.isNullOrBlank()) {
                    AsyncImage(
                        model = File(previewImagePath),
                        contentDescription = "Captured specimen preview",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.secondary,
                                shape = MaterialTheme.shapes.medium
                            )
                    )
                }
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = stringResource(R.string.scanner_analyzing_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.scanner_analyzing_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
