package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = ForestEmerald,
    onPrimary = Color.White,
    primaryContainer = SageContainer,
    onPrimaryContainer = OnSageContainer,
    secondary = WarmOchre,
    onSecondary = Color.White,
    secondaryContainer = SunlitContainer,
    onSecondaryContainer = OnSunlitContainer,
    tertiary = BotanicalTeal,
    onTertiary = Color.White,
    tertiaryContainer = MistTealContainer,
    onTertiaryContainer = OnMistTealContainer,
    background = ParchmentBackground,
    onBackground = CharcoalInk,
    surface = ParchmentSurface,
    onSurface = CharcoalInk,
    surfaceVariant = ParchmentSurfaceVariant,
    onSurfaceVariant = MutedMossInk,
    outline = ParchmentOutline
)

private val DarkColorScheme = darkColorScheme(
    primary = BioluminescentMint,
    onPrimary = DeepCanopyPrimary,
    primaryContainer = ForestMoss,
    onPrimaryContainer = MistWhiteInk,
    secondary = DarkOchre,
    onSecondary = OnSunlitContainer,
    secondaryContainer = DarkSunlitContainer,
    onSecondaryContainer = SunlitContainer,
    tertiary = BioluminescentMint,
    onTertiary = DeepCanopyPrimary,
    background = NightCanopyBackground,
    onBackground = MistWhiteInk,
    surface = NightCanopySurface,
    onSurface = MistWhiteInk,
    surfaceVariant = NightCanopySurfaceVariant,
    onSurfaceVariant = SoftSageInk,
    outline = NightOutline
)

val FaunaLensShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun FaunaLensTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FaunaLensTypography,
        shapes = FaunaLensShapes,
        content = content
    )
}
