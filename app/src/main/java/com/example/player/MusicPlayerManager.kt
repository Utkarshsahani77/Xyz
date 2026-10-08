package com.example.player

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import com.example.model.PlaybackState
import com.example.model.RepeatMode
import com.example.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MusicPlayerManager(private val context: Context) {

    private val tag = "UtkarshPlayer"
    private var mediaPlayer: MediaPlayer? = null
    private var ambientMediaPlayer: MediaPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var progressJob: Job? = null
    private var sleepTimerJob: Job? = null
    private var masterVolumeBeforeFade: Float = 1.0f

    private val _playbackState = MutableStateFlow(PlaybackState())
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private var originalQueue: List<Song> = emptyList()

    init {
        initMediaPlayer()
    }

    private fun initMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setOnPreparedListener { mp ->
                _playbackState.update {
                    it.copy(
                        isBuffering = false,
                        isPlaying = true,
                        durationMs = mp.duration.toLong().coerceAtLeast(it.currentSong?.durationMs ?: 0L)
                    )
                }
                mp.start()
                startProgressTicker()
            }
            setOnCompletionListener {
                handleSongCompletion()
            }
            setOnErrorListener { _, what, extra ->
                Log.e(tag, "MediaPlayer error: what=$what extra=$extra")
                _playbackState.update { it.copy(isPlaying = false, isBuffering = false) }
                true
            }
            setOnBufferingUpdateListener { _, percent ->
                // Can track buffer if needed
            }
        }
    }

    fun playSong(song: Song, queue: List<Song> = listOf(song)) {
        val currentQueue = if (queue.isEmpty()) listOf(song) else queue
        originalQueue = currentQueue
        val effectiveQueue = if (_playbackState.value.isShuffled) {
            val remaining = currentQueue.filter { it.id != song.id }.shuffled()
            listOf(song) + remaining
        } else {
            currentQueue
        }

        val index = effectiveQueue.indexOfFirst { it.id == song.id }.coerceAtLeast(0)

        _playbackState.update {
            it.copy(
                currentSong = song,
                queue = effectiveQueue,
                queueIndex = index,
                currentPositionMs = 0L,
                durationMs = song.durationMs,
                isBuffering = true,
                isPlaying = false
            )
        }

        playAudioSource(song.streamUrl)
    }

    private fun playAudioSource(streamUrl: String) {
        try {
            stopProgressTicker()
            val mp = mediaPlayer ?: return
            mp.reset()
            _playbackState.update { it.copy(isBuffering = true) }

            if (streamUrl.startsWith("content://")) {
                val uri = Uri.parse(streamUrl)
                mp.setDataSource(context, uri)
            } else if (streamUrl.startsWith("file://")) {
                val path = streamUrl.removePrefix("file://")
                mp.setDataSource(path)
            } else {
                mp.setDataSource(streamUrl)
            }
            mp.prepareAsync()
        } catch (e: Exception) {
            Log.e(tag, "Failed to set audio source: $streamUrl", e)
            _playbackState.update { it.copy(isBuffering = false, isPlaying = false) }
        }
    }

    fun togglePlayPause() {
        val mp = mediaPlayer ?: return
        val current = _playbackState.value
        if (current.currentSong == null) return

        if (mp.isPlaying) {
            mp.pause()
            stopProgressTicker()
            _playbackState.update { it.copy(isPlaying = false) }
        } else {
            mp.start()
            startProgressTicker()
            _playbackState.update { it.copy(isPlaying = true) }
        }
    }

    fun seekTo(positionMs: Long) {
        val mp = mediaPlayer ?: return
        try {
            mp.seekTo(positionMs.toInt())
            _playbackState.update { it.copy(currentPositionMs = positionMs) }
        } catch (e: Exception) {
            Log.e(tag, "Seek error", e)
        }
    }

    fun playNext() {
        val state = _playbackState.value
        if (state.queue.isEmpty()) return

        val currentSong = state.currentSong
        val updatedHistory = if (currentSong != null) (state.history + currentSong).takeLast(30) else state.history

        val nextIndex = state.queueIndex + 1
        if (nextIndex < state.queue.size) {
            val nextSong = state.queue[nextIndex]
            _playbackState.update {
                it.copy(
                    queueIndex = nextIndex,
                    currentSong = nextSong,
                    history = updatedHistory
                )
            }
            playAudioSource(nextSong.streamUrl)
        } else if (state.repeatMode == RepeatMode.ALL) {
            val firstSong = state.queue.first()
            _playbackState.update {
                it.copy(
                    queueIndex = 0,
                    currentSong = firstSong,
                    history = updatedHistory
                )
            }
            playAudioSource(firstSong.streamUrl)
        } else if (state.isAutoplayEnabled) {
            // Autoplay system: automatically queue similar recommendations!
            val currentGenre = currentSong?.genre ?: "Bollywood"
            val currentArtist = currentSong?.artist ?: ""
            val existingIds = (state.queue.map { it.id } + updatedHistory.map { it.id }).toSet()
            
            // Prioritize same genre or same artist
            val matchedSongs = com.example.data.CuratedMusicData.curatedSongs.filter { 
                it.id !in existingIds && (it.genre.equals(currentGenre, ignoreCase = true) || it.artist.equals(currentArtist, ignoreCase = true))
            }
            val remainingSongs = com.example.data.CuratedMusicData.curatedSongs.filter { it.id !in existingIds && it !in matchedSongs }
            val recommendations = (matchedSongs + remainingSongs).ifEmpty {
                com.example.data.CuratedMusicData.curatedSongs.shuffled().take(5)
            }

            val nextSong = recommendations.firstOrNull()
            if (nextSong != null) {
                val extendedQueue = state.queue + recommendations
                _playbackState.update {
                    it.copy(
                        queue = extendedQueue,
                        queueIndex = nextIndex,
                        currentSong = nextSong,
                        history = updatedHistory
                    )
                }
                playAudioSource(nextSong.streamUrl)
            } else {
                mediaPlayer?.pause()
                stopProgressTicker()
                _playbackState.update { it.copy(isPlaying = false, currentPositionMs = 0L) }
            }
        } else {
            // Queue ended
            mediaPlayer?.pause()
            stopProgressTicker()
            _playbackState.update { it.copy(isPlaying = false, currentPositionMs = 0L) }
        }
    }

    fun playPrevious() {
        val state = _playbackState.value
        if (state.queue.isEmpty()) return

        // If played more than 3 seconds, replay current song from beginning
        if (state.currentPositionMs > 3000L) {
            seekTo(0L)
            return
        }

        val prevIndex = state.queueIndex - 1
        if (prevIndex >= 0) {
            val prevSong = state.queue[prevIndex]
            _playbackState.update { it.copy(queueIndex = prevIndex, currentSong = prevSong) }
            playAudioSource(prevSong.streamUrl)
        } else {
            seekTo(0L)
        }
    }

    private fun handleSongCompletion() {
        val state = _playbackState.value

        // Check if sleep timer was set for end of current track
        if (state.sleepTimerEndOfTrack) {
            cancelSleepTimer()
            mediaPlayer?.pause()
            ambientMediaPlayer?.pause()
            stopProgressTicker()
            _playbackState.update { it.copy(isPlaying = false, currentPositionMs = 0L) }
            return
        }

        when (state.repeatMode) {
            RepeatMode.ONE -> {
                seekTo(0L)
                mediaPlayer?.start()
                startProgressTicker()
                _playbackState.update { it.copy(isPlaying = true) }
            }
            RepeatMode.ALL -> {
                playNext()
            }
            RepeatMode.OFF -> {
                if (state.queueIndex < state.queue.size - 1 || state.isAutoplayEnabled) {
                    playNext()
                } else {
                    stopProgressTicker()
                    _playbackState.update { it.copy(isPlaying = false, currentPositionMs = 0L) }
                }
            }
        }
    }

    fun toggleAutoplay() {
        _playbackState.update {
            it.copy(isAutoplayEnabled = !it.isAutoplayEnabled)
        }
    }

    fun setAutoplay(enabled: Boolean) {
        _playbackState.update {
            it.copy(isAutoplayEnabled = enabled)
        }
    }

    fun playSongNext(song: Song) {
        _playbackState.update { state ->
            val mutable = state.queue.toMutableList()
            val insertAt = (state.queueIndex + 1).coerceAtMost(mutable.size)
            mutable.add(insertAt, song)
            state.copy(queue = mutable)
        }
    }

    fun playSongLater(song: Song) {
        _playbackState.update { state ->
            state.copy(queue = state.queue + song)
        }
    }

    fun clearUpcomingQueue() {
        _playbackState.update { state ->
            val upcoming = state.queue.take(state.queueIndex + 1)
            state.copy(queue = upcoming)
        }
    }

    fun clearQueue() {
        clearUpcomingQueue()
    }

    fun closePlayback() {
        scope.launch {
            val mp = mediaPlayer
            val initialVol = _playbackState.value.volume
            // Smooth fluid fade out of volume before stopping
            try {
                for (step in 8 downTo 0) {
                    val v = (initialVol * step) / 8f
                    mp?.setVolume(v, v)
                    delay(20L)
                }
            } catch (ignored: Exception) {}

            mp?.pause()
            ambientMediaPlayer?.pause()
            stopProgressTicker()
            try {
                mp?.setVolume(initialVol, initialVol)
            } catch (ignored: Exception) {}

            _playbackState.update {
                it.copy(
                    isPlaying = false,
                    currentSong = null,
                    currentPositionMs = 0L,
                    durationMs = 0L,
                    queue = emptyList(),
                    queueIndex = 0
                )
            }
        }
    }

    fun moveQueueItem(fromIndex: Int, toIndex: Int) {
        _playbackState.update { state ->
            if (fromIndex in state.queue.indices && toIndex in state.queue.indices) {
                val mutable = state.queue.toMutableList()
                val item = mutable.removeAt(fromIndex)
                mutable.add(toIndex, item)
                val newCurrentIndex = if (fromIndex == state.queueIndex) {
                    toIndex
                } else if (fromIndex < state.queueIndex && toIndex >= state.queueIndex) {
                    state.queueIndex - 1
                } else if (fromIndex > state.queueIndex && toIndex <= state.queueIndex) {
                    state.queueIndex + 1
                } else {
                    state.queueIndex
                }
                state.copy(queue = mutable, queueIndex = newCurrentIndex)
            } else {
                state
            }
        }
    }

    fun toggleShuffle() {
        val state = _playbackState.value
        val newShuffle = !state.isShuffled
        val currentSong = state.currentSong

        val newQueue = if (newShuffle) {
            if (currentSong != null) {
                listOf(currentSong) + (originalQueue.filter { it.id != currentSong.id }.shuffled())
            } else {
                originalQueue.shuffled()
            }
        } else {
            originalQueue
        }

        val newIndex = currentSong?.let { song ->
            newQueue.indexOfFirst { it.id == song.id }.coerceAtLeast(0)
        } ?: 0

        _playbackState.update {
            it.copy(
                isShuffled = newShuffle,
                queue = newQueue,
                queueIndex = newIndex
            )
        }
    }

    fun cycleRepeatMode() {
        _playbackState.update {
            val nextMode = when (it.repeatMode) {
                RepeatMode.OFF -> RepeatMode.ALL
                RepeatMode.ALL -> RepeatMode.ONE
                RepeatMode.ONE -> RepeatMode.OFF
            }
            it.copy(repeatMode = nextMode)
        }
    }

    fun setVolume(volume: Float) {
        val clamped = volume.coerceIn(0.0f, 1.0f)
        mediaPlayer?.setVolume(clamped, clamped)
        _playbackState.update { it.copy(volume = clamped) }
    }

    fun addToQueue(song: Song) {
        _playbackState.update {
            it.copy(queue = it.queue + song)
        }
    }

    fun removeFromQueue(index: Int) {
        _playbackState.update { state ->
            if (index in state.queue.indices && index != state.queueIndex) {
                val updated = state.queue.toMutableList().apply { removeAt(index) }
                val newIndex = if (index < state.queueIndex) state.queueIndex - 1 else state.queueIndex
                state.copy(queue = updated, queueIndex = newIndex)
            } else {
                state
            }
        }
    }

    // --- Sleep Timer with Gradual Volume Fade-Out ---

    fun startSleepTimer(minutes: Int) {
        cancelSleepTimer()
        val totalSeconds = minutes * 60
        masterVolumeBeforeFade = _playbackState.value.volume

        _playbackState.update {
            it.copy(
                isSleepTimerActive = true,
                sleepTimerRemainingSeconds = totalSeconds,
                sleepTimerEndOfTrack = false
            )
        }

        sleepTimerJob = scope.launch {
            var remaining = totalSeconds
            val fadeThreshold = 45.coerceAtMost(totalSeconds / 2).coerceAtLeast(5)

            while (remaining > 0 && isActive) {
                delay(1000L)
                remaining--
                _playbackState.update { it.copy(sleepTimerRemainingSeconds = remaining) }

                // Gradual volume fade-out over final seconds
                if (remaining <= fadeThreshold) {
                    val fadeFraction = remaining.toFloat() / fadeThreshold.toFloat()
                    val targetVolume = (masterVolumeBeforeFade * fadeFraction).coerceIn(0f, 1f)
                    val targetAmbientVolume = (_playbackState.value.ambientVolume * fadeFraction).coerceIn(0f, 1f)
                    mediaPlayer?.setVolume(targetVolume, targetVolume)
                    ambientMediaPlayer?.setVolume(targetAmbientVolume, targetAmbientVolume)
                }
            }

            // Timer expired: smoothly pause and restore volume for subsequent play
            if (isActive) {
                mediaPlayer?.pause()
                ambientMediaPlayer?.pause()
                stopProgressTicker()
                val restored = masterVolumeBeforeFade
                mediaPlayer?.setVolume(restored, restored)
                _playbackState.update {
                    it.copy(
                        isPlaying = false,
                        isSleepTimerActive = false,
                        sleepTimerRemainingSeconds = null,
                        volume = restored
                    )
                }
            }
        }
    }

    fun startSleepTimerEndOfTrack() {
        cancelSleepTimer()
        masterVolumeBeforeFade = _playbackState.value.volume
        val state = _playbackState.value
        val remainingMs = (state.durationMs - state.currentPositionMs).coerceAtLeast(0L)
        val remainingSec = (remainingMs / 1000L).toInt()

        _playbackState.update {
            it.copy(
                isSleepTimerActive = true,
                sleepTimerRemainingSeconds = remainingSec,
                sleepTimerEndOfTrack = true
            )
        }

        sleepTimerJob = scope.launch {
            while (isActive) {
                val current = _playbackState.value
                val remMs = (current.durationMs - current.currentPositionMs).coerceAtLeast(0L)
                val remSec = (remMs / 1000L).toInt()
                _playbackState.update { it.copy(sleepTimerRemainingSeconds = remSec) }

                // Fade out in last 20 seconds of the song
                if (remSec in 1..20) {
                    val fadeFraction = remSec.toFloat() / 20f
                    val targetVolume = (masterVolumeBeforeFade * fadeFraction).coerceIn(0f, 1f)
                    mediaPlayer?.setVolume(targetVolume, targetVolume)
                }
                delay(1000L)
            }
        }
    }

    fun cancelSleepTimer() {
        sleepTimerJob?.cancel()
        sleepTimerJob = null
        val restoredVol = masterVolumeBeforeFade
        mediaPlayer?.setVolume(restoredVol, restoredVol)
        _playbackState.update {
            it.copy(
                isSleepTimerActive = false,
                sleepTimerRemainingSeconds = null,
                sleepTimerEndOfTrack = false
            )
        }
    }

    // --- Ambient Background Playback ---

    fun toggleAmbientPlayback(soundId: String? = null) {
        val current = _playbackState.value
        val targetId = soundId ?: current.selectedAmbientSoundId ?: com.example.data.CuratedMusicData.ambientSounds.first().id

        if (current.isAmbientPlaybackEnabled && soundId == null) {
            stopAmbient()
        } else {
            playAmbientSound(targetId)
        }
    }

    fun playAmbientSound(soundId: String) {
        val sound = com.example.data.CuratedMusicData.ambientSounds.find { it.id == soundId }
            ?: com.example.data.CuratedMusicData.ambientSounds.first()

        try {
            ambientMediaPlayer?.release()
            ambientMediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                isLooping = true
                setDataSource(sound.audioUrl)
                val vol = _playbackState.value.ambientVolume
                setVolume(vol, vol)
                setOnPreparedListener { mp ->
                    mp.start()
                    _playbackState.update {
                        it.copy(
                            isAmbientPlaybackEnabled = true,
                            selectedAmbientSoundId = sound.id
                        )
                    }
                }
                setOnErrorListener { _, what, extra ->
                    Log.e(tag, "Ambient player error: $what $extra")
                    _playbackState.update { it.copy(isAmbientPlaybackEnabled = false) }
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            Log.e(tag, "Failed to start ambient sound: ${sound.name}", e)
            _playbackState.update { it.copy(isAmbientPlaybackEnabled = false) }
        }
    }

    fun setAmbientVolume(volume: Float) {
        val clamped = volume.coerceIn(0.0f, 1.0f)
        ambientMediaPlayer?.setVolume(clamped, clamped)
        _playbackState.update { it.copy(ambientVolume = clamped) }
    }

    fun stopAmbient() {
        try {
            ambientMediaPlayer?.stop()
            ambientMediaPlayer?.release()
            ambientMediaPlayer = null
        } catch (e: Exception) {
            Log.e(tag, "Error stopping ambient player", e)
        }
        _playbackState.update { it.copy(isAmbientPlaybackEnabled = false) }
    }

    private fun startProgressTicker() {
        stopProgressTicker()
        progressJob = scope.launch {
            while (isActive) {
                val mp = mediaPlayer
                if (mp != null && mp.isPlaying) {
                    val currentPos = mp.currentPosition.toLong()
                    val dur = mp.duration.toLong().coerceAtLeast(_playbackState.value.durationMs)
                    _playbackState.update {
                        it.copy(currentPositionMs = currentPos, durationMs = dur)
                    }
                }
                delay(250L)
            }
        }
    }

    private fun stopProgressTicker() {
        progressJob?.cancel()
        progressJob = null
    }

    fun release() {
        stopProgressTicker()
        sleepTimerJob?.cancel()
        sleepTimerJob = null
        mediaPlayer?.release()
        mediaPlayer = null
        ambientMediaPlayer?.release()
        ambientMediaPlayer = null
    }
}
