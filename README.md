# Voice Control Phone (English + Roman Urdu)

An Android app starter that listens to voice commands in English or Roman Urdu and performs basic phone actions hands-free.

## Features
- Speech recognition with free-form commands.
- Dual-language command parsing (English + Roman Urdu patterns).
- Core actions:
  - Save contact from spoken number.
  - Open WhatsApp chat with spoken number.
  - Place phone call.
  - Open SMS composer with message text.
  - Open installed apps like Camera / Contacts.

## Example voice commands
- "Save this number 03001234567"
- "Is number ko save karo 03001234567"
- "Go to WhatsApp and message 03001234567"
- "WhatsApp kholo aur message bhejo 03001234567"
- "Call 03001234567"
- "SMS bhejo 03001234567 hello"

## How it works
1. `MainActivity` starts Android `SpeechRecognizer`.
2. Transcript is sent to `CommandParser`.
3. Parsed `VoiceCommand` is executed by `PhoneActionHandler` using Android intents / Contacts provider.

## Run
1. Open in Android Studio (Hedgehog+ recommended).
2. Let Gradle sync.
3. Run on an Android phone.
4. Grant permissions when prompted:
   - Microphone
   - Contacts
   - Phone
   - SMS

## Notes
- This version uses pattern matching for simple commands.
- For "full control" production behavior, add:
  - Wake-word/background service.
  - Safer confirmation layer for destructive actions.
  - Accessibility service integration for deeper UI automation.
  - On-device/offline ASR + multilingual NLP.
