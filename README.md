# Voice Control Website (English + Roman Urdu)

A modern single-page voice assistant demo for browser usage.

## What it does
- Listens to voice commands in English + Roman Urdu
- Parses commands for:
  - save contact number (downloads `.vcf`)
  - open WhatsApp chat
  - call (`tel:`)
  - SMS (`sms:`)
- Shows live status, transcript, action output, and saved-contact list
- Includes clickable sample command chips for quick demo

## Project files
- `index.html` → upgraded UI layout
- `styles.css` → modern glassmorphism + responsive styling
- `app.js` → speech recognition, parser, actions

## Run
```bash
python3 -m http.server 8080
```
Open `http://localhost:8080` in Chrome.

## Notes
- Browser limitations apply; this is a web demo, not full OS-level phone automation.
- Best results on Chrome/Chromium with microphone permission enabled.
