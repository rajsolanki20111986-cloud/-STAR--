# STAR - AI Assistant App

STAR is a confident, friendly, and human-like Android AI assistant powered by Google's Gemini API.

## Features

- 🎤 **Voice-to-Voice Chat** - Talk directly with STAR via voice
- 💬 **Text Chat Mode** - Type messages and get instant responses
- 🔊 **Voice Control** - Adjustable voice level (0-10)
- 💾 **Message Copy** - Copy STAR's responses easily
- ⚙️ **Settings** - Configure API key and voice preferences

## Requirements

- Android 7.0+ (API 24)
- Google Gemini API key
- Internet connection

## Setup

1. **Get Gemini API Key:**
   - Visit [Google AI Studio](https://aistudio.google.com/)
   - Create a new API key
   - Copy the API key

2. **Install App:**
   - Build APK using GitHub Actions
   - Download APK from build artifacts
   - Install on Android device

3. **Configure:**
   - Open STAR app
   - Tap Settings (⚙️)
   - Paste API key
   - Adjust voice level
   - Tap Save

## Architecture

```
MainActivity (Voice Button)
├── SettingsActivity (API Key + Voice Control)
└── ChatActivity (Chat Interface)
    ├── GeminiManager (API Calls)
    ├── AudioStreamer (Voice I/O)
    └── Persona (Personality & Prompts)
```

## Files Structure

```
STAR-App/
├── app/
│   ├── src/main/java/com/star/app/
│   │   ├── Persona.kt
│   │   ├── GeminiManager.kt
│   │   ├── AudioStreamer.kt
│   │   ├── MainActivity.kt
│   │   ├── ChatActivity.kt
│   │   └── SettingsActivity.kt
│   ├── src/main/res/
│   │   ├── layout/
│   │   └── values/
│   └── build.gradle
├── .github/workflows/
│   └── build.yml
└── build.gradle.kts
```

## Building

### Local Build
```bash
./gradlew assembleDebug
```

### GitHub Actions
- Push to GitHub
- Actions automatically builds APK
- Download APK from artifacts

## Permissions

- `RECORD_AUDIO` - Microphone access for voice input
- `INTERNET` - API communication
- `MODIFY_AUDIO_SETTINGS` - Speaker control

## STAR Personality

- Confident & friendly
- Human-like conversation
- Context-aware responses
- Adaptive to user mood
- Takes initiative when appropriate

## Version

**v1.0** - Initial Release

## Build Info

- Compiled with: Kotlin 1.9.23
- Target SDK: 35
- Min SDK: 24
- Gradle: 8.1.0

---

Made with ❤️ by Raj
