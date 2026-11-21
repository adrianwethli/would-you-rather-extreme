package com.wouldyouratherextreme.game.data

import com.wouldyouratherextreme.game.domain.Question
import com.wouldyouratherextreme.game.domain.QuestionPack
import org.json.JSONObject

/**
 * Reads a question pack from JSON text and checks it for mistakes.
 * A mistake (empty text, duplicate id, ...) stops with a clear message instead of
 * showing a broken question to the players later.
 */
object QuestionPackParser {

    fun parse(json: String): QuestionPack {
        val root = JSONObject(json)
        val packId = root.getString("packId").trim()
        require(packId.isNotEmpty()) { "packId is empty." }
        val premium = root.getBoolean("premium")

        val array = root.getJSONArray("questions")
        val questions = (0 until array.length()).map { position ->
            val item = array.getJSONObject(position)
            val id = item.getString("id").trim()
            val label = "Question ${position + 1} ($id)"
            require(id.isNotEmpty()) { "Question ${position + 1} has an empty id." }

            val intro = if (item.has("intro") && !item.isNull("intro")) {
                item.getString("intro").trim().also { require(it.isNotEmpty()) { "$label: intro is empty." } }
            } else {
                null
            }
            val optionA = item.getString("optionA").trim()
            val optionB = item.getString("optionB").trim()
            require(optionA.isNotEmpty()) { "$label: optionA is empty." }
            require(optionB.isNotEmpty()) { "$label: optionB is empty." }
            require(!optionA.equals(optionB, ignoreCase = true)) { "$label: both options are the same." }

            Question(id = id, optionA = optionA, optionB = optionB, intro = intro)
        }

        require(questions.isNotEmpty()) { "Pack '$packId' has no questions." }
        val duplicates = questions.groupBy { it.id }.filterValues { it.size > 1 }.keys
        require(duplicates.isEmpty()) { "Duplicate question ids: ${duplicates.joinToString()}." }

        return QuestionPack(id = packId, isPremium = premium, questions = questions)
    }
}
