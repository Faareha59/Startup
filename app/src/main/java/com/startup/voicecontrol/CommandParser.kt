package com.startup.voicecontrol

class CommandParser {
    private val phoneRegex = Regex("(?:\\+92|0)?[0-9]{10,12}")

    fun parse(transcript: String): VoiceCommand {
        val text = transcript.lowercase().trim()
        val number = phoneRegex.find(text)?.value?.normalizePhone() ?: ""

        if (number.isNotBlank() && text.containsAny(
                "save this number", "save number", "add contact", "is number ko save", "number save karo", "contact mein save"
            )
        ) {
            val spokenName = extractName(text)
            return VoiceCommand.SaveContact(number, spokenName)
        }

        if (number.isNotBlank() && text.containsAny(
                "whatsapp", "what's app", "message this number", "message bhejo", "whatsapp kholo"
            )
        ) {
            return VoiceCommand.WhatsAppMessage(number)
        }

        if (number.isNotBlank() && text.containsAny("call", "phone", "dial", "call karo", "phone lagao")) {
            return VoiceCommand.CallNumber(number)
        }

        if (number.isNotBlank() && text.containsAny("sms", "text", "message", "paigham", "msg")) {
            val body = text.substringAfter(number, "hello").trim().ifBlank { "Hello" }
            return VoiceCommand.SendSms(number, body)
        }

        if (text.containsAny("open camera", "camera kholo")) {
            return VoiceCommand.OpenApp("com.android.camera")
        }

        if (text.containsAny("open contacts", "contacts kholo")) {
            return VoiceCommand.OpenApp("com.android.contacts")
        }

        return VoiceCommand.Unknown
    }

    private fun extractName(text: String): String {
        val byNameToken = listOf("name", "naam")
        byNameToken.forEach { token ->
            if (text.contains(token)) {
                return text.substringAfter(token).replace(phoneRegex, "").trim().replaceFirstChar {
                    it.uppercase()
                }.ifBlank { "New Contact" }
            }
        }
        return "New Contact"
    }

    private fun String.containsAny(vararg patterns: String): Boolean {
        return patterns.any { this.contains(it) }
    }

    private fun String.normalizePhone(): String {
        val digits = this.filter { it.isDigit() }
        return when {
            digits.startsWith("92") && digits.length == 12 -> "+$digits"
            digits.startsWith("0") && digits.length == 11 -> "+92${digits.drop(1)}"
            digits.length == 10 -> "+92$digits"
            else -> this
        }
    }
}
