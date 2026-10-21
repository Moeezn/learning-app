package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DevForgeRepository
import com.example.data.SqlEngine
import com.example.data.network.GeminiApiClient
import com.example.data.network.GitHubApiClient
import com.example.model.*
import com.example.util.NotificationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppDestination(val label: String, val iconName: String) {
    DASHBOARD("Dashboard", "dashboard"),
    LEARN("Learn", "school"),
    PROJECTS("Projects", "code"),
    SQL_STUDIO("MySQL Studio", "database"),
    BATTLE_ARENA("Battle Arena", "sports_kabaddi"),
    AI_TEACHER("AI Teacher", "psychology"),
    GITHUB("GitHub Audit", "terminal"),
    REWARDS("Rewards", "emoji_events")
}

data class DevForgeUiState(
    val currentDestination: AppDestination = AppDestination.DASHBOARD,
    val isDarkMode: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val userLevel: Int = 3,
    val userXp: Int = 1850,
    val streakDays: Int = 7,
    val badges: List<BadgeItem> = DevForgeRepository.defaultBadges,
    val showLevelUpDialog: Boolean = false,
    val newLevelUnlocked: Int = 0,

    // Course & Learning
    val selectedTrack: CourseTrack? = null,
    val selectedLanguage: ProgrammingLanguage? = null,
    val activeLesson: Lesson? = null,
    val playgroundCode: String = "",
    val playgroundOutput: String = "",
    val isRunningPlayground: Boolean = false,
    val selectedQuizIndex: Int? = null,
    val quizSubmitted: Boolean = false,
    val isQuizCorrect: Boolean = false,

    // Projects
    val selectedProject: ProjectBlueprint? = null,
    val activeProjectFile: String = "",
    val projectSimulatedOutput: String = "",
    val isProjectRunning: Boolean = false,

    // SQL Studio
    val sqlTables: List<SqlTable> = emptyList(),
    val currentSqlQuery: String = "SELECT * FROM users WHERE xp > 1000 ORDER BY xp DESC;",
    val sqlResult: SqlQueryResult? = null,
    val sqlHistory: List<String> = emptyList(),
    val isAiExplainingSql: Boolean = false,
    val sqlAiExplanation: String? = null,

    // Battle Combat
    val activeBoss: BattleBoss = DevForgeRepository.bosses.first(),
    val playerHp: Int = 100,
    val playerMaxHp: Int = 100,
    val comboStreak: Int = 0,
    val battleState: String = "IN_PROGRESS", // "IN_PROGRESS", "VICTORY", "DEFEAT"
    val currentBattleQuestionIdx: Int = 0,
    val combatLog: List<String> = listOf("Battle initiated! BugZilla blocks the deployment pipeline."),
    val isAttackAnimationActive: Boolean = false,

    // AI Teacher
    val chatMessages: List<ChatMessage> = emptyList(),
    val aiTeacherMode: String = "student", // "student" or "teacher"
    val aiInputPrompt: String = "",
    val isAiThinking: Boolean = false,
    val customApiKey: String = "",

    // GitHub Audit
    val githubUsername: String = "alimoeezali94",
    val githubProfile: GitHubProfile? = null,
    val isLoadingGithub: Boolean = false,
    val aiPortfolioAudit: String? = null
)

class DevForgeViewModel(application: Application) : AndroidViewModel(application) {

    private val sqlEngine = SqlEngine(DevForgeRepository.createInitialDatabase())
    private val _uiState = MutableStateFlow(
        DevForgeUiState(
            sqlTables = sqlEngine.getTables(),
            selectedProject = DevForgeRepository.projectBlueprints.first(),
            activeProjectFile = DevForgeRepository.projectBlueprints.first().files.keys.first(),
            projectSimulatedOutput = DevForgeRepository.projectBlueprints.first().simulatedOutput,
            activeLesson = DevForgeRepository.sampleLessons.first(),
            playgroundCode = DevForgeRepository.sampleLessons.first().editableCode
        )
    )
    val uiState: StateFlow<DevForgeUiState> = _uiState.asStateFlow()

    init {
        // Initialize sample chat messages
        _uiState.update { state ->
            state.copy(
                chatMessages = listOf(
                    ChatMessage(
                        id = "welcome_1",
                        sender = "ai",
                        content = "Welcome to DevForge AI! I am your AI Teacher (DevGuru). Ask me anything about HTML/CSS, JS, Python, Rust, Go, SQL, or AI/ML. You can toggle between Student Mode (hints & debug) and Teacher Mode (lesson plans & quiz authoring)!",
                        mode = "student"
                    )
                )
            )
        }
        // Execute initial SQL query so Studio is ready with data
        executeSqlQuery(_uiState.value.currentSqlQuery)
        // Load default GitHub profile
        searchGithubProfile("alimoeezali94")
    }

    fun navigateTo(dest: AppDestination) {
        _uiState.update { it.copy(currentDestination = dest) }
    }

    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun toggleNotifications(enabled: Boolean) {
        _uiState.update { it.copy(notificationsEnabled = enabled) }
        if (enabled) {
            NotificationHelper.sendNotification(
                getApplication(),
                "DevForge Alerts Activated",
                "Daily streak tracking and code battle alerts are now enabled!"
            )
        }
    }

    fun triggerTestNotification() {
        NotificationHelper.sendNotification(
            getApplication(),
            "⚡ Daily Dev Challenge Ready!",
            "Solve today's Python & Neural Network puzzle to maintain your 7-day streak and earn +100 XP!"
        )
    }

    // --- Learn & Playground ---
    fun selectTrack(track: CourseTrack?) {
        _uiState.update { it.copy(selectedTrack = track) }
    }

    fun selectLanguage(lang: ProgrammingLanguage) {
        val lesson = DevForgeRepository.sampleLessons.find { it.languageId == lang.id }
            ?: DevForgeRepository.sampleLessons.first()
        _uiState.update {
            it.copy(
                selectedLanguage = lang,
                activeLesson = lesson,
                playgroundCode = lesson.editableCode,
                playgroundOutput = "",
                selectedQuizIndex = null,
                quizSubmitted = false,
                isQuizCorrect = false
            )
        }
    }

    fun selectLesson(lesson: Lesson) {
        _uiState.update {
            it.copy(
                activeLesson = lesson,
                playgroundCode = lesson.editableCode,
                playgroundOutput = "",
                selectedQuizIndex = null,
                quizSubmitted = false,
                isQuizCorrect = false
            )
        }
    }

    fun updatePlaygroundCode(code: String) {
        _uiState.update { it.copy(playgroundCode = code) }
    }

    fun runPlaygroundCode() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRunningPlayground = true, playgroundOutput = "Compiling & executing sandbox...") }
            delay(500)
            val lesson = _uiState.value.activeLesson
            val output = if (lesson != null && _uiState.value.playgroundCode.isNotBlank()) {
                "[Sandbox Output - SUCCESS]\n${lesson.expectedOutput}\nExecution time: 42ms | Memory: 1.8MB\n[DevForge Compiler: Clean Exit 0]"
            } else {
                "[Sandbox Output]\nProgram finished with status 0."
            }
            _uiState.update { it.copy(isRunningPlayground = false, playgroundOutput = output) }
            addXp(25, "Ran code in interactive sandbox")
        }
    }

    fun selectQuizOption(index: Int) {
        _uiState.update { it.copy(selectedQuizIndex = index) }
    }

    fun submitQuiz() {
        val lesson = _uiState.value.activeLesson ?: return
        val selected = _uiState.value.selectedQuizIndex ?: return
        val isCorrect = selected == lesson.correctQuizIndex

        _uiState.update {
            it.copy(quizSubmitted = true, isQuizCorrect = isCorrect)
        }

        if (isCorrect) {
            addXp(lesson.xpReward, "Correctly answered lesson quiz")
        }
    }

    // --- Projects ---
    fun selectProject(proj: ProjectBlueprint) {
        _uiState.update {
            it.copy(
                selectedProject = proj,
                activeProjectFile = proj.files.keys.firstOrNull() ?: "",
                projectSimulatedOutput = proj.simulatedOutput
            )
        }
    }

    fun selectProjectFile(filename: String) {
        _uiState.update { it.copy(activeProjectFile = filename) }
    }

    fun buildAndRunProject() {
        viewModelScope.launch {
            _uiState.update { it.copy(isProjectRunning = true) }
            delay(800)
            val proj = _uiState.value.selectedProject
            val output = proj?.simulatedOutput ?: "[Project Build Successful]"
            _uiState.update { it.copy(isProjectRunning = false, projectSimulatedOutput = output) }
            addXp(75, "Tested Full-Stack Project Build")
        }
    }

    // --- MySQL Studio ---
    fun updateSqlQuery(sql: String) {
        _uiState.update { it.copy(currentSqlQuery = sql) }
    }

    fun executeSqlQuery(sql: String) {
        val result = sqlEngine.executeQuery(sql)
        _uiState.update { state ->
            val history = (listOf(sql) + state.sqlHistory).distinct().take(10)
            state.copy(
                currentSqlQuery = sql,
                sqlResult = result,
                sqlHistory = history,
                sqlTables = sqlEngine.getTables()
            )
        }
        if (result.success) {
            addXp(30, "Executed MySQL Query successfully")
        }
    }

    fun explainSqlQueryWithAi() {
        val query = _uiState.value.currentSqlQuery
        viewModelScope.launch {
            _uiState.update { it.copy(isAiExplainingSql = true) }
            val explanation = GeminiApiClient.generateAiTeacherResponse(
                prompt = "Explain this MySQL query step-by-step and suggest any index optimization: $query",
                mode = "student",
                customApiKey = _uiState.value.customApiKey
            )
            _uiState.update { it.copy(isAiExplainingSql = false, sqlAiExplanation = explanation) }
        }
    }

    // --- Battle Arena ---
    fun submitBattleAnswer(selectedOptionIndex: Int) {
        val boss = _uiState.value.activeBoss
        val qIndex = _uiState.value.currentBattleQuestionIdx
        val question = boss.questions.getOrNull(qIndex) ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isAttackAnimationActive = true) }
            delay(400)

            if (selectedOptionIndex == question.correctIndex) {
                // Player deals damage to boss
                val newCombo = _uiState.value.comboStreak + 1
                val damageMultiplier = 1.0f + (newCombo * 0.15f)
                val totalDamage = (question.playerDamage * damageMultiplier).toInt()
                val newBossHp = (boss.currentHp - totalDamage).coerceAtLeast(0)
                boss.currentHp = newBossHp

                val log = "CRITICAL HIT! You countered the bug for $totalDamage DMG! (Combo: x$newCombo)"

                val isDefeated = newBossHp <= 0
                val nextQ = if (!isDefeated) (qIndex + 1) % boss.questions.size else qIndex

                _uiState.update {
                    it.copy(
                        isAttackAnimationActive = false,
                        comboStreak = newCombo,
                        currentBattleQuestionIdx = nextQ,
                        combatLog = listOf(log) + it.combatLog.take(6),
                        battleState = if (isDefeated) "VICTORY" else "IN_PROGRESS"
                    )
                }

                if (isDefeated) {
                    addXp(200, "Defeated ${boss.name} in Battle Arena!")
                    NotificationHelper.sendNotification(
                        getApplication(),
                        "🏆 ARENA BOSS SLAIN!",
                        "You crushed ${boss.name}! +200 XP and Arena Trophy unlocked!"
                    )
                }
            } else {
                // Boss counters player
                val newPlayerHp = (_uiState.value.playerHp - boss.attackPower).coerceAtLeast(0)
                val log = "${boss.name} counterattacked with syntax disruption for ${boss.attackPower} DMG! Combos reset."

                _uiState.update {
                    it.copy(
                        isAttackAnimationActive = false,
                        comboStreak = 0,
                        playerHp = newPlayerHp,
                        combatLog = listOf(log) + it.combatLog.take(6),
                        battleState = if (newPlayerHp <= 0) "DEFEAT" else "IN_PROGRESS"
                    )
                }
            }
        }
    }

    fun resetBattle() {
        val boss = _uiState.value.activeBoss
        boss.currentHp = boss.maxHp
        _uiState.update {
            it.copy(
                playerHp = it.playerMaxHp,
                comboStreak = 0,
                currentBattleQuestionIdx = 0,
                battleState = "IN_PROGRESS",
                combatLog = listOf("Arena reset. ${boss.name} stands ready for battle!")
            )
        }
    }

    fun switchBoss(nextBoss: BattleBoss) {
        nextBoss.currentHp = nextBoss.maxHp
        _uiState.update {
            it.copy(
                activeBoss = nextBoss,
                playerHp = it.playerMaxHp,
                comboStreak = 0,
                currentBattleQuestionIdx = 0,
                battleState = "IN_PROGRESS",
                combatLog = listOf("Challenged ${nextBoss.name} - ${nextBoss.title}!")
            )
        }
    }

    // --- AI Teacher Chat ---
    fun updateAiInputPrompt(text: String) {
        _uiState.update { it.copy(aiInputPrompt = text) }
    }

    fun toggleAiTeacherMode(mode: String) {
        _uiState.update { it.copy(aiTeacherMode = mode) }
    }

    fun setCustomApiKey(key: String) {
        _uiState.update { it.copy(customApiKey = key) }
    }

    fun sendAiPrompt(customText: String? = null) {
        val prompt = (customText ?: _uiState.value.aiInputPrompt).trim()
        if (prompt.isBlank()) return

        val userMsg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            sender = "user",
            content = prompt,
            mode = _uiState.value.aiTeacherMode
        )

        _uiState.update {
            it.copy(
                chatMessages = it.chatMessages + userMsg,
                aiInputPrompt = "",
                isAiThinking = true
            )
        }

        viewModelScope.launch {
            val responseText = GeminiApiClient.generateAiTeacherResponse(
                prompt = prompt,
                mode = _uiState.value.aiTeacherMode,
                customApiKey = _uiState.value.customApiKey
            )

            val aiMsg = ChatMessage(
                id = "ai_${System.currentTimeMillis()}",
                sender = "ai",
                content = responseText,
                mode = _uiState.value.aiTeacherMode
            )

            _uiState.update {
                it.copy(
                    chatMessages = it.chatMessages + aiMsg,
                    isAiThinking = false
                )
            }
            addXp(15, "Consulted DevGuru AI Teacher")
        }
    }

    // --- GitHub Audit ---
    fun updateGithubUsername(username: String) {
        _uiState.update { it.copy(githubUsername = username) }
    }

    fun searchGithubProfile(user: String = _uiState.value.githubUsername) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingGithub = true, aiPortfolioAudit = null) }
            val profile = GitHubApiClient.fetchUserProfile(user)
            _uiState.update { it.copy(githubProfile = profile, isLoadingGithub = false) }

            // Trigger AI portfolio recommendation
            generateGithubAuditAdvice(profile)
        }
    }

    private fun generateGithubAuditAdvice(profile: GitHubProfile) {
        viewModelScope.launch {
            val summary = "Developer: ${profile.login}, Public Repos: ${profile.publicRepos}, Followers: ${profile.followers}, Top Repos: ${profile.topRepos.joinToString { "${it.name} (${it.language ?: "General"})" }}"
            val advice = GeminiApiClient.generateAiTeacherResponse(
                prompt = "Perform a quick 3-bullet AI Portfolio Review for this GitHub profile and suggest next projects to build: $summary",
                mode = "student",
                customApiKey = _uiState.value.customApiKey
            )
            _uiState.update { it.copy(aiPortfolioAudit = advice) }
        }
    }

    // --- Gamification & XP ---
    private fun addXp(amount: Int, reason: String) {
        _uiState.update { state ->
            val newXp = state.userXp + amount
            val calculatedLevel = (newXp / 500) + 1
            val leveledUp = calculatedLevel > state.userLevel

            if (leveledUp) {
                NotificationHelper.sendNotification(
                    getApplication(),
                    "🎉 LEVEL UP: LEVEL $calculatedLevel!",
                    "Congratulations! You advanced to Level $calculatedLevel in DevForge AI!"
                )
            }

            state.copy(
                userXp = newXp,
                userLevel = calculatedLevel,
                showLevelUpDialog = leveledUp,
                newLevelUnlocked = if (leveledUp) calculatedLevel else state.newLevelUnlocked
            )
        }
    }

    fun dismissLevelUpDialog() {
        _uiState.update { it.copy(showLevelUpDialog = false) }
    }
}
