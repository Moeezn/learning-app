package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BadgeItem
import com.example.ui.DevForgeUiState
import com.example.ui.DevForgeViewModel

@Composable
fun RewardsScreen(
    uiState: DevForgeUiState,
    viewModel: DevForgeViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("rewards_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title & Level Summary
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Gamified Rewards & Level Roadmap",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = "Complete lessons, build real-time projects, and slay arena bosses to unlock exclusive developer badges.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Current Rank Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f))
                            .border(2.dp, MaterialTheme.colorScheme.tertiary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Lv.${uiState.userLevel}",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = getLevelTitle(uiState.userLevel),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Total Experience: ${uiState.userXp} XP",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        val nextXp = uiState.userLevel * 500
                        val remaining = (nextXp - uiState.userXp).coerceAtLeast(0)
                        Text(
                            text = "$remaining XP remaining to Level ${uiState.userLevel + 1}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Badges & Achievements Header
        item {
            Text(
                text = "🏆 Developer Trophy Cabinet",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        // Badges Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                uiState.badges.chunked(2).forEach { rowBadges ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowBadges.forEach { badge ->
                            val isUnlocked = badge.unlocked || uiState.userLevel >= badge.requiredLevel
                            BadgeCard(
                                badge = badge,
                                isUnlocked = isUnlocked,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowBadges.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // Career Certifications Checklist
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "🎖️ Industry Skill Milestones",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    MilestoneRow("Full-Stack Web Foundations (HTML/CSS/JS)", true)
                    MilestoneRow("Backend Microservices with Python FastAPI", true)
                    MilestoneRow("Relational Schema Mastery & MySQL Joins", true)
                    MilestoneRow("Supervised & Unsupervised Machine Learning", uiState.userLevel >= 4)
                    MilestoneRow("Deep Learning Convolutions & PyTorch", uiState.userLevel >= 5)
                    MilestoneRow("Generative AI & LLM Vector RAG Architectures", uiState.userLevel >= 6)
                    MilestoneRow("Rust Memory Safety & High-Concurrency Systems", uiState.userLevel >= 8)
                }
            }
        }
    }
}

@Composable
fun BadgeCard(
    badge: BadgeItem,
    isUnlocked: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        modifier = modifier.border(
            1.dp,
            if (isUnlocked) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f) else Color.Transparent,
            RoundedCornerShape(12.dp)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            if (isUnlocked) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            else Color(0xFF334155)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isUnlocked) Icons.Default.MilitaryTech else Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (isUnlocked) MaterialTheme.colorScheme.primary else Color(0xFF64748B),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isUnlocked) Color(0x2200E676) else Color(0x2264748B)
                ) {
                    Text(
                        text = if (isUnlocked) "UNLOCKED" else "LV.${badge.requiredLevel}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isUnlocked) Color(0xFF00E676) else Color(0xFF94A3B8),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = badge.title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = badge.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp,
                maxLines = 2
            )
        }
    }
}

@Composable
fun MilestoneRow(title: String, achieved: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = if (achieved) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (achieved) Color(0xFF00E676) else Color(0xFF64748B),
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (achieved) FontWeight.SemiBold else FontWeight.Normal,
            color = if (achieved) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

fun getLevelTitle(level: Int): String {
    return when {
        level <= 1 -> "Script Initiate"
        level == 2 -> "Syntax Apprentice"
        level == 3 -> "Junior Code Artisan"
        level == 4 -> "Full-Stack Practitioner"
        level == 5 -> "API & Systems Architect"
        level == 6 -> "Neural Network Engineer"
        level == 7 -> "Deep Learning Specialist"
        level == 8 -> "LLM & GenAI Virtuoso"
        level == 9 -> "High-Performance Concurrency Master"
        else -> "Grand Architect of AI"
    }
}
