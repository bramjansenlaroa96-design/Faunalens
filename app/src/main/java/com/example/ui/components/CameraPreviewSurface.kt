package com.example.ui.components

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.io.File
import java.util.concurrent.TimeUnit

private const val TAG = "CameraPreviewSurface"

class CameraControllerHandle(
    private val context: Context
) {
    internal var imageCapture: ImageCapture? = null
    internal var camera: Camera? = null

    fun setTorch(enabled: Boolean) {
        runCatching {
            if (camera?.cameraInfo?.hasFlashUnit() == true) {
                camera?.cameraControl?.enableTorch(enabled)
            }
        }
    }

    fun hasFlashUnit(): Boolean = camera?.cameraInfo?.hasFlashUnit() == true

    fun capturePhoto(
        onCaptured: (File) -> Unit,
        onError: (String) -> Unit
    ) {
        val capture = imageCapture
        if (capture == null) {
            onError("Camera is still initializing or unavailable.")
            return
        }
        val tempFile = File(context.cacheDir, "capture_${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(tempFile).build()
        capture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    onCaptured(tempFile)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed", exception)
                    runCatching { tempFile.delete() }
                    onError(exception.localizedMessage ?: "Failed to capture photo.")
                }
            }
        )
    }
}

@Composable
fun rememberCameraControllerHandle(): CameraControllerHandle {
    val context = LocalContext.current
    return remember(context) { CameraControllerHandle(context) }
}

@Composable
fun CameraPreviewSurface(
    controllerHandle: CameraControllerHandle,
    lensFacing: Int,
    torchEnabled: Boolean,
    onCameraAvailabilityChanged: (Boolean, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }

    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    LaunchedEffect(torchEnabled) {
        controllerHandle.setTorch(torchEnabled)
    }

    DisposableEffect(lifecycleOwner, lensFacing) {
        val providerFuture = ProcessCameraProvider.getInstance(context)
        val executor = ContextCompat.getMainExecutor(context)
        var active = true

        providerFuture.addListener({
            if (!active) return@addListener
            try {
                val provider = providerFuture.get()
                cameraProvider = provider

                val selector = when {
                    provider.hasCamera(
                        CameraSelector.Builder().requireLensFacing(lensFacing).build()
                    ) -> CameraSelector.Builder().requireLensFacing(lensFacing).build()

                    provider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ->
                        CameraSelector.DEFAULT_BACK_CAMERA

                    provider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ->
                        CameraSelector.DEFAULT_FRONT_CAMERA

                    else -> null
                }

                if (selector == null) {
                    onCameraAvailabilityChanged(false, false)
                    return@addListener
                }

                val preview = Preview.Builder().build().apply {
                    surfaceProvider = previewView.surfaceProvider
                }
                val imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()

                provider.unbindAll()
                val boundCamera = provider.bindToLifecycle(
                    lifecycleOwner,
                    selector,
                    preview,
                    imageCapture
                )

                controllerHandle.imageCapture = imageCapture
                controllerHandle.camera = boundCamera
                controllerHandle.setTorch(torchEnabled)
                onCameraAvailabilityChanged(true, boundCamera.cameraInfo.hasFlashUnit())

                previewView.setOnTouchListener { view, event ->
                    if (event.action == MotionEvent.ACTION_UP) {
                        val factory = previewView.meteringPointFactory
                        val point = factory.createPoint(event.x, event.y)
                        val action = FocusMeteringAction.Builder(point)
                            .setAutoCancelDuration(3, TimeUnit.SECONDS)
                            .build()
                        boundCamera.cameraControl.startFocusAndMetering(action)
                        view.performClick()
                    }
                    true
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to bind CameraX use cases", e)
                onCameraAvailabilityChanged(false, false)
            }
        }, executor)

        onDispose {
            active = false
            runCatching { cameraProvider?.unbindAll() }
            controllerHandle.imageCapture = null
            controllerHandle.camera = null
        }
    }

    AndroidView(
        factory = { previewView },
        modifier = modifier
    )
}
