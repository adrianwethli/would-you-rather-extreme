package com.wouldyouratherextreme.game.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class QuestionLibraryTest {

    private fun question(id: String) = Question(id = id, optionA = "A $id", optionB = "B $id")

    private val free = listOf(question("f1"), question("f2"))
    private val premium = listOf(question("p1"), question("p2"), question("p3"))

    @Test
    fun withoutPremiumOnlyTheFreeQuestionsAreAvailable() {
        val library = QuestionLibrary(free, premium)
        assertEquals(listOf("f1", "f2"), library.playable(premiumUnlocked = false).map { it.id })
    }

    @Test
    fun withPremiumAllQuestionsAreAvailable() {
        val library = QuestionLibrary(free, premium)
        assertEquals(
            listOf("f1", "f2", "p1", "p2", "p3"),
            library.playable(premiumUnlocked = true).map { it.id },
        )
    }

    @Test
    fun aLibraryWithoutPremiumQuestionsIsAllowed() {
        val library = QuestionLibrary(free, emptyList())
        assertEquals(2, library.playable(premiumUnlocked = true).size)
    }

    @Test(expected = IllegalArgumentException::class)
    fun duplicateIdsBetweenFreeAndPremiumAreRejected() {
        QuestionLibrary(free, listOf(question("f1")))
    }

    @Test(expected = IllegalArgumentException::class)
    fun aLibraryWithoutFreeQuestionsIsRejected() {
        QuestionLibrary(emptyList(), premium)
    }
}
