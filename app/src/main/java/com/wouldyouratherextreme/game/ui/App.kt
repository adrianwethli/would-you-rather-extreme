package com.wouldyouratherextreme.game.ui

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.wouldyouratherextreme.game.domain.Choice
import com.wouldyouratherextreme.game.domain.GameSession
import com.wouldyouratherextreme.game.domain.Question
import com.wouldyouratherextreme.game.ui.agegate.AgeGateScreen
import com.wouldyouratherextreme.game.ui.game.FinishedScreen
import com.wouldyouratherextreme.game.ui.game.GameScreen
import com.wouldyouratherextreme.game.ui.home.HomeScreen
import com.wouldyouratherextreme.game.ui.premium.PremiumScreen

/**
 * AGE_GATE and FINISHED are never stored as "the current screen": they are shown automatically
 * (before the age is confirmed, and when the round is over).
 */
private enum class Screen { AGE_GATE, HOME, GAME, PREMIUM, FINISHED }

/**
 * Root of the app. With only a few screens, a simple switch between them is enough;
 * a proper navigation library can be added later if the app grows.
 *
 * [questions] are all the questions the players can currently play (the free pack for now).
 */
@Composable
fun WouldYouRatherExtremeApp(questions: List<Question>, onExit: () -> Unit) {
    // The 18+ warning shows every time the app starts, until it is confirmed.
    // (Remembering the answer between starts comes later, together with saved progress.)
    var ageConfirmed by rememberSaveable { mutableStateOf(false) }
    var screen by rememberSaveable { mutableStateOf(Screen.HOME) }
    // Every new start of the app begins with the questions in a fresh random order.
    val sessionSaver = remember(questions) { gameSessionSaver(questions) }
    var session by rememberSaveable(stateSaver = sessionSaver) {
        mutableStateOf(GameSession.shuffled(questions))
    }

    val shown = when {
        !ageConfirmed -> Screen.AGE_GATE
        screen == Screen.GAME && session.finished -> Screen.FINISHED
        else -> screen
    }

    Crossfade(targetState = shown, label = "screen") { current ->
        when (current) {
            Screen.AGE_GATE -> AgeGateScreen(
                onConfirm = { ageConfirmed = true },
                onLeave = onExit,
            )

            Screen.HOME -> HomeScreen(
                onPlay = {
                    // Coming back to a round that is already over starts a new shuffled round.
                    if (session.finished) session = session.reshuffled()
                    screen = Screen.GAME
                },
                onPremium = { screen = Screen.PREMIUM },
            )

            Screen.GAME -> GameScreen(
                session = session,
                onChoose = { session = session.choose(it) },
                onNext = { session = session.next() },
                onBack = { screen = Screen.HOME },
            )

            Screen.FINISHED -> FinishedScreen(
                total = session.total,
                onReshuffle = { session = session.reshuffled() },
                onPremium = { screen = Screen.PREMIUM },
                onHome = { screen = Screen.HOME },
            )

            Screen.PREMIUM -> PremiumScreen(onBack = { screen = Screen.HOME })
        }
    }
}

// Saved layout: [index, choice (-1 = none), finished (0/1), question ids in the shuffled order...]
private const val SAVED_HEADER_SIZE = 3

/**
 * Keeps the shuffled order, the current question, the pick and the finished state
 * when the phone is rotated. If anything looks wrong, it quietly starts a fresh shuffled round.
 */
private fun gameSessionSaver(pool: List<Question>): Saver<GameSession, Any> = listSaver<GameSession, Any>(
    save = { session ->
        listOf<Any>(
            session.index,
            session.choice?.ordinal ?: -1,
            if (session.finished) 1 else 0,
        ) + session.questions.map { it.id }
    },
    restore = { saved ->
        val byId = pool.associateBy { it.id }
        val ordered = saved.drop(SAVED_HEADER_SIZE).mapNotNull { byId[it as? String] }
        if (ordered.size != pool.size) {
            GameSession.shuffled(pool)
        } else {
            GameSession(
                questions = ordered,
                index = (saved[0] as Int).coerceIn(ordered.indices),
                choice = Choice.entries.getOrNull(saved[1] as Int),
                finished = saved[2] == 1,
            )
        }
    },
)
