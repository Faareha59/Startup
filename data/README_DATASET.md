# Voice Intent Dataset (Elder-friendly)

This is a starter dataset for training an intent model for English + Roman Urdu voice commands.

## Files
- `voice_intents_train.jsonl` - training samples
- `voice_intents_dev.jsonl` - validation samples
- `intent_schema.json` - intent labels and field schema

## Covered scenarios
- Opening apps/sites (WhatsApp, YouTube, Facebook, Snapchat)
- Contact save flow (number + ask name + confirm name)
- Calling and chatting with known contacts
- Voice note and image sending intents
- Scroll/navigation + saved items access
- Help/unknown intent

## Data format
Each line is JSON:
```json
{"text":"mujhe whatsapp khol do","intent":"open_whatsapp","language":"mixed","target_users":"older_adults","entities":{}}
```

## Notes
- This is synthetic starter data for rapid prototyping.
- For production, add real anonymized utterances from target users (older adults), including accent/spelling variation and noisy ASR text.
