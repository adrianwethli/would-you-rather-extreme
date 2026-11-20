package com.wouldyouratherextreme.game.ui.game

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wouldyouratherextreme.game.data.SampleQuestions
import com.wouldyouratherextreme.game.domain.Choice
import com.wouldyouratherextreme.game.domain.GameSession
import com.wouldyouratherextreme.game.ui.theme.WouldYouRatherExtremeTheme

@Composable
fun GameScreen(
    session: GameSession,
    onChoose: (Choice) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TextButton(onClick = onBack) {
                Text(text = "← Home", style = MaterialTheme.typography.titleMedium)
            }
            Text(
                text = "${session.number} / ${session.total}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        // The animation only runs when the question itself changes, not when a pick is made.
        AnimatedContent(
            targetState = session.current,
            modifier = Modifier.weight(1f),
            transitionSpec = {
                (fadeIn(tween(300)) + slideInHorizontally(tween(300)) { it / 6 }) togetherWith
                    fadeOut(tween(150))
            },
            label = "question",
        ) { question ->
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Would you rather…",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(vertical = 12.dp),
                )
                AnswerCard(
                    text = question.optionA,
                    isSelected = session.choice == Choice.A,
                    onClick = { onChoose(Choice.A) },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                )
                Text(
                    text = "OR",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                )
                AnswerCard(
                    text = question.optionB,
                    isSelected = session.choice == Choice.B,
                    onClick = { onChoose(Choice.B) },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                )
            }
        }

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(64.dp),
            shape = RoundedCornerShape(32.dp),
        ) {
            Text(
                text = "Next question",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

/**
 * A large, rounded answer card. A picked card is shown with a thicker border,
 * a tinted background and a "✓ OUR PICK" label, so it never relies on colour alone.
 */
@Composable
private fun AnswerCard(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) colors.primaryContainer else colors.surface,
        label = "cardColor",
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) colors.primary else colors.outlineVariant,
        label = "borderColor",
    )
    val borderWidth by animateDpAsState(
        targetValue = if (isSelected) 3.dp else 1.dp,
        label = "borderWidth",
    )

    Surface(
        onClick = onClick,
        modifier = modifier.semantics {
            stateDescription = if (isSelected) "Selected" else "Not selected"
        },
        shape = RoundedCornerShape(28.dp),
        color = containerColor,
        border = BorderStroke(borderWidth, borderColor),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (isSelected) {
                Text(
                    text = "✓ OUR PICK",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    color = colors.primary,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GameScreenPreview() {
    WouldYouRatherExtremeTheme {
        GameScreen(
            session = GameSession(SampleQuestions.all, choice = Choice.A),
            onChoose = {},
            onNext = {},
            onBack = {},
        )
    }
}
