package com.wouldyouratherextreme.game.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Checks the real free question file that ships inside the app, so a typo in it is caught by the build. */
class FreePackFileTest {

    private val pack = QuestionPackParser.parse(TestAssets.read("free_pack.json"))

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
