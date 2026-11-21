package com.example.domain.model

enum class AnimalClass(val displayName: String, val pluralLabel: String) {
    MAMMAL("Mammal", "Mammals"),
    BIRD("Bird", "Birds"),
    REPTILE("Reptile", "Reptiles"),
    AMPHIBIAN("Amphibian", "Amphibians"),
    FISH("Fish", "Fish & Marine"),
    ARTHROPOD("Arthropod", "Insects & Arachnids"),
    MOLLUSK("Mollusk", "Mollusks & Reef")
}

enum class ConservationStatus(val code: String, val label: String) {
    LEAST_CONCERN("LC", "Least Concern"),
    NEAR_THREATENED("NT", "Near Threatened"),
    VULNERABLE("VU", "Vulnerable"),
    ENDANGERED("EN", "Endangered"),
    CRITICALLY_ENDANGERED("CR", "Critically Endangered"),
    DATA_DEFICIENT("DD", "Data Deficient")
}

data class TaxonomyTree(
    val kingdom: String = "Animalia",
    val phylum: String,
    val className: String,
    val order: String,
    val family: String,
    val genus: String,
    val species: String
)

data class SpeciesInfo(
    val id: String,
    val commonName: String,
    val scientificName: String,
    val animalClass: AnimalClass,
    val taxonomy: TaxonomyTree,
    val conservationStatus: ConservationStatus,
    val confidencePercent: Int,
    val summary: String,
    val sizeAndMass: String,
    val lifespan: String,
    val activityPattern: String,
    val physicalTraits: String,
    val habitatAndRange: String,
    val dietAndEcology: String,
    val behaviorAndReproduction: String,
    val safetyAndInteraction: String,
    val funFacts: List<String>,
    val suggestedQuestions: List<String>,
    val isSampleResult: Boolean = true,
    val imagePath: String? = null,
    val scannedAtMillis: Long = System.currentTimeMillis()
)

data class QaMessage(
    val id: String,
    val question: String,
    val answer: String,
    val isSampleAnswer: Boolean = true,
    val timestampMillis: Long = System.currentTimeMillis()
)
