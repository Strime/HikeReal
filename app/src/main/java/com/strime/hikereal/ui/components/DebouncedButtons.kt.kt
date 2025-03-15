package com.strime.hikereal.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.strime.hikereal.ui.theme.Dimens.borderRadiusLarge
import com.strime.hikereal.ui.theme.Dimens.iconSizeMedium
import com.strime.hikereal.ui.theme.Dimens.iconSizeSmall
import com.strime.hikereal.ui.theme.Dimens.paddingExtraLarge
import com.strime.hikereal.ui.theme.Dimens.paddingLarge
import kotlinx.coroutines.delay

/**
 * Composable utility class that provides debounced button components to prevent double-clicking
 */
object DebouncedButtons {

    private const val DEFAULT_DEBOUNCE_TIME_MS = 800L

    /**
     * A basic debounced button that prevents multiple rapid clicks
     *
     * @param onClick Function to execute when the button is clicked
     * @param modifier Modifier to be applied to the button
     * @param enabled Whether the button is enabled
     * @param debounceTimeMs The time in milliseconds to debounce clicks (default: 800ms)
     * @param content The content to be displayed inside the button
     */
    @Composable
    fun DebouncedButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        debounceTimeMs: Long = DEFAULT_DEBOUNCE_TIME_MS,
        content: @Composable() (RowScope.() -> Unit)
    ) {
        var isClickable by remember { mutableStateOf(true) }
        val isEnabled = enabled && isClickable

        Button(
            onClick = {
                if (isEnabled) {
                    isClickable = false
                    onClick()
                }
            },
            modifier = modifier,
            enabled = isEnabled,
            content = content
        )

        LaunchedEffect(isClickable) {
            if (!isClickable) {
                delay(debounceTimeMs)
                isClickable = true
            }
        }
    }

    /**
     * A debounced button with custom colors
     *
     * @param onClick Function to execute when the button is clicked
     * @param modifier Modifier to be applied to the button
     * @param enabled Whether the button is enabled
     * @param containerColor The background color of the button
     * @param contentColor The content color of the button
     * @param disabledContainerColor The background color when button is disabled
     * @param disabledContentColor The content color when button is disabled
     * @param debounceTimeMs The time in milliseconds to debounce clicks (default: 800ms)
     * @param content The content to be displayed inside the button
     */
    @Composable
    fun DebouncedColorButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        containerColor: Color = MaterialTheme.colorScheme.primary,
        contentColor: Color = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
        disabledContentColor: Color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
        debounceTimeMs: Long = DEFAULT_DEBOUNCE_TIME_MS,
        content: @Composable() (RowScope.() -> Unit)
    ) {
        var isClickable by remember { mutableStateOf(true) }
        val isEnabled = enabled && isClickable

        Button(
            onClick = {
                if (isEnabled) {
                    isClickable = false
                    onClick()
                }
            },
            modifier = modifier,
            enabled = isEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = disabledContainerColor,
                disabledContentColor = disabledContentColor
            ),
            content = content
        )

        LaunchedEffect(isClickable) {
            if (!isClickable) {
                delay(debounceTimeMs)
                isClickable = true
            }
        }
    }

    /**
     * A full width debounced button with text
     *
     * @param text Text to display in the button
     * @param onClick Function to execute when the button is clicked
     * @param modifier Modifier to be applied to the button
     * @param enabled Whether the button is enabled
     * @param containerColor The background color of the button
     * @param contentColor The content color of the button
     * @param debounceTimeMs The time in milliseconds to debounce clicks (default: 800ms)
     */
    @Composable
    fun DebouncedFullWidthButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        containerColor: Color = MaterialTheme.colorScheme.primary,
        contentColor: Color = MaterialTheme.colorScheme.onPrimary,
        debounceTimeMs: Long = DEFAULT_DEBOUNCE_TIME_MS
    ) {
        DebouncedColorButton(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            enabled = enabled,
            containerColor = containerColor,
            contentColor = contentColor,
            debounceTimeMs = debounceTimeMs
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }

    /**
     * A debounced version of the LiveStartButton with pulse animation
     *
     * @param onClick Function to execute when the button is clicked
     * @param text Text to display in the button
     * @param modifier Modifier to be applied to the button
     * @param enabled Whether the button is enabled
     * @param debounceTimeMs The time in milliseconds to debounce clicks (default: 800ms)
     */
    @Composable
    fun DebouncedPulseButton(
        onClick: () -> Unit,
        text: String,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        debounceTimeMs: Long = DEFAULT_DEBOUNCE_TIME_MS
    ) {
        var isClickable by remember { mutableStateOf(true) }
        val isEnabled = enabled && isClickable

        // Animation for the pulse effect
        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.3f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scaleAnimation"
        )

        Card(
            modifier = modifier
                .shadow(borderRadiusLarge, RoundedCornerShape(borderRadiusLarge))
                .clickable {
                    if (isEnabled) {
                        isClickable = false
                        onClick()
                    }
                },
            shape = RoundedCornerShape(borderRadiusLarge),
            colors = CardDefaults.cardColors(
                containerColor = if (isEnabled)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = paddingExtraLarge, vertical = paddingLarge),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(paddingLarge)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(iconSizeMedium)
                ) {
                    Box(
                        modifier = Modifier
                            .size(iconSizeMedium)
                            .scale(scale)
                            .clip(CircleShape)
                            .background(Color.Red.copy(alpha = 0.3f))
                    )

                    Box(
                        modifier = Modifier
                            .size(iconSizeSmall)
                            .clip(CircleShape)
                            .background(Color.Red)
                    )
                }

                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isEnabled)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                )
            }
        }

        LaunchedEffect(isClickable) {
            if (!isClickable) {
                delay(debounceTimeMs)
                isClickable = true
            }
        }
    }

    /**
     * A debounced card button with custom shape and content
     *
     * @param onClick Function to execute when the button is clicked
     * @param modifier Modifier to be applied to the button
     * @param enabled Whether the button is enabled
     * @param shape The shape of the card button
     * @param containerColor The background color of the button
     * @param debounceTimeMs The time in milliseconds to debounce clicks (default: 800ms)
     * @param content The content to be displayed inside the button
     */
    @Composable
    fun DebouncedCardButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        shape: Shape = RoundedCornerShape(8.dp),
        containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
        debounceTimeMs: Long = DEFAULT_DEBOUNCE_TIME_MS,
        content: @Composable () -> Unit
    ) {
        var isClickable by remember { mutableStateOf(true) }
        val isEnabled = enabled && isClickable

        Card(
            modifier = modifier
                .clickable(enabled = isEnabled) {
                    isClickable = false
                    onClick()
                },
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = if (isEnabled)
                    containerColor
                else
                    containerColor.copy(alpha = 0.6f)
            )
        ) {
            content()
        }

        LaunchedEffect(isClickable) {
            if (!isClickable) {
                delay(debounceTimeMs)
                isClickable = true
            }
        }
    }
}