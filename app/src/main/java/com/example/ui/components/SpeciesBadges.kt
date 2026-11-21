package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.FlutterDash
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.Phishing
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.Waves
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.domain.model.AnimalClass
import com.example.domain.model.ConservationStatus
import com.example.ui.theme.StatusCritical
import com.example.ui.theme.StatusDataDeficient
import com.example.ui.theme.StatusEndangered
import com.example.ui.theme.StatusLeastConcern
import com.example.ui.theme.StatusNearThreatened
import com.example.ui.theme.StatusVulnerable

fun AnimalClass.icon(): ImageVector = when (this) {
    AnimalClass.MAMMAL -> Icons.Rounded.Pets
    AnimalClass.BIRD -> Icons.Rounded.FlutterDash
    AnimalClass.REPTILE -> Icons.Rounded.Science
    AnimalClass.AMPHIBIAN -> Icons.Rounded.WaterDrop
    AnimalClass.FISH -> Icons.Rounded.Phishing
    AnimalClass.ARTHROPOD -> Icons.Rounded.BugReport
    AnimalClass.MOLLUSK -> Icons.Rounded.Waves
}

fun ConservationStatus.badgeColor(): Color = when (this) {
    ConservationStatus.LEAST_CONCERN -> StatusLeastConcern
    ConservationStatus.NEAR_THREATENED -> StatusNearThreatened
    ConservationStatus.VULNERABLE -> StatusVulnerable
    ConservationStatus.ENDANGERED -> StatusEndangered
    ConservationStatus.CRITICALLY_ENDANGERED -> StatusCritical
    ConservationStatus.DATA_DEFICIENT -> StatusDataDeficient
}

@Composable
fun ConservationStatusBadge(
    status: ConservationStatus,
    modifier: Modifier = Modifier,
    showFullLabel: Boolean = false
) {
    val color = status.badgeColor()
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        color = color.copy(alpha = 0.14f)
    ) {
        Text(
            text = if (showFullLabel) "${status.code} • ${status.label}" else status.code,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = color,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun AnimalClassBadge(
    animalClass: AnimalClass,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = animalClass.icon(),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = animalClass.displayName,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun SampleModePill(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = stringResource(R.string.sample_mode_badge),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
