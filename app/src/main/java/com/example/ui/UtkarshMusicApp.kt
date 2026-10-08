package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.Playlist
import com.example.model.Song
import com.example.ui.components.AirPlayDialog
import com.example.ui.components.AppleMusicSplashScreen
import com.example.ui.components.MiniPlayer
import com.example.ui.components.NowPlayingSheet
import com.example.ui.components.ProfileDialog
import com.example.ui.screens.BrowseScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.ListenNowScreen
import com.example.ui.screens.PlaylistDetailScreen
import com.example.ui.screens.RadioScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.theme.AppleTabBarDark
import com.example.ui.theme.UtkarshRed

@Composable
fun UtkarshMusicApp(viewModel: MainViewModel = viewModel()) {
    val playbackState by viewModel.playbackState.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val destination by viewModel.currentDestination.collectAsStateWithLifecycle()
    val isNowPlayingExpanded by viewModel.isNowPlayingExpanded.collectAsStateWithLifecycle()
    val isLyricsMode by viewModel.isLyricsMode.collectAsStateWithLifecycle()
    val isQueueMode by viewModel.isQueueMode.collectAsStateWithLifecycle()
    val isAirPlayVisible by viewModel.isAirPlayDialogVisible.collectAsStateWithLifecycle()
    val isOfflineOnly by viewModel.isOfflineOnly.collectAsStateWithLifecycle()
    val isScanningDevice by viewModel.isScanningDevice.collectAsStateWithLifecycle()
    val downloadingIds by viewModel.downloadingSongIds.collectAsStateWithLifecycle()
    val profileImageUri by viewModel.profileImageUri.collectAsStateWithLifecycle()

    val allLibrarySongs by viewModel.allLibrarySongs.collectAsStateWithLifecycle()
    val downloadedSongs by viewModel.downloadedSongs.collectAsStateWithLifecycle()
    val favoriteSongs by viewModel.favoriteSongs.collectAsStateWithLifecycle()
    val recentlyPlayed by viewModel.recentlyPlayed.collectAsStateWithLifecycle()
    val userPlaylists by viewModel.userPlaylists.collectAsStateWithLifecycle()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()

    var showProfileDialog by remember { mutableStateOf(false) }
    var songForPlaylistPicker by remember { mutableStateOf<Song?>(null) }
    var showSplashScreen by remember { mutableStateOf(true) }

    // Playlist picker dialog
    if (songForPlaylistPicker != null) {
        val targetSong = songForPlaylistPicker!!
        AlertDialog(
            onDismissRequest = { songForPlaylistPicker = null },
            title = { Text("Add to Playlist", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    if (userPlaylists.isEmpty()) {
                        Text("No playlists yet. Create one from the Library tab.")
                    } else {
                        userPlaylists.forEach { pl ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        viewModel.addSongToPlaylist(pl.id, targetSong.id)
                                        songForPlaylistPicker = null
                                    }
                                    .padding(vertical = 10.dp, horizontal = 8.dp)
                            ) {
                                Text(
                                    text = pl.name,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { songForPlaylistPicker = null }) {
                    Text("Cancel", color = UtkarshRed)
                }
            }
        )
    }

    if (showProfileDialog) {
        ProfileDialog(
            profileImageUri = profileImageUri,
            playbackState = playbackState,
            isOfflineOnly = isOfflineOnly,
            onToggleOfflineOnly = { viewModel.toggleOfflineOnly() },
            onToggleAutoplay = { viewModel.playerManager.toggleAutoplay() },
            onToggleShuffle = { viewModel.playerManager.toggleShuffle() },
            onCycleRepeat = { viewModel.playerManager.cycleRepeatMode() },
            onClearQueue = { viewModel.playerManager.clearQueue() },
            onSetSleepTimerMinutes = { viewModel.playerManager.startSleepTimer(it) },
            onCancelSleepTimer = { viewModel.playerManager.cancelSleepTimer() },
            onToggleAmbientPlayback = { viewModel.playerManager.toggleAmbientPlayback() },
            onAmbientVolumeChange = { viewModel.playerManager.setAmbientVolume(it) },
            onScanDeviceAudio = { viewModel.scanDeviceAudio() },
            onProfileImageChange = { viewModel.updateProfileImage(it) },
            onDismiss = { showProfileDialog = false }
        )
    }

    if (isAirPlayVisible) {
        AirPlayDialog(
            volume = playbackState.volume,
            onVolumeChange = { viewModel.playerManager.setVolume(it) },
            onDismiss = { viewModel.setAirPlayDialogVisible(false) }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                // Frosted Glass Docked Bottom Bar + Mini Player
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xB3121218),
                                    Color(0xEB0C0C10)
                                )
                            )
                        )
                        .navigationBarsPadding()
                ) {
                    // Mini player docked directly above tab bar with fluid closing/opening animation
                    AnimatedVisibility(
                        visible = playbackState.currentSong != null && !isNowPlayingExpanded,
                        enter = slideInVertically(
                            animationSpec = spring(dampingRatio = 0.78f, stiffness = 380f),
                            initialOffsetY = { it }
                        ) + fadeIn(animationSpec = tween(220)) + scaleIn(initialScale = 0.92f),
                        exit = slideOutVertically(
                            animationSpec = spring(dampingRatio = 0.82f, stiffness = 340f),
                            targetOffsetY = { it }
                        ) + fadeOut(animationSpec = tween(200, easing = FastOutSlowInEasing)) + scaleOut(targetScale = 0.88f)
                    ) {
                        MiniPlayer(
                            playbackState = playbackState,
                            onExpandClick = { viewModel.setNowPlayingExpanded(true) },
                            onPlayPauseClick = { viewModel.playerManager.togglePlayPause() },
                            onNextClick = { viewModel.playerManager.playNext() },
                            onClosePlayback = { viewModel.playerManager.closePlayback() }
                        )
                    }

                    HorizontalDivider(
                        color = Color.White.copy(alpha = 0.12f),
                        thickness = 0.5.dp
                    )

                    // 5 Apple Music Tabs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("bottom_nav_bar"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppleNavBarItem(
                            title = "Listen Now",
                            icon = Icons.Default.PlayCircleOutline,
                            selected = currentTab == NavigationTab.LISTEN_NOW && destination is ScreenDestination.Main,
                            onClick = { viewModel.selectTab(NavigationTab.LISTEN_NOW) }
                        )
                        AppleNavBarItem(
                            title = "Browse",
                            icon = Icons.Default.Widgets,
                            selected = currentTab == NavigationTab.BROWSE && destination is ScreenDestination.Main,
                            onClick = { viewModel.selectTab(NavigationTab.BROWSE) }
                        )
                        AppleNavBarItem(
                            title = "Radio",
                            icon = Icons.Default.Radio,
                            selected = currentTab == NavigationTab.RADIO && destination is ScreenDestination.Main,
                            onClick = { viewModel.selectTab(NavigationTab.RADIO) }
                        )
                        AppleNavBarItem(
                            title = "Library",
                            icon = Icons.Default.LibraryMusic,
                            selected = currentTab == NavigationTab.LIBRARY && destination is ScreenDestination.Main,
                            onClick = { viewModel.selectTab(NavigationTab.LIBRARY) }
                        )
                        AppleNavBarItem(
                            title = "Search",
                            icon = Icons.Default.Search,
                            selected = currentTab == NavigationTab.SEARCH && destination is ScreenDestination.Main,
                            onClick = { viewModel.selectTab(NavigationTab.SEARCH) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (val dest = destination) {
                    is ScreenDestination.PlaylistDetail -> {
                        PlaylistDetailScreen(
                            playlist = dest.playlist,
                            onBackClick = { viewModel.navigateBack() },
                            onSongClick = { song, queue -> viewModel.playSong(song, queue) },
                            onPlayAllClick = { songs ->
                                songs.firstOrNull()?.let { viewModel.playSong(it, songs) }
                            },
                            onShuffleAllClick = { songs ->
                                songs.shuffled().firstOrNull()?.let {
                                    viewModel.playSong(it, songs.shuffled())
                                }
                            },
                            onDownloadAllClick = { songs ->
                                songs.forEach { viewModel.downloadSong(it) }
                            },
                            onFavoriteToggle = { viewModel.toggleFavorite(it) },
                            onDownloadSongClick = { viewModel.downloadSong(it) },
                            onAddToPlaylistClick = { songForPlaylistPicker = it },
                            currentSongId = playbackState.currentSong?.id,
                            isPlaying = playbackState.isPlaying,
                            downloadingIds = downloadingIds
                        )
                    }
                    is ScreenDestination.AlbumDetail -> {
                        val albumPlaylist = Playlist(
                            id = dest.album.id,
                            name = dest.album.title,
                            description = "${dest.album.artist} • ${dest.album.year} • ${dest.album.badge}",
                            coverUrl = dest.album.coverUrl,
                            songs = dest.album.songs
                        )
                        PlaylistDetailScreen(
                            playlist = albumPlaylist,
                            onBackClick = { viewModel.navigateBack() },
                            onSongClick = { song, queue -> viewModel.playSong(song, queue) },
                            onPlayAllClick = { songs ->
                                songs.firstOrNull()?.let { viewModel.playSong(it, songs) }
                            },
                            onShuffleAllClick = { songs ->
                                songs.shuffled().firstOrNull()?.let {
                                    viewModel.playSong(it, songs.shuffled())
                                }
                            },
                            onDownloadAllClick = { songs ->
                                songs.forEach { viewModel.downloadSong(it) }
                            },
                            onFavoriteToggle = { viewModel.toggleFavorite(it) },
                            onDownloadSongClick = { viewModel.downloadSong(it) },
                            onAddToPlaylistClick = { songForPlaylistPicker = it },
                            currentSongId = playbackState.currentSong?.id,
                            isPlaying = playbackState.isPlaying,
                            downloadingIds = downloadingIds
                        )
                    }
                    is ScreenDestination.Main -> {
                        // Fluid AnimatedContent Tab Transition
                        AnimatedContent(
                            targetState = currentTab,
                            transitionSpec = {
                                (fadeIn(animationSpec = tween(220, easing = FastOutSlowInEasing)) +
                                        slideInHorizontally(animationSpec = tween(220), initialOffsetX = { 30 }))
                                    .togetherWith(
                                        fadeOut(animationSpec = tween(180, easing = FastOutSlowInEasing)) +
                                                slideOutHorizontally(animationSpec = tween(180), targetOffsetX = { -30 })
                                    )
                            },
                            label = "tabTransition"
                        ) { tab ->
                            when (tab) {
                                NavigationTab.LISTEN_NOW -> {
                                    ListenNowScreen(
                                        recentlyPlayed = recentlyPlayed,
                                        curatedPlaylists = viewModel.curatedPlaylists,
                                        featuredAlbums = viewModel.featuredAlbums,
                                        onSongClick = { viewModel.playSong(it) },
                                        onPlaylistClick = { viewModel.navigateToPlaylist(it) },
                                        onAlbumClick = { viewModel.navigateToAlbum(it) },
                                        onFavoriteToggle = { viewModel.toggleFavorite(it) },
                                        onDownloadClick = { viewModel.downloadSong(it) },
                                        onAddToPlaylistClick = { songForPlaylistPicker = it },
                                        currentSongId = playbackState.currentSong?.id,
                                        isPlaying = playbackState.isPlaying,
                                        downloadingIds = downloadingIds,
                                        profileImageUri = profileImageUri,
                                        onProfileClick = { showProfileDialog = true }
                                    )
                                }
                                NavigationTab.BROWSE -> {
                                    BrowseScreen(
                                        curatedPlaylists = viewModel.curatedPlaylists,
                                        featuredAlbums = viewModel.featuredAlbums,
                                        allSongs = allLibrarySongs,
                                        onSongClick = { viewModel.playSong(it) },
                                        onPlaylistClick = { viewModel.navigateToPlaylist(it) },
                                        onAlbumClick = { viewModel.navigateToAlbum(it) },
                                        onFavoriteToggle = { viewModel.toggleFavorite(it) },
                                        onDownloadClick = { viewModel.downloadSong(it) },
                                        onAddToPlaylistClick = { songForPlaylistPicker = it },
                                        currentSongId = playbackState.currentSong?.id,
                                        isPlaying = playbackState.isPlaying,
                                        downloadingIds = downloadingIds,
                                        profileImageUri = profileImageUri,
                                        onProfileClick = { showProfileDialog = true }
                                    )
                                }
                                NavigationTab.RADIO -> {
                                    RadioScreen(
                                        radioStations = viewModel.radioStations,
                                        onStationClick = { viewModel.playRadioStation(it) },
                                        currentPlayingId = playbackState.currentSong?.id,
                                        isPlaying = playbackState.isPlaying,
                                        profileImageUri = profileImageUri,
                                        onProfileClick = { showProfileDialog = true }
                                    )
                                }
                                NavigationTab.LIBRARY -> {
                                    LibraryScreen(
                                        librarySongs = allLibrarySongs,
                                        downloadedSongs = downloadedSongs,
                                        favoriteSongs = favoriteSongs,
                                        userPlaylists = userPlaylists,
                                        isOfflineOnly = isOfflineOnly,
                                        isScanningDevice = isScanningDevice,
                                        onToggleOfflineOnly = { viewModel.toggleOfflineOnly() },
                                        onScanDeviceAudio = { viewModel.scanDeviceAudio() },
                                        onCreatePlaylist = { viewModel.createPlaylist(it) },
                                        onPlaylistClick = { viewModel.navigateToPlaylist(it) },
                                        onSongClick = { viewModel.playSong(it) },
                                        onFavoriteToggle = { viewModel.toggleFavorite(it) },
                                        onDownloadClick = { viewModel.downloadSong(it) },
                                        onAddToPlaylistClick = { songForPlaylistPicker = it },
                                        currentSongId = playbackState.currentSong?.id,
                                        isPlaying = playbackState.isPlaying,
                                        downloadingIds = downloadingIds,
                                        profileImageUri = profileImageUri,
                                        onProfileClick = { showProfileDialog = true },
                                        isAutoplayEnabled = playbackState.isAutoplayEnabled,
                                        onToggleAutoplay = { viewModel.playerManager.toggleAutoplay() }
                                    )
                                }
                                NavigationTab.SEARCH -> {
                                    SearchScreen(
                                        searchQuery = searchQuery,
                                        searchResults = searchResults,
                                        isSearching = isSearching,
                                        onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                                        onSongClick = { viewModel.playSong(it) },
                                        onFavoriteToggle = { viewModel.toggleFavorite(it) },
                                        onDownloadClick = { viewModel.downloadSong(it) },
                                        onAddToPlaylistClick = { songForPlaylistPicker = it },
                                        currentSongId = playbackState.currentSong?.id,
                                        isPlaying = playbackState.isPlaying,
                                        downloadingIds = downloadingIds,
                                        profileImageUri = profileImageUri,
                                        onProfileClick = { showProfileDialog = true }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Fluid Animated Full Screen Now Playing Sheet Modal
        AnimatedVisibility(
            visible = isNowPlayingExpanded && playbackState.currentSong != null,
            enter = slideInVertically(
                animationSpec = spring(dampingRatio = 0.82f, stiffness = 380f),
                initialOffsetY = { it }
            ),
            exit = slideOutVertically(
                animationSpec = spring(dampingRatio = 0.9f, stiffness = 420f),
                targetOffsetY = { it }
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            NowPlayingSheet(
                playbackState = playbackState,
                isLyricsMode = isLyricsMode,
                isQueueMode = isQueueMode,
                onDismiss = { viewModel.setNowPlayingExpanded(false) },
                onPlayPauseClick = { viewModel.playerManager.togglePlayPause() },
                onNextClick = { viewModel.playerManager.playNext() },
                onPreviousClick = { viewModel.playerManager.playPrevious() },
                onSeekTo = { viewModel.playerManager.seekTo(it) },
                onFavoriteToggle = { viewModel.toggleFavorite(it) },
                onDownloadClick = { viewModel.downloadSong(it) },
                onAddToPlaylistClick = { songForPlaylistPicker = it },
                onToggleLyrics = { viewModel.toggleLyricsMode() },
                onToggleQueue = { viewModel.toggleQueueMode() },
                onOpenAirPlay = { viewModel.setAirPlayDialogVisible(true) },
                onVolumeChange = { viewModel.playerManager.setVolume(it) },
                onSongSelectFromQueue = { viewModel.playSong(it, playbackState.queue) },
                onRemoveFromQueue = { viewModel.playerManager.removeFromQueue(it) },
                onShuffleToggle = { viewModel.playerManager.toggleShuffle() },
                onRepeatToggle = { viewModel.playerManager.cycleRepeatMode() },
                onAutoplayToggle = { viewModel.playerManager.toggleAutoplay() },
                onClearQueue = { viewModel.playerManager.clearQueue() },
                onSetSleepTimerMinutes = { viewModel.playerManager.startSleepTimer(it) },
                onSetSleepTimerEndOfTrack = { viewModel.playerManager.startSleepTimerEndOfTrack() },
                onCancelSleepTimer = { viewModel.playerManager.cancelSleepTimer() },
                onToggleAmbientPlayback = { viewModel.playerManager.toggleAmbientPlayback() },
                onSelectAmbientSound = { viewModel.playerManager.playAmbientSound(it) },
                onAmbientVolumeChange = { viewModel.playerManager.setAmbientVolume(it) }
            )
        }

        // Apple Music Opening Splash Animation
        if (showSplashScreen) {
            AppleMusicSplashScreen(
                onAnimationFinished = { showSplashScreen = false }
            )
        }
    }
}

@Composable
private fun RowScope.AppleNavBarItem(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val itemScale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1.0f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "navItemScale"
    )

    Column(
        modifier = Modifier
            .weight(1f)
            .scale(itemScale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 4.dp)
            .testTag("nav_tab_${title.lowercase().replace(" ", "_")}"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (selected) UtkarshRed else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            ),
            color = if (selected) UtkarshRed else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f)
        )
    }
}
