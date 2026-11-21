package com.example.data.ai

import com.example.data.local.AtlasCatalog
import com.example.domain.model.SpeciesInfo
import java.util.UUID
import kotlin.math.abs
import kotlinx.coroutines.delay

/**
 * Placeholder implementation used when Firebase AI Logic is not enabled.
 * Returns clearly labeled sample species identifications and Q&A answers
 * so the full camera, gallery, field log, and species dossier UX works cleanly.
 */
class SampleSpeciesAnalyzerRepository : SpeciesAnalyzerRepository {
    override val isLiveAiEnabled: Boolean = false

    private var lastSampleIndex = -1

    override suspend fun identifySpeciesFromImage(
        imagePath: String?,
        imageWidth: Int,
        imageHeight: Int,
        hintLabel: String?
    ): SpeciesInfo {
        // Simulate realistic multimodal inference latency
        delay(900L)

        val catalog = AtlasCatalog.entries
        val matchedFromHint = hintLabel?.let { hint ->
            catalog.firstOrNull {
                it.commonName.contains(hint, ignoreCase = true) ||
                    it.scientificName.contains(hint, ignoreCase = true)
            }
        }

        val chosen = matchedFromHint ?: run {
            val nextIdx = if (!imagePath.isNullOrBlank()) {
                val seed = abs(imagePath.hashCode() + imageWidth * 31 + imageHeight)
                val candidate = seed % catalog.size
                if (candidate == lastSampleIndex) (candidate + 1) % catalog.size else candidate
            } else {
                (lastSampleIndex + 1) % catalog.size
            }
            lastSampleIndex = nextIdx
            catalog[nextIdx]
        }

        return chosen.copy(
            id = "scan_${UUID.randomUUID()}",
            confidencePercent = 91 + (abs(chosen.commonName.hashCode()) % 8),
            isSampleResult = true,
            imagePath = imagePath,
            scannedAtMillis = System.currentTimeMillis()
        )
    }

    override suspend fun askSpeciesQuestion(
        species: SpeciesInfo,
        question: String
    ): String {
        delay(550L)
        val q = question.lowercase()

        val coreAnswer = when {
            "diet" in q || "eat" in q || "food" in q || "prey" in q ->
                "${species.commonName} (${species.scientificName}) feeding profile: ${species.dietAndEcology}"

            "habitat" in q || "live" in q || "where" in q || "range" in q || "find" in q ->
                "Habitat & Range: ${species.habitatAndRange}"

            "danger" in q || "poison" in q || "venom" in q || "safe" in q || "bite" in q || "touch" in q || "human" in q ->
                "Safety & Interaction: ${species.safetyAndInteraction}"

            "tell" in q || "look" in q || "identify" in q || "color" in q || "spot" in q || "male" in q || "female" in q || "apart" in q ->
                "Key Field Marks: ${species.physicalTraits}"

            "breed" in q || "nest" in q || "egg" in q || "young" in q || "hunt" in q || "sleep" in q || "behavior" in q || "navigate" in q || "jump" in q || "swim" in q ->
                "Behavior & Life Cycle: ${species.behaviorAndReproduction}"

            "size" in q || "big" in q || "heavy" in q || "weight" in q || "long" in q || "live" in q ->
                "Dimensions & Longevity: ${species.sizeAndMass}. Typical lifespan: ${species.lifespan}. Activity: ${species.activityPattern}."

            else ->
                buildString {
                    append("${species.commonName} (${species.scientificName}) belongs to family ${species.taxonomy.family} ")
                    append("(${species.taxonomy.order}) and holds an IUCN status of ${species.conservationStatus.label}. ")
                    append(species.summary)
                    if (species.funFacts.isNotEmpty()) {
                        append("\n\nField Note: ${species.funFacts.first()}")
                    }
                }
        }

        return "[Sample Q&A — Live AI requires Firebase]\n$coreAnswer"
    }
}
