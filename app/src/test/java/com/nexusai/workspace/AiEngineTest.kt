package com.nexusai.workspace

import com.nexusai.workspace.data.ai.AiEngine
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class AiEngineTest {

    private val aiEngine = AiEngine()

    @Test
    fun testStudyPlanGeneration() = runBlocking {
        val prompt = "Can you create a study plan for my exams?"
        val response = aiEngine.processUserPrompt(
            prompt = prompt,
            userRole = "University Student",
            userGoal = "Understand lecture notes",
            geminiApiKey = ""
        )

        assertNotNull(response)
        assertTrue(response.replyText.contains("7-Day Master Study Plan"))
        assertNotNull(response.suggestedAction)
        assertEquals("STUDY_PLAN", response.suggestedAction?.cardType)
        assertTrue(response.suggestedAction!!.items.isNotEmpty())
    }

    @Test
    fun testDocumentQaPageReferences() = runBlocking {
        val docContext = "[Page 1]\nIntroduction to Android Architecture and Room DB.\n[Page 2]\nDetailed breakdown of DAOs, Entities, and ViewModels."
        val prompt = "Where are DAOs explained?"

        val response = aiEngine.processUserPrompt(
            prompt = prompt,
            userRole = "University Student",
            userGoal = "Understand lecture notes",
            geminiApiKey = "",
            documentContext = docContext
        )

        assertNotNull(response)
        assertTrue(response.replyText.contains("Document Analysis Response"))
        assertTrue(response.replyText.contains("[Page 2]"))
    }
}
