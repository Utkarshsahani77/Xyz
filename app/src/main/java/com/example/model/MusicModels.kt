package com.example.model

enum class RepeatMode {
    OFF,
    ALL,
    ONE
}

data class LyricLine(
    val timeMs: Long,
    val text: String,
    val romanizedText: String? = null
)

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val durationMs: Long,
    val coverUrl: String,
    val streamUrl: String,
    val isLocal: Boolean = false,
    val isDownloaded: Boolean = false,
    val isFavorite: Boolean = false,
    val genre: String = "Pop",
    val audioQualityBadge: String = "LOSSLESS",
    val lyrics: List<LyricLine> = emptyList()
)

data class RadioStation(
    val id: String,
    val name: String,
    val tagline: String,
    val hostOrShow: String,
    val coverUrl: String,
    val streamUrl: String,
    val isLive: Boolean = true,
    val frequencyLabel: String = "LIVE 24/7"
)

data class Album(
    val id: String,
    val title: String,
    val artist: String,
    val coverUrl: String,
    val year: String,
    val badge: String = "Utkarsh Master",
    val songs: List<Song> = emptyList()
)

data class Playlist(
    val id: String,
    val name: String,
    val description: String,
    val coverUrl: String,
    val songs: List<Song> = emptyList(),
    val isCurated: Boolean = true
)

data class ChartCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val coverUrl: String,
    val songs: List<Song>
)

data class AmbientSound(
    val id: String,
    val name: String,
    val hindiName: String,
    val iconEmoji: String,
    val audioUrl: String
)

data class PlaybackState(
    val currentSong: Song? = null,
    val isPlaying: Boolean = false,
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L,
    val isBuffering: Boolean = false,
    val queue: List<Song> = emptyList(),
    val queueIndex: Int = 0,
    val isShuffled: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val volume: Float = 1.0f,
    val isAutoplayEnabled: Boolean = true,
    val history: List<Song> = emptyList(),
    val sleepTimerRemainingSeconds: Int? = null,
    val isSleepTimerActive: Boolean = false,
    val sleepTimerEndOfTrack: Boolean = false,
    val isAmbientPlaybackEnabled: Boolean = false,
    val selectedAmbientSoundId: String? = null,
    val ambientVolume: Float = 0.4f
)
