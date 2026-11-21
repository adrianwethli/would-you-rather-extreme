package com.wouldyouratherextreme.game

import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.wouldyouratherextreme.game.data.QuestionRepository
import com.wouldyouratherextreme.game.ui.WouldYouRatherExtremeApp
import com.wouldyouratherextreme.game.ui.theme.WouldYouRatherExtremeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // The app is always dark, so keep the status/navigation bar icons light.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT),
        )
        // The questions are stored inside the app (assets/questions), so the game works offline.
        val library = QuestionRepository(assets).loadLibrary()
        setContent {
            WouldYouRatherExtremeTheme {
                WouldYouRatherExtremeApp(
                    library = library,
                    // The simulated payment exists only in test (debug) builds, never in a public release.
                    allowTestUnlock = BuildConfig.DEBUG,
                    onExit = { finish() },
                )
            }
        }
    }
}
