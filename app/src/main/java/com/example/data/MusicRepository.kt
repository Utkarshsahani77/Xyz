package com.example.data

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.data.database.AppDatabase
import com.example.data.database.PlaylistEntity
import com.example.data.database.PlaylistSongCrossRef
import com.example.data.database.SongEntity
import com.example.model.Playlist
import com.example.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder
import java.util.UUID

class MusicRepository(private val context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val musicDao = db.musicDao()
    private val httpClient = OkHttpClient.Builder().build()

    suspend fun initializeDatabase() = withContext(Dispatchers.IO) {
        val existing = musicDao.getAllSongs().first()
        if (existing.isEmpty()) {
            val entities = CuratedMusicData.curatedSongs.map { song ->
                SongEntity(
                    id = song.id,
                    title = song.title,
                    artist = song.artist,
                    album = song.album,
                    durationMs = song.durationMs,
                    coverUrl = song.coverUrl,
                    streamUrl = song.streamUrl,
                    isDownloaded = false,
                    isFavorite = false,
                    genre = song.genre,
                    lastPlayedTimestamp = 0L
                )
            }
            musicDao.insertSongs(entities)

            // Seed user playlist "Favorites" & "Utkarsh Heavy Rotation"
            musicDao.insertPlaylist(
                PlaylistEntity(
                    id = "pl_favorites",
                    name = "Favorite Songs",
                    description = "Songs you've loved on Utkarsh Music",
                    coverUrl = CuratedMusicData.curatedSongs[0].coverUrl
                )
            )
            musicDao.insertPlaylist(
                PlaylistEntity(
                    id = "pl_rotation",
                    name = "Heavy Rotation Mix",
                    description = "Your most played tracks this week",
                    coverUrl = CuratedMusicData.curatedSongs[1].coverUrl
                )
            )
        }
    }

    fun getAllLibrarySongs(): Flow<List<Song>> {
        return musicDao.getAllSongs().map { entities ->
            entities.map { it.toSong() }
        }.flowOn(Dispatchers.IO)
    }

    fun getDownloadedSongs(): Flow<List<Song>> {
        return musicDao.getDownloadedSongs().map { entities ->
            entities.map { it.toSong() }
        }.flowOn(Dispatchers.IO)
    }

    fun getFavoriteSongs(): Flow<List<Song>> {
        return musicDao.getFavoriteSongs().map { entities ->
            entities.map { it.toSong() }
        }.flowOn(Dispatchers.IO)
    }

    fun getRecentlyPlayedSongs(): Flow<List<Song>> {
        return musicDao.getRecentlyPlayedSongs().map { entities ->
            entities.map { it.toSong() }
        }.flowOn(Dispatchers.IO)
    }

    fun getUserPlaylists(): Flow<List<Playlist>> {
        return musicDao.getAllPlaylists().map { entities ->
            entities.map { entity ->
                Playlist(
                    id = entity.id,
                    name = entity.name,
                    description = entity.description,
                    coverUrl = entity.coverUrl,
                    isCurated = false
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun toggleFavorite(song: Song) = withContext(Dispatchers.IO) {
        val newFav = !song.isFavorite
        ensureSongSaved(song)
        musicDao.setFavorite(song.id, newFav)
    }

    suspend fun recordPlayed(song: Song) = withContext(Dispatchers.IO) {
        ensureSongSaved(song)
        musicDao.updateLastPlayed(song.id, System.currentTimeMillis())
    }

    private suspend fun ensureSongSaved(song: Song) {
        val existing = musicDao.getSongById(song.id)
        if (existing == null) {
            musicDao.insertOrUpdateSong(
                SongEntity(
                    id = song.id,
                    title = song.title,
                    artist = song.artist,
                    album = song.album,
                    durationMs = song.durationMs,
                    coverUrl = song.coverUrl,
                    streamUrl = song.streamUrl,
                    localFilePath = if (song.isLocal) song.streamUrl else null,
                    isDownloaded = song.isDownloaded,
                    isFavorite = song.isFavorite,
                    genre = song.genre
                )
            )
        }
    }

    suspend fun downloadSong(song: Song, onProgress: (Float) -> Unit = {}): Boolean = withContext(Dispatchers.IO) {
        try {
            ensureSongSaved(song)
            if (song.isDownloaded && song.streamUrl.startsWith("file://")) {
                return@withContext true
            }

            // Download bytes to app filesDir/music
            val musicDir = File(context.filesDir, "downloads").apply { if (!exists()) mkdirs() }
            val sanitizedName = song.id.replace(Regex("[^a-zA-Z0-9_]"), "_") + ".mp3"
            val targetFile = File(musicDir, sanitizedName)

            val request = Request.Builder().url(song.streamUrl).build()
            val response = httpClient.newCall(request).execute()

            if (response.isSuccessful && response.body != null) {
                val body = response.body!!
                val inputStream = body.byteStream()
                val outputStream = FileOutputStream(targetFile)
                val totalLength = body.contentLength()
                val buffer = ByteArray(8192)
                var bytesRead: Int
                var totalRead = 0L

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                    totalRead += bytesRead
                    if (totalLength > 0) {
                        onProgress(totalRead.toFloat() / totalLength)
                    }
                }
                outputStream.flush()
                outputStream.close()
                inputStream.close()

                musicDao.setDownloaded(song.id, true, targetFile.absolutePath)
                return@withContext true
            }
            false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun createPlaylist(name: String, description: String = ""): Playlist = withContext(Dispatchers.IO) {
        val newId = "user_pl_" + UUID.randomUUID().toString().take(8)
        val defaultCover = CuratedMusicData.curatedSongs.random().coverUrl
        val entity = PlaylistEntity(
            id = newId,
            name = name,
            description = description.ifEmpty { "Created in Utkarsh Music" },
            coverUrl = defaultCover
        )
        musicDao.insertPlaylist(entity)
        Playlist(
            id = newId,
            name = name,
            description = entity.description,
            coverUrl = defaultCover,
            isCurated = false
        )
    }

    suspend fun addSongToPlaylist(playlistId: String, songId: String) = withContext(Dispatchers.IO) {
        musicDao.addSongToPlaylist(
            PlaylistSongCrossRef(playlistId = playlistId, songId = songId)
        )
    }

    fun getSongsForPlaylist(playlistId: String): Flow<List<Song>> {
        return musicDao.getSongsForPlaylist(playlistId).map { entities ->
            entities.map { it.toSong() }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Scan device MediaStore for local audio files (MP3, M4A, FLAC, WAV)
     */
    suspend fun scanDeviceAudioFiles(): List<Song> = withContext(Dispatchers.IO) {
        val localSongs = mutableListOf<Song>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        try {
            val cursor = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                "${MediaStore.Audio.Media.TITLE} ASC"
            )

            cursor?.use {
                val idCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val titleCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artistCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val albumCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                val durationCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

                while (it.moveToNext()) {
                    val id = it.getLong(idCol)
                    val title = it.getString(titleCol) ?: "Unknown Track"
                    val artist = it.getString(artistCol) ?: "Unknown Artist"
                    val album = it.getString(albumCol) ?: "Unknown Album"
                    val duration = it.getLong(durationCol)
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        id
                    ).toString()

                    val song = Song(
                        id = "local_$id",
                        title = title,
                        artist = artist,
                        album = album,
                        durationMs = if (duration > 0) duration else 180000L,
                        coverUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800&auto=format&fit=crop&q=80",
                        streamUrl = contentUri,
                        isLocal = true,
                        isDownloaded = true,
                        audioQualityBadge = "HI-RES LOSSLESS"
                    )
                    localSongs.add(song)
                    ensureSongSaved(song)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        localSongs
    }

    /**
     * Search online songs via open Apple / iTunes Search API with real 30s previews and 600x600 artwork
     */
    suspend fun searchOnlineSongs(query: String): List<Song> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()
        val results = mutableListOf<Song>()
        try {
            val encodedQuery = URLEncoder.encode(query, "UTF-8")
            val url = "https://itunes.apple.com/search?term=$encodedQuery&entity=song&limit=25"
            val request = Request.Builder().url(url).build()
            val response = httpClient.newCall(request).execute()

            if (response.isSuccessful && response.body != null) {
                val jsonString = response.body!!.string()
                val json = JSONObject(jsonString)
                val array = json.optJSONArray("results")
                if (array != null) {
                    for (i in 0 until array.length()) {
                        val item = array.getJSONObject(i)
                        val trackId = item.optLong("trackId", 0L)
                        val trackName = item.optString("trackName", "")
                        val artistName = item.optString("artistName", "")
                        val collectionName = item.optString("collectionName", "")
                        val previewUrl = item.optString("previewUrl", "")
                        var artworkUrl = item.optString("artworkUrl100", "")
                        if (artworkUrl.isNotEmpty()) {
                            // Upgrade to 600x600 high resolution album art
                            artworkUrl = artworkUrl.replace("100x100bb", "600x600bb")
                        }
                        val trackTimeMillis = item.optLong("trackTimeMillis", 180000L)
                        val genre = item.optString("primaryGenreName", "Pop")

                        if (trackName.isNotEmpty() && previewUrl.isNotEmpty()) {
                            results.add(
                                Song(
                                    id = "itunes_$trackId",
                                    title = trackName,
                                    artist = artistName,
                                    album = collectionName.ifEmpty { "Single" },
                                    durationMs = trackTimeMillis,
                                    coverUrl = artworkUrl,
                                    streamUrl = previewUrl,
                                    genre = genre,
                                    audioQualityBadge = "UTKARSH MASTER"
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        results
    }

    private fun SongEntity.toSong(): Song {
        val matchingCurated = CuratedMusicData.curatedSongs.find { it.id == id }
        val effectiveStream = localFilePath?.let { "file://$it" } ?: streamUrl
        return Song(
            id = id,
            title = title,
            artist = artist,
            album = album,
            durationMs = durationMs,
            coverUrl = coverUrl,
            streamUrl = effectiveStream,
            isLocal = localFilePath != null || streamUrl.startsWith("content://"),
            isDownloaded = isDownloaded,
            isFavorite = isFavorite,
            genre = genre,
            audioQualityBadge = matchingCurated?.audioQualityBadge ?: "LOSSLESS",
            lyrics = matchingCurated?.lyrics ?: emptyList()
        )
    }
}
