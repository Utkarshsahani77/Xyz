package com.example.util

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin

/**
 * High-fidelity acoustic synthesizer for the iconic Sony PlayStation 5 (PS5) startup chime & ambient bloom sound.
 * Plays a spatialized, crystal-resonant chord swell with sub-bass, harmonic synth pad,
 * and ethereal crystal shimmer.
 */
object PlayStationSoundPlayer {

    private const val TAG = "PS5SoundPlayer"
    private const val SAMPLE_RATE = 44100
    private const val DURATION_SECONDS = 3.2

    var isSoundEnabled: Boolean = true

    // Cached PCM stereo buffer (16-bit)
    private var cachedPcmData: ByteArray? = null
    private val scope = CoroutineScope(Dispatchers.Default + Job())

    init {
        // Pre-generate the audio buffer in the background for zero-latency startup
        scope.launch {
            getOrGeneratePcmData()
        }
    }

    /**
     * Plays the authentic Sony PlayStation 5 startup sound immediately.
     */
    fun playPS5StartupSound(onComplete: (() -> Unit)? = null) {
        if (!isSoundEnabled) {
            onComplete?.invoke()
            return
        }

        scope.launch {
            try {
                val pcmData = getOrGeneratePcmData()
                val minBufferSize = AudioTrack.getMinBufferSize(
                    SAMPLE_RATE,
                    AudioFormat.CHANNEL_OUT_STEREO,
                    AudioFormat.ENCODING_PCM_16BIT
                )

                val bufferSize = pcmData.size.coerceAtLeast(minBufferSize)

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(SAMPLE_RATE)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                audioTrack.play()
                audioTrack.write(pcmData, 0, pcmData.size)

                // Wait for playback to complete before releasing
                val sleepMs = (DURATION_SECONDS * 1000).toLong()
                kotlinx.coroutines.delay(sleepMs)

                try {
                    audioTrack.stop()
                    audioTrack.release()
                } catch (ignored: Exception) {}

                onComplete?.invoke()
            } catch (e: Exception) {
                Log.e(TAG, "Error playing PS5 startup sound", e)
                onComplete?.invoke()
            }
        }
    }

    @Synchronized
    private fun getOrGeneratePcmData(): ByteArray {
        cachedPcmData?.let { return it }

        val totalSamples = (SAMPLE_RATE * DURATION_SECONDS).toInt()
        val numChannels = 2 // Stereo
        val bytesPerSample = 2 // 16-bit
        val buffer = ByteArray(totalSamples * numChannels * bytesPerSample)

        // Musical frequencies for the PS5 D-major / G-major ambient swell
        val beepFreq = 1864.66 // Bb6 initial crystalline chime
        val beepOvertone = 3729.31

        val subBassFreq = 73.42 // D2 sub drone
        val fundamentalFreq = 146.83 // D3
        val fifthFreq = 220.00 // A3
        val octaveFreq = 293.66 // D4
        val thirdFreq = 369.99 // F#4
        val highFifthFreq = 440.00 // A4
        val highOctaveFreq = 587.33 // D5

        // High ethereal crystal harmonics (PS5 signature shimmer)
        val shimmer1 = 880.00 // A5
        val shimmer2 = 1174.66 // D6
        val shimmer3 = 1479.98 // F#6
        val shimmer4 = 1760.00 // A6
        val shimmer5 = 2349.32 // D7

        var byteIndex = 0

        for (i in 0 until totalSamples) {
            val t = i.toDouble() / SAMPLE_RATE

            // 1. Initial crystal startup ping (0ms to 180ms)
            val beepEnv = if (t < 0.25) {
                val attack = (t / 0.005).coerceIn(0.0, 1.0)
                attack * exp(-t * 22.0)
            } else 0.0

            val beepSignal = beepEnv * (
                0.7 * sin(2.0 * PI * beepFreq * t) +
                0.3 * sin(2.0 * PI * beepOvertone * t)
            )

            // 2. Warm Orchestral Synth Swell Envelope
            // Soft cinematic fade-in peaking at ~850ms, then long warm decay
            val swellEnv = when {
                t < 0.85 -> {
                    val p = t / 0.85
                    // Smooth S-curve swell
                    0.5 * (1.0 - cos(PI * p))
                }
                else -> {
                    val decayT = t - 0.85
                    exp(-decayT * 0.95)
                }
            }

            // 3. Sub-bass and body harmonics
            val subBass = 0.32 * sin(2.0 * PI * subBassFreq * t)
            val fundamental = 0.28 * sin(2.0 * PI * fundamentalFreq * t)
            val fifth = 0.22 * sin(2.0 * PI * fifthFreq * t)
            val octave = 0.20 * sin(2.0 * PI * octaveFreq * t)
            val third = 0.18 * sin(2.0 * PI * thirdFreq * t)
            val highFifth = 0.15 * sin(2.0 * PI * highFifthFreq * t)
            val highOctave = 0.12 * sin(2.0 * PI * highOctaveFreq * t)

            val bodyPad = subBass + fundamental + fifth + octave + third + highFifth + highOctave

            // 4. Shimmering Crystal Overtones with gentle chorus LFO (3.2 Hz)
            val lfo = 1.0 + 0.08 * sin(2.0 * PI * 3.2 * t)
            val shimmerEnv = if (t > 0.15) {
                val st = t - 0.15
                if (st < 0.7) (st / 0.7) else exp(-(st - 0.7) * 1.1)
            } else 0.0

            val shimmerLeft = shimmerEnv * (
                0.10 * sin(2.0 * PI * (shimmer1 * 0.998) * t * lfo) +
                0.08 * sin(2.0 * PI * (shimmer2 * 1.002) * t) +
                0.06 * sin(2.0 * PI * (shimmer3 * 0.999) * t * lfo) +
                0.05 * sin(2.0 * PI * (shimmer4 * 1.001) * t) +
                0.03 * sin(2.0 * PI * shimmer5 * t)
            )

            val shimmerRight = shimmerEnv * (
                0.10 * sin(2.0 * PI * (shimmer1 * 1.002) * t * lfo) +
                0.08 * sin(2.0 * PI * (shimmer2 * 0.998) * t) +
                0.06 * sin(2.0 * PI * (shimmer3 * 1.001) * t * lfo) +
                0.05 * sin(2.0 * PI * (shimmer4 * 0.999) * t) +
                0.03 * sin(2.0 * PI * shimmer5 * t)
            )

            // Combine into Left and Right channels with wide stereo bloom
            val leftSample = (beepSignal * 0.8 + bodyPad * swellEnv * 0.65 + shimmerLeft * 0.55)
            val rightSample = (beepSignal * 0.8 + bodyPad * swellEnv * 0.65 + shimmerRight * 0.55)

            // Clamp to 16-bit signed range [-32767, 32767]
            val leftInt = (leftSample.coerceIn(-1.0, 1.0) * 32000).toInt().toShort()
            val rightInt = (rightSample.coerceIn(-1.0, 1.0) * 32000).toInt().toShort()

            // Write Left channel (Little-endian)
            buffer[byteIndex++] = (leftInt.toInt() and 0xFF).toByte()
            buffer[byteIndex++] = ((leftInt.toInt() shr 8) and 0xFF).toByte()

            // Write Right channel (Little-endian)
            buffer[byteIndex++] = (rightInt.toInt() and 0xFF).toByte()
            buffer[byteIndex++] = ((rightInt.toInt() shr 8) and 0xFF).toByte()
        }

        cachedPcmData = buffer
        return buffer
    }
}
