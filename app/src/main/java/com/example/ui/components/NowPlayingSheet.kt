package com.example.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode as ComposeRepeatMode
import androidx.compose.animation.core.Spring
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
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.CuratedMusicData
import com.example.model.PlaybackState
import com.example.model.RepeatMode
import com.example.model.Song
import com.example.ui.theme.AppleDarkSecondaryCard
import com.example.ui.theme.AppleGlassBorder
import com.example.ui.theme.UtkarshPink
import com.example.ui.theme.UtkarshRed
import com.example.util.rememberHapticFeedback

@Composable
fun NowPlayingSheet(
    playbackState: PlaybackState,
    isLyricsMode: Boolean,
    isQueueMode: Boolean,
    onDismiss: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onSeekTo: (Long) -> Unit,
    onFavoriteToggle: (Song) -> Unit,
    onDownloadClick: (Song) -> Unit,
    onAddToPlaylistClick: (Song) -> Unit,
    onToggleLyrics: () -> Unit,
    onToggleQueue: () -> Unit,
    onOpenAirPlay: () -> Unit,
    onVolumeChange: (Float) -> Unit,
    onSongSelectFromQueue: (Song) -> Unit,
    onRemoveFromQueue: (Int) -> Unit,
    onShuffleToggle: () -> Unit,
    onRepeatToggle: () -> Unit,
    onAutoplayToggle: () -> Unit = {},
    onClearQueue: () -> Unit = {},
    onSetSleepTimerMinutes: (Int) -> Unit = {},
    onSetSleepTimerEndOfTrack: () -> Unit = {},
    onCancelSleepTimer: () -> Unit = {},
    onToggleAmbientPlayback: () -> Unit = {},
    onSelectAmbientSound: (String) -> Unit = {},
    onAmbientVolumeChange: (Float) -> Unit = {}
) {
    val song = playbackState.currentSong ?: return
    val haptics = rememberHapticFeedback()
    BackHandler { onDismiss() }

    var isScrubbing by remember { mutableStateOf(false) }
    var scrubProgress by remember { mutableFloatStateOf(0f) }
    var showOptionsMenu by remember { mutableStateOf(false) }
    var showSleepTimerDialog by remember { mutableStateOf(false) }
    var showAmbientDialog by remember { mutableStateOf(false) }

    // YouTube Music Mode Switcher: 0: Main Player, 1: Up Next, 2: Lyrics, 3: Related/Ambient
    var activeBottomTab by remember { mutableIntStateOf(if (isLyricsMode) 2 else if (isQueueMode) 1 else 0) }
    var isVideoMode by remember { mutableStateOf(false) }

    // YouTube Music fluid ambient aura animation
    val infiniteTransition = rememberInfiniteTransition(label = "ytmAmbientGlow")
    val ambientPulse by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = ComposeRepeatMode.Reverse
        ),
        label = "ambientPulse"
    )
    val ambientRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(36000, easing = FastOutSlowInEasing),
            repeatMode = ComposeRepeatMode.Restart
        ),
        label = "ambientRotation"
    )

    // Artwork spring scale on play/pause
    val artworkScale by animateFloatAsState(
        targetValue = if (playbackState.isPlaying) 1.0f else 0.92f,
        animationSpec = spring(
            dampingRatio = 0.68f,
            stiffness = 300f
        ),
        label = "artworkScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("now_playing_sheet")
    ) {
        // --- Fluid YouTube Music Ambient Mesh Canvas ---
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = song.coverUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(1.7f * ambientPulse)
                    .rotate(ambientRotation * 0.15f)
                    .blur(95.dp),
                contentScale = ContentScale.Crop
            )

            // Dynamic dark gradient veil (deep YouTube Music blacks with colored bleed)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xD9060608),
                                Color(0xBF0A0A10),
                                Color(0xF2070709),
                                Color(0xFA050507)
                            )
                        )
                    )
            )

            // Radial fluid glow
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                UtkarshRed.copy(alpha = 0.30f),
                                Color.Transparent
                            ),
                            radius = 750f
                        )
                    )
            )
        }

        // --- Main Player Structure ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp, bottom = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Drag Down Handle
            Box(
                modifier = Modifier
                    .width(38.dp)
                    .height(4.5.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.35f))
                    .clickable { onDismiss() }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // --- YouTube Music Top Header Row ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Down Chevron
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(40.dp).testTag("now_playing_collapse")
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Collapse",
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(30.dp)
                    )
                }

                // YouTube Music Song | Video Switcher Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                        .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(22.dp))
                        .padding(3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(18.dp))
                                .background(if (!isVideoMode) Color.White.copy(alpha = 0.28f) else Color.Transparent)
                                .clickable {
                                    haptics.toggleMode()
                                    isVideoMode = false
                                }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.MusicNote,
                                    contentDescription = null,
                                    tint = if (!isVideoMode) Color.White else Color.White.copy(alpha = 0.6f),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Song",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    ),
                                    color = if (!isVideoMode) Color.White else Color.White.copy(alpha = 0.6f)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(18.dp))
                                .background(if (isVideoMode) UtkarshRed.copy(alpha = 0.4f) else Color.Transparent)
                                .clickable {
                                    haptics.toggleMode()
                                    isVideoMode = true
                                }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Videocam,
                                    contentDescription = null,
                                    tint = if (isVideoMode) Color.White else Color.White.copy(alpha = 0.6f),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Video",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    ),
                                    color = if (isVideoMode) Color.White else Color.White.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }

                // Cast + Options Menu
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onOpenAirPlay,
                        modifier = Modifier.size(38.dp).testTag("airplay_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cast,
                            contentDescription = "Cast Audio",
                            tint = Color.White.copy(alpha = 0.85f),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Box {
                        IconButton(
                            onClick = {
                                haptics.toggleMode()
                                showOptionsMenu = true
                            },
                            modifier = Modifier.size(38.dp).testTag("now_playing_more")
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More Options",
                                tint = Color.White.copy(alpha = 0.85f),
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        // YouTube Music Overflow Menu
                        DropdownMenu(
                            expanded = showOptionsMenu,
                            onDismissRequest = { showOptionsMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(if (song.isFavorite) "Liked Track" else "Add to Liked Songs") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (song.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = null,
                                        tint = UtkarshRed
                                    )
                                },
                                onClick = {
                                    showOptionsMenu = false
                                    haptics.toggleFavorite()
                                    onFavoriteToggle(song)
                                }
                            )

                            DropdownMenuItem(
                                text = { Text(if (song.isDownloaded) "Downloaded (Offline)" else "Download Track") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (song.isDownloaded) Icons.Default.CheckCircle else Icons.Default.Download,
                                        contentDescription = null
                                    )
                                },
                                onClick = {
                                    showOptionsMenu = false
                                    haptics.toggleMode()
                                    onDownloadClick(song)
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Add to Playlist") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.PlaylistAdd,
                                        contentDescription = null
                                    )
                                },
                                onClick = {
                                    showOptionsMenu = false
                                    haptics.toggleMode()
                                    onAddToPlaylistClick(song)
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(if (playbackState.isAutoplayEnabled) "Autoplay: ON (Continuous)" else "Autoplay: OFF (Stop at End)")
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(if (playbackState.isAutoplayEnabled) UtkarshRed else Color.White.copy(alpha = 0.2f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = if (playbackState.isAutoplayEnabled) "ON" else "OFF",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                                                color = Color.White
                                            )
                                        }
                                    }
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.AllInclusive,
                                        contentDescription = null,
                                        tint = if (playbackState.isAutoplayEnabled) UtkarshRed else Color.White.copy(alpha = 0.7f)
                                    )
                                },
                                onClick = {
                                    showOptionsMenu = false
                                    haptics.toggleMode()
                                    onAutoplayToggle()
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        if (playbackState.isSleepTimerActive) {
                                            val remSec = playbackState.sleepTimerRemainingSeconds ?: 0
                                            "Sleep Timer: %dm left (Fade Out)".format((remSec / 60).coerceAtLeast(1))
                                        } else {
                                            "Sleep Timer (Gradual Fade Out)"
                                        }
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.AccessTime,
                                        contentDescription = null,
                                        tint = if (playbackState.isSleepTimerActive) UtkarshRed else Color.White.copy(alpha = 0.7f)
                                    )
                                },
                                onClick = {
                                    showOptionsMenu = false
                                    haptics.toggleMode()
                                    showSleepTimerDialog = true
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        if (playbackState.isAmbientPlaybackEnabled) {
                                            "Ambient: Soundscape Active"
                                        } else {
                                            "Ambient Playback"
                                        }
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Spa,
                                        contentDescription = null,
                                        tint = if (playbackState.isAmbientPlaybackEnabled) UtkarshPink else Color.White.copy(alpha = 0.7f)
                                    )
                                },
                                onClick = {
                                    showOptionsMenu = false
                                    haptics.toggleMode()
                                    showAmbientDialog = true
                                }
                            )
                        }
                    }
                }
            }

            // Quick Active Indicators Row (Sleep Timer, Ambient, Hindi Lyrics indicator)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (playbackState.isSleepTimerActive) {
                    val remMin = ((playbackState.sleepTimerRemainingSeconds ?: 0) / 60).coerceAtLeast(1)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(UtkarshRed.copy(alpha = 0.25f))
                            .border(1.dp, UtkarshRed.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .clickable {
                                haptics.toggleMode()
                                showSleepTimerDialog = true
                            }
                            .padding(horizontal = 9.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = UtkarshPink,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Sleep: ${remMin}m",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                if (playbackState.isAmbientPlaybackEnabled) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.12f))
                            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .clickable {
                                haptics.toggleMode()
                                showAmbientDialog = true
                            }
                            .padding(horizontal = 9.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Spa,
                            contentDescription = null,
                            tint = UtkarshPink,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Ambient",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                // Autoplay Interactive ON / OFF Chip
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (playbackState.isAutoplayEnabled) UtkarshRed.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.08f))
                        .border(
                            1.dp,
                            if (playbackState.isAutoplayEnabled) UtkarshRed.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.15f),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            haptics.toggleMode()
                            onAutoplayToggle()
                        }
                        .padding(horizontal = 9.dp, vertical = 3.dp)
                        .testTag("now_playing_autoplay_chip")
                ) {
                    Icon(
                        imageVector = Icons.Default.AllInclusive,
                        contentDescription = "Autoplay",
                        tint = if (playbackState.isAutoplayEnabled) UtkarshPink else Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (playbackState.isAutoplayEnabled) "Autoplay ON" else "Autoplay OFF",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = if (playbackState.isAutoplayEnabled) Color.White else Color.White.copy(alpha = 0.65f)
                    )
                }
            }

            // --- Center Stage: Artwork OR Lyrics OR Queue OR Related ---
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                when (activeBottomTab) {
                    1 -> {
                        // UP NEXT QUEUE TAB
                        QueueSheet(
                            playbackState = playbackState,
                            onSongClick = onSongSelectFromQueue,
                            onRemoveFromQueue = onRemoveFromQueue,
                            onShuffleToggle = onShuffleToggle,
                            onRepeatToggle = onRepeatToggle,
                            onAutoplayToggle = onAutoplayToggle,
                            onClearQueue = onClearQueue
                        )
                    }
                    2 -> {
                        // LYRICS TAB (Hindi Synced)
                        SyncedLyricsView(
                            song = song,
                            currentPositionMs = playbackState.currentPositionMs,
                            onSeekTo = {
                                haptics.scrubTick()
                                onSeekTo(it)
                            }
                        )
                    }
                    3 -> {
                        // RELATED & AMBIENT TAB
                        RelatedAmbientTab(
                            song = song,
                            playbackState = playbackState,
                            onSongClick = onSongSelectFromQueue,
                            onOpenAmbientDialog = { showAmbientDialog = true }
                        )
                    }
                    else -> {
                        // YouTube Music Large Square Artwork
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.92f)
                                .aspectRatio(1f)
                                .scale(artworkScale),
                            contentAlignment = Alignment.Center
                        ) {
                            // Fluid ambient backlight shadow
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(0.92f)
                                    .shadow(
                                        elevation = 32.dp,
                                        shape = RoundedCornerShape(18.dp),
                                        spotColor = UtkarshRed.copy(alpha = 0.5f),
                                        ambientColor = Color.Black
                                    )
                            )

                            AsyncImage(
                                model = song.coverUrl,
                                contentDescription = song.title,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(AppleDarkSecondaryCard)
                                    .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )

                            if (isVideoMode) {
                                // Video Simulation Watermark
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(12.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.Black.copy(alpha = 0.7f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "1080p • HD VIDEO",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = UtkarshRed
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // --- Song Title & Metadata + Like/Favorite Row (YouTube Music layout) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = song.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp
                        ),
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = song.artist,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                            color = Color.White.copy(alpha = 0.7f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.White.copy(alpha = 0.15f))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = song.audioQualityBadge,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }

                // Interactive Like / Thumbs Up Button
                IconButton(
                    onClick = {
                        haptics.toggleFavorite()
                        onFavoriteToggle(song)
                    },
                    modifier = Modifier.size(46.dp)
                ) {
                    Icon(
                        imageVector = if (song.isFavorite) Icons.Default.ThumbUp else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (song.isFavorite) UtkarshRed else Color.White.copy(alpha = 0.75f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- Scrubber Progress Bar (YouTube Music style) ---
            val duration = playbackState.durationMs.coerceAtLeast(1L)
            val currentPos = if (isScrubbing) {
                (scrubProgress * duration).toLong()
            } else {
                playbackState.currentPositionMs
            }
            val progressFraction = (currentPos.toFloat() / duration.toFloat()).coerceIn(0f, 1f)

            Slider(
                value = progressFraction,
                onValueChange = {
                    isScrubbing = true
                    scrubProgress = it
                    haptics.scrubTick()
                },
                onValueChangeFinished = {
                    isScrubbing = false
                    val targetMs = (scrubProgress * duration).toLong()
                    onSeekTo(targetMs)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .testTag("now_playing_slider"),
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = UtkarshRed,
                    inactiveTrackColor = Color.White.copy(alpha = 0.22f)
                )
            )

            // Time Elapsed & Total Duration
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatMs(currentPos),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                    color = Color.White.copy(alpha = 0.6f)
                )
                Text(
                    text = formatMs(duration),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                    color = Color.White.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // --- Transport Controls (YouTube Music Circular Play Button Layout) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Shuffle Button
                IconButton(
                    onClick = {
                        haptics.toggleMode()
                        onShuffleToggle()
                    },
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = "Shuffle",
                        tint = if (playbackState.isShuffled) UtkarshRed else Color.White.copy(alpha = 0.65f),
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Previous Track
                IconButton(
                    onClick = {
                        haptics.skipTrack()
                        onPreviousClick()
                    },
                    modifier = Modifier.size(54.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous Track",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                // YouTube Music Big Play/Pause Circular FAB
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .shadow(16.dp, CircleShape, spotColor = UtkarshRed)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable {
                            haptics.playPauseClick()
                            onPlayPauseClick()
                        }
                        .testTag("now_playing_play_pause"),
                    contentAlignment = Alignment.Center
                ) {
                    if (playbackState.isBuffering) {
                        CircularProgressIndicator(
                            color = UtkarshRed,
                            modifier = Modifier.size(32.dp),
                            strokeWidth = 3.dp
                        )
                    } else {
                        Icon(
                            imageVector = if (playbackState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (playbackState.isPlaying) "Pause" else "Play",
                            tint = Color.Black,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                // Next Track
                IconButton(
                    onClick = {
                        haptics.skipTrack()
                        onNextClick()
                    },
                    modifier = Modifier.size(54.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next Track",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                // Repeat Button
                IconButton(
                    onClick = {
                        haptics.toggleMode()
                        onRepeatToggle()
                    },
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        imageVector = when (playbackState.repeatMode) {
                            RepeatMode.ONE -> Icons.Default.RepeatOne
                            else -> Icons.Default.Repeat
                        },
                        contentDescription = "Repeat",
                        tint = if (playbackState.repeatMode != RepeatMode.OFF) UtkarshRed else Color.White.copy(alpha = 0.65f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // --- Iconic YouTube Music 3-Tab Bottom Pill: UP NEXT | LYRICS | RELATED ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.08f))
                    .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(24.dp))
                    .padding(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    YtmBottomTabItem(
                        title = "UP NEXT",
                        isSelected = activeBottomTab == 1,
                        onClick = {
                            haptics.toggleMode()
                            activeBottomTab = if (activeBottomTab == 1) 0 else 1
                        }
                    )
                    YtmBottomTabItem(
                        title = "LYRICS",
                        isSelected = activeBottomTab == 2,
                        onClick = {
                            haptics.toggleMode()
                            activeBottomTab = if (activeBottomTab == 2) 0 else 2
                        }
                    )
                    YtmBottomTabItem(
                        title = "RELATED",
                        isSelected = activeBottomTab == 3,
                        onClick = {
                            haptics.toggleMode()
                            activeBottomTab = if (activeBottomTab == 3) 0 else 3
                        }
                    )
                }
            }
        }

        // --- Dialogs ---
        if (showSleepTimerDialog) {
            SleepTimerDialog(
                playbackState = playbackState,
                onSetTimerMinutes = onSetSleepTimerMinutes,
                onSetEndOfTrack = onSetSleepTimerEndOfTrack,
                onCancelTimer = onCancelSleepTimer,
                onDismiss = { showSleepTimerDialog = false }
            )
        }

        if (showAmbientDialog) {
            AmbientPlaybackDialog(
                playbackState = playbackState,
                onToggleAmbient = onToggleAmbientPlayback,
                onSelectSound = onSelectAmbientSound,
                onVolumeChange = onAmbientVolumeChange,
                onDismiss = { showAmbientDialog = false }
            )
        }
    }
}

@Composable
private fun YtmBottomTabItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) Color.White.copy(alpha = 0.22f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 22.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                letterSpacing = 1.2.sp,
                fontSize = 12.sp
            ),
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun RelatedAmbientTab(
    song: Song,
    playbackState: PlaybackState,
    onSongClick: (Song) -> Unit,
    onOpenAmbientDialog: () -> Unit
) {
    val haptics = rememberHapticFeedback()
    val relatedSongs = remember(song.id) {
        CuratedMusicData.curatedSongs.filter { it.id != song.id }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            // Ambient Soundscapes Action Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                UtkarshRed.copy(alpha = 0.35f),
                                UtkarshPink.copy(alpha = 0.2f)
                            )
                        )
                    )
                    .border(1.dp, UtkarshRed.copy(alpha = 0.45f), RoundedCornerShape(16.dp))
                    .clickable {
                        haptics.toggleMode()
                        onOpenAmbientDialog()
                    }
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🌧️", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Ambient Nature Soundscapes",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Text(
                                text = "Monsoon Rain, Temple Chimes, Ganga Stream",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        item {
            Text(
                text = "SIMILAR INDIAN ARTISTS & TRACKS",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                ),
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(relatedSongs) { relSong ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.04f))
                    .clickable {
                        haptics.playPauseClick()
                        onSongClick(relSong)
                    }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = relSong.coverUrl,
                    contentDescription = relSong.title,
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = relSong.title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${relSong.artist} • ${relSong.genre}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.6f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = UtkarshRed,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

private fun formatMs(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0L)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}
