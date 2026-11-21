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
        val questions = QuestionRepository(assets).loadFreePack().questions
        setContent {
            WouldYouRatherExtremeTheme {
                WouldYouRatherExtremeApp(questions = questions, onExit = { finish() })
            }
        }
    }
}
