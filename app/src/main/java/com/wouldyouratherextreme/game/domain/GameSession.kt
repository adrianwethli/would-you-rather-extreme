package com.wouldyouratherextreme.game.domain

/**
 * The state of one game on the shared phone: which question is showing and
 * which answer the group has picked so far. It is a plain Kotlin class with no
 * Android code, so it is easy to test.
 */
data class GameSession(
    val questions: List<Question>,
    val index: Int = 0,
    val choice: Choice? = null,
) {
    init {
        require(questions.isNotEmpty()) { "A game needs at least one question." }
        require(index in questions.indices) { "Question index $index is out of range." }
    }

    val current: Question get() = questions[index]

    /** Human-friendly position, starting at 1. */
    val number: Int get() = index + 1

    val total: Int get() = questions.size

    /** Records the group's pick. Picking again simply replaces the earlier pick. */
    fun choose(choice: Choice): GameSession = copy(choice = choice)

    /** Moves to the next question and clears the pick. After the last question it starts over. */
    fun next(): GameSession = copy(index = (index + 1) % questions.size, choice = null)
}
