package com.wouldyouratherextreme.game.data

import org.json.JSONException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class QuestionPackParserTest {

    private fun pack(vararg questions: String) = """
        {"packId": "test", "premium": false, "questions": [${questions.joinToString(",")}]}
    """.trimIndent()

    private fun q(id: String, a: String = "Option A", b: String = "Option B", intro: String? = null): String {
        val introPart = if (intro != null) """"intro": "$intro",""" else ""
        return """{"id": "$id", $introPart "optionA": "$a", "optionB": "$b"}"""
    }

    @Test
    fun readsAValidPack() {
        val result = QuestionPackParser.parse(pack(q("t-1"), q("t-2", a = "Sing", b = "Dance")))
        assertEquals("test", result.id)
        assertFalse(result.isPremium)
        assertEquals(2, result.questions.size)
        assertEquals("Sing", result.questions[1].optionA)
        assertEquals("Dance", result.questions[1].optionB)
    }

    @Test
    fun introIsOptional() {
        val result = QuestionPackParser.parse(pack(q("t-1"), q("t-2", intro = "A runaway train.")))
        assertNull(result.questions[0].intro)
        assertEquals("A runaway train.", result.questions[1].intro)
    }

    @Test
    fun readsThePremiumFlag() {
        val json = """{"packId": "p1", "premium": true, "questions": [${q("p-1")}]}"""
        assertTrue(QuestionPackParser.parse(json).isPremium)
    }

    @Test
    fun keepsSpecialCharacters() {
        val result = QuestionPackParser.parse(pack(q("t-1", a = "Shout \\\"Faster, mein Führer!\\\"")))
        assertEquals("Shout \"Faster, mein Führer!\"", result.questions[0].optionA)
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsAnEmptyOption() {
        QuestionPackParser.parse(pack(q("t-1", a = "   ")))
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsTwoIdenticalOptions() {
        QuestionPackParser.parse(pack(q("t-1", a = "Same", b = "same")))
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsDuplicateIds() {
        QuestionPackParser.parse(pack(q("t-1"), q("t-1")))
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsAnEmptyIntro() {
        QuestionPackParser.parse(pack(q("t-1", intro = " ")))
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsAPackWithoutQuestions() {
        QuestionPackParser.parse(pack())
    }

    @Test(expected = JSONException::class)
    fun rejectsBrokenJson() {
        QuestionPackParser.parse("{ this is not json")
    }
}
