package com.example.ui.scanner

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.ai.SpeciesAnalyzerRepository
import com.example.data.local.ImageStorageHelper
import com.example.data.local.ScanHistoryRepository
import java.io.File
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "ScannerViewModel"

sealed interface ScannerUiState {
    data object Ready : ScannerUiState
    data class Analyzing(val previewImagePath: String?) : ScannerUiState
    data class Error(val message: String) : ScannerUiState
}

class ScannerViewModel(
    private val analyzerRepository: SpeciesAnalyzerRepository,
    private val historyRepository: ScanHistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ScannerUiState>(ScannerUiState.Ready)
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    private val _scanCompletedEvents = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val scanCompletedEvents: SharedFlow<String> = _scanCompletedEvents.asSharedFlow()

    val isLiveAiEnabled: Boolean
        get() = analyzerRepository.isLiveAiEnabled

    fun processCapturedFile(context: Context, file: File) {
        viewModelScope.launch {
            _uiState.value = ScannerUiState.Analyzing(file.absolutePath)
            try {
                val stored = ImageStorageHelper.normalizeCapturedFile(context, file)
                if (stored != null) {
                    _uiState.value = ScannerUiState.Analyzing(stored.filePath)
                }
                val result = analyzerRepository.identifySpeciesFromImage(
                    imagePath = stored?.filePath,
                    imageWidth = stored?.width ?: 1080,
                    imageHeight = stored?.height ?: 1440
                )
                historyRepository.saveScan(result)
                _uiState.value = ScannerUiState.Ready
                _scanCompletedEvents.tryEmit(result.id)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to analyze captured file", e)
                _uiState.value = ScannerUiState.Error(
                    e.localizedMessage ?: "Unable to analyze captured photo."
                )
            }
        }
    }

    fun processPickedUri(context: Context, uri: Uri) {
        viewModelScope.launch {
            _uiState.value = ScannerUiState.Analyzing(null)
            try {
                val stored = ImageStorageHelper.persistUriToInternalScans(context, uri)
                if (stored == null) {
                    _uiState.value = ScannerUiState.Error("Could not read the selected image.")
                    return@launch
                }
                _uiState.value = ScannerUiState.Analyzing(stored.filePath)
                val result = analyzerRepository.identifySpeciesFromImage(
                    imagePath = stored.filePath,
                    imageWidth = stored.width,
                    imageHeight = stored.height
                )
                historyRepository.saveScan(result)
                _uiState.value = ScannerUiState.Ready
                _scanCompletedEvents.tryEmit(result.id)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to analyze picked image", e)
                _uiState.value = ScannerUiState.Error(
                    e.localizedMessage ?: "Unable to analyze selected photo."
                )
            }
        }
    }

    fun runSampleScan() {
        viewModelScope.launch {
            _uiState.value = ScannerUiState.Analyzing(null)
            try {
                val result = analyzerRepository.identifySpeciesFromImage(
                    imagePath = null,
                    imageWidth = 1080,
                    imageHeight = 1440
                )
                historyRepository.saveScan(result)
                _uiState.value = ScannerUiState.Ready
                _scanCompletedEvents.tryEmit(result.id)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Sample scan failed", e)
                _uiState.value = ScannerUiState.Error(
                    e.localizedMessage ?: "Sample scan failed."
                )
            }
        }
    }

    fun reportError(message: String) {
        _uiState.value = ScannerUiState.Error(message)
    }

    fun dismissError() {
        _uiState.value = ScannerUiState.Ready
    }

    class Factory(
        private val analyzerRepository: SpeciesAnalyzerRepository,
        private val historyRepository: ScanHistoryRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ScannerViewModel(analyzerRepository, historyRepository) as T
        }
    }
}
