package com.wouldyouratherextreme.game.domain

/** One "Would you rather..." question with its two answers. */
data class Question(
    val id: String,
    val optionA: String,
    val optionB: String,
)

/** Which answer the group picked. */
enum class Choice { A, B }
