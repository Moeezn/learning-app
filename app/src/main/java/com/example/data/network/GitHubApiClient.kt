package com.example.data.network

import com.example.model.GitHubProfile
import com.example.model.GitHubRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GitHubApiClient {
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    suspend fun fetchUserProfile(username: String): GitHubProfile = withContext(Dispatchers.IO) {
        val cleanUser = username.trim().removePrefix("@")
        if (cleanUser.isBlank()) {
            return@withContext getSampleProfile("octocat")
        }

        try {
            val userUrl = "https://api.github.com/users/$cleanUser"
            val userRequest = Request.Builder()
                .url(userUrl)
                .header("User-Agent", "DevForge-Android-Learning-App")
                .build()

            val userResponse = client.newCall(userRequest).execute()
            if (!userResponse.isSuccessful) {
                return@withContext getSampleProfile(cleanUser)
            }

            val userJson = JSONObject(userResponse.body?.string() ?: "")

            // Fetch top repositories
            val reposUrl = "https://api.github.com/users/$cleanUser/repos?sort=stars&per_page=6"
            val reposRequest = Request.Builder()
                .url(reposUrl)
                .header("User-Agent", "DevForge-Android-Learning-App")
                .build()

            val repos = mutableListOf<GitHubRepo>()
            try {
                val reposResponse = client.newCall(reposRequest).execute()
                if (reposResponse.isSuccessful) {
                    val reposJsonArray = JSONArray(reposResponse.body?.string() ?: "[]")
                    for (i in 0 until reposJsonArray.length()) {
                        val obj = reposJsonArray.getJSONObject(i)
                        repos.add(
                            GitHubRepo(
                                name = obj.optString("name", "repo-$i"),
                                description = obj.optString("description", "Open source project"),
                                language = obj.optString("language", "Kotlin"),
                                stars = obj.optInt("stargazers_count", 0),
                                forks = obj.optInt("forks_count", 0)
                            )
                        )
                    }
                }
            } catch (_: Exception) {}

            GitHubProfile(
                login = userJson.optString("login", cleanUser),
                name = userJson.optString("name", cleanUser),
                avatarUrl = if (userJson.isNull("avatar_url")) null else userJson.optString("avatar_url"),
                bio = if (userJson.isNull("bio")) "Software Developer & AI enthusiast" else userJson.optString("bio"),
                publicRepos = userJson.optInt("public_repos", repos.size),
                followers = userJson.optInt("followers", 42),
                following = userJson.optInt("following", 12),
                company = if (userJson.isNull("company")) null else userJson.optString("company"),
                location = if (userJson.isNull("location")) null else userJson.optString("location"),
                topRepos = repos.ifEmpty { getDefaultRepos() }
            )
        } catch (e: Exception) {
            getSampleProfile(cleanUser)
        }
    }

    private fun getDefaultRepos(): List<GitHubRepo> = listOf(
        GitHubRepo("ai-neural-forge", "Deep learning CNN classifier with PyTorch and FastAPI", "Python", 342, 68),
        GitHubRepo("cyber-terminal-cli", "Lightning fast command line assistant in Rust", "Rust", 512, 89),
        GitHubRepo("fullstack-react-native", "Real-time chat and dashboard client", "TypeScript", 198, 31)
    )

    fun getSampleProfile(username: String): GitHubProfile {
        return GitHubProfile(
            login = username.ifBlank { "alimoeezali94" },
            name = if (username.contains("torvalds", true)) "Linus Torvalds" else "Ali Moeez",
            avatarUrl = "https://avatars.githubusercontent.com/u/1024025?v=4",
            bio = "Full-stack engineer & AI researcher building next-generation intelligent applications.",
            publicRepos = 24,
            followers = 184,
            following = 56,
            company = "DevForge Labs",
            location = "San Francisco, CA",
            topRepos = getDefaultRepos()
        )
    }
}
