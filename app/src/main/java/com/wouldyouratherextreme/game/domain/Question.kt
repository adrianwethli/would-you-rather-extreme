package com.wouldyouratherextreme.game.domain

/** The five question categories shown to players. */
enum class Category(val label: String) {
    FUNNY_CRAZY("Funny & Crazy"),
    EVERYDAY_LIFESTYLE("Everyday & Lifestyle"),
    LOVE_RELATIONSHIPS("Love & Relationships"),
    ADVENTURE_COURAGE("Adventure & Courage"),
    IMPOSSIBLE_DECISIONS("Impossible Decisions"),
}

/** One "Would you rather..." question with its two answers. */
data class Question(
    val id: String,
    val category: Category,
    val optionA: String,
    val optionB: String,
)

/** Which answer the group picked. */
enum class Choice { A, B }
