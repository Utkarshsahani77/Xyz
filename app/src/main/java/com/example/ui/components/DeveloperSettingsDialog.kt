package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.DeveloperMode
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.PlaybackState
import com.example.model.RepeatMode
import com.example.ui.theme.AppleDarkCard
import com.example.ui.theme.AppleDarkSecondaryCard
import com.example.ui.theme.UtkarshPink
import com.example.ui.theme.UtkarshRed
import com.example.util.HapticFeedbackHelper
import com.example.util.PlayStationSoundPlayer
import com.example.util.rememberHapticFeedback

@Composable
fun DeveloperSettingsDialog(
    playbackState: PlaybackState,
    isOfflineOnly: Boolean,
    onToggleOfflineOnly: () -> Unit,
    onToggleAutoplay: () -> Unit,
    onToggleShuffle: () -> Unit,
    onCycleRepeat: () -> Unit,
    onClearQueue: () -> Unit,
    onSetSleepTimerMinutes: (Int) -> Unit,
    onCancelSleepTimer: () -> Unit,
    onToggleAmbientPlayback: () -> Unit,
    onAmbientVolumeChange: (Float) -> Unit,
    onScanDeviceAudio: () -> Unit,
    onDismiss: () -> Unit
) {
    val haptics = rememberHapticFeedback()

    // Local mutable state for modular app settings toggles
    var premiumEnabled by remember { mutableStateOf(true) }
    var hapticsEnabled by remember { mutableStateOf(HapticFeedbackHelper.isHapticsGloballyEnabled) }
    var losslessEnabled by remember { mutableStateOf(true) }
    var spatialAudioEnabled by remember { mutableStateOf(true) }
    var crossfadeEnabled by remember { mutableStateOf(true) }
    var crossfadeSeconds by remember { mutableFloatStateOf(4f) }
    var soundCheckEnabled by remember { mutableStateOf(true) }
    var highBitrateEnabled by remember { mutableStateOf(true) }
    var fluidBacklightEnabled by remember { mutableStateOf(true) }
    var syncedLyricsEnabled by remember { mutableStateOf(true) }
    var hindiPreferenceEnabled by remember { mutableStateOf(true) }
    var waveformAnimationEnabled by remember { mutableStateOf(true) }
    var videoSimulationEnabled by remember { mutableStateOf(false) }
    var debugLogToastEnabled by remember { mutableStateOf(false) }
    var ps5SoundEnabled by remember { mutableStateOf(PlayStationSoundPlayer.isSoundEnabled) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(24.dp))
                .testTag("developer_settings_dialog"),
            color = AppleDarkCard,
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.04f))
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(UtkarshRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeveloperMode,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Developer Options",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 20.sp
                                    ),
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(UtkarshPink.copy(alpha = 0.25f))
                                        .border(0.5.dp, UtkarshPink, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "UTKARSH",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 9.sp
                                        ),
                                        color = UtkarshPink
                                    )
                                }
                            }
                            Text(
                                text = "Developer: UTKARSH • Master Control System",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

                // Scrollable Settings Content
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 18.dp),
                    contentPadding = PaddingValues(vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 0. PREMIUM STATUS MASTER CARD WITH ICON
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (premiumEnabled) Color(0xFF241619) else Color.White.copy(alpha = 0.05f)
                            ),
                            border = BorderStroke(
                                1.5.dp,
                                if (premiumEnabled) {
                                    androidx.compose.ui.graphics.Brush.horizontalGradient(
                                        listOf(Color(0xFFFFD700), UtkarshRed, UtkarshPink)
                                    )
                                } else {
                                    androidx.compose.ui.graphics.Brush.horizontalGradient(
                                        listOf(Color.White.copy(alpha = 0.15f), Color.White.copy(alpha = 0.08f))
                                    )
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("dev_premium_status_card")
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        // Premium Enabled Icon in Glowing Badge
                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (premiumEnabled) {
                                                        androidx.compose.ui.graphics.Brush.linearGradient(
                                                            listOf(Color(0xFFFFD700), Color(0xFFFF4D4D), UtkarshRed)
                                                        )
                                                    } else {
                                                        androidx.compose.ui.graphics.Brush.linearGradient(
                                                            listOf(Color.Gray, Color.DarkGray)
                                                        )
                                                    }
                                                )
                                                .border(1.5.dp, if (premiumEnabled) Color(0xFFFFF099) else Color.Transparent, CircleShape)
                                                .testTag("dev_premium_icon"),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.WorkspacePremium,
                                                contentDescription = "Premium Enabled Icon",
                                                tint = Color.White,
                                                modifier = Modifier.size(26.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "Utkarsh Music Premium",
                                                    style = MaterialTheme.typography.titleMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 16.sp
                                                    ),
                                                    color = Color.White
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                if (premiumEnabled) {
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(4.dp))
                                                            .background(Color(0xFF22C55E).copy(alpha = 0.2f))
                                                            .border(0.5.dp, Color(0xFF22C55E), RoundedCornerShape(4.dp))
                                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                                    ) {
                                                        Row(
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.CheckCircle,
                                                                contentDescription = null,
                                                                tint = Color(0xFF22C55E),
                                                                modifier = Modifier.size(10.dp)
                                                            )
                                                            Text(
                                                                text = "PREMIUM ENABLED",
                                                                style = MaterialTheme.typography.labelSmall.copy(
                                                                    fontWeight = FontWeight.ExtraBold,
                                                                    fontSize = 9.sp,
                                                                    letterSpacing = 0.5.sp
                                                                ),
                                                                color = Color(0xFF22C55E)
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                            Text(
                                                text = if (premiumEnabled) 
                                                    "Active Lifetime Plan • All VIP streaming, offline vault & lossless master unlocked" 
                                                else 
                                                    "Free Tier Simulated (Standard 16-bit, offline vault locked)",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.White.copy(alpha = 0.7f)
                                            )
                                        }
                                    }

                                    Switch(
                                        checked = premiumEnabled,
                                        onCheckedChange = {
                                            haptics.toggleMode()
                                            premiumEnabled = it
                                            if (it) {
                                                losslessEnabled = true
                                                spatialAudioEnabled = true
                                                highBitrateEnabled = true
                                            }
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = UtkarshRed
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Quick Action Master Row
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.06f)),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "MASTER QUICK ACTIONS",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = Color.White.copy(alpha = 0.5f)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            haptics.toggleMode()
                                            losslessEnabled = true
                                            spatialAudioEnabled = true
                                            crossfadeEnabled = true
                                            soundCheckEnabled = true
                                            highBitrateEnabled = true
                                            fluidBacklightEnabled = true
                                            syncedLyricsEnabled = true
                                            hindiPreferenceEnabled = true
                                            waveformAnimationEnabled = true
                                            HapticFeedbackHelper.isHapticsGloballyEnabled = true
                                            hapticsEnabled = true
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = UtkarshRed)
                                    ) {
                                        Text("Enable All", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }

                                    Button(
                                        onClick = {
                                            haptics.toggleMode()
                                            losslessEnabled = false
                                            spatialAudioEnabled = false
                                            crossfadeEnabled = false
                                            soundCheckEnabled = false
                                            highBitrateEnabled = false
                                            fluidBacklightEnabled = false
                                            syncedLyricsEnabled = false
                                            hindiPreferenceEnabled = false
                                            waveformAnimationEnabled = false
                                            HapticFeedbackHelper.isHapticsGloballyEnabled = false
                                            hapticsEnabled = false
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.15f))
                                    ) {
                                        Text("Disable All", fontSize = 12.sp, color = Color.White)
                                    }

                                    Button(
                                        onClick = {
                                            haptics.toggleMode()
                                            losslessEnabled = true
                                            spatialAudioEnabled = true
                                            crossfadeEnabled = true
                                            crossfadeSeconds = 4f
                                            soundCheckEnabled = true
                                            highBitrateEnabled = true
                                            fluidBacklightEnabled = true
                                            syncedLyricsEnabled = true
                                            hindiPreferenceEnabled = true
                                            waveformAnimationEnabled = true
                                            HapticFeedbackHelper.isHapticsGloballyEnabled = true
                                            hapticsEnabled = true
                                            videoSimulationEnabled = false
                                            debugLogToastEnabled = false
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.08f))
                                    ) {
                                        Text("Reset", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                                    }
                                }
                            }
                        }
                    }

                    // 1. PLAYBACK & QUEUE SETTINGS
                    item {
                        DevSectionHeader("1. PLAYBACK & QUEUE CONTROLS")
                    }

                    item {
                        DevSettingItem(
                            icon = Icons.Default.AllInclusive,
                            title = "Autoplay System",
                            subtitle = "Automatically queue similar Hindi & World tracks when queue finishes",
                            checked = playbackState.isAutoplayEnabled,
                            onCheckedChange = {
                                haptics.toggleMode()
                                onToggleAutoplay()
                            },
                            badgeText = if (playbackState.isAutoplayEnabled) "ACTIVE" else "OFF",
                            badgeColor = if (playbackState.isAutoplayEnabled) UtkarshRed else Color.White.copy(alpha = 0.2f)
                        )
                    }

                    item {
                        DevSettingItem(
                            icon = Icons.Default.Vibration,
                            title = "Tactile Haptic Feedback",
                            subtitle = "Hardware vibration ticks on play/pause, scrub, track skip & toggles",
                            checked = hapticsEnabled,
                            onCheckedChange = {
                                hapticsEnabled = it
                                HapticFeedbackHelper.isHapticsGloballyEnabled = it
                                if (it) haptics.toggleMode()
                            },
                            badgeText = if (hapticsEnabled) "ON" else "OFF",
                            badgeColor = if (hapticsEnabled) UtkarshPink else Color.White.copy(alpha = 0.2f)
                        )
                    }

                    item {
                        DevSettingItem(
                            icon = Icons.Default.Shuffle,
                            title = "Shuffle Queue",
                            subtitle = "Randomize upcoming song play order",
                            checked = playbackState.isShuffled,
                            onCheckedChange = {
                                haptics.toggleMode()
                                onToggleShuffle()
                            },
                            badgeText = if (playbackState.isShuffled) "ON" else "OFF"
                        )
                    }

                    item {
                        DevSettingItem(
                            icon = Icons.Default.Repeat,
                            title = "Repeat Playback Mode",
                            subtitle = "Current: ${playbackState.repeatMode.name} (Tap toggle to cycle OFF -> ALL -> ONE)",
                            checked = playbackState.repeatMode != RepeatMode.OFF,
                            onCheckedChange = {
                                haptics.toggleMode()
                                onCycleRepeat()
                            },
                            badgeText = playbackState.repeatMode.name
                        )
                    }

                    item {
                        DevSettingItem(
                            icon = Icons.Default.AccessTime,
                            title = "Sleep Timer (Gradual Fade-Out)",
                            subtitle = if (playbackState.isSleepTimerActive) {
                                val rem = playbackState.sleepTimerRemainingSeconds ?: 0
                                "Active: ${rem / 60}m ${rem % 60}s remaining • Fading volume"
                            } else {
                                "Timed music pause with gradual volume fade-out"
                            },
                            checked = playbackState.isSleepTimerActive,
                            onCheckedChange = {
                                haptics.toggleMode()
                                if (it) onSetSleepTimerMinutes(30) else onCancelSleepTimer()
                            },
                            badgeText = if (playbackState.isSleepTimerActive) "ACTIVE" else "OFF",
                            badgeColor = if (playbackState.isSleepTimerActive) UtkarshRed else Color.White.copy(alpha = 0.2f)
                        )
                    }

                    item {
                        DevSettingItem(
                            icon = Icons.Default.Spa,
                            title = "Ambient Soundscape Layer",
                            subtitle = "Background natural sounds (Rain, Temple Bells, Om, Flute) blended with audio",
                            checked = playbackState.isAmbientPlaybackEnabled,
                            onCheckedChange = {
                                haptics.toggleMode()
                                onToggleAmbientPlayback()
                            },
                            badgeText = if (playbackState.isAmbientPlaybackEnabled) "PLAYING" else "OFF",
                            badgeColor = if (playbackState.isAmbientPlaybackEnabled) UtkarshPink else Color.White.copy(alpha = 0.2f)
                        )
                    }

                    // 2. AUDIO ENGINE & QUALITY SETTINGS
                    item {
                        DevSectionHeader("2. AUDIO ENGINE & QUALITY")
                    }

                    item {
                        DevSettingItem(
                            icon = Icons.Default.Speed,
                            title = "Hi-Res Lossless Audio",
                            subtitle = "24-bit / 48kHz ALAC Master Quality streaming decoding",
                            checked = losslessEnabled,
                            onCheckedChange = {
                                haptics.toggleMode()
                                losslessEnabled = it
                            },
                            badgeText = if (losslessEnabled) "ALAC" else "AAC"
                        )
                    }

                    item {
                        DevSettingItem(
                            icon = Icons.Default.Tune,
                            title = "Spatial Audio with Dolby Atmos",
                            subtitle = "3D multi-dimensional acoustic soundstage modeling",
                            checked = spatialAudioEnabled,
                            onCheckedChange = {
                                haptics.toggleMode()
                                spatialAudioEnabled = it
                            },
                            badgeText = if (spatialAudioEnabled) "SPATIAL" else "STEREO"
                        )
                    }

                    item {
                        DevSettingItem(
                            icon = Icons.Default.MusicNote,
                            title = "Crossfade Track Transitions",
                            subtitle = "Smooth ${crossfadeSeconds.toInt()}s blend between consecutive tracks",
                            checked = crossfadeEnabled,
                            onCheckedChange = {
                                haptics.toggleMode()
                                crossfadeEnabled = it
                            },
                            badgeText = if (crossfadeEnabled) "${crossfadeSeconds.toInt()}s" else "OFF"
                        )
                    }

                    if (crossfadeEnabled) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 48.dp, end = 8.dp)
                            ) {
                                Text(
                                    text = "Crossfade Duration: ${crossfadeSeconds.toInt()} seconds",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                                Slider(
                                    value = crossfadeSeconds,
                                    onValueChange = { crossfadeSeconds = it },
                                    valueRange = 1f..12f,
                                    steps = 10,
                                    colors = SliderDefaults.colors(
                                        thumbColor = UtkarshRed,
                                        activeTrackColor = UtkarshRed
                                    )
                                )
                            }
                        }
                    }

                    item {
                        DevSettingItem(
                            icon = Icons.Default.VolumeUp,
                            title = "Sound Check (Volume Normalization)",
                            subtitle = "Balances volume differences between quiet and loud recordings",
                            checked = soundCheckEnabled,
                            onCheckedChange = {
                                haptics.toggleMode()
                                soundCheckEnabled = it
                            }
                        )
                    }

                    item {
                        DevSettingItem(
                            icon = Icons.Default.Speed,
                            title = "Force High Bitrate (320 kbps)",
                            subtitle = "Bypass automatic bandwidth adaptation for ultra-clean audio",
                            checked = highBitrateEnabled,
                            onCheckedChange = {
                                haptics.toggleMode()
                                highBitrateEnabled = it
                            }
                        )
                    }

                    // SONY PLAYSTATION 5 OPENING SOUND CONTROLLER
                    item {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0A1428)),
                            border = BorderStroke(1.dp, Color(0xFF00439C)),
                            modifier = Modifier.fillMaxWidth().testTag("ps5_opening_sound_card")
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(38.dp)
                                                .clip(CircleShape)
                                                .background(if (ps5SoundEnabled) Color(0xFF00439C) else Color.White.copy(alpha = 0.08f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.VolumeUp,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "PlayStation 5 Opening Sound",
                                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                                    color = Color.White
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(Color(0xFF00439C))
                                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                                ) {
                                                    Text(
                                                        text = "SONY PS5",
                                                        style = MaterialTheme.typography.labelSmall.copy(
                                                            fontWeight = FontWeight.ExtraBold,
                                                            fontSize = 9.sp
                                                        ),
                                                        color = Color.White
                                                    )
                                                }
                                            }
                                            Text(
                                                text = "Iconic crystal chime & ambient orchestral bloom on app startup",
                                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                                                color = Color.White.copy(alpha = 0.65f)
                                            )
                                        }
                                    }

                                    Switch(
                                        checked = ps5SoundEnabled,
                                        onCheckedChange = {
                                            haptics.toggleMode()
                                            ps5SoundEnabled = it
                                            PlayStationSoundPlayer.isSoundEnabled = it
                                            if (it) {
                                                PlayStationSoundPlayer.playPS5StartupSound()
                                            }
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = Color(0xFF0070D1)
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Button(
                                    onClick = {
                                        haptics.toggleMode()
                                        PlayStationSoundPlayer.playPS5StartupSound()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(38.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00439C))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        "Play Sony PS5 Startup Chime & Swell",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }

                    // 3. UI, LYRICS & VISUAL ENGINE
                    item {
                        DevSectionHeader("3. UI & VISUAL ENGINE")
                    }

                    item {
                        DevSettingItem(
                            icon = Icons.Default.Translate,
                            title = "Prioritize Hindi Lyrics & Titles",
                            subtitle = "Real-time synchronized Hindi lyrics in Devanagari script",
                            checked = hindiPreferenceEnabled,
                            onCheckedChange = {
                                haptics.toggleMode()
                                hindiPreferenceEnabled = it
                            },
                            badgeText = if (hindiPreferenceEnabled) "HINDI" else "STANDARD"
                        )
                    }

                    item {
                        DevSettingItem(
                            icon = Icons.Default.GraphicEq,
                            title = "Synchronized Lyrics Engine",
                            subtitle = "Time-coded line highlighting with smooth auto-scroll",
                            checked = syncedLyricsEnabled,
                            onCheckedChange = {
                                haptics.toggleMode()
                                syncedLyricsEnabled = it
                            }
                        )
                    }

                    item {
                        DevSettingItem(
                            icon = Icons.Default.Tune,
                            title = "Fluid Ambient Artwork Glow",
                            subtitle = "Dynamic backlights and translucent backdrop color blending",
                            checked = fluidBacklightEnabled,
                            onCheckedChange = {
                                haptics.toggleMode()
                                fluidBacklightEnabled = it
                            }
                        )
                    }

                    item {
                        DevSettingItem(
                            icon = Icons.Default.GraphicEq,
                            title = "Equalizer Waveform Animations",
                            subtitle = "Animated sound bars in MiniPlayer, now playing bar & queue",
                            checked = waveformAnimationEnabled,
                            onCheckedChange = {
                                haptics.toggleMode()
                                waveformAnimationEnabled = it
                            }
                        )
                    }

                    item {
                        DevSettingItem(
                            icon = Icons.Default.Videocam,
                            title = "1080p Music Video Simulator",
                            subtitle = "Simulate video stream layer switch in Now Playing player",
                            checked = videoSimulationEnabled,
                            onCheckedChange = {
                                haptics.toggleMode()
                                videoSimulationEnabled = it
                            },
                            badgeText = if (videoSimulationEnabled) "1080p" else "AUDIO"
                        )
                    }

                    // 4. STORAGE & OFFLINE ENGINE
                    item {
                        DevSectionHeader("4. STORAGE & OFFLINE ENGINE")
                    }

                    item {
                        DevSettingItem(
                            icon = Icons.Default.CloudOff,
                            title = "Offline-Only Mode",
                            subtitle = "Restrict playback strictly to downloaded & device audio",
                            checked = isOfflineOnly,
                            onCheckedChange = {
                                haptics.toggleMode()
                                onToggleOfflineOnly()
                            },
                            badgeText = if (isOfflineOnly) "OFFLINE" else "ONLINE",
                            badgeColor = if (isOfflineOnly) UtkarshRed else Color.White.copy(alpha = 0.2f)
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.06f))
                                .clickable {
                                    haptics.toggleMode()
                                    onScanDeviceAudio()
                                }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.FolderOpen,
                                contentDescription = null,
                                tint = UtkarshRed,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Scan Local Device Storage",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                                Text(
                                    text = "Inspect MediaStore for local audio files and songs",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                            }
                            Button(
                                onClick = {
                                    haptics.toggleMode()
                                    onScanDeviceAudio()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = UtkarshRed),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Scan", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.06f))
                                .clickable {
                                    haptics.toggleMode()
                                    onClearQueue()
                                }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Clear Upcoming Queue",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                                Text(
                                    text = "Remove all upcoming tracks except current song",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                            }
                            Button(
                                onClick = {
                                    haptics.toggleMode()
                                    onClearQueue()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.15f)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Clear", fontSize = 11.sp, color = Color.White)
                            }
                        }
                    }

                    // 5. LIVE ENGINE DIAGNOSTICS & TELEMETRY
                    item {
                        DevSectionHeader("5. LIVE ENGINE DIAGNOSTICS")
                    }

                    item {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.45f)),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Current Song",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White.copy(alpha = 0.5f)
                                    )
                                    Text(
                                        text = playbackState.currentSong?.title ?: "None",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Playing State",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White.copy(alpha = 0.5f)
                                    )
                                    Text(
                                        text = if (playbackState.isPlaying) "PLAYING" else "PAUSED",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (playbackState.isPlaying) Color.Green else Color.Yellow
                                        )
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Queue Size",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White.copy(alpha = 0.5f)
                                    )
                                    Text(
                                        text = "${playbackState.queue.size} songs (Index: ${playbackState.queueIndex})",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Position / Duration",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White.copy(alpha = 0.5f)
                                    )
                                    Text(
                                        text = "${playbackState.currentPositionMs / 1000}s / ${playbackState.durationMs / 1000}s",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Engine Target",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White.copy(alpha = 0.5f)
                                    )
                                    Text(
                                        text = "Android MediaPlayer + Ambient Loop Engine",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = UtkarshPink
                                    )
                                }
                            }
                        }
                    }
                }

                // Bottom Done Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.04f))
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UtkarshRed),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Apply & Close Developer Options", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun DevSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.ExtraBold,
            fontSize = 11.sp,
            letterSpacing = 1.2.sp
        ),
        color = Color.White.copy(alpha = 0.5f),
        modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
    )
}

@Composable
private fun DevSettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    badgeText: String? = null,
    badgeColor: Color = UtkarshRed
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(if (checked) badgeColor.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (checked) Color.White else Color.White.copy(alpha = 0.45f),
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = Color.White
                    )
                    if (badgeText != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(badgeColor.copy(alpha = 0.25f))
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = badgeText,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                ),
                                color = Color.White
                            )
                        }
                    }
                }
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = Color.White.copy(alpha = 0.55f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = UtkarshRed,
                uncheckedThumbColor = Color.White.copy(alpha = 0.6f),
                uncheckedTrackColor = Color.White.copy(alpha = 0.15f)
            )
        )
    }
}
