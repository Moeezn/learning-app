package com.example.model

enum class CourseTrack(val displayName: String, val description: String) {
    FRONTEND("Frontend Web", "HTML5, Modern CSS, Responsive Design & JavaScript ES6+"),
    BACKEND("Backend & APIs", "Python, Node.js, PHP, Java, Rust & Go"),
    AI_ML("AI, ML & Deep Learning", "Supervised, Unsupervised, Neural Networks & LLMs"),
    DATABASE("Database & MySQL", "Relational Schemas, Queries, Joins & Optimization"),
    SYSTEMS_DEVTOOLS("AI Commands & Git", "CLI Automation, Prompt Engineering & GitHub APIs")
}

data class ProgrammingLanguage(
    val id: String,
    val name: String,
    val track: CourseTrack,
    val description: String,
    val iconTag: String,
    val badgeColorHex: Long,
    val totalLessons: Int,
    val difficulty: String = "Beginner to Pro"
)

data class Lesson(
    val id: String,
    val languageId: String,
    val title: String,
    val summary: String,
    val durationMinutes: Int,
    val markdownContent: String,
    val sampleCode: String,
    val editableCode: String,
    val expectedOutput: String,
    val challengePrompt: String,
    val quizQuestion: String,
    val quizOptions: List<String>,
    val correctQuizIndex: Int,
    val xpReward: Int = 50
)

data class ProjectBlueprint(
    val id: String,
    val title: String,
    val category: String,
    val difficulty: String,
    val techStack: List<String>,
    val description: String,
    val files: Map<String, String>, // filename to code content
    val simulatedOutput: String,
    val simulatedHtmlPreview: String? = null,
    val xpReward: Int = 150
)

data class SqlTable(
    val tableName: String,
    val columns: List<String>,
    val rows: MutableList<MutableMap<String, String>>
)

data class SqlQueryResult(
    val success: Boolean,
    val message: String,
    val columns: List<String> = emptyList(),
    val rows: List<Map<String, String>> = emptyList(),
    val rowsAffected: Int = 0
)

data class GitHubProfile(
    val login: String,
    val name: String?,
    val avatarUrl: String?,
    val bio: String?,
    val publicRepos: Int,
    val followers: Int,
    val following: Int,
    val company: String?,
    val location: String?,
    val topRepos: List<GitHubRepo> = emptyList()
)

data class GitHubRepo(
    val name: String,
    val description: String?,
    val language: String?,
    val stars: Int,
    val forks: Int
)

data class BattleBoss(
    val id: String,
    val name: String,
    val title: String,
    val imageResName: String,
    val maxHp: Int,
    var currentHp: Int,
    val attackPower: Int,
    val weakness: String,
    val questions: List<BattleQuestion>
)

data class BattleQuestion(
    val id: String,
    val question: String,
    val codeSnippet: String,
    val options: List<String>,
    val correctIndex: Int,
    val playerDamage: Int = 35,
    val explanation: String
)

data class BadgeItem(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val unlocked: Boolean,
    val requiredLevel: Int,
    val category: String
)

data class ChatMessage(
    val id: String,
    val sender: String, // "user" or "ai"
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val codeSnippet: String? = null,
    val mode: String = "student" // "student" or "teacher"
)
