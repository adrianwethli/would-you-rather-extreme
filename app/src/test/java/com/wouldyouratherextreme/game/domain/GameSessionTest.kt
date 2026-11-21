package com.wouldyouratherextreme.game.domain

import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GameSessionTest {

    private fun question(id: String) = Question(
        id = id,
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
        assertFalse(session.finished)
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
        assertFalse(session.finished)
    }

    @Test
    fun lastQuestionIsRecognised() {
        assertFalse(GameSession(questions, index = 1).isLast)
        assertTrue(GameSession(questions, index = 2).isLast)
    }

    @Test
    fun nextOnTheLastQuestionFinishesTheRound() {
        val session = GameSession(questions, index = 2).choose(Choice.A).next()
        assertTrue(session.finished)
        assertNull(session.choice)
        assertEquals("3", session.current.id)
    }

    @Test
    fun shuffledContainsEveryQuestionExactlyOnce() {
        val session = GameSession.shuffled(questions, Random(42))
        assertEquals(questions.map { it.id }.sorted(), session.questions.map { it.id }.sorted())
        assertEquals(0, session.index)
        assertFalse(session.finished)
    }

    @Test
    fun differentSeedsCanGiveDifferentOrders() {
        val many = (1..20).map { question("q$it") }
        val orders = (1..5).map { seed ->
            GameSession.shuffled(many, Random(seed)).questions.map { it.id }
        }.toSet()
        assertTrue("Expected more than one distinct order", orders.size > 1)
    }

    @Test
    fun reshuffleStartsAFreshRoundWithTheSameQuestions() {
        val finished = GameSession(questions, index = 2).next()
        val again = finished.reshuffled(Random(1))
        assertFalse(again.finished)
        assertEquals(0, again.index)
        assertNull(again.choice)
        assertEquals(questions.map { it.id }.sorted(), again.questions.map { it.id }.sorted())
    }

    @Test
    fun reshuffleNeverStartsWithTheQuestionJustFinishedOn() {
        // Try many random seeds so the "same question twice in a row" case would show up.
        for (seed in 0..200) {
            val finished = GameSession(questions, index = 2).next()
            val again = finished.reshuffled(Random(seed))
            assertNotEquals("Seed $seed repeated the last question first", "3", again.current.id)
        }
    }

    @Test
    fun reshuffleWorksWithASingleQuestion() {
        val single = GameSession(listOf(question("only")))
        assertEquals("only", single.reshuffled(Random(1)).current.id)
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
