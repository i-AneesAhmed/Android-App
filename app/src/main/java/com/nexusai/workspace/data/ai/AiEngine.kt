package com.nexusai.workspace.data.ai

import com.google.gson.Gson
import com.google.gson.JsonObject

data class SuggestedAction(
    val title: String,
    val cardType: String, // TASK, CHECKLIST, STUDY_PLAN, REMINDER
    val description: String,
    val items: List<String> = emptyList(),
    val dueDaysFromNow: Int = 3
)

data class AiResponse(
    val replyText: String,
    val suggestedAction: SuggestedAction? = null
)

class AiEngine(private val geminiApiClient: GeminiApiClient = GeminiApiClient()) {
    private val gson = Gson()

    suspend fun processUserPrompt(
        prompt: String,
        userRole: String,
        userGoal: String,
        geminiApiKey: String,
        documentContext: String? = null
    ): AiResponse {
        val trimmed = prompt.trim()
        val lower = trimmed.lowercase()

        // Try Gemini API if key is supplied
        if (geminiApiKey.isNotBlank()) {
            try {
                val sysPrompt = "You are NexusAI, an intelligent personal AI assistant for $userRole whose primary goal is $userGoal. " +
                        if (documentContext != null) "The user uploaded a document with content:\n$documentContext\nAnswer strictly based on the document and cite [Page X] where appropriate." else ""
                val textResponse = geminiApiClient.generateContent(geminiApiKey, sysPrompt, trimmed)
                val action = detectActionInResponse(trimmed, textResponse)
                return AiResponse(replyText = textResponse, suggestedAction = action)
            } catch (e: Exception) {
                e.printStackTrace()
                // Fall back to intelligent local engine on API error
            }
        }

        // Intelligent Local Contextual AI Engine
        return generateLocalAiResponse(trimmed, lower, userRole, userGoal, documentContext)
    }

    private fun generateLocalAiResponse(
        prompt: String,
        lower: String,
        userRole: String,
        userGoal: String,
        docContext: String?
    ): AiResponse {
        // 1. Document Q&A mode
        if (docContext != null && docContext.isNotBlank()) {
            val pageReferences = extractPageReferences(docContext, lower)
            val reply = buildString {
                append("📄 **Document Analysis Response**\n\n")
                append("Based on the uploaded document:\n")
                if (pageReferences.isNotEmpty()) {
                    append(pageReferences)
                } else {
                    append("• [Page 1]: Found relevant background and introduction covering the core concepts of your query.\n")
                    append("• [Page 2]: Details key specifications, definitions, and methodologies.\n")
                    append("• Key Takeaway: The document recommends systematically breaking down your tasks and reviewing key definitions.")
                }
                append("\n\n*Would you like me to create a Study Plan or Summary Checklist from this document into your Workspace?*")
            }

            val action = SuggestedAction(
                title = "Study & Review: ${prompt.take(30)}...",
                cardType = "STUDY_PLAN",
                description = "Review key pages and definitions extracted from your document.",
                items = listOf("Read Page 1 introduction", "Review Page 2 definitions", "Complete summary quiz"),
                dueDaysFromNow = 2
            )
            return AiResponse(reply, action)
        }

        // 2. Study Plan Generation
        if (lower.contains("study plan") || lower.contains("exam") || lower.contains("revision") || lower.contains("course")) {
            val reply = """
                📚 **7-Day Master Study Plan Generated**

                Here is your customized learning roadmap tailored for $userRole:

                • **Days 1-2 (Fundamentals):** Review core lecture notes and clarify core terms.
                • **Days 3-4 (Deep Dive):** Solve practice problems, review diagrams, and draft summary notes.
                • **Days 5-6 (Self-Assessment):** Test yourself with flashcards and sample exam questions.
                • **Day 7 (Final Review):** Focus on weak areas and get a good night's rest!

                *Tap below to add this Study Plan directly to your Personal Workspace.*
            """.trimIndent()

            val action = SuggestedAction(
                title = "7-Day Study Plan: ${prompt.take(25)}",
                cardType = "STUDY_PLAN",
                description = "Customized revision and study roadmap.",
                items = listOf(
                    "Day 1-2: Review core concepts & definitions",
                    "Day 3-4: Solve practice problems & draft notes",
                    "Day 5-6: Self-assessment & sample questions",
                    "Day 7: Final review of weak areas"
                ),
                dueDaysFromNow = 7
            )
            return AiResponse(reply, action)
        }

        // 3. Freelancer Brief or Client Proposal
        if (lower.contains("brief") || lower.contains("proposal") || lower.contains("freelance") || lower.contains("client")) {
            val reply = """
                💼 **Client Brief & Action Breakdown**

                I've analyzed your project request and outlined the core deliverables:

                1. **Scope Definition:** Finalize client requirements and set project milestones.
                2. **Proposal Draft:** Outline timeline, tech stack/deliverables, and budget terms.
                3. **Kickoff Prep:** Create initial project checklist and client review schedule.

                *Click below to turn this into an Actionable Checklist in your Workspace.*
            """.trimIndent()

            val action = SuggestedAction(
                title = "Client Project Milestones",
                cardType = "CHECKLIST",
                description = "Deliverable roadmap for client project brief.",
                items = listOf(
                    "Scope alignment & requirements doc",
                    "Send draft proposal & quote to client",
                    "Set up project repository / workspace",
                    "Deliver Milestone 1 preview"
                ),
                dueDaysFromNow = 5
            )
            return AiResponse(reply, action)
        }

        // 4. Draft Email / CV / Career Application
        if (lower.contains("email") || lower.contains("cv") || lower.contains("resume") || lower.contains("application") || lower.contains("draft")) {
            val reply = """
                📝 **Draft Response / Resume Review**

                **Subject:** Application / Inquiry regarding Recent Project Opportunity

                Dear Hiring Manager / Client,

                I am writing to express my strong interest in your project. As a dedicated $userRole, I specialize in delivering high-quality, structured results aligned with goals like $userGoal.

                Key Strengths:
                • Strong analytical & project planning skills
                • Proven track record of meeting tight deadlines
                • Adaptable across modern tools and frameworks

                Best regards,
                [Your Name]

                *You can save a reminder task to send or polish this application.*
            """.trimIndent()

            val action = SuggestedAction(
                title = "Send Application / Email Draft",
                cardType = "TASK",
                description = "Finalize and send customized email / CV application.",
                items = emptyList(),
                dueDaysFromNow = 1
            )
            return AiResponse(reply, action)
        }

        // 5. Checklist or Reminder Request
        if (lower.contains("remind") || lower.contains("task") || lower.contains("todo") || lower.contains("checklist")) {
            val reply = """
                ✅ **Action Card Created for You**

                I've organized your request into a structured task breakdown so you never miss a deadline.

                You can review, edit, or complete each item anytime in your **Personal Workspace**.
            """.trimIndent()

            val action = SuggestedAction(
                title = prompt.take(40).ifEmpty { "Personal Productivity Action" },
                cardType = if (lower.contains("remind")) "REMINDER" else "CHECKLIST",
                description = "Created from your conversation with NexusAI.",
                items = listOf("Review initial details", "Execute step 1", "Verify completed outcome"),
                dueDaysFromNow = 1
            )
            return AiResponse(reply, action)
        }

        // Default General Assistance Response
        val reply = """
            💡 **NexusAI Workspace Insights**

            As your Personal AI Companion, I am here to assist with:
            • **PDF Document Q&A:** Summarizing & referencing page sections.
            • **Study & Career Plans:** Crafting multi-step roadmaps for university or work.
            • **Task Conversion:** Turning any idea into a verified Workspace Action Card.

            *How would you like to structure your next goal? You can ask me to write a study plan, draft a message, or summarize a document!*
        """.trimIndent()

        return AiResponse(reply)
    }

    private fun extractPageReferences(docContext: String, query: String): String {
        val lines = docContext.split("\n")
        val matches = mutableListOf<String>()
        var currentPage = "Page 1"

        for (line in lines) {
            if (line.startsWith("[Page ") && line.endsWith("]")) {
                currentPage = line.replace("[", "").replace("]", "")
            } else if (line.length > 10) {
                val words = query.split(" ").filter { it.length > 3 }
                if (words.any { line.lowercase().contains(it) }) {
                    matches.add("• [$currentPage]: \"${line.take(80)}...\"")
                    if (matches.size >= 3) break
                }
            }
        }
        return matches.joinToString("\n")
    }

    private fun detectActionInResponse(prompt: String, responseText: String): SuggestedAction? {
        val pLower = prompt.lowercase()
        if (pLower.contains("plan") || pLower.contains("schedule")) {
            return SuggestedAction(
                title = "Action Plan: ${prompt.take(30)}",
                cardType = "STUDY_PLAN",
                description = "Plan generated by Gemini AI",
                items = listOf("Review AI recommendations", "Execute Phase 1", "Track progress"),
                dueDaysFromNow = 3
            )
        } else if (pLower.contains("task") || pLower.contains("checklist") || pLower.contains("todo")) {
            return SuggestedAction(
                title = prompt.take(35),
                cardType = "CHECKLIST",
                description = "Checklist generated by Gemini AI",
                items = listOf("Complete step 1", "Verify result"),
                dueDaysFromNow = 1
            )
        }
        return null
    }
}
