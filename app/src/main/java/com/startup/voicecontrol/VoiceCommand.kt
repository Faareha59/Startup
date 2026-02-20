package com.startup.voicecontrol

sealed class VoiceCommand {
    data class SaveContact(val phoneNumber: String, val name: String = "New Contact") : VoiceCommand()
    data class WhatsAppMessage(val phoneNumber: String) : VoiceCommand()
    data class CallNumber(val phoneNumber: String) : VoiceCommand()
    data class SendSms(val phoneNumber: String, val body: String) : VoiceCommand()
    data class OpenApp(val packageName: String) : VoiceCommand()
    object Unknown : VoiceCommand()
}
