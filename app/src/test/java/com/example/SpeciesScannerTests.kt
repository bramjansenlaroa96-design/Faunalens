package com.example

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.ai.SampleSpeciesAnalyzerRepository
import com.example.data.local.AtlasCatalog
import com.example.data.local.FaunaDatabase
import com.example.data.local.ScanHistoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class SpeciesScannerTests {

    private lateinit var database: FaunaDatabase
    private lateinit var historyRepository: ScanHistoryRepository
    private lateinit var analyzerRepository: SampleSpeciesAnalyzerRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        database = Room.inMemoryDatabaseBuilder(context, FaunaDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        historyRepository = ScanHistoryRepository(database.speciesScanDao())
        analyzerRepository = SampleSpeciesAnalyzerRepository()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `sample analyzer identifies species and saves to Room field log`() = runTest {
        assertFalse(analyzerRepository.isLiveAiEnabled)

        val identified = analyzerRepository.identifySpeciesFromImage(
            imagePath = null,
            imageWidth = 1080,
            imageHeight = 1440
        )

        assertTrue(identified.isSampleResult)
        assertTrue(identified.commonName.isNotBlank())
        assertTrue(identified.scientificName.isNotBlank())

        historyRepository.saveScan(identified)
        val savedList = historyRepository.allScans.first()
        assertEquals(1, savedList.size)
        assertEquals(identified.id, savedList.first().id)

        val fetched = historyRepository.observeScanById(identified.id).first()
        assertNotNull(fetched)
        assertEquals(identified.scientificName, fetched?.scientificName)
    }

    @Test
    fun `sample analyzer answers follow-up questions with sample notice`() = runTest {
        val sampleSpecies = AtlasCatalog.entries.first()
        val answer = analyzerRepository.askSpeciesQuestion(sampleSpecies, "What does it eat?")

        assertTrue(answer.contains("Sample Q&A"))
        assertTrue(answer.contains("Firebase"))
        assertTrue(answer.contains(sampleSpecies.commonName))
    }
}
