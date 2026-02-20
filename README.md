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
- Supports non-number commands like `open YouTube`, `open WhatsApp`, and `open Google`

## Project files
- `index.html` → upgraded UI layout
- `styles.css` → modern glassmorphism + responsive styling
- `app.js` → speech recognition, parser, actions

## Run locally
```bash
python3 -m http.server 8080
```
Open `http://localhost:8080` in Chrome.

## Run on GitHub (GitHub Pages)
You can host this website directly from your GitHub repo.

### 1) Push your code to GitHub
```bash
git remote add origin https://github.com/<your-username>/<your-repo>.git
git push -u origin <your-branch>
```

### 2) Enable GitHub Pages
1. Open your repository on GitHub.
2. Go to **Settings** → **Pages**.
3. Under **Build and deployment**:
   - **Source**: `Deploy from a branch`
   - **Branch**: choose your branch (commonly `main`) and folder `/ (root)`
4. Click **Save**.

### 3) Open your live site
After 1–3 minutes, GitHub shows a URL like:

`https://<your-username>.github.io/<your-repo>/`

Use that URL to run the app on the web.

## Important for microphone on GitHub Pages
- GitHub Pages uses HTTPS, so microphone access works in supported browsers.
- Open site in Chrome/Edge and allow microphone permission when prompted.

## Notes
- Browser limitations apply; this is a web demo, not full OS-level phone automation.
- Best results on Chrome/Chromium with microphone permission enabled.
