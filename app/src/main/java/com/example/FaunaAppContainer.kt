package com.example

import android.content.Context
import com.example.data.ai.SampleSpeciesAnalyzerRepository
import com.example.data.ai.SpeciesAnalyzerRepository
import com.example.data.local.FaunaDatabase
import com.example.data.local.ScanHistoryRepository

class FaunaAppContainer(context: Context) {
    private val database: FaunaDatabase by lazy {
        FaunaDatabase.getInstance(context)
    }

    val scanHistoryRepository: ScanHistoryRepository by lazy {
        ScanHistoryRepository(database.speciesScanDao())
    }

    val speciesAnalyzerRepository: SpeciesAnalyzerRepository by lazy {
        SampleSpeciesAnalyzerRepository()
    }
}
