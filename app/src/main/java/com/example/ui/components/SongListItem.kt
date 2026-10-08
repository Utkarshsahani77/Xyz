package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Song
import com.example.ui.theme.AppleDarkSecondaryCard
import com.example.ui.theme.UtkarshRed

@Composable
fun SongListItem(
    song: Song,
    isPlaying: Boolean = false,
    isCurrentSong: Boolean = false,
    isDownloading: Boolean = false,
    showCover: Boolean = true,
    trackNumber: Int? = null,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onDownloadClick: () -> Unit,
    onAddToPlaylistClick: () -> Unit,
    onPlayNextClick: (() -> Unit)? = null,
    onPlayLaterClick: (() -> Unit)? = null
) {
    val haptics = com.example.util.rememberHapticFeedback()
    var showMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                haptics.playPauseClick()
                onClick()
            })
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .testTag("song_item_${song.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (trackNumber != null && !showCover) {
            Box(
                modifier = Modifier.width(32.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isCurrentSong && isPlaying) {
                    AnimatedWaveform(
                        isPlaying = true,
                        maxHeight = 16.dp,
                        barWidth = 2.5.dp,
                        barCount = 3,
                        barColor = UtkarshRed
                    )
                } else {
                    Text(
                        text = "$trackNumber",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (isCurrentSong) FontWeight.Bold else FontWeight.Normal,
                            color = if (isCurrentSong) UtkarshRed else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        if (showCover) {
            Box(modifier = Modifier.size(48.dp)) {
                AsyncImage(
                    model = song.coverUrl,
                    contentDescription = song.title,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(AppleDarkSecondaryCard),
                    contentScale = ContentScale.Crop
                )
                if (isCurrentSong && isPlaying) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.55f)),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedWaveform(
                            isPlaying = true,
                            maxHeight = 18.dp,
                            barWidth = 3.dp,
                            barCount = 3,
                            barColor = Color.White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
        }

        // Title & Artist
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = song.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (isCurrentSong) FontWeight.Bold else FontWeight.Medium
                ),
                color = if (isCurrentSong) UtkarshRed else MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (song.isDownloaded) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Downloaded Offline",
                        tint = UtkarshRed,
                        modifier = Modifier
                            .size(13.dp)
                            .padding(end = 3.dp)
                    )
                }

                Text(
                    text = "${song.artist} • ${song.album}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Quality Pill
        if (song.audioQualityBadge.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 5.dp, vertical = 2.dp)
            ) {
                Text(
                    text = if (song.audioQualityBadge.contains("MASTER")) "MASTER" else "LOSSLESS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
        }

        // Downloading indicator or 3-dot menu
        if (isDownloading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(20.dp)
                    .padding(2.dp),
                strokeWidth = 2.dp,
                color = UtkarshRed
            )
        } else {
            Box {
                IconButton(
                    onClick = { showMenu = true },
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("more_button_${song.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    if (onPlayNextClick != null) {
                        DropdownMenuItem(
                            text = { Text("Play Next") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.PlaylistPlay,
                                    contentDescription = null,
                                    tint = UtkarshRed
                                )
                            },
                            onClick = {
                                showMenu = false
                                haptics.toggleMode()
                                onPlayNextClick()
                            }
                        )
                    }

                    if (onPlayLaterClick != null) {
                        DropdownMenuItem(
                            text = { Text("Play Later") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.QueueMusic,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                showMenu = false
                                haptics.toggleMode()
                                onPlayLaterClick()
                            }
                        )
                    }

                    DropdownMenuItem(
                        text = { Text(if (song.isFavorite) "Remove from Favorites" else "Favorite") },
                        leadingIcon = {
                            Icon(
                                imageVector = if (song.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = UtkarshRed
                            )
                        },
                        onClick = {
                            showMenu = false
                            haptics.toggleFavorite()
                            onFavoriteToggle()
                        }
                    )

                    DropdownMenuItem(
                        text = { Text(if (song.isDownloaded) "Downloaded (Offline)" else "Download for Offline") },
                        leadingIcon = {
                            Icon(
                                imageVector = if (song.isDownloaded) Icons.Default.CheckCircle else Icons.Default.Download,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            showMenu = false
                            onDownloadClick()
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
                            showMenu = false
                            onAddToPlaylistClick()
                        }
                    )
                }
            }
        }
    }
}
