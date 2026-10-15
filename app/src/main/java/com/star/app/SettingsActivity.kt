package com.star.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    
    private lateinit var apiKeyInput: EditText
    private lateinit var voiceSlider: SeekBar
    private lateinit var voiceLabel: TextView
    private lateinit var saveButton: Button
    private lateinit var clearButton: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        
        initViews()
        loadSavedSettings()
        setupListeners()
    }
    
    private fun initViews() {
        apiKeyInput = findViewById(R.id.apiKeyInput)
        voiceSlider = findViewById(R.id.voiceSlider)
        voiceLabel = findViewById(R.id.voiceLabel)
        saveButton = findViewById(R.id.saveButton)
        clearButton = findViewById(R.id.clearButton)
    }
    
    private fun loadSavedSettings() {
        val prefs = getSharedPreferences("star_prefs", MODE_PRIVATE)
        val savedKey = prefs.getString("api_key", "")
        val voiceLevel = prefs.getInt("voice_level", 5)
        
        apiKeyInput.setText(savedKey)
        voiceSlider.progress = voiceLevel
        updateVoiceLabel(voiceLevel)
    }
    
    private fun setupListeners() {
        saveButton.setOnClickListener {
            saveSettings()
        }
        
        clearButton.setOnClickListener {
            apiKeyInput.text.clear()
            voiceSlider.progress = 5
            updateVoiceLabel(5)
        }
        
        voiceSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateVoiceLabel(progress)
            }
            
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
    
    private fun updateVoiceLabel(level: Int) {
        voiceLabel.text = "Voice Control: $level / 10"
    }
    
    private fun saveSettings() {
        val apiKey = apiKeyInput.text.toString().trim()
        
        if (apiKey.isEmpty()) {
            Toast.makeText(this, "Please enter API key", Toast.LENGTH_SHORT).show()
            return
        }
        
        val prefs = getSharedPreferences("star_prefs", MODE_PRIVATE)
        prefs.edit().apply {
            putString("api_key", apiKey)
            putInt("voice_level", voiceSlider.progress)
            apply()
        }
        
        Toast.makeText(this, "Settings saved!", Toast.LENGTH_SHORT).show()
        finish()
    }
}
