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

/**
 * Root of the app. With only two screens, a simple "playing or not" switch is enough;
 * a proper navigation library can be added when more screens (like Premium) arrive.
 */
@Composable
fun WouldYouRatherExtremeApp() {
    var playing by rememberSaveable { mutableStateOf(false) }
    var session by rememberSaveable(stateSaver = GameSessionSaver) {
        mutableStateOf(GameSession(SampleQuestions.all))
    }

    Crossfade(targetState = playing, label = "screen") { isPlaying ->
        if (isPlaying) {
            GameScreen(
                session = session,
                onChoose = { session = session.choose(it) },
                onNext = { session = session.next() },
                onBack = { playing = false },
            )
        } else {
            HomeScreen(onPlay = { playing = true })
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
