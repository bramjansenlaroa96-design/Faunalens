package com.example.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.ai.SpeciesAnalyzerRepository
import com.example.data.local.AtlasCatalog
import com.example.data.local.ScanHistoryRepository
import com.example.domain.model.QaMessage
import com.example.domain.model.SpeciesInfo
import java.util.UUID
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "SpeciesDetailViewModel"

sealed interface SpeciesDetailUiState {
    data object Loading : SpeciesDetailUiState
    data class Ready(
        val species: SpeciesInfo,
        val qaMessages: List<QaMessage> = emptyList(),
        val isAsking: Boolean = false,
        val qaError: String? = null
    ) : SpeciesDetailUiState
    data object NotFound : SpeciesDetailUiState
}

class SpeciesDetailViewModel(
    private val speciesId: String,
    private val isAtlasEntry: Boolean,
    private val historyRepository: ScanHistoryRepository,
    private val analyzerRepository: SpeciesAnalyzerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SpeciesDetailUiState>(SpeciesDetailUiState.Loading)
    val uiState: StateFlow<SpeciesDetailUiState> = _uiState.asStateFlow()

    val isLiveAiEnabled: Boolean
        get() = analyzerRepository.isLiveAiEnabled

    init {
        loadSpecies()
    }

    private fun loadSpecies() {
        if (isAtlasEntry) {
            val found = AtlasCatalog.entries.firstOrNull { it.id == speciesId }
            _uiState.value = if (found != null) {
                SpeciesDetailUiState.Ready(species = found)
            } else {
                SpeciesDetailUiState.NotFound
            }
            return
        }

        viewModelScope.launch {
            historyRepository.observeScanById(speciesId).collect { scan ->
                val current = _uiState.value
                _uiState.value = if (scan != null) {
                    if (current is SpeciesDetailUiState.Ready) {
                        current.copy(species = scan)
                    } else {
                        SpeciesDetailUiState.Ready(species = scan)
                    }
                } else {
                    SpeciesDetailUiState.NotFound
                }
            }
        }
    }

    fun askQuestion(question: String) {
        val trimmed = question.trim()
        if (trimmed.isEmpty()) return

        val current = _uiState.value as? SpeciesDetailUiState.Ready ?: return
        if (current.isAsking) return

        viewModelScope.launch {
            _uiState.value = current.copy(isAsking = true, qaError = null)
            try {
                val answer = analyzerRepository.askSpeciesQuestion(current.species, trimmed)
                val newMsg = QaMessage(
                    id = UUID.randomUUID().toString(),
                    question = trimmed,
                    answer = answer,
                    isSampleAnswer = !analyzerRepository.isLiveAiEnabled
                )
                val latest = _uiState.value as? SpeciesDetailUiState.Ready ?: return@launch
                _uiState.value = latest.copy(
                    qaMessages = latest.qaMessages + newMsg,
                    isAsking = false
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Species Q&A failed", e)
                val latest = _uiState.value as? SpeciesDetailUiState.Ready ?: return@launch
                _uiState.value = latest.copy(
                    isAsking = false,
                    qaError = e.localizedMessage ?: "Unable to answer question."
                )
            }
        }
    }

    class Factory(
        private val speciesId: String,
        private val isAtlasEntry: Boolean,
        private val historyRepository: ScanHistoryRepository,
        private val analyzerRepository: SpeciesAnalyzerRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SpeciesDetailViewModel(
                speciesId = speciesId,
                isAtlasEntry = isAtlasEntry,
                historyRepository = historyRepository,
                analyzerRepository = analyzerRepository
            ) as T
        }
    }
}
