package com.wouldyouratherextreme.game.ui.premium

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wouldyouratherextreme.game.ui.theme.WouldYouRatherExtremeTheme

/**
 * The Premium page.
 *
 * Real purchases (Google Play Billing) come in a later stage. Until then:
 * - in a public build [onTestUnlock] is null and the unlock button is switched off;
 * - in a test build [onTestUnlock] simulates the purchase, without any payment.
 */
@Composable
fun PremiumScreen(
    onBack: () -> Unit,
    premiumUnlocked: Boolean,
    onTestUnlock: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    BackHandler(onBack = onBack)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        TextButton(onClick = onBack) {
            Text(text = "← Home", style = MaterialTheme.typography.titleMedium)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                text = "PREMIUM",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp,
                color = MaterialTheme.colorScheme.secondary,
            )
            Text(
                text = "MORE INSANE QUESTIONS",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
            )
            StoryCard()
            IncludedCard()
        }

        Button(
            onClick = { onTestUnlock?.invoke() },
            enabled = onTestUnlock != null && !premiumUnlocked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(64.dp),
            shape = RoundedCornerShape(32.dp),
        ) {
            Text(
                text = when {
                    premiumUnlocked -> "Premium unlocked ✓"
                    onTestUnlock != null -> "Unlock Premium (test)"
                    else -> "Unlock Premium"
                },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }
        Text(
            text = when {
                premiumUnlocked -> "The extra questions are now in the game. Have fun!"
                onTestUnlock != null -> "Test version: nothing is charged. This button only simulates the purchase."
                else -> "Preview only. Purchases are not switched on yet."
            },
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )
    }
}

@Composable
private fun StoryCard(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "We are a small group of friends with a fucked-up sense of humor. " +
                    "If you buy more questions, you not only continue to have the best debates " +
                    "of your life, you also support us in getting world domination.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "Well, we probably said too much, so ignore that last part and enjoy!",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Composable
private fun IncludedCard(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = "What you get",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            listOf(
                "50 more insane questions",
                "New packs planned for the future",
                "One-time purchase, no subscription",
                "No ads, works fully offline",
            ).forEach { line ->
                Text(
                    text = "✓  $line",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PremiumScreenPreview() {
    WouldYouRatherExtremeTheme {
        PremiumScreen(onBack = {}, premiumUnlocked = false, onTestUnlock = null)
    }
}
