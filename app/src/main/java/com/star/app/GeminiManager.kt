package com.star.app

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiManager(private val apiKey: String) {
    
    private val model = GenerativeModel(
        modelName = "gemini-2.0-flash-exp",
        apiKey = apiKey
    )
    
    private val chatHistory = mutableListOf<Pair<String, String>>()
    
    suspend fun sendMessage(userMessage: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val fullPrompt = buildPrompt(userMessage)
                val response = model.generateContent(fullPrompt)
                val text = response.text ?: Persona.getErrorMessage()
                
                chatHistory.add(userMessage to text)
                return@withContext text
                
            } catch (e: Exception) {
                return@withContext "Error: ${e.message ?: "Unknown error"}"
            }
        }
    }
    
    private fun buildPrompt(userMessage: String): String {
        val history = chatHistory.takeLast(5)
            .joinToString("\n") { (user, star) -> "User: $user\nSTAR: $star" }
        
        return """
${Persona.getSystemPrompt()}

Previous conversation:
$history

User: $userMessage

Respond as STAR:
        """.trimIndent()
    }
    
    fun clearHistory() {
        chatHistory.clear()
    }
    
    fun getHistory(): List<Pair<String, String>> = chatHistory.toList()
    
    fun isKeyValid(key: String): Boolean = key.isNotEmpty() && key.length > 10
}
