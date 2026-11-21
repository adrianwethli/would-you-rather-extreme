package com.wouldyouratherextreme.game.data

import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Checks the real question file that ships inside the app, so a typo in it is caught by the build. */
class FreePackFileTest {

    private val pack = QuestionPackParser.parse(readFreePackFile())

    private fun readFreePackFile(): String {
        // Gradle runs unit tests from the app folder; the second path covers running from the repo root.
        val file = listOf(
            "src/main/assets/questions/free_pack.json",
            "app/src/main/assets/questions/free_pack.json",
        ).map(::File).firstOrNull { it.exists() }
            ?: error("Could not find free_pack.json")
        return file.readText(Charsets.UTF_8)
    }

    @Test
    fun hasExactlyFiftyQuestions() {
        assertEquals(50, pack.questions.size)
    }

    @Test
    fun isTheFreePack() {
        assertEquals("free", pack.id)
        assertFalse(pack.isPremium)
    }

    @Test
    fun idsAreNumberedFromOneToFifty() {
        val expected = (1..50).map { "free-%03d".format(it) }
        assertEquals(expected, pack.questions.map { it.id })
    }

    @Test
    fun textsAreShortEnoughToFitOnTheScreen() {
        pack.questions.forEach { q ->
            assertTrue("${q.id}: optionA too long", q.optionA.length <= 120)
            assertTrue("${q.id}: optionB too long", q.optionB.length <= 120)
            assertTrue("${q.id}: intro too long", (q.intro?.length ?: 0) <= 200)
        }
    }
}
