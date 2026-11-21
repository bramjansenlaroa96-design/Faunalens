package com.example.data.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class StoredScanImage(
    val filePath: String,
    val width: Int,
    val height: Int
)

object ImageStorageHelper {
    private const val TAG = "ImageStorageHelper"
    private const val MAX_DIMENSION = 1440

    suspend fun persistUriToInternalScans(
        context: Context,
        uri: Uri
    ): StoredScanImage? = withContext(Dispatchers.IO) {
        try {
            val boundsOpts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            context.contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream, null, boundsOpts)
            }
            val sampleSize = computeInSampleSize(boundsOpts.outWidth, boundsOpts.outHeight, MAX_DIMENSION)
            val decodeOpts = BitmapFactory.Options().apply { inSampleSize = sampleSize }
            val bitmap = context.contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream, null, decodeOpts)
            } ?: return@withContext null

            saveBitmapToScans(context, bitmap)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to persist URI image", e)
            null
        }
    }

    suspend fun normalizeCapturedFile(
        context: Context,
        sourceFile: File,
        rotationDegrees: Int = 0
    ): StoredScanImage? = withContext(Dispatchers.IO) {
        try {
            val boundsOpts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeFile(sourceFile.absolutePath, boundsOpts)
            val sampleSize = computeInSampleSize(boundsOpts.outWidth, boundsOpts.outHeight, MAX_DIMENSION)
            val decodeOpts = BitmapFactory.Options().apply { inSampleSize = sampleSize }
            val rawBitmap = BitmapFactory.decodeFile(sourceFile.absolutePath, decodeOpts)
                ?: return@withContext null

            val rotated = if (rotationDegrees != 0) {
                val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
                Bitmap.createBitmap(rawBitmap, 0, 0, rawBitmap.width, rawBitmap.height, matrix, true)
                    .also { if (it != rawBitmap) rawBitmap.recycle() }
            } else {
                rawBitmap
            }

            val saved = saveBitmapToScans(context, rotated)
            runCatching { sourceFile.delete() }
            saved
        } catch (e: Exception) {
            Log.e(TAG, "Failed to normalize captured image", e)
            null
        }
    }

    private fun saveBitmapToScans(context: Context, bitmap: Bitmap): StoredScanImage {
        val scansDir = File(context.filesDir, "scans").apply { mkdirs() }
        val targetFile = File(scansDir, "scan_${UUID.randomUUID()}.jpg")
        FileOutputStream(targetFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 88, out)
        }
        val width = bitmap.width
        val height = bitmap.height
        bitmap.recycle()
        return StoredScanImage(targetFile.absolutePath, width, height)
    }

    private fun computeInSampleSize(width: Int, height: Int, maxDim: Int): Int {
        var inSampleSize = 1
        if (height > maxDim || width > maxDim) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while ((halfHeight / inSampleSize) >= maxDim && (halfWidth / inSampleSize) >= maxDim) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}
