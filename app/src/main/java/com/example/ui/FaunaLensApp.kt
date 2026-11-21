package com.example.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.CenterFocusWeak
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.CenterFocusStrong
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.FaunaAppContainer
import com.example.R
import com.example.ui.atlas.AtlasScreen
import com.example.ui.detail.SpeciesDetailScreen
import com.example.ui.detail.SpeciesDetailViewModel
import com.example.ui.log.FieldLogScreen
import com.example.ui.log.FieldLogViewModel
import com.example.ui.scanner.ScannerScreen
import com.example.ui.scanner.ScannerViewModel

object FaunaRoutes {
    const val SCANNER = "scanner"
    const val FIELD_LOG = "field_log"
    const val ATLAS = "atlas"
    const val SPECIES_DETAIL = "species_detail/{source}/{speciesId}"

    fun detailRoute(source: String, speciesId: String): String =
        "species_detail/$source/$speciesId"
}

private enum class TopDestination(
    val route: String,
    val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    SCANNER(
        route = FaunaRoutes.SCANNER,
        labelRes = R.string.nav_scan,
        selectedIcon = Icons.Rounded.CenterFocusStrong,
        unselectedIcon = Icons.Outlined.CenterFocusWeak,
        testTag = "nav_tab_scan"
    ),
    FIELD_LOG(
        route = FaunaRoutes.FIELD_LOG,
        labelRes = R.string.nav_field_log,
        selectedIcon = Icons.Rounded.Book,
        unselectedIcon = Icons.Outlined.Book,
        testTag = "nav_tab_field_log"
    ),
    ATLAS(
        route = FaunaRoutes.ATLAS,
        labelRes = R.string.nav_atlas,
        selectedIcon = Icons.Rounded.Explore,
        unselectedIcon = Icons.Outlined.Explore,
        testTag = "nav_tab_atlas"
    )
}

@Composable
fun FaunaLensApp(container: FaunaAppContainer) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val showTopLevelNav = currentRoute in setOf(
        FaunaRoutes.SCANNER,
        FaunaRoutes.FIELD_LOG,
        FaunaRoutes.ATLAS
    )

    val navigateToTopDestination: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useNavigationRail = maxWidth >= 600.dp

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                if (showTopLevelNav && !useNavigationRail) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        TopDestination.entries.forEach { destination ->
                            val selected = currentRoute == destination.route
                            NavigationBarItem(
                                selected = selected,
                                onClick = { navigateToTopDestination(destination.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                                        contentDescription = stringResource(destination.labelRes)
                                    )
                                },
                                label = {
                                    Text(
                                        text = stringResource(destination.labelRes),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                modifier = Modifier.testTag(destination.testTag)
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (showTopLevelNav && useNavigationRail) {
                    NavigationRail(
                        modifier = Modifier.fillMaxHeight(),
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        TopDestination.entries.forEach { destination ->
                            val selected = currentRoute == destination.route
                            NavigationRailItem(
                                selected = selected,
                                onClick = { navigateToTopDestination(destination.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                                        contentDescription = stringResource(destination.labelRes)
                                    )
                                },
                                label = {
                                    Text(
                                        text = stringResource(destination.labelRes),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                modifier = Modifier.testTag(destination.testTag)
                            )
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = FaunaRoutes.SCANNER,
                    modifier = Modifier.weight(1f)
                ) {
                    composable(FaunaRoutes.SCANNER) {
                        val scannerViewModel: ScannerViewModel = viewModel(
                            factory = ScannerViewModel.Factory(
                                analyzerRepository = container.speciesAnalyzerRepository,
                                historyRepository = container.scanHistoryRepository
                            )
                        )
                        ScannerScreen(
                            viewModel = scannerViewModel,
                            onNavigateToDetail = { scanId ->
                                navController.navigate(FaunaRoutes.detailRoute("scan", scanId))
                            }
                        )
                    }

                    composable(FaunaRoutes.FIELD_LOG) {
                        val fieldLogViewModel: FieldLogViewModel = viewModel(
                            factory = FieldLogViewModel.Factory(
                                historyRepository = container.scanHistoryRepository
                            )
                        )
                        FieldLogScreen(
                            viewModel = fieldLogViewModel,
                            onOpenScanner = { navigateToTopDestination(FaunaRoutes.SCANNER) },
                            onOpenScanDetail = { scanId ->
                                navController.navigate(FaunaRoutes.detailRoute("scan", scanId))
                            }
                        )
                    }

                    composable(FaunaRoutes.ATLAS) {
                        AtlasScreen(
                            isLiveAiEnabled = container.speciesAnalyzerRepository.isLiveAiEnabled,
                            onOpenAtlasSpecies = { speciesId ->
                                navController.navigate(FaunaRoutes.detailRoute("atlas", speciesId))
                            }
                        )
                    }

                    composable(
                        route = FaunaRoutes.SPECIES_DETAIL,
                        arguments = listOf(
                            navArgument("source") { type = NavType.StringType },
                            navArgument("speciesId") { type = NavType.StringType }
                        )
                    ) { entry ->
                        val source = entry.arguments?.getString("source") ?: "scan"
                        val speciesId = entry.arguments?.getString("speciesId") ?: ""
                        val detailViewModel: SpeciesDetailViewModel = viewModel(
                            key = "$source:$speciesId",
                            factory = SpeciesDetailViewModel.Factory(
                                speciesId = speciesId,
                                isAtlasEntry = source == "atlas",
                                historyRepository = container.scanHistoryRepository,
                                analyzerRepository = container.speciesAnalyzerRepository
                            )
                        )
                        SpeciesDetailScreen(
                            viewModel = detailViewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
