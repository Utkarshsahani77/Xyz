package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LyricLine
import com.example.model.Song
import com.example.ui.theme.UtkarshPink
import com.example.ui.theme.UtkarshRed
import com.example.util.rememberHapticFeedback
import kotlinx.coroutines.delay

enum class LyricsDisplayMode {
    DEVANAGARI, // केवल हिंदी (देवनागरी)
    ROMANIZED,  // English / Hinglish
    BOTH        // दोनों (Dual Script)
}

@Composable
fun SyncedLyricsView(
    song: Song,
    currentPositionMs: Long,
    onSeekTo: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptics = rememberHapticFeedback()
    val lyrics = if (song.lyrics.isNotEmpty()) {
        song.lyrics
    } else {
        listOf(
            LyricLine(0L, "♪ (संगीत बज रहा है • Hi-Res Lossless) ♪", "♪ (Music playing in Lossless Quality) ♪"),
            LyricLine(6000L, "उत्कर्ष म्यूज़िक पर लाइव स्ट्रीमिंग", "Streaming live on Utkarsh Music"),
            LyricLine(16000L, "शुद्ध एकॉस्टिक और रूहानी सुर", "Pure acoustics and soulful melodies"),
            LyricLine(26000L, "♪ (इंस्ट्रुमेंटल सोलो) ♪", "♪ (Instrumental Solo) ♪")
        )
    }

    var displayMode by remember { mutableStateOf(LyricsDisplayMode.BOTH) }
    var isUserScrolling by remember { mutableStateOf(false) }

    val activeIndex by remember(lyrics, currentPositionMs) {
        derivedStateOf {
            val idx = lyrics.indexOfLast { currentPositionMs >= it.timeMs }
            if (idx == -1) 0 else idx
        }
    }

    val listState = rememberLazyListState()

    // Smooth scroll to active line whenever active line changes (unless user actively scrolled recently)
    LaunchedEffect(activeIndex, isUserScrolling) {
        if (!isUserScrolling && activeIndex in lyrics.indices) {
            listState.animateScrollToItem(
                index = activeIndex,
                scrollOffset = -180
            )
        }
    }

    // Detect user scroll and pause auto-centering for 4 seconds
    LaunchedEffect(listState.isScrollInProgress) {
        if (listState.isScrollInProgress) {
            isUserScrolling = true
        } else if (isUserScrolling) {
            delay(4000L)
            isUserScrolling = false
        }
    }

    // Ambient glow pulse for active lyric
    val infiniteTransition = rememberInfiniteTransition(label = "lyricGlow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("synced_lyrics_view")
    ) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 72.dp),
            verticalArrangement = Arrangement.spacedBy(26.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(lyrics) { index, lyric ->
                val isActive = index == activeIndex
                val isPast = index < activeIndex
                val isMusicalIntro = lyric.text.startsWith("♪")

                // Real-time line progress calculation
                val lineDurationMs = if (index < lyrics.size - 1) {
                    (lyrics[index + 1].timeMs - lyric.timeMs).coerceAtLeast(1000L)
                } else 6000L

                val lineProgress = if (isActive) {
                    ((currentPositionMs - lyric.timeMs).toFloat() / lineDurationMs.toFloat()).coerceIn(0f, 1f)
                } else if (isPast) 1f else 0f

                val textColor by animateColorAsState(
                    targetValue = when {
                        isActive -> Color.White
                        isPast -> Color.White.copy(alpha = 0.45f)
                        else -> Color.White.copy(alpha = 0.22f)
                    },
                    animationSpec = tween(280),
                    label = "lyricTextColor"
                )

                val lineScale by animateFloatAsState(
                    targetValue = if (isActive) 1.05f else 1.0f,
                    animationSpec = spring(dampingRatio = 0.72f, stiffness = 350f),
                    label = "lyricScale"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(lineScale)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            haptics.scrubTick()
                            isUserScrolling = false
                            onSeekTo(lyric.timeMs)
                        }
                        .padding(vertical = 4.dp)
                ) {
                    if (isMusicalIntro) {
                        // Styled Musical Interlude Badge for Hindi Instrumental / Alaap
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isActive) UtkarshRed.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.05f))
                                .border(
                                    1.dp,
                                    if (isActive) UtkarshRed.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.1f),
                                    RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = if (isActive) UtkarshPink else Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = lyric.text,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 15.sp
                                ),
                                color = if (isActive) Color.White else Color.White.copy(alpha = 0.5f)
                            )
                        }
                    } else {
                        // High-Fidelity Vocal Lyric Line
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            // Active Left Indicator Pill with Real-Time Progress Height
                            if (isActive) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 6.dp, end = 14.dp)
                                        .width(4.dp)
                                        .height(34.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(UtkarshRed, UtkarshPink)
                                            )
                                        )
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                // Devanagari Hindi Text (Primary)
                                if (displayMode == LyricsDisplayMode.DEVANAGARI || displayMode == LyricsDisplayMode.BOTH) {
                                    Box {
                                        // Ambient Soft Glow behind Active Devanagari Text
                                        if (isActive) {
                                            Text(
                                                text = lyric.text,
                                                style = MaterialTheme.typography.headlineLarge.copy(
                                                    fontSize = 28.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    lineHeight = 40.sp,
                                                    letterSpacing = (-0.2).sp
                                                ),
                                                color = UtkarshRed.copy(alpha = 0.35f * glowAlpha),
                                                modifier = Modifier.blur(14.dp)
                                            )
                                        }

                                        Text(
                                            text = lyric.text,
                                            style = MaterialTheme.typography.headlineLarge.copy(
                                                fontSize = if (isActive) 27.sp else 23.sp,
                                                fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Bold,
                                                lineHeight = 38.sp,
                                                letterSpacing = (-0.2).sp
                                            ),
                                            color = textColor
                                        )
                                    }
                                }

                                // Romanized Hindi / Transliteration (Hinglish)
                                if (lyric.romanizedText != null &&
                                    (displayMode == LyricsDisplayMode.ROMANIZED || displayMode == LyricsDisplayMode.BOTH)
                                ) {
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = lyric.romanizedText,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = if (isActive) 16.sp else 14.sp,
                                            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                                            lineHeight = 22.sp
                                        ),
                                        color = if (isActive) UtkarshPink.copy(alpha = 0.95f) else textColor.copy(alpha = 0.45f)
                                    )
                                }

                                // Subtle live line progress hairline under active line
                                if (isActive && lineProgress > 0f) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(2.5.dp)
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.12f))
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(fraction = lineProgress)
                                                .height(2.5.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    Brush.horizontalGradient(
                                                        listOf(UtkarshRed, UtkarshPink)
                                                    )
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(70.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(UtkarshRed)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Hindi Synced Lyrics • Utkarsh Music",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White.copy(alpha = 0.45f)
                    )
                }
            }
        }

        // --- Top Script Switcher Pill: हिंदी | English | Both ---
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 10.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(Color.Black.copy(alpha = 0.65f))
                .border(1.dp, Color.White.copy(alpha = 0.16f), RoundedCornerShape(22.dp))
                .padding(3.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ScriptModePill(
                    label = "हिंदी",
                    isSelected = displayMode == LyricsDisplayMode.DEVANAGARI,
                    onClick = {
                        haptics.toggleMode()
                        displayMode = LyricsDisplayMode.DEVANAGARI
                    }
                )
                ScriptModePill(
                    label = "Hinglish",
                    isSelected = displayMode == LyricsDisplayMode.ROMANIZED,
                    onClick = {
                        haptics.toggleMode()
                        displayMode = LyricsDisplayMode.ROMANIZED
                    }
                )
                ScriptModePill(
                    label = "दोनों (Both)",
                    isSelected = displayMode == LyricsDisplayMode.BOTH,
                    onClick = {
                        haptics.toggleMode()
                        displayMode = LyricsDisplayMode.BOTH
                    }
                )
            }
        }

        // --- Floating "Sync to Vocal" Button when User Scrolled Away ---
        AnimatedVisibility(
            visible = isUserScrolling,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(UtkarshRed)
                    .clickable {
                        haptics.toggleMode()
                        isUserScrolling = false
                    }
                    .padding(horizontal = 18.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Sync to Vocal",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun ScriptModePill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(if (isSelected) UtkarshRed.copy(alpha = 0.85f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                fontSize = 11.sp
            ),
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.65f)
        )
    }
}
