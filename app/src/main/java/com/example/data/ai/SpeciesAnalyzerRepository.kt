package com.example.data.ai

import com.example.domain.model.SpeciesInfo

/**
 * Single AI boundary for species identification from camera/gallery images
 * and follow-up natural-language species Q&A.
 */
interface SpeciesAnalyzerRepository {
    val isLiveAiEnabled: Boolean

    suspend fun identifySpeciesFromImage(
        imagePath: String?,
        imageWidth: Int,
        imageHeight: Int,
        hintLabel: String? = null
    ): SpeciesInfo

    suspend fun askSpeciesQuestion(
        species: SpeciesInfo,
        question: String
    ): String
}
