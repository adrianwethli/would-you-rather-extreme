package com.wouldyouratherextreme.game.domain

/**
 * All questions the app ships with, split into the free ones and the premium ones.
 * Premium questions are only part of the game once premium is unlocked.
 */
data class QuestionLibrary(
    val free: List<Question>,
    val premium: List<Question>,
) {
    init {
        require(free.isNotEmpty()) { "The library needs at least one free question." }
        val duplicates = (free + premium).groupBy { it.id }.filterValues { it.size > 1 }.keys
        require(duplicates.isEmpty()) { "Duplicate question ids across packs: ${duplicates.joinToString()}." }
    }

    /** The questions the players can get in a round right now. */
    fun playable(premiumUnlocked: Boolean): List<Question> =
        if (premiumUnlocked) free + premium else free
}
