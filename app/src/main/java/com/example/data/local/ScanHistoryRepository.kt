package com.example.data.local

import com.example.domain.model.SpeciesInfo
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ScanHistoryRepository(private val dao: SpeciesScanDao) {
    val allScans: Flow<List<SpeciesInfo>> = dao.getAllScans().map { entities ->
        entities.map { it.toDomain() }
    }

    fun observeScanById(id: String): Flow<SpeciesInfo?> = dao.getScanById(id).map { it?.toDomain() }

    suspend fun saveScan(speciesInfo: SpeciesInfo) {
        dao.insertScan(SpeciesScanEntity.fromDomain(speciesInfo))
    }

    suspend fun deleteScan(id: String, imagePath: String?) {
        dao.deleteScanById(id)
        if (!imagePath.isNullOrBlank()) {
            withContext(Dispatchers.IO) {
                runCatching { File(imagePath).delete() }
            }
        }
    }
}
