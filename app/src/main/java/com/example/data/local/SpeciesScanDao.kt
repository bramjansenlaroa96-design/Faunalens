package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SpeciesScanDao {
    @Query("SELECT * FROM species_scans ORDER BY scannedAtMillis DESC")
    fun getAllScans(): Flow<List<SpeciesScanEntity>>

    @Query("SELECT * FROM species_scans WHERE id = :id LIMIT 1")
    fun getScanById(id: String): Flow<SpeciesScanEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(scan: SpeciesScanEntity)

    @Query("DELETE FROM species_scans WHERE id = :id")
    suspend fun deleteScanById(id: String)
}
