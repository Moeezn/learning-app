package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DevForgeRepository
import com.example.model.CourseTrack
import com.example.model.ProgrammingLanguage
import com.example.ui.DevForgeUiState
import com.example.ui.DevForgeViewModel

@Composable
fun LearnScreen(
    uiState: DevForgeUiState,
    viewModel: DevForgeViewModel,
    modifier: Modifier = Modifier
) {
    val languages = DevForgeRepository.languages.filter {
        uiState.selectedTrack == null || it.track == uiState.selectedTrack
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("learn_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Track Filter Chips
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "📚 Learning Curriculum Tracks",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = uiState.selectedTrack == null,
                            onClick = { viewModel.selectTrack(null) },
                            label = { Text("All Tracks (${DevForgeRepository.languages.size})") },
                            modifier = Modifier.testTag("filter_all_tracks")
                        )
                    }
                    items(CourseTrack.values()) { track ->
                        FilterChip(
                            selected = uiState.selectedTrack == track,
                            onClick = { viewModel.selectTrack(track) },
                            label = { Text(track.displayName) },
                            modifier = Modifier.testTag("filter_${track.name.lowercase()}")
                        )
                    }
                }
            }
        }

        // Horizontal Language Carousel
        item {
            Text(
                text = "Technologies & Frameworks",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(languages) { lang ->
                    val isSelected = uiState.selectedLanguage?.id == lang.id
                    LanguageCard(
                        language = lang,
                        isSelected = isSelected,
                        onClick = { viewModel.selectLanguage(lang) }
                    )
                }
            }
        }

        // Active Lesson Card & Sandbox
        item {
            val lesson = uiState.activeLesson
            if (lesson != null) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Lesson Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "${lesson.durationMinutes} min • +${lesson.xpReward} XP",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Text(
                                text = "Interactive Lesson",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = lesson.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Text(
                            text = lesson.summary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Lesson Theory / Markdown
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "📖 Core Concept Theory",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = lesson.markdownContent,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Reference Sample Code
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Code Example",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF0D1117),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color(0xFF30363D), RoundedCornerShape(8.dp))
                            ) {
                                Text(
                                    text = lesson.sampleCode,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    color = Color(0xFF7EE787),
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }

                        // Interactive Sandbox / Playground
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Interactive Sandbox Editor",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                                Text(
                                    text = lesson.challengePrompt,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontSize = 11.sp
                                )
                            }

                            OutlinedTextField(
                                value = uiState.playgroundCode,
                                onValueChange = { viewModel.updatePlaygroundCode(it) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .testTag("sandbox_code_input"),
                                textStyle = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp
                                ),
                                placeholder = { Text("Write runnable code here...") }
                            )

                            Button(
                                onClick = { viewModel.runPlaygroundCode() },
                                enabled = !uiState.isRunningPlayground,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("run_sandbox_button")
                            ) {
                                if (uiState.isRunningPlayground) {
                                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Executing Code...")
                                } else {
                                    Icon(Icons.Default.PlayCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Run Code in Sandbox")
                                }
                            }

                            // Sandbox Output Console
                            if (uiState.playgroundOutput.isNotBlank()) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFF090D14),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "TERMINAL CONSOLE",
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = uiState.playgroundOutput,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 11.sp,
                                            color = Color(0xFF58A6FF)
                                        )
                                    }
                                }
                            }
                        }

                        HorizontalDivider()

                        // Lesson Knowledge Quiz
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "🎯 Quick Knowledge Check",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = lesson.quizQuestion,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            lesson.quizOptions.forEachIndexed { index, option ->
                                val isSelected = uiState.selectedQuizIndex == index
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                    else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.selectQuizOption(index) }
                                        .border(
                                            1.dp,
                                            if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                            RoundedCornerShape(8.dp)
                                        )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = { viewModel.selectQuizOption(index) }
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = option, style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }

                            Button(
                                onClick = { viewModel.submitQuiz() },
                                enabled = uiState.selectedQuizIndex != null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("submit_quiz_button")
                            ) {
                                Text("Check Answer & Claim XP")
                            }

                            if (uiState.quizSubmitted) {
                                val correct = uiState.isQuizCorrect
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (correct) Color(0x2200E676) else Color(0x22FF5252),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = if (correct) "✅ Excellent! That is the correct answer. +${lesson.xpReward} XP awarded!"
                                        else "❌ Not quite. Review the concept and try again!",
                                        color = if (correct) Color(0xFF00E676) else Color(0xFFFF5252),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LanguageCard(
    language: ProgrammingLanguage,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .width(180.dp)
            .clickable { onClick() }
            .border(
                1.dp,
                if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                RoundedCornerShape(12.dp)
            )
            .testTag("lang_card_${language.id}")
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(language.badgeColorHex).copy(alpha = 0.2f)
                ) {
                    Text(
                        text = language.iconTag,
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        color = Color(language.badgeColorHex),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Text(
                    text = "${language.totalLessons} lessons",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = language.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1
            )

            Text(
                text = language.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                fontSize = 11.sp
            )
        }
    }
}
