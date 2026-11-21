package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.AnimalClass
import com.example.domain.model.ConservationStatus
import com.example.domain.model.SpeciesInfo
import com.example.domain.model.TaxonomyTree

@Entity(tableName = "species_scans")
data class SpeciesScanEntity(
    @PrimaryKey val id: String,
    val commonName: String,
    val scientificName: String,
    val animalClassName: String,
    val kingdom: String,
    val phylum: String,
    val className: String,
    val orderName: String,
    val family: String,
    val genus: String,
    val species: String,
    val conservationStatusName: String,
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
    val funFactsSerialized: String,
    val suggestedQuestionsSerialized: String,
    val isSampleResult: Boolean,
    val imagePath: String?,
    val scannedAtMillis: Long
) {
    fun toDomain(): SpeciesInfo {
        val animalClass = runCatching { AnimalClass.valueOf(animalClassName) }
            .getOrDefault(AnimalClass.MAMMAL)
        val status = runCatching { ConservationStatus.valueOf(conservationStatusName) }
            .getOrDefault(ConservationStatus.DATA_DEFICIENT)
        return SpeciesInfo(
            id = id,
            commonName = commonName,
            scientificName = scientificName,
            animalClass = animalClass,
            taxonomy = TaxonomyTree(
                kingdom = kingdom,
                phylum = phylum,
                className = className,
                order = orderName,
                family = family,
                genus = genus,
                species = species
            ),
            conservationStatus = status,
            confidencePercent = confidencePercent,
            summary = summary,
            sizeAndMass = sizeAndMass,
            lifespan = lifespan,
            activityPattern = activityPattern,
            physicalTraits = physicalTraits,
            habitatAndRange = habitatAndRange,
            dietAndEcology = dietAndEcology,
            behaviorAndReproduction = behaviorAndReproduction,
            safetyAndInteraction = safetyAndInteraction,
            funFacts = funFactsSerialized.split(LIST_SEPARATOR).filter { it.isNotBlank() },
            suggestedQuestions = suggestedQuestionsSerialized.split(LIST_SEPARATOR).filter { it.isNotBlank() },
            isSampleResult = isSampleResult,
            imagePath = imagePath,
            scannedAtMillis = scannedAtMillis
        )
    }

    companion object {
        private const val LIST_SEPARATOR = "\n|||\n"

        fun fromDomain(info: SpeciesInfo): SpeciesScanEntity = SpeciesScanEntity(
            id = info.id,
            commonName = info.commonName,
            scientificName = info.scientificName,
            animalClassName = info.animalClass.name,
            kingdom = info.taxonomy.kingdom,
            phylum = info.taxonomy.phylum,
            className = info.taxonomy.className,
            orderName = info.taxonomy.order,
            family = info.taxonomy.family,
            genus = info.taxonomy.genus,
            species = info.taxonomy.species,
            conservationStatusName = info.conservationStatus.name,
            confidencePercent = info.confidencePercent,
            summary = info.summary,
            sizeAndMass = info.sizeAndMass,
            lifespan = info.lifespan,
            activityPattern = info.activityPattern,
            physicalTraits = info.physicalTraits,
            habitatAndRange = info.habitatAndRange,
            dietAndEcology = info.dietAndEcology,
            behaviorAndReproduction = info.behaviorAndReproduction,
            safetyAndInteraction = info.safetyAndInteraction,
            funFactsSerialized = info.funFacts.joinToString(LIST_SEPARATOR),
            suggestedQuestionsSerialized = info.suggestedQuestions.joinToString(LIST_SEPARATOR),
            isSampleResult = info.isSampleResult,
            imagePath = info.imagePath,
            scannedAtMillis = info.scannedAtMillis
        )
    }
}
