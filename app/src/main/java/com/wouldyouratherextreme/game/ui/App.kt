package com.wouldyouratherextreme.game.ui

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.wouldyouratherextreme.game.data.SampleQuestions
import com.wouldyouratherextreme.game.domain.Choice
import com.wouldyouratherextreme.game.domain.GameSession
import com.wouldyouratherextreme.game.ui.game.GameScreen
import com.wouldyouratherextreme.game.ui.home.HomeScreen
import com.wouldyouratherextreme.game.ui.premium.PremiumScreen

private enum class Screen { HOME, GAME, PREMIUM }

/**
 * Root of the app. With only three screens, a simple switch between them is enough;
 * a proper navigation library can be added later if the app grows.
 */
@Composable
fun WouldYouRatherExtremeApp() {
    var screen by rememberSaveable { mutableStateOf(Screen.HOME) }
    var session by rememberSaveable(stateSaver = GameSessionSaver) {
        mutableStateOf(GameSession(SampleQuestions.all))
    }

    Crossfade(targetState = screen, label = "screen") { current ->
        when (current) {
            Screen.HOME -> HomeScreen(
                onPlay = { screen = Screen.GAME },
                onPremium = { screen = Screen.PREMIUM },
            )

            Screen.GAME -> GameScreen(
                session = session,
                onChoose = { session = session.choose(it) },
                onNext = { session = session.next() },
                onBack = { screen = Screen.HOME },
            )

            Screen.PREMIUM -> PremiumScreen(onBack = { screen = Screen.HOME })
        }
    }
}

/** Keeps the current question and pick when the phone is rotated. */
private val GameSessionSaver = listSaver<GameSession, Int>(
    save = { listOf(it.index, it.choice?.ordinal ?: -1) },
    restore = {
        GameSession(
            questions = SampleQuestions.all,
            index = it[0],
            choice = Choice.entries.getOrNull(it[1]),
        )
    },
)
