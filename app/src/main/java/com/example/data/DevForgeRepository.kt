package com.example.data

import com.example.model.*

object DevForgeRepository {

    val languages = listOf(
        ProgrammingLanguage(
            id = "html_css",
            name = "HTML5 & Modern CSS",
            track = CourseTrack.FRONTEND,
            description = "Master semantic markup, Flexbox, CSS Grid, Glassmorphism, animations and responsive viewports.",
            iconTag = "HTML",
            badgeColorHex = 0xFFFF6D00,
            totalLessons = 8,
            difficulty = "Beginner"
        ),
        ProgrammingLanguage(
            id = "javascript",
            name = "JavaScript (ES6+)",
            track = CourseTrack.FRONTEND,
            description = "Interactive DOM scripting, async/await, closures, promises, Web APIs, and dynamic UIs.",
            iconTag = "JS",
            badgeColorHex = 0xFFFFD600,
            totalLessons = 10,
            difficulty = "Beginner to Intermediate"
        ),
        ProgrammingLanguage(
            id = "python",
            name = "Python 3 & FastAPI",
            track = CourseTrack.BACKEND,
            description = "Pythonic data structures, object-oriented design, RESTful APIs with FastAPI, and data manipulation.",
            iconTag = "PY",
            badgeColorHex = 0xFF3776AB,
            totalLessons = 12,
            difficulty = "Beginner to Advanced"
        ),
        ProgrammingLanguage(
            id = "php",
            name = "Modern PHP & APIs",
            track = CourseTrack.BACKEND,
            description = "Modern PHP 8 typed properties, PDO database security, MVC patterns, and backend web services.",
            iconTag = "PHP",
            badgeColorHex = 0xFF777BB4,
            totalLessons = 7,
            difficulty = "Intermediate"
        ),
        ProgrammingLanguage(
            id = "java",
            name = "Java & Spring Core",
            track = CourseTrack.BACKEND,
            description = "Robust OOP, memory management, Java Streams, concurrency, and enterprise microservices architecture.",
            iconTag = "JAVA",
            badgeColorHex = 0xFFF89820,
            totalLessons = 9,
            difficulty = "Intermediate"
        ),
        ProgrammingLanguage(
            id = "rust",
            name = "Rust Systems",
            track = CourseTrack.BACKEND,
            description = "Zero-cost abstractions, borrow checker, ownership, pattern matching, memory safety without GC.",
            iconTag = "RS",
            badgeColorHex = 0xFFDEA584,
            totalLessons = 9,
            difficulty = "Advanced"
        ),
        ProgrammingLanguage(
            id = "golang",
            name = "Go (Golang)",
            track = CourseTrack.BACKEND,
            description = "Blazing concurrency with Goroutines, channels, fast HTTP servers, and cloud-native services.",
            iconTag = "GO",
            badgeColorHex = 0xFF00ADD8,
            totalLessons = 8,
            difficulty = "Intermediate"
        ),
        ProgrammingLanguage(
            id = "r_lang",
            name = "R Data Science",
            track = CourseTrack.AI_ML,
            description = "Statistical computing, data frames, dplyr filtering, ggplot2 visual analytics, and hypothesis testing.",
            iconTag = "R",
            badgeColorHex = 0xFF276DC3,
            totalLessons = 6,
            difficulty = "Intermediate"
        ),
        ProgrammingLanguage(
            id = "supervised_ml",
            name = "Supervised Learning",
            track = CourseTrack.AI_ML,
            description = "Linear/Logistic Regression, Decision Trees, Random Forests, train-test splits, and loss minimization.",
            iconTag = "SML",
            badgeColorHex = 0xFF00E5FF,
            totalLessons = 8,
            difficulty = "Intermediate"
        ),
        ProgrammingLanguage(
            id = "unsupervised_ml",
            name = "Unsupervised Learning",
            track = CourseTrack.AI_ML,
            description = "K-Means clustering, PCA dimensionality reduction, anomaly detection, and latent space embeddings.",
            iconTag = "UML",
            badgeColorHex = 0xFFB388FF,
            totalLessons = 7,
            difficulty = "Intermediate to Advanced"
        ),
        ProgrammingLanguage(
            id = "deep_learning",
            name = "Deep Learning & Neural Nets",
            track = CourseTrack.AI_ML,
            description = "Multi-layer perceptrons, backpropagation, CNNs for computer vision, Transformers & PyTorch.",
            iconTag = "DL",
            badgeColorHex = 0xFFFF4081,
            totalLessons = 10,
            difficulty = "Advanced"
        ),
        ProgrammingLanguage(
            id = "llm_genai",
            name = "LLMs & Generative AI",
            track = CourseTrack.AI_ML,
            description = "Prompt engineering, RAG pipelines, vector databases, fine-tuning, and Gemini/OpenAI API integration.",
            iconTag = "LLM",
            badgeColorHex = 0xFF00E676,
            totalLessons = 10,
            difficulty = "Intermediate to Advanced"
        ),
        ProgrammingLanguage(
            id = "mysql_db",
            name = "MySQL Database Studio",
            track = CourseTrack.DATABASE,
            description = "Relational schema design, normalization, complex INNER/LEFT JOINs, indexing, and SQL transactions.",
            iconTag = "SQL",
            badgeColorHex = 0xFF4479A1,
            totalLessons = 8,
            difficulty = "Beginner to Intermediate"
        ),
        ProgrammingLanguage(
            id = "ai_commands",
            name = "AI Command Learning & CLI",
            track = CourseTrack.SYSTEMS_DEVTOOLS,
            description = "Terminal AI copilots, curl API queries, automated bash scripting, and Git profile workflows.",
            iconTag = "CLI",
            badgeColorHex = 0xFF69F0AE,
            totalLessons = 6,
            difficulty = "All Levels"
        )
    )

    val sampleLessons = listOf(
        Lesson(
            id = "html_1",
            languageId = "html_css",
            title = "Semantic HTML5 & Responsive Flexbox",
            summary = "Construct modern accessible webpage layouts with header, nav, main, section, and flexible CSS containers.",
            durationMinutes = 15,
            markdownContent = """
                ### Semantic HTML5
                Semantic tags inform both browser rendering engines and assistive technologies about document structure.
                
                ```html
                <main class="dashboard">
                  <header><h1>DevForge</h1></header>
                  <section class="card-grid">...</section>
                </main>
                ```
                
                ### Flexbox Layout
                Use `display: flex; justify-content: space-between; align-items: center;` for responsive toolbars and navigation bars.
            """.trimIndent(),
            sampleCode = """
<!DOCTYPE html>
<html>
<head>
  <style>
    body { font-family: sans-serif; background: #0A0E17; color: #fff; padding: 16px; }
    .nav-bar { display: flex; justify-content: space-between; align-items: center; background: #131B2B; padding: 12px 20px; border-radius: 8px; border: 1px solid #00E5FF; }
    .brand { font-size: 20px; font-weight: bold; color: #00E5FF; }
    .badge { background: #B388FF; color: #000; padding: 4px 10px; border-radius: 12px; font-size: 12px; }
  </style>
</head>
<body>
  <nav class="nav-bar">
    <div class="brand">DevForge AI</div>
    <div class="badge">Level 1</div>
  </nav>
  <p>Welcome to interactive Frontend engineering!</p>
</body>
</html>
            """.trimIndent(),
            editableCode = """
<div style="display:flex; gap:12px; background:#1e293b; padding:16px; border-radius:8px;">
  <button style="background:#00e5ff; color:#000; border:none; padding:8px 16px; border-radius:4px; font-weight:bold;">Run Build</button>
  <button style="background:#b388ff; color:#000; border:none; padding:8px 16px; border-radius:4px; font-weight:bold;">Deploy API</button>
</div>
            """.trimIndent(),
            expectedOutput = "Renders two cyber buttons aligned horizontally inside a dark card container.",
            challengePrompt = "Change the button style or add a third button for 'Train Model'!",
            quizQuestion = "Which CSS property establishes a flex container context for direct children?",
            quizOptions = listOf("display: flex;", "flex-direction: row;", "align-content: stretch;", "float: flex;"),
            correctQuizIndex = 0,
            xpReward = 60
        ),
        Lesson(
            id = "js_1",
            languageId = "javascript",
            title = "Async/Await & Modern Fetch API",
            summary = "Consume REST APIs asynchronously with error handling, JSON parsing, and UI updates.",
            durationMinutes = 20,
            markdownContent = """
                ### JavaScript Asynchronous Execution
                The `async` keyword enables coroutine-like syntax. Use `await` on promises returned by `fetch()` to streamline async workflows.
            """.trimIndent(),
            sampleCode = """
async function fetchGithubProfile(username) {
  try {
    const response = await fetch(`https://api.github.com/users/${'$'}{username}`);
    if (!response.ok) throw new Error("User not found");
    const data = await response.json();
    console.log(`Loaded ${'$'}{data.name} with ${'$'}{data.public_repos} repos.`);
    return data;
  } catch (err) {
    console.error("API error:", err.message);
  }
}
            """.trimIndent(),
            editableCode = """
const calculateStudentScore = (xp, streak) => {
  const bonus = streak > 5 ? 1.5 : 1.0;
  return Math.round(xp * bonus);
};
console.log("Calculated Score:", calculateStudentScore(240, 7));
            """.trimIndent(),
            expectedOutput = "Calculated Score: 360",
            challengePrompt = "Modify calculateStudentScore to award a double bonus if streak > 10!",
            quizQuestion = "What does the 'await' keyword do when placed before a Promise?",
            quizOptions = listOf("Pauses async function execution until Promise resolves", "Cancels the Promise immediately", "Creates a new background thread", "Converts the Promise to a JSON string"),
            correctQuizIndex = 0,
            xpReward = 75
        ),
        Lesson(
            id = "python_1",
            languageId = "python",
            title = "FastAPI Endpoint & Pydantic Data Models",
            summary = "Build lightning-fast typed REST APIs in Python with asynchronous route handlers and automated validation.",
            durationMinutes = 25,
            markdownContent = """
                ### FastAPI Fundamentals
                FastAPI combines Starlette for fast async I/O and Pydantic for data schema validation with Python type hints.
            """.trimIndent(),
            sampleCode = """
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel

app = FastAPI(title="DevForge AI Engine")

class ModelQuery(BaseModel):
    prompt: str
    temperature: float = 0.7

@app.post("/predict")
async def generate_response(query: ModelQuery):
    if len(query.prompt.strip()) == 0:
        raise HTTPException(status_code=400, detail="Empty prompt")
    return {"status": "success", "echo": query.prompt.upper()}
            """.trimIndent(),
            editableCode = """
def train_linear_model(x_vals, y_vals, learning_rate=0.01):
    w, b = 0.0, 0.0
    for _ in range(100):
        for x, y in zip(x_vals, y_vals):
            pred = w * x + b
            err = pred - y
            w -= learning_rate * err * x
            b -= learning_rate * err
    return round(w, 2), round(b, 2)

weight, bias = train_linear_model([1, 2, 3, 4], [2, 4, 6, 8])
print(f"Trained: w={weight}, b={bias}")
            """.trimIndent(),
            expectedOutput = "Trained: w=2.0, b=0.0",
            challengePrompt = "Run the code and see the gradient descent weights converge to slope 2.0!",
            quizQuestion = "Which Python library does FastAPI leverage for data validation and parsing?",
            quizOptions = listOf("Pydantic", "NumPy", "Marshmallow", "Requests"),
            correctQuizIndex = 0,
            xpReward = 80
        ),
        Lesson(
            id = "ai_ml_1",
            languageId = "supervised_ml",
            title = "Supervised Learning: Loss Functions & Gradients",
            summary = "Explore Mean Squared Error (MSE), Cross-Entropy loss, and gradient descent optimization.",
            durationMinutes = 20,
            markdownContent = """
                ### Supervised Learning
                In supervised learning, models learn mapping functions from labeled input pairs $(X, y)$.
                - **Regression**: Predicting continuous numeric outputs (e.g., house price, temperature).
                - **Classification**: Assigning inputs to discrete classes (e.g., spam vs ham, disease detection).
            """.trimIndent(),
            sampleCode = """
import numpy as np

def mean_squared_error(y_true, y_pred):
    return np.mean((np.array(y_true) - np.array(y_pred)) ** 2)

actual = [10.5, 20.0, 30.2]
prediction = [11.0, 19.5, 31.0]
loss = mean_squared_error(actual, prediction)
print(f"MSE Loss: {loss:.4f}")
            """.trimIndent(),
            editableCode = """
# Compute Classification Accuracy
predictions = [1, 0, 1, 1, 0, 1, 0, 1]
labels      = [1, 0, 1, 0, 0, 1, 0, 1]
correct = sum(p == y for p, y in zip(predictions, labels))
acc = (correct / len(labels)) * 100
print(f"Accuracy: {acc}% ({correct}/{len(labels)})")
            """.trimIndent(),
            expectedOutput = "Accuracy: 87.5% (7/8)",
            challengePrompt = "Add precision and recall calculations to the code snippet!",
            quizQuestion = "Which loss function is most commonly used for binary classification tasks?",
            quizOptions = listOf("Binary Cross-Entropy (Log Loss)", "Mean Absolute Error (L1)", "Huber Loss", "Hinge Loss"),
            correctQuizIndex = 0,
            xpReward = 85
        ),
        Lesson(
            id = "llm_1",
            languageId = "llm_genai",
            title = "Prompt Engineering & RAG Architecture",
            summary = "Design robust system prompts, temperature controls, few-shot examples, and Retrieval-Augmented Generation.",
            durationMinutes = 25,
            markdownContent = """
                ### LLM Prompt Architecture
                Effective GenAI applications guide foundational models using:
                1. **System Persona & Constraints**: Define role and boundaries.
                2. **Few-Shot Demonstrations**: Give high-quality input-output pairs.
                3. **RAG (Retrieval-Augmented Generation)**: Inject relevant factual context retrieved from vector databases.
            """.trimIndent(),
            sampleCode = """
// Gemini REST API Request Body
val request = GenerateContentRequest(
    contents = listOf(Content(parts = listOf(Part(text = "Explain Transformer Attention in 3 bullets")))),
    generationConfig = GenerationConfig(temperature = 0.4f)
)
            """.trimIndent(),
            editableCode = """
const promptTemplate = (role, context, question) => {
  return `You are ${'$'}{role}. Context: ${'$'}{context}\nQuestion: ${'$'}{question}\nAnswer step-by-step:`;
};
console.log(promptTemplate("DevGuru AI Teacher", "User is learning Rust borrow checker", "Why do I get E0382?"));
            """.trimIndent(),
            expectedOutput = "Outputs a formatted structured system prompt.",
            challengePrompt = "Test tailoring the system prompt with markdown output requirements!",
            quizQuestion = "In RAG architecture, what is the role of Vector Embeddings?",
            quizOptions = listOf("To semantically retrieve relevant document chunks based on query similarity", "To encrypt user passwords", "To speed up video rendering", "To replace SQL databases entirely"),
            correctQuizIndex = 0,
            xpReward = 90
        ),
        Lesson(
            id = "rust_1",
            languageId = "rust",
            title = "Ownership & Borrowing in Rust",
            summary = "Understand Rust's compile-time memory guarantees: single owner, move semantics, and mutable vs immutable references.",
            durationMinutes = 25,
            markdownContent = """
                ### The Three Rules of Ownership
                1. Each value in Rust has an owner.
                2. There can only be one owner at a time.
                3. When the owner goes out of scope, the value is dropped.
            """.trimIndent(),
            sampleCode = """
fn main() {
    let s1 = String::from("DevForge");
    let len = calculate_length(&s1); // Borrowing reference
    println!("Length of '{}' is {}", s1, len);
}

fn calculate_length(s: &String) -> usize {
    s.len()
}
            """.trimIndent(),
            editableCode = """
fn main() {
    let mut data = vec![10, 20, 30];
    data.push(40);
    let total: i32 = data.iter().sum();
    println!("Vector sum: {}", total);
}
            """.trimIndent(),
            expectedOutput = "Vector sum: 100",
            challengePrompt = "Try using pattern matching or fold to compute vector product!",
            quizQuestion = "How many mutable references to a specific piece of data can exist in a given scope in Rust?",
            quizOptions = listOf("Exactly one", "Unlimited as long as they are distinct threads", "Up to two", "Any number if unsafe is not used"),
            correctQuizIndex = 0,
            xpReward = 85
        )
    )

    val projectBlueprints = listOf(
        ProjectBlueprint(
            id = "proj_ai_recipe",
            title = "Full-Stack AI Recipe & Nutrition Engine",
            category = "Full-Stack AI",
            difficulty = "Intermediate",
            techStack = listOf("Python", "FastAPI", "HTML5", "CSS3", "JavaScript", "Gemini API"),
            description = "A responsive web application that accepts user ingredients, queries a Python FastAPI backend, calls Gemini AI to craft custom macro-balanced recipes, and displays animated meal cards.",
            files = mapOf(
                "app.py" to """
from fastapi import FastAPI, Request
from pydantic import BaseModel
import os

app = FastAPI(title="RecipeGen Backend")

class IngredientPayload(BaseModel):
    ingredients: list[str]
    dietary_goal: str

@app.post("/api/generate-recipe")
async def generate_recipe(payload: IngredientPayload):
    prompt = f"Create a healthy meal using: {', '.join(payload.ingredients)}. Goal: {payload.dietary_goal}"
    # Calls Gemini API (handled by client or server-side endpoint)
    return {
        "title": "Neon Cyber Protein Bowl",
        "cook_time_mins": 18,
        "macros": {"protein": "34g", "carbs": "42g", "fats": "12g"},
        "steps": [
            "Saute ingredients in olive oil for 6 mins.",
            "Add seasoning and simmer on medium heat.",
            "Garnish with fresh greens and serve hot."
        ]
    }
                """.trimIndent(),
                "index.html" to """
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="style.css">
  <title>AI Recipe Studio</title>
</head>
<body>
  <div class="container">
    <h1>🍳 AI Nutrition Generator</h1>
    <input id="ingInput" placeholder="Enter ingredients (e.g. eggs, spinach, oats)">
    <button onclick="requestRecipe()">Generate with Gemini</button>
    <div id="recipeResult" class="card"></div>
  </div>
  <script src="main.js"></script>
</body>
</html>
                """.trimIndent(),
                "main.js" to """
async function requestRecipe() {
  const input = document.getElementById('ingInput').value;
  const resultDiv = document.getElementById('recipeResult');
  resultDiv.innerHTML = '<p class="loading">Consulting AI Chef...</p>';
  
  const payload = {
    ingredients: input.split(',').map(s => s.trim()),
    dietary_goal: "High Protein"
  };

  const res = await fetch('/api/generate-recipe', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  });
  const data = await res.json();
  resultDiv.innerHTML = `<h3>${'$'}{data.title}</h3><p>Cook Time: ${'$'}{data.cook_time_mins} mins</p>`;
}
                """.trimIndent(),
                "style.css" to """
body { background: #0A0E17; color: #FFF; font-family: system-ui; }
.container { max-width: 600px; margin: 40px auto; padding: 24px; border: 1px solid #00E5FF; border-radius: 12px; }
input { width: 100%; padding: 12px; border-radius: 8px; border: 1px solid #334155; background: #131B2B; color: #fff; }
button { background: #00E5FF; color: #000; font-weight: bold; border: none; padding: 12px 24px; border-radius: 8px; margin-top: 12px; cursor: pointer; }
.card { margin-top: 20px; background: #1E293B; padding: 16px; border-radius: 8px; }
                """.trimIndent()
            ),
            simulatedOutput = """
[FastAPI] Server listening on http://127.0.0.1:8000
[POST] /api/generate-recipe - 200 OK (elapsed: 142ms)
Generated: 'Neon Cyber Protein Bowl' (34g Protein, 18 mins)
[UI] Recipe Card rendered with live macros and steps.
            """.trimIndent()
        ),
        ProjectBlueprint(
            id = "proj_github_analyzer",
            title = "Real-Time GitHub Profile & Portfolio Analyzer",
            category = "Web & API Integration",
            difficulty = "Beginner to Intermediate",
            techStack = listOf("JavaScript", "HTML5", "CSS Grid", "GitHub REST API", "Chart Visualization"),
            description = "Fetches actual user repositories, star counts, commit activity streaks, and computes developer skill distribution with AI-powered portfolio suggestions.",
            files = mapOf(
                "index.html" to """
<div id="github-app">
  <div class="search-bar">
    <input id="ghUsername" placeholder="e.g. torvalds or alimoeezali94" value="torvalds">
    <button onclick="analyzeDeveloper()">Audit Profile</button>
  </div>
  <div id="metrics" class="grid-stats"></div>
  <div id="repoList" class="repo-cards"></div>
</div>
                """.trimIndent(),
                "script.js" to """
async function analyzeDeveloper() {
  const user = document.getElementById('ghUsername').value;
  const userRes = await fetch(`https://api.github.com/users/${'$'}{user}`);
  const userData = await userRes.json();
  
  const reposRes = await fetch(`https://api.github.com/users/${'$'}{user}/repos?sort=stars&per_page=5`);
  const repos = await reposRes.json();
  
  console.log(`Analyzed ${'$'}{userData.login}: ${'$'}{repos.length} top repositories loaded.`);
}
                """.trimIndent()
            ),
            simulatedOutput = """
[GitHub API] GET https://api.github.com/users/torvalds -> 200 OK
[Metrics] Public Repos: 7 | Followers: 205k+
[AI Review] "Outstanding Linux kernel contributor. Recommend highlighting architecture diagrams and Rust integration."
            """.trimIndent()
        ),
        ProjectBlueprint(
            id = "proj_pytorch_classifier",
            title = "Deep Learning Image Classifier with PyTorch",
            category = "Machine Learning",
            difficulty = "Advanced",
            techStack = listOf("Python", "PyTorch", "NumPy", "Deep Learning CNN"),
            description = "Construct a Convolutional Neural Network (CNN) with convolutional filters, max-pooling, ReLU activations, and a softmax classification head.",
            files = mapOf(
                "model.py" to """
import torch
import torch.nn as nn
import torch.nn.functional as F

class CyberNet(nn.Module):
    def __init__(self, num_classes=10):
        super(CyberNet, self).__init__()
        self.conv1 = nn.Conv2d(in_channels=1, out_channels=32, kernel_size=3)
        self.conv2 = nn.Conv2d(in_channels=32, out_channels=64, kernel_size=3)
        self.pool = nn.MaxPool2d(2, 2)
        self.fc1 = nn.Linear(64 * 5 * 5, 128)
        self.fc2 = nn.Linear(128, num_classes)
        self.dropout = nn.Dropout(0.25)

    def forward(self, x):
        x = F.relu(self.conv1(x))
        x = self.pool(F.relu(self.conv2(x)))
        x = x.view(-1, 64 * 5 * 5)
        x = self.dropout(F.relu(self.fc1(x)))
        return self.fc2(x)
                """.trimIndent(),
                "train.py" to """
import torch
import torch.optim as optim

model = CyberNet()
criterion = nn.CrossEntropyLoss()
optimizer = optim.Adam(model.parameters(), lr=0.001)

print("CNN Architecture Initialized successfully.")
print(f"Total Parameters: {sum(p.numel() for p in model.parameters())}")
                """.trimIndent()
            ),
            simulatedOutput = """
[PyTorch] CUDA Device: Activated
[Epoch 1/5] Loss: 0.412 | Train Acc: 88.4%
[Epoch 2/5] Loss: 0.187 | Train Acc: 94.6%
[Epoch 3/5] Loss: 0.092 | Train Acc: 97.8%
Model weights saved to cyber_classifier.pt
            """.trimIndent()
        )
    )

    fun createInitialDatabase(): List<SqlTable> {
        val users = SqlTable(
            tableName = "users",
            columns = listOf("id", "username", "email", "xp", "level", "role"),
            rows = mutableListOf(
                mutableMapOf("id" to "1", "username" to "cyber_coder", "email" to "coder@devforge.ai", "xp" to "1450", "level" to "4", "role" to "student"),
                mutableMapOf("id" to "2", "username" to "alimoeez", "email" to "alimoeezali94@gmail.com", "xp" to "2800", "level" to "7", "role" to "teacher"),
                mutableMapOf("id" to "3", "username" to "rustacean_pro", "email" to "ferris@rust.lang", "xp" to "3200", "level" to "9", "role" to "student"),
                mutableMapOf("id" to "4", "username" to "ai_researcher", "email" to "deep@neural.io", "xp" to "4100", "level" to "12", "role" to "mentor")
            )
        )

        val courses = SqlTable(
            tableName = "courses",
            columns = listOf("id", "title", "track", "difficulty", "lessons_count"),
            rows = mutableListOf(
                mutableMapOf("id" to "101", "title" to "Fullstack HTML & JS", "track" to "FRONTEND", "difficulty" to "Beginner", "lessons_count" to "18"),
                mutableMapOf("id" to "102", "title" to "Python FastAPI Microservices", "track" to "BACKEND", "difficulty" to "Intermediate", "lessons_count" to "12"),
                mutableMapOf("id" to "103", "title" to "Deep Learning & LLM Masterclass", "track" to "AI_ML", "difficulty" to "Advanced", "lessons_count" to "20"),
                mutableMapOf("id" to "104", "title" to "Rust Concurrency & Safety", "track" to "BACKEND", "difficulty" to "Advanced", "lessons_count" to "14")
            )
        )

        val submissions = SqlTable(
            tableName = "submissions",
            columns = listOf("id", "user_id", "course_id", "status", "score"),
            rows = mutableListOf(
                mutableMapOf("id" to "1", "user_id" to "1", "course_id" to "101", "status" to "PASSED", "score" to "95"),
                mutableMapOf("id" to "2", "user_id" to "1", "course_id" to "102", "status" to "IN_PROGRESS", "score" to "70"),
                mutableMapOf("id" to "3", "user_id" to "2", "course_id" to "103", "status" to "PASSED", "score" to "100"),
                mutableMapOf("id" to "4", "user_id" to "3", "course_id" to "104", "status" to "PASSED", "score" to "98")
            )
        )

        val rewards = SqlTable(
            tableName = "rewards",
            columns = listOf("id", "badge_name", "xp_needed", "tier"),
            rows = mutableListOf(
                mutableMapOf("id" to "1", "badge_name" to "Bug Slayer", "xp_needed" to "500", "tier" to "Bronze"),
                mutableMapOf("id" to "2", "badge_name" to "SQL Commander", "xp_needed" to "1200", "tier" to "Silver"),
                mutableMapOf("id" to "3", "badge_name" to "Full-Stack Titan", "xp_needed" to "2500", "tier" to "Gold"),
                mutableMapOf("id" to "4", "badge_name" to "Neural Overlord", "xp_needed" to "5000", "tier" to "Diamond")
            )
        )

        return listOf(users, courses, submissions, rewards)
    }

    val bosses = listOf(
        BattleBoss(
            id = "boss_bugzilla",
            name = "BugZilla",
            title = "Titan of Null Pointers & Memory Leaks",
            imageResName = "code_boss_battle",
            maxHp = 100,
            currentHp = 100,
            attackPower = 20,
            weakness = "Strict Null Safety & Safe Navigation",
            questions = listOf(
                BattleQuestion(
                    id = "q1",
                    question = "Fix BugZilla's lethal NullPointerException in this snippet:",
                    codeSnippet = "val user: User? = null\nval nameLength = user.name.length // CRASH!",
                    options = listOf(
                        "val nameLength = user?.name?.length ?: 0",
                        "val nameLength = user!!.name.length",
                        "val nameLength = (user as User).name.length",
                        "val nameLength = user.name.toString().length"
                    ),
                    correctIndex = 0,
                    playerDamage = 35,
                    explanation = "The safe-call (?.) and elvis (?:) operator safely handle nullable references without throwing NullPointerException!"
                ),
                BattleQuestion(
                    id = "q2",
                    question = "BugZilla unleashes an infinite loop! Which line prevents the freeze?",
                    codeSnippet = "let mut count = 0;\nwhile count < 10 {\n    println!(\"{}\", count);\n    // Missing line!\n}",
                    options = listOf(
                        "count += 1;",
                        "break true;",
                        "count = count - 1;",
                        "continue;"
                    ),
                    correctIndex = 0,
                    playerDamage = 35,
                    explanation = "Incrementing count ensures the loop condition (count < 10) eventually evaluates to false."
                ),
                BattleQuestion(
                    id = "q3",
                    question = "Dispel BugZilla's SQL Injection attack! How do you safely query user data?",
                    codeSnippet = "query = \"SELECT * FROM users WHERE username = '\" + input + \"'\"",
                    options = listOf(
                        "Use Prepared Statements with parameterized placeholders (?)",
                        "Escape single quotes with double quotes manually",
                        "Convert the query to uppercase before execution",
                        "Execute the query inside a try-catch block"
                    ),
                    correctIndex = 0,
                    playerDamage = 40,
                    explanation = "Parameterized Prepared Statements separate SQL query code from user data, eliminating SQL injection vulnerability."
                )
            )
        ),
        BattleBoss(
            id = "boss_syntax_demon",
            name = "Syntax Demon",
            title = "Lord of Missing Semicolons & Indentation Errors",
            imageResName = "code_boss_battle",
            maxHp = 120,
            currentHp = 120,
            attackPower = 25,
            weakness = "Strict Typing & Linter Compliance",
            questions = listOf(
                BattleQuestion(
                    id = "sq1",
                    question = "The Demon created an async unhandled promise rejection in JS. How to resolve?",
                    codeSnippet = "async function run() {\n  const res = fetch('/api/data');\n  console.log(res.status);\n}",
                    options = listOf(
                        "const res = await fetch('/api/data');",
                        "const res = sync fetch('/api/data');",
                        "const res = yield fetch('/api/data');",
                        "const res = defer fetch('/api/data');"
                    ),
                    correctIndex = 0,
                    playerDamage = 40,
                    explanation = "You must await the fetch Promise to receive the actual Response object before accessing its properties."
                ),
                BattleQuestion(
                    id = "sq2",
                    question = "Solve the Python indentation error blocking the neural training loop:",
                    codeSnippet = "for epoch in range(10):\nprint(f'Epoch {epoch}')",
                    options = listOf(
                        "Indent the body of the for loop with 4 spaces",
                        "Add a semicolon after print statement",
                        "Wrap epoch in brackets: [epoch]",
                        "Use 'while' instead of 'for'"
                    ),
                    correctIndex = 0,
                    playerDamage = 40,
                    explanation = "Python uses indentation blocks rather than curly braces to delimit control flow scopes."
                ),
                BattleQuestion(
                    id = "sq3",
                    question = "Counter the Demon's Rust borrow checker violation:",
                    codeSnippet = "let s = String::from(\"hello\");\nlet r1 = &mut s;\nlet r2 = &mut s; // ERROR!",
                    options = listOf(
                        "Keep only one mutable reference in scope at a time",
                        "Declare s as a global static variable",
                        "Cast s to an integer pointer",
                        "Add the async keyword to s"
                    ),
                    correctIndex = 0,
                    playerDamage = 50,
                    explanation = "Rust's aliasing XOR mutability rule permits only one mutable reference in an active scope to prevent data races."
                )
            )
        )
    )

    val defaultBadges = listOf(
        BadgeItem("b1", "Code Initiate", "Completed your very first lesson", "school", true, 1, "Starter"),
        BadgeItem("b2", "Bug Slayer", "Defeated BugZilla in Battle Arena", "pest_control", true, 2, "Combat"),
        BadgeItem("b3", "SQL Commander", "Executed advanced JOIN queries in MySQL Studio", "database", true, 3, "Database"),
        BadgeItem("b4", "Full-Stack Titan", "Constructed full-stack Python + JS project", "layers", false, 4, "Projects"),
        BadgeItem("b5", "AI Whisperer", "Mastered LLM Prompt Engineering & RAG", "psychology", false, 5, "AI & ML"),
        BadgeItem("b6", "Rustacean Ironclad", "Conquered Rust Ownership & Borrowing", "shield", false, 6, "Systems"),
        BadgeItem("b7", "GitHub Maestro", "Integrated real GitHub profile analytics", "terminal", false, 7, "DevOps"),
        BadgeItem("b8", "Grand Architect", "Reached Level 10 mastery with 5000+ XP", "military_tech", false, 10, "Mastery")
    )
}
