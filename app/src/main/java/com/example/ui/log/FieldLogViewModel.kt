package com.example.ui.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.ScanHistoryRepository
import com.example.domain.model.AnimalClass
import com.example.domain.model.SpeciesInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FieldLogUiState(
    val scans: List<SpeciesInfo> = emptyList(),
    val searchQuery: String = "",
    val selectedClass: AnimalClass? = null
)

class FieldLogViewModel(
    private val historyRepository: ScanHistoryRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedClass = MutableStateFlow<AnimalClass?>(null)

    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    val selectedClass: StateFlow<AnimalClass?> = _selectedClass.asStateFlow()

    val uiState: StateFlow<FieldLogUiState> = combine(
        historyRepository.allScans,
        _searchQuery,
        _selectedClass
    ) { allScans, query, selectedClass ->
        val filtered = allScans.filter { item ->
            val matchesClass = selectedClass == null || item.animalClass == selectedClass
            val q = query.trim()
            val matchesQuery = q.isEmpty() ||
                item.commonName.contains(q, ignoreCase = true) ||
                item.scientificName.contains(q, ignoreCase = true) ||
                item.habitatAndRange.contains(q, ignoreCase = true) ||
                item.taxonomy.family.contains(q, ignoreCase = true)
            matchesClass && matchesQuery
        }
        FieldLogUiState(
            scans = filtered,
            searchQuery = query,
            selectedClass = selectedClass
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = FieldLogUiState()
    )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectClassFilter(animalClass: AnimalClass?) {
        _selectedClass.value = animalClass
    }

    fun deleteScan(speciesInfo: SpeciesInfo) {
        viewModelScope.launch {
            historyRepository.deleteScan(speciesInfo.id, speciesInfo.imagePath)
        }
    }

    class Factory(
        private val historyRepository: ScanHistoryRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FieldLogViewModel(historyRepository) as T
        }
    }
}
