package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CuratedMusicData
import com.example.data.MusicRepository
import com.example.model.Album
import com.example.model.PlaybackState
import com.example.model.Playlist
import com.example.model.RadioStation
import com.example.model.Song
import com.example.player.MusicPlayerManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class NavigationTab(val title: String) {
    LISTEN_NOW("Listen Now"),
    BROWSE("Browse"),
    RADIO("Radio"),
    LIBRARY("Library"),
    SEARCH("Search")
}

sealed class ScreenDestination {
    data object Main : ScreenDestination()
    data class PlaylistDetail(val playlist: Playlist) : ScreenDestination()
    data class AlbumDetail(val album: Album) : ScreenDestination()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MusicRepository(application)
    val playerManager = MusicPlayerManager(application)

    val playbackState: StateFlow<PlaybackState> = playerManager.playbackState

    // Navigation & UI state
    private val _currentTab = MutableStateFlow(NavigationTab.LISTEN_NOW)
    val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

    private val _currentDestination = MutableStateFlow<ScreenDestination>(ScreenDestination.Main)
    val currentDestination: StateFlow<ScreenDestination> = _currentDestination.asStateFlow()

    private val _isNowPlayingExpanded = MutableStateFlow(false)
    val isNowPlayingExpanded: StateFlow<Boolean> = _isNowPlayingExpanded.asStateFlow()

    private val _isLyricsMode = MutableStateFlow(false)
    val isLyricsMode: StateFlow<Boolean> = _isLyricsMode.asStateFlow()

    private val _isQueueMode = MutableStateFlow(false)
    val isQueueMode: StateFlow<Boolean> = _isQueueMode.asStateFlow()

    private val _isAirPlayDialogVisible = MutableStateFlow(false)
    val isAirPlayDialogVisible: StateFlow<Boolean> = _isAirPlayDialogVisible.asStateFlow()

    private val _profileImageUri = MutableStateFlow<String?>(null)
    val profileImageUri: StateFlow<String?> = _profileImageUri.asStateFlow()

    fun updateProfileImage(uriString: String?) {
        _profileImageUri.value = uriString
    }

    private val _isOfflineOnly = MutableStateFlow(false)
    val isOfflineOnly: StateFlow<Boolean> = _isOfflineOnly.asStateFlow()

    private val _isScanningDevice = MutableStateFlow(false)
    val isScanningDevice: StateFlow<Boolean> = _isScanningDevice.asStateFlow()

    private val _downloadingSongIds = MutableStateFlow<Set<String>>(emptySet())
    val downloadingSongIds: StateFlow<Set<String>> = _downloadingSongIds.asStateFlow()

    // Library flows
    val allLibrarySongs: StateFlow<List<Song>> = repository.getAllLibrarySongs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CuratedMusicData.curatedSongs)

    val downloadedSongs: StateFlow<List<Song>> = repository.getDownloadedSongs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteSongs: StateFlow<List<Song>> = repository.getFavoriteSongs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentlyPlayed: StateFlow<List<Song>> = repository.getRecentlyPlayedSongs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userPlaylists: StateFlow<List<Playlist>> = repository.getUserPlaylists()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Effective library songs taking offline mode into account
    val displayedLibrarySongs: StateFlow<List<Song>> = combine(
        allLibrarySongs,
        downloadedSongs,
        _isOfflineOnly
    ) { all, downloaded, offlineOnly ->
        if (offlineOnly) downloaded else all
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CuratedMusicData.curatedSongs)

    // Curated catalog
    val curatedPlaylists = CuratedMusicData.curatedPlaylists
    val featuredAlbums = CuratedMusicData.featuredAlbums
    val radioStations = CuratedMusicData.radioStations

    // Search state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Song>>(emptyList())
    val searchResults: StateFlow<List<Song>> = _searchResults.asStateFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            repository.initializeDatabase()
        }
    }

    fun selectTab(tab: NavigationTab) {
        _currentTab.value = tab
        _currentDestination.value = ScreenDestination.Main
    }

    fun navigateToPlaylist(playlist: Playlist) {
        _currentDestination.value = ScreenDestination.PlaylistDetail(playlist)
    }

    fun navigateToAlbum(album: Album) {
        _currentDestination.value = ScreenDestination.AlbumDetail(album)
    }

    fun navigateBack() {
        if (_currentDestination.value !is ScreenDestination.Main) {
            _currentDestination.value = ScreenDestination.Main
        }
    }

    fun setNowPlayingExpanded(expanded: Boolean) {
        _isNowPlayingExpanded.value = expanded
        if (!expanded) {
            _isLyricsMode.value = false
            _isQueueMode.value = false
        }
    }

    fun toggleLyricsMode() {
        _isLyricsMode.update { !it }
        if (_isLyricsMode.value) {
            _isQueueMode.value = false
        }
    }

    fun toggleQueueMode() {
        _isQueueMode.update { !it }
        if (_isQueueMode.value) {
            _isLyricsMode.value = false
        }
    }

    fun setAirPlayDialogVisible(visible: Boolean) {
        _isAirPlayDialogVisible.value = visible
    }

    fun toggleOfflineOnly() {
        _isOfflineOnly.update { !it }
    }

    fun playSong(song: Song, queue: List<Song> = listOf(song)) {
        playerManager.playSong(song, queue)
        viewModelScope.launch {
            repository.recordPlayed(song)
        }
    }

    fun playRadioStation(station: RadioStation) {
        val radioSong = Song(
            id = station.id,
            title = station.name,
            artist = station.hostOrShow,
            album = station.frequencyLabel,
            durationMs = 0L,
            coverUrl = station.coverUrl,
            streamUrl = station.streamUrl,
            audioQualityBadge = "LIVE BROADCAST"
        )
        playerManager.playSong(radioSong, listOf(radioSong))
    }

    fun toggleFavorite(song: Song) {
        viewModelScope.launch {
            repository.toggleFavorite(song)
        }
    }

    fun downloadSong(song: Song) {
        viewModelScope.launch {
            _downloadingSongIds.update { it + song.id }
            repository.downloadSong(song)
            _downloadingSongIds.update { it - song.id }
        }
    }

    fun createPlaylist(name: String, description: String = "") {
        viewModelScope.launch {
            repository.createPlaylist(name, description)
        }
    }

    fun addSongToPlaylist(playlistId: String, songId: String) {
        viewModelScope.launch {
            repository.addSongToPlaylist(playlistId, songId)
        }
    }

    fun scanDeviceAudio() {
        viewModelScope.launch {
            _isScanningDevice.value = true
            try {
                repository.scanDeviceAudioFiles()
            } finally {
                _isScanningDevice.value = false
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()

        if (query.isBlank()) {
            _searchResults.value = emptyList()
            _isSearching.value = false
            return
        }

        searchJob = viewModelScope.launch {
            delay(350L) // Debounce
            _isSearching.value = true

            // Local / curated matching
            val localMatches = allLibrarySongs.value.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.artist.contains(query, ignoreCase = true) ||
                it.album.contains(query, ignoreCase = true) ||
                it.genre.contains(query, ignoreCase = true)
            }

            // Online iTunes catalog search
            val onlineMatches = repository.searchOnlineSongs(query)

            // Deduplicate by title + artist
            val combined = (localMatches + onlineMatches).distinctBy { "${it.title.lowercase()}_${it.artist.lowercase()}" }
            _searchResults.value = combined
            _isSearching.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
    }
}
