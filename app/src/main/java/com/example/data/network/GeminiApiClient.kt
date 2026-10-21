package com.example.data.network

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiApiClient {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

    suspend fun generateAiTeacherResponse(
        prompt: String,
        mode: String = "student",
        customApiKey: String? = null
    ): String = withContext(Dispatchers.IO) {
        val activeKey = when {
            !customApiKey.isNullOrBlank() -> customApiKey.trim()
            BuildConfig.GEMINI_API_KEY.isNotBlank() && BuildConfig.GEMINI_API_KEY != "MY_GEMINI_API_KEY" -> BuildConfig.GEMINI_API_KEY
            else -> null
        }

        if (activeKey == null) {
            // Intelligent local built-in fallback response
            return@withContext getOfflineTutorResponse(prompt, mode)
        }

        try {
            val systemPersona = if (mode.equals("teacher", ignoreCase = true)) {
                "You are DevGuru AI in TEACHER MODE. You help educators and instructors create lesson plans, generate quizzes, design curriculum blueprints, and write grading rubrics for programming (HTML, CSS, JS, Python, PHP, Java, Rust, Go, R, MySQL, AI/ML, LLMs). Be structured, concise, and pedagogical."
            } else {
                "You are DevGuru AI in STUDENT MODE. You are an expert, encouraging coding teacher and mentor. You explain code step-by-step, spot bugs, provide copyable code snippets, teach AI/ML concepts, SQL queries, and terminal commands. Keep answers clear, modern, and engaging with markdown formatting."
            }

            val requestJson = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", "$systemPersona\n\nUser Question:\n$prompt"))
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.7)
                    put("maxOutputTokens", 1200)
                })
            }

            val body = requestJson.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL?key=$activeKey")
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                return@withContext "AI Teacher note: API responded with status ${response.code}. Switching to local knowledge engine:\n\n${getOfflineTutorResponse(prompt, mode)}"
            }

            val jsonResponse = JSONObject(responseBody)
            val candidates = jsonResponse.optJSONArray("candidates")
            val candidate = candidates?.optJSONObject(0)
            val content = candidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val text = parts?.optJSONObject(0)?.optString("text")

            text ?: "AI returned an empty response. Let me help you with: ${getOfflineTutorResponse(prompt, mode)}"
        } catch (e: Exception) {
            "DevGuru AI Connection note (${e.message ?: "network issue"}). Using local knowledge engine:\n\n${getOfflineTutorResponse(prompt, mode)}"
        }
    }

    private fun getOfflineTutorResponse(prompt: String, mode: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("transformer") || lower.contains("attention") -> """
                ### 🧠 Self-Attention Mechanism in Transformers
                Self-Attention calculates relationships between all tokens simultaneously:
                1. **Query (Q), Key (K), Value (V)** vectors are computed via linear projections.
                2. **Attention Weights**: $ Attention(Q, K, V) = \text{softmax}\left(\frac{Q K^T}{\sqrt{d_k}}\right) V $
                3. **Multi-Head Attention** allows the model to attend to information from different representation subspaces concurrently.
                
                **Tip:** Use Transformers for contextual embeddings and sequence tasks!
            """.trimIndent()

            lower.contains("rust") || lower.contains("borrow") || lower.contains("ownership") -> """
                ### 🦀 Rust Ownership & Borrow Checker
                - **Ownership**: Every value has a single owner variable.
                - **Borrowing (`&T`)**: Multiple immutable references can exist.
                - **Mutable Borrow (`&mut T`)**: Only ONE active mutable reference permitted per scope to guarantee race-free concurrency.
                
                ```rust
                fn print_len(s: &String) {
                    println!("Length: {}", s.len());
                }
                ```
            """.trimIndent()

            lower.contains("sql") || lower.contains("join") || lower.contains("mysql") -> """
                ### 🗄️ MySQL Optimization & JOINs
                Use `INNER JOIN` when you require matching records in both tables:
                
                ```sql
                SELECT u.username, c.title, s.score
                FROM users u
                INNER JOIN submissions s ON u.id = s.user_id
                INNER JOIN courses c ON s.course_id = c.id
                WHERE s.score >= 90
                ORDER BY s.score DESC;
                ```
                *Index Tip*: Add an index on foreign keys (`user_id`, `course_id`) to avoid full table scans.
            """.trimIndent()

            lower.contains("fastapi") || lower.contains("python") -> """
                ### ⚡ Python FastAPI Async Endpoint
                ```python
                from fastapi import FastAPI, Depends
                from pydantic import BaseModel

                app = FastAPI()

                class PredictRequest(BaseModel):
                    features: list[float]

                @app.post("/predict")
                async def predict(data: PredictRequest):
                    # Fast asynchronous computation
                    return {"result": sum(data.features) * 1.5}
                ```
            """.trimIndent()

            lower.contains("quiz") || mode == "teacher" -> """
                ### 📋 DevGuru Lesson Plan & Quiz Generator
                **Objective:** Master Asynchronous JavaScript & Promises
                - **Concept 1:** Event Loop & Microtask Queue
                - **Concept 2:** `async/await` syntax sugar over Promises
                
                **Quiz Question:**
                *What will `Promise.resolve(1).then(console.log)` output?*
                - A) 1 in the next microtask (Correct)
                - B) undefined immediately
                - C) Uncaught TypeError
            """.trimIndent()

            else -> """
                ### 🤖 DevGuru AI Guidance
                You asked: "$prompt"
                
                **Key Engineering Takeaway:**
                - In modern software engineering, decouple state from presentation.
                - Keep functions pure and side-effects isolated.
                - When integrating AI (Gemini/LLMs), always validate model outputs against a typed schema before execution.
                
                *Need code examples for Python, JS, Rust, Go, or AI/ML? Try asking: "Show me a PyTorch CNN" or "How to write a Rust goroutine equivalent!"*
            """.trimIndent()
        }
    }
}
