package com.wouldyouratherextreme.game.data

import com.wouldyouratherextreme.game.domain.Category
import com.wouldyouratherextreme.game.domain.Question

/**
 * Temporary sample questions so the prototype is playable.
 * They will be replaced by the real question packs (JSON files) in a later stage.
 */
object SampleQuestions {
    val all: List<Question> = listOf(
        Question(
            id = "sample-1",
            category = Category.FUNNY_CRAZY,
            optionA = "Narrate everything you do out loud like a sports commentator",
            optionB = "Sing every sentence you say as if you were in a musical",
        ),
        Question(
            id = "sample-2",
            category = Category.EVERYDAY_LIFESTYLE,
            optionA = "Always arrive 30 minutes early",
            optionB = "Always arrive 5 minutes late",
        ),
        Question(
            id = "sample-3",
            category = Category.ADVENTURE_COURAGE,
            optionA = "Jump out of a plane tomorrow",
            optionB = "Spend a month alone in a cabin in the woods without a phone",
        ),
    )
}
