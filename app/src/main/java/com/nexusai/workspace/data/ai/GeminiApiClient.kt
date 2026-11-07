package com.nexusai.workspace.data.ai

import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class GeminiApiClient {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    suspend fun generateContent(apiKey: String, systemPrompt: String, userMessage: String): String = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) {
            throw IllegalArgumentException("Gemini API key is missing.")
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey"

        val jsonPayload = JsonObject().apply {
            val contentsArray = com.google.gson.JsonArray()

            if (systemPrompt.isNotBlank()) {
                val systemObj = JsonObject().apply {
                    addProperty("role", "user")
                    val parts = com.google.gson.JsonArray().apply {
                        add(JsonObject().apply { addProperty("text", "System Instruction: $systemPrompt") })
                    }
                    add("parts", parts)
                }
                contentsArray.add(systemObj)
            }

            val userObj = JsonObject().apply {
                addProperty("role", "user")
                val parts = com.google.gson.JsonArray().apply {
                    add(JsonObject().apply { addProperty("text", userMessage) })
                }
                add("parts", parts)
            }
            contentsArray.add(userObj)

            add("contents", contentsArray)
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = jsonPayload.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            val bodyString = response.body?.string() ?: ""
            if (!response.isSuccessful) {
                throw Exception("API Call Failed (${response.code}): $bodyString")
            }

            val jsonResponse = gson.fromJson(bodyString, JsonObject::class.java)
            val candidates = jsonResponse.getAsJsonArray("candidates")
            if (candidates != null && candidates.size() > 0) {
                val firstCandidate = candidates[0].asJsonObject
                val content = firstCandidate.getAsJsonObject("content")
                val parts = content.getAsJsonArray("parts")
                if (parts != null && parts.size() > 0) {
                    return@withContext parts[0].asJsonObject.get("text").asString
                }
            }
            "No response text received from Gemini API."
        }
    }
}
