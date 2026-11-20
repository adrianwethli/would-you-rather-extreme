package com.wouldyouratherextreme.game.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GameSessionTest {

    private fun question(id: String) = Question(
        id = id,
        category = Category.FUNNY_CRAZY,
        optionA = "A $id",
        optionB = "B $id",
    )

    private val questions = listOf(question("1"), question("2"), question("3"))

    @Test
    fun startsAtFirstQuestionWithNoChoice() {
        val session = GameSession(questions)
        assertEquals("1", session.current.id)
        assertEquals(1, session.number)
        assertEquals(3, session.total)
        assertNull(session.choice)
    }

    @Test
    fun choosingStoresTheChoice() {
        val session = GameSession(questions).choose(Choice.B)
        assertEquals(Choice.B, session.choice)
    }

    @Test
    fun choosingAgainReplacesTheEarlierChoice() {
        val session = GameSession(questions).choose(Choice.A).choose(Choice.B)
        assertEquals(Choice.B, session.choice)
    }

    @Test
    fun nextMovesForwardAndClearsTheChoice() {
        val session = GameSession(questions).choose(Choice.A).next()
        assertEquals("2", session.current.id)
        assertNull(session.choice)
    }

    @Test
    fun nextStartsOverAfterTheLastQuestion() {
        val session = GameSession(questions, index = 2).next()
        assertEquals("1", session.current.id)
    }

    @Test(expected = IllegalArgumentException::class)
    fun emptyQuestionListIsRejected() {
        GameSession(emptyList())
    }

    @Test(expected = IllegalArgumentException::class)
    fun outOfRangeIndexIsRejected() {
        GameSession(questions, index = 3)
    }
}
