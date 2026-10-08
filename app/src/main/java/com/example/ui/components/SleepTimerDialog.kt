package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.PlaybackState
import com.example.ui.theme.AppleDarkCard
import com.example.ui.theme.AppleGlassBorder
import com.example.ui.theme.UtkarshPink
import com.example.ui.theme.UtkarshRed
import com.example.util.rememberHapticFeedback

@Composable
fun SleepTimerDialog(
    playbackState: PlaybackState,
    onSetTimerMinutes: (Int) -> Unit,
    onSetEndOfTrack: () -> Unit,
    onCancelTimer: () -> Unit,
    onDismiss: () -> Unit
) {
    val haptics = rememberHapticFeedback()
    val isTimerActive = playbackState.isSleepTimerActive
    val remainingSec = playbackState.sleepTimerRemainingSeconds ?: 0

    val remainingFormatted = if (playbackState.sleepTimerEndOfTrack) {
        val min = remainingSec / 60
        val sec = remainingSec % 60
        "End of Song (~%d:%02d remaining)".format(min, sec)
    } else {
        val min = remainingSec / 60
        val sec = remainingSec % 60
        "%d min %02d sec remaining".format(min, sec)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = AppleDarkCard.copy(alpha = 0.95f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .border(1.dp, AppleGlassBorder, RoundedCornerShape(26.dp))
                .shadow(28.dp, RoundedCornerShape(26.dp))
                .testTag("sleep_timer_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(UtkarshRed.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = UtkarshRed,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Sleep Timer",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                ),
                                color = Color.White
                            )
                            Text(
                                text = "Fades out volume smoothly before stop",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White.copy(alpha = 0.65f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Active Countdown Banner
                if (isTimerActive) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(UtkarshRed.copy(alpha = 0.15f))
                            .border(1.dp, UtkarshRed.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.VolumeDown,
                                    contentDescription = null,
                                    tint = UtkarshPink,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = remainingFormatted,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        ),
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Gradual fade-out active",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = UtkarshPink
                                    )
                                }
                            }

                            TextButton(
                                onClick = {
                                    haptics.toggleMode()
                                    onCancelTimer()
                                }
                            ) {
                                Text(
                                    text = "Cancel",
                                    color = UtkarshRed,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Options list
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                        .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                ) {
                    SleepTimerRow(
                        title = "15 Minutes",
                        subtitle = "Fade out after 15 min",
                        onClick = {
                            haptics.toggleMode()
                            onSetTimerMinutes(15)
                            onDismiss()
                        }
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f))

                    SleepTimerRow(
                        title = "30 Minutes",
                        subtitle = "Fade out after 30 min",
                        onClick = {
                            haptics.toggleMode()
                            onSetTimerMinutes(30)
                            onDismiss()
                        }
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f))

                    SleepTimerRow(
                        title = "45 Minutes",
                        subtitle = "Fade out after 45 min",
                        onClick = {
                            haptics.toggleMode()
                            onSetTimerMinutes(45)
                            onDismiss()
                        }
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f))

                    SleepTimerRow(
                        title = "1 Hour",
                        subtitle = "Fade out after 60 min",
                        onClick = {
                            haptics.toggleMode()
                            onSetTimerMinutes(60)
                            onDismiss()
                        }
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f))

                    SleepTimerRow(
                        title = "When Current Song Ends",
                        subtitle = "Stops gently after current track",
                        onClick = {
                            haptics.toggleMode()
                            onSetEndOfTrack()
                            onDismiss()
                        }
                    )
                }

                if (isTimerActive) {
                    Spacer(modifier = Modifier.height(14.dp))
                    TextButton(
                        onClick = {
                            haptics.toggleMode()
                            onCancelTimer()
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Turn Off Timer",
                            color = UtkarshRed,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SleepTimerRow(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 13.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                ),
                color = Color.White
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = Color.White.copy(alpha = 0.5f)
            )
        }

        Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.35f),
            modifier = Modifier.size(16.dp)
        )
    }
}
