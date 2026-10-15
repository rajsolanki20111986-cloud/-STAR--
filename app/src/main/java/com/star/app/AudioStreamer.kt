package com.star.app

import android.media.MediaPlayer
import android.media.MediaRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class AudioStreamer(private val cacheDir: File) {

    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var audioFile: File? = null
    private var isRecording = false

    fun startRecording(): Boolean {
        return try {
            audioFile = File(cacheDir, "voice_${System.currentTimeMillis()}.m4a")

            // MediaRecorder() is deprecated on newer Android versions but remains
            // available and avoids requiring a Context in this File-based class.
            @Suppress("DEPRECATION")
            mediaRecorder = MediaRecorder()

            mediaRecorder?.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioSamplingRate(16000)
                setAudioEncodingBitRate(128000)
                setOutputFile(audioFile!!.absolutePath)
                prepare()
                start()
            }

            isRecording = true
            true
        } catch (e: Exception) {
            try {
                mediaRecorder?.release()
            } catch (_: Exception) {
            }
            mediaRecorder = null
            isRecording = false
            false
        }
    }

    fun stopRecording(): File? {
        return try {
            val recorder = mediaRecorder ?: return null
            recorder.stop()
            recorder.release()
            mediaRecorder = null
            isRecording = false
            audioFile
        } catch (e: Exception) {
            try {
                mediaRecorder?.release()
            } catch (_: Exception) {
            }
            mediaRecorder = null
            isRecording = false
            null
        }
    }

    suspend fun playAudio(filePath: String) {
        withContext(Dispatchers.Main) {
            try {
                stopAudio()
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(filePath)
                    prepare()
                    start()
                }
            } catch (e: Exception) {
                stopAudio()
            }
        }
    }

    fun stopAudio() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
            }
        } catch (_: Exception) {
        } finally {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    fun isRecordingNow(): Boolean = isRecording

    fun release() {
        stopRecording()
        stopAudio()
    }
}
