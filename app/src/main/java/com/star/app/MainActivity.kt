package com.star.app

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    
    private val RECORD_AUDIO_PERMISSION_CODE = 1
    private var apiKey: String = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val voiceButton = findViewById<Button>(R.id.voiceButton)
        val settingsButton = findViewById<ImageButton>(R.id.settingsButton)
        val chatButton = findViewById<Button>(R.id.chatButton)
        
        voiceButton.text = "🎤 STAR\nPress to speak"
        
        voiceButton.setOnClickListener {
            checkPermissionAndStartChat(isVoiceMode = true)
        }
        
        chatButton.setOnClickListener {
            checkPermissionAndStartChat(isVoiceMode = false)
        }
        
        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
    
    private fun checkPermissionAndStartChat(isVoiceMode: Boolean) {
        val prefs = getSharedPreferences("star_prefs", MODE_PRIVATE)
        apiKey = prefs.getString("api_key", "") ?: ""
        
        if (apiKey.isEmpty()) {
            Toast.makeText(this, "Please set API key in settings", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, SettingsActivity::class.java))
            return
        }
        
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startChat(isVoiceMode)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_PERMISSION_CODE
            )
        }
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if (requestCode == RECORD_AUDIO_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startChat(isVoiceMode = true)
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun startChat(isVoiceMode: Boolean) {
        val intent = Intent(this, ChatActivity::class.java).apply {
            putExtra("api_key", apiKey)
            putExtra("voice_mode", isVoiceMode)
        }
        startActivity(intent)
    }
}
