package com.wouldyouratherextreme.game.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Checks the real first premium question file that ships inside the app. */
class PremiumPackFileTest {

    private val pack = QuestionPackParser.parse(TestAssets.read("premium_pack_01.json"))

    @Test
    fun hasExactlyFiftyQuestions() {
        assertEquals(50, pack.questions.size)
    }

    @Test
    fun isMarkedAsPremium() {
        assertEquals("premium-1", pack.id)
        assertTrue(pack.isPremium)
    }

    @Test
    fun idsAreNumberedFromOneToFifty() {
        val expected = (1..50).map { "premium1-%03d".format(it) }
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
