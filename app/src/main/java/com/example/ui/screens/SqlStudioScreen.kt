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
import com.example.ui.DevForgeUiState
import com.example.ui.DevForgeViewModel

@Composable
fun SqlStudioScreen(
    uiState: DevForgeUiState,
    viewModel: DevForgeViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("sql_studio_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title & Description
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Storage,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "MySQL Interactive Database Studio",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = "Execute live relational queries, inspect database schemas, join foreign keys, and optimize with AI.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Schema / Table Browser Chips
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Database Tables (devforge_db)",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(uiState.sqlTables) { table ->
                        AssistChip(
                            onClick = { viewModel.executeSqlQuery("SELECT * FROM ${table.tableName};") },
                            leadingIcon = {
                                Icon(Icons.Default.TableChart, contentDescription = null, modifier = Modifier.size(16.dp))
                            },
                            label = {
                                Text("${table.tableName} (${table.rows.size})", fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                            },
                            modifier = Modifier.testTag("sql_table_${table.tableName}")
                        )
                    }
                    item {
                        AssistChip(
                            onClick = { viewModel.executeSqlQuery("SHOW TABLES;") },
                            label = { Text("SHOW TABLES", fontFamily = FontFamily.Monospace, fontSize = 12.sp) }
                        )
                    }
                }
            }
        }

        // Preset Query Templates
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Quick SQL Query Presets",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                val presets = listOf(
                    "High XP Users" to "SELECT * FROM users WHERE xp > 1000 ORDER BY xp DESC;",
                    "Submissions JOIN" to "SELECT u.username, s.score, c.title FROM users u INNER JOIN submissions s ON u.id = s.user_id INNER JOIN courses c ON s.course_id = c.id;",
                    "Insert Student" to "INSERT INTO users (id, username, email, xp, level, role) VALUES ('5', 'alex_ai', 'alex@ai.io', '1900', '4', 'student');",
                    "Update XP" to "UPDATE users SET xp = '3500' WHERE username = 'alimoeez';"
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(presets) { (label, sql) ->
                        SuggestionChip(
                            onClick = { viewModel.updateSqlQuery(sql) },
                            label = { Text(label, fontSize = 11.sp) },
                            modifier = Modifier.testTag("sql_preset_$label")
                        )
                    }
                }
            }
        }

        // Query Input Editor
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "SQL Command Terminal",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedTextField(
                        value = uiState.currentSqlQuery,
                        onValueChange = { viewModel.updateSqlQuery(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .testTag("sql_query_input"),
                        textStyle = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp
                        ),
                        placeholder = { Text("SELECT * FROM users...") }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.executeSqlQuery(uiState.currentSqlQuery) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("execute_sql_button")
                        ) {
                            Icon(Icons.Default.FlashOn, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Execute Query")
                        }

                        OutlinedButton(
                            onClick = { viewModel.explainSqlQueryWithAi() },
                            enabled = !uiState.isAiExplainingSql,
                            modifier = Modifier.testTag("explain_sql_button")
                        ) {
                            if (uiState.isAiExplainingSql) {
                                CircularProgressIndicator(modifier = Modifier.size(14.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("AI Explain")
                            }
                        }
                    }
                }
            }
        }

        // AI Query Explanation Card (if triggered)
        item {
            val explanation = uiState.sqlAiExplanation
            if (!explanation.isNullOrBlank()) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "DevGuru AI Query Optimization",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        Text(
                            text = explanation,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Tabular Query Result View
        item {
            val result = uiState.sqlResult
            if (result != null) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF090D14)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF2B3A55), RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = result.message,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (result.success) Color(0xFF00E676) else Color(0xFFFF5252)
                            )
                            if (result.success) {
                                Text(
                                    text = "${result.rows.size} rows",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = Color(0xFF8B949E)
                                )
                            }
                        }

                        if (result.success && result.columns.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = Color(0xFF21262D))
                            Spacer(modifier = Modifier.height(8.dp))

                            // Scrollable Table
                            val scrollState = rememberScrollState()
                            Column(modifier = Modifier.horizontalScroll(scrollState)) {
                                // Table Header
                                Row(
                                    modifier = Modifier
                                        .background(Color(0xFF161B22), RoundedCornerShape(4.dp))
                                        .padding(vertical = 6.dp)
                                ) {
                                    result.columns.forEach { col ->
                                        Text(
                                            text = col,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier
                                                .width(130.dp)
                                                .padding(horizontal = 8.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                // Table Rows
                                result.rows.forEachIndexed { rowIndex, row ->
                                    val bg = if (rowIndex % 2 == 0) Color.Transparent else Color(0xFF0D1117)
                                    Row(
                                        modifier = Modifier
                                            .background(bg)
                                            .padding(vertical = 6.dp)
                                    ) {
                                        result.columns.forEach { col ->
                                            Text(
                                                text = row[col] ?: "NULL",
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 11.sp,
                                                color = Color(0xFFC9D1D9),
                                                modifier = Modifier
                                                    .width(130.dp)
                                                    .padding(horizontal = 8.dp)
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
    }
}
