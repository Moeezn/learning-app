package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.ui.AppDestination
import com.example.ui.DevForgeViewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: DevForgeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            var showSettings by remember { mutableStateOf(false) }

            // Notification permission request for Android 13+
            val notificationPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { granted ->
                    viewModel.toggleNotifications(granted)
                }
            )

            LaunchedEffect(Unit) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val hasPermission = ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                    if (!hasPermission) {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }

            // BackHandler: Navigate back to Dashboard if in any sub-screen
            BackHandler(enabled = uiState.currentDestination != AppDestination.DASHBOARD) {
                viewModel.navigateTo(AppDestination.DASHBOARD)
            }

            MyApplicationTheme(darkTheme = uiState.isDarkMode) {
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val isWideScreen = maxWidth > 600.dp

                    Scaffold(
                        topBar = {
                            DevForgeTopBar(
                                uiState = uiState,
                                onToggleDark = { viewModel.toggleDarkMode() },
                                onOpenSettings = { showSettings = true }
                            )
                        },
                        bottomBar = {
                            if (!isWideScreen) {
                                DevForgeBottomNav(
                                    currentDest = uiState.currentDestination,
                                    onNavigate = { viewModel.navigateTo(it) }
                                )
                            }
                        }
                    ) { innerPadding ->
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            if (isWideScreen) {
                                DevForgeNavigationRail(
                                    currentDest = uiState.currentDestination,
                                    onNavigate = { viewModel.navigateTo(it) }
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            ) {
                                when (uiState.currentDestination) {
                                    AppDestination.DASHBOARD -> DashboardScreen(uiState, viewModel)
                                    AppDestination.LEARN -> LearnScreen(uiState, viewModel)
                                    AppDestination.PROJECTS -> ProjectsScreen(uiState, viewModel)
                                    AppDestination.SQL_STUDIO -> SqlStudioScreen(uiState, viewModel)
                                    AppDestination.BATTLE_ARENA -> BattleArenaScreen(uiState, viewModel)
                                    AppDestination.AI_TEACHER -> AiTeacherScreen(uiState, viewModel)
                                    AppDestination.GITHUB -> GitHubAnalyzerScreen(uiState, viewModel)
                                    AppDestination.REWARDS -> RewardsScreen(uiState, viewModel)
                                }
                            }
                        }
                    }
                }

                // Settings Dialog
                if (showSettings) {
                    SettingsDialog(
                        uiState = uiState,
                        viewModel = viewModel,
                        onDismiss = { showSettings = false }
                    )
                }

                // Level-Up Celebration Dialog
                if (uiState.showLevelUpDialog) {
                    LevelUpCelebrationDialog(
                        level = uiState.newLevelUnlocked,
                        onDismiss = { viewModel.dismissLevelUpDialog() }
                    )
                }
            }
        }
    }
}
