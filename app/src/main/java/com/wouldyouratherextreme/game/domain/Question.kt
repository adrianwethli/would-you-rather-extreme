package com.wouldyouratherextreme.game.domain

/**
 * One question with its two answers.
 *
 * Most questions are plain "Would you rather..." questions. A few start with a short
 * set-up story first (for example a runaway train); that story is [intro].
 */
data class Question(
    val id: String,
    val optionA: String,
    val optionB: String,
    val intro: String? = null,
)

/** A bundle of questions that belong together, for example the free pack or a premium pack. */
data class QuestionPack(
    val id: String,
    val isPremium: Boolean,
    val questions: List<Question>,
)

/** Which answer the group picked. */
enum class Choice { A, B }
