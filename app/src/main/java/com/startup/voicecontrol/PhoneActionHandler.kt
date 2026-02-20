package com.startup.voicecontrol

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentProviderOperation
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PhoneActionHandler(private val context: Context) {

    fun execute(command: VoiceCommand): String {
        return when (command) {
            is VoiceCommand.SaveContact -> saveContact(command)
            is VoiceCommand.WhatsAppMessage -> openWhatsApp(command.phoneNumber)
            is VoiceCommand.CallNumber -> callNumber(command.phoneNumber)
            is VoiceCommand.SendSms -> sendSms(command.phoneNumber, command.body)
            is VoiceCommand.OpenApp -> openApp(command.packageName)
            VoiceCommand.Unknown -> "Sorry, command samajh nahi aaya"
        }
    }

    private fun saveContact(command: VoiceCommand): String {
        if (!hasPermission(Manifest.permission.WRITE_CONTACTS)) {
            requestPermission(Manifest.permission.WRITE_CONTACTS, PERMISSION_CONTACTS)
            return "Contacts permission required. Please allow and repeat."
        }

        val ops = arrayListOf<ContentProviderOperation>()
        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build()
        )
        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                )
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, command.name)
                .build()
        )
        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                )
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, command.phoneNumber)
                .withValue(
                    ContactsContract.CommonDataKinds.Phone.TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                )
                .build()
        )

        context.contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
        return "Saved ${command.phoneNumber} as ${command.name}"
    }

    private fun openWhatsApp(phoneNumber: String): String {
        val normalized = phoneNumber.removePrefix("+")
        val uri = Uri.parse("https://wa.me/$normalized")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            `package` = "com.whatsapp"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return try {
            context.startActivity(intent)
            "Opening WhatsApp chat for $phoneNumber"
        } catch (_: ActivityNotFoundException) {
            "WhatsApp is not installed"
        }
    }

    private fun callNumber(phoneNumber: String): String {
        if (!hasPermission(Manifest.permission.CALL_PHONE)) {
            requestPermission(Manifest.permission.CALL_PHONE, PERMISSION_CALL)
            return "Call permission required. Please allow and repeat."
        }

        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$phoneNumber")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
        return "Calling $phoneNumber"
    }

    private fun sendSms(phoneNumber: String, body: String): String {
        if (!hasPermission(Manifest.permission.SEND_SMS)) {
            requestPermission(Manifest.permission.SEND_SMS, PERMISSION_SMS)
            return "SMS permission required. Please allow and repeat."
        }

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$phoneNumber")
            putExtra("sms_body", body)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
        return "Opening SMS for $phoneNumber"
    }

    private fun openApp(packageName: String): String {
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            ?: return "Requested app is not installed"

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        return "Opening app"
    }

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(permission: String, code: Int) {
        if (context is Activity) {
            ActivityCompat.requestPermissions(context, arrayOf(permission), code)
        }
    }

    companion object {
        const val PERMISSION_CALL = 101
        const val PERMISSION_SMS = 102
        const val PERMISSION_CONTACTS = 103
    }
}
