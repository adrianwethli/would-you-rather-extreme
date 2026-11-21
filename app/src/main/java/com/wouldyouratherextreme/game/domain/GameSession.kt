package com.wouldyouratherextreme.game.domain

import kotlin.random.Random

/**
 * The state of one round on the shared phone: the shuffled list of questions,
 * which one is showing, which answer the group has picked, and whether the round is over.
 * It is a plain Kotlin class with no Android code, so it is easy to test.
 */
data class GameSession(
    val questions: List<Question>,
    val index: Int = 0,
    val choice: Choice? = null,
    /** True once the group has gone through the last question. */
    val finished: Boolean = false,
) {
    init {
        require(questions.isNotEmpty()) { "A game needs at least one question." }
        require(index in questions.indices) { "Question index $index is out of range." }
    }

    val current: Question get() = questions[index]

    /** Human-friendly position, starting at 1. */
    val number: Int get() = index + 1

    val total: Int get() = questions.size

    val isLast: Boolean get() = index == questions.lastIndex

    /** Records the group's pick. Picking again simply replaces the earlier pick. */
    fun choose(choice: Choice): GameSession = copy(choice = choice)

    /** Moves to the next question and clears the pick. After the last question the round is finished. */
    fun next(): GameSession =
        if (isLast) copy(choice = null, finished = true)
        else copy(index = index + 1, choice = null)

    /** Starts a new round with the same questions in a new random order. */
    fun reshuffled(random: Random = Random.Default): GameSession {
        val fresh = shuffleAvoidingStart(questions, avoidFirst = questions.last(), random = random)
        return GameSession(fresh)
    }

    companion object {
        /** A brand-new round: all questions from [pool] in a random order. */
        fun shuffled(pool: List<Question>, random: Random = Random.Default): GameSession =
            GameSession(pool.shuffled(random))

        /**
         * Shuffles [pool], but makes sure the round does not start with [avoidFirst]
         * (the question the group just finished on), so the same question never shows twice in a row.
         */
        private fun shuffleAvoidingStart(
            pool: List<Question>,
            avoidFirst: Question,
            random: Random,
        ): List<Question> {
            val shuffled = pool.shuffled(random)
            return if (shuffled.size > 1 && shuffled.first() == avoidFirst) {
                shuffled.drop(1) + shuffled.first()
            } else {
                shuffled
            }
        }
    }
}
