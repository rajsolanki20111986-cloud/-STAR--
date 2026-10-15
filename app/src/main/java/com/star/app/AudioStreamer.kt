package com.star.app

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
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
            
            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(cacheDir.context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }
            
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
            false
        }
    }
    
    fun stopRecording(): File? {
        return try {
            if (mediaRecorder != null) {
                mediaRecorder!!.stop()
                mediaRecorder!!.release()
                mediaRecorder = null
            }
            isRecording = false
            audioFile
        } catch (e: Exception) {
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
                // Handle error
            }
        }
    }
    
    fun stopAudio() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
            }
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            // Handle error
        }
    }
    
    fun isRecordingNow(): Boolean = isRecording
    
    fun release() {
        stopRecording()
        stopAudio()
    }
}
