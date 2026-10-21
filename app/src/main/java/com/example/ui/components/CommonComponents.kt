package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppDestination
import com.example.ui.DevForgeUiState
import com.example.ui.DevForgeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevForgeTopBar(
    uiState: DevForgeUiState,
    onToggleDark: () -> Unit,
    onOpenSettings: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Terminal,
                        contentDescription = "Logo",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "DevForge AI",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        actions = {
            // Streak Pill
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                modifier = Modifier
                    .padding(end = 6.dp)
                    .border(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Streak",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${uiState.streakDays}d",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }

            // Level & XP Pill
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                modifier = Modifier
                    .padding(end = 6.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "XP",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Lv.${uiState.userLevel} | ${uiState.userXp} XP",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Dark Mode Toggle
            IconButton(
                onClick = onToggleDark,
                modifier = Modifier.testTag("dark_mode_toggle")
            ) {
                Icon(
                    imageVector = if (uiState.isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Toggle Theme",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            // Settings Button
            IconButton(
                onClick = onOpenSettings,
                modifier = Modifier.testTag("settings_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun DevForgeBottomNav(
    currentDest: AppDestination,
    onNavigate: (AppDestination) -> Unit
) {
    val items = listOf(
        AppDestination.DASHBOARD,
        AppDestination.LEARN,
        AppDestination.PROJECTS,
        AppDestination.SQL_STUDIO,
        AppDestination.BATTLE_ARENA,
        AppDestination.AI_TEACHER,
        AppDestination.GITHUB,
        AppDestination.REWARDS
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        items.forEach { dest ->
            val selected = currentDest == dest
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(dest) },
                icon = {
                    Icon(
                        imageVector = getDestinationIcon(dest),
                        contentDescription = dest.label
                    )
                },
                label = {
                    Text(
                        text = dest.label,
                        maxLines = 1,
                        fontSize = 10.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                modifier = Modifier.testTag("nav_item_${dest.name.lowercase()}")
            )
        }
    }
}

@Composable
fun DevForgeNavigationRail(
    currentDest: AppDestination,
    onNavigate: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        AppDestination.DASHBOARD,
        AppDestination.LEARN,
        AppDestination.PROJECTS,
        AppDestination.SQL_STUDIO,
        AppDestination.BATTLE_ARENA,
        AppDestination.AI_TEACHER,
        AppDestination.GITHUB,
        AppDestination.REWARDS
    )

    NavigationRail(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        items.forEach { dest ->
            val selected = currentDest == dest
            NavigationRailItem(
                selected = selected,
                onClick = { onNavigate(dest) },
                icon = {
                    Icon(
                        imageVector = getDestinationIcon(dest),
                        contentDescription = dest.label
                    )
                },
                label = {
                    Text(
                        text = dest.label,
                        maxLines = 1,
                        fontSize = 11.sp
                    )
                },
                modifier = Modifier.testTag("rail_item_${dest.name.lowercase()}")
            )
        }
    }
}

fun getDestinationIcon(dest: AppDestination): ImageVector {
    return when (dest) {
        AppDestination.DASHBOARD -> Icons.Default.Dashboard
        AppDestination.LEARN -> Icons.Default.School
        AppDestination.PROJECTS -> Icons.Default.IntegrationInstructions
        AppDestination.SQL_STUDIO -> Icons.Default.Storage
        AppDestination.BATTLE_ARENA -> Icons.Default.SportsKabaddi
        AppDestination.AI_TEACHER -> Icons.Default.Psychology
        AppDestination.GITHUB -> Icons.Default.Terminal
        AppDestination.REWARDS -> Icons.Default.EmojiEvents
    }
}

@Composable
fun SettingsDialog(
    uiState: DevForgeUiState,
    viewModel: DevForgeViewModel,
    onDismiss: () -> Unit
) {
    var apiKeyText by remember { mutableStateOf(uiState.customApiKey) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("DevForge AI Settings", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Theme Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Dark Mode", fontWeight = FontWeight.SemiBold)
                        Text("Cyberpunk coder theme", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(
                        checked = uiState.isDarkMode,
                        onCheckedChange = { viewModel.toggleDarkMode() },
                        modifier = Modifier.testTag("settings_theme_switch")
                    )
                }

                HorizontalDivider()

                // Push Notifications
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Push Notifications", fontWeight = FontWeight.SemiBold)
                        Text("Streak alerts & challenge updates", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(
                        checked = uiState.notificationsEnabled,
                        onCheckedChange = { viewModel.toggleNotifications(it) },
                        modifier = Modifier.testTag("settings_notifications_switch")
                    )
                }

                Button(
                    onClick = { viewModel.triggerTestNotification() },
                    modifier = Modifier.fillMaxWidth().testTag("trigger_notification_button")
                ) {
                    Icon(Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Test Push Notification")
                }

                HorizontalDivider()

                // Custom Gemini / OpenAI API Key
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Gemini / AI API Key (Optional)", fontWeight = FontWeight.SemiBold)
                    Text(
                        "AI Studio automatically injects GEMINI_API_KEY from Secrets. You can also specify an override key here:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        value = apiKeyText,
                        onValueChange = {
                            apiKeyText = it
                            viewModel.setCustomApiKey(it)
                        },
                        placeholder = { Text("Paste custom Gemini or ChatGPT key") },
                        modifier = Modifier.fillMaxWidth().testTag("custom_api_key_input"),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.testTag("settings_close_button")
            ) {
                Text("Done")
            }
        }
    )
}

@Composable
fun LevelUpCelebrationDialog(
    level: Int,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("LEVEL UP UNLOCKED!", fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.tertiary)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Lv.$level",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = "Congratulations, Developer!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Text(
                    text = "You reached Level $level! New advanced AI/ML project blueprints and battle arena challenges have been unlocked for your developer journey.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.testTag("level_up_claim_button")
            ) {
                Text("Claim Rewards & Continue")
            }
        }
    )
}
