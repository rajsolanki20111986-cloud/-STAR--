package com.star.app

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.TextSwitcher
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewSwitcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {
    
    private lateinit var chatDisplay: TextSwitcher
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var micButton: ImageButton
    private lateinit var speakerButton: ImageButton
    private lateinit var copyButton: ImageButton
    private lateinit var scrollView: ScrollView
    
    private lateinit var geminiManager: GeminiManager
    private var voiceMode = false
    private val chatMessages = mutableListOf<String>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        
        val apiKey = intent.getStringExtra("api_key") ?: ""
        voiceMode = intent.getBooleanExtra("voice_mode", false)
        
        geminiManager = GeminiManager(apiKey)
        
        initViews()
        setupListeners()
        
        displayMessage("STAR", Persona.getGreeting())
    }
    
    private fun initViews() {
        chatDisplay = findViewById(R.id.chatDisplay)
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)
        micButton = findViewById(R.id.micButton)
        speakerButton = findViewById(R.id.speakerButton)
        copyButton = findViewById(R.id.copyButton)
        scrollView = findViewById(R.id.scrollView)
        
        chatDisplay.setFactory {
            TextView(this).apply {
                textSize = 16f
                setTextColor(0xFF000000.toInt())
            }
        }
    }
    
    private fun setupListeners() {
        sendButton.setOnClickListener {
            val message = messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message)
                messageInput.text.clear()
            }
        }
        
        micButton.setOnClickListener {
            // Voice input - connect to speech recognition
            Toast.makeText(this, "Voice input - to be implemented", Toast.LENGTH_SHORT).show()
        }
        
        speakerButton.setOnClickListener {
            // Text to speech for last STAR message
            Toast.makeText(this, "Play audio - to be implemented", Toast.LENGTH_SHORT).show()
        }
        
        copyButton.setOnClickListener {
            copyLastMessage()
        }
    }
    
    private fun sendMessage(text: String) {
        displayMessage("You", text)
        
        lifecycleScope.launch {
            try {
                displayMessage("STAR", Persona.getLoadingMessage())
                val response = geminiManager.sendMessage(text)
                displayMessage("STAR", response)
            } catch (e: Exception) {
                displayMessage("STAR", Persona.getErrorMessage())
            }
        }
    }
    
    private fun displayMessage(sender: String, message: String) {
        val formatted = if (sender == "You") {
            "You: $message"
        } else {
            "STAR: $message"
        }
        
        chatMessages.add(formatted)
        chatDisplay.setText(chatMessages.joinToString("\n\n"))
        scrollView.post { scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
    }
    
    private fun copyLastMessage() {
        if (chatMessages.isNotEmpty()) {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("STAR message", chatMessages.last())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Message copied", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onDestroy() {
        geminiManager.clearHistory()
        super.onDestroy()
    }
}
