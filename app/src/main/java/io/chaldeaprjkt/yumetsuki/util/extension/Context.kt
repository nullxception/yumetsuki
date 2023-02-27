package io.chaldeaprjkt.yumetsuki.util.extension

import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.widget.Toast

fun Context.copyToClipboard(value: String, message: String = "") {
    (getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.setPrimaryClip(
        ClipData.newPlainText(
            "YumetsukiApp",
            value
        )
    )

    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2 && message.isNotEmpty()) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

fun Context.hasTextClipboard() =
    (getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.primaryClipDescription?.hasMimeType(
        MIMETYPE_TEXT_PLAIN
    ) == true

fun Context.grabTextClipboard() =
    (getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.primaryClip?.getItemAt(0)?.text?.toString()
