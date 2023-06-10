@file:Suppress("unused")

package io.chaldeaprjkt.yumetsuki.util

import android.util.Log
import io.chaldeaprjkt.yumetsuki.BuildConfig

private fun Exception.summary(msg: String? = null): String {
    val el = stackTrace[1]
    val className = el.className.split('.').last()
    val summary = "(${el.fileName}:${el.lineNumber}) > ${className}::${el.methodName}"
    return "$summary > $msg".takeIf { msg != null } ?: summary
}

private const val LoggerTAG = "YumetsukiLogger"

internal fun dlog(exception: Exception) {
    if (BuildConfig.DEBUG) Log.d(LoggerTAG, exception.summary())
}

internal fun dlog(message: String?) {
    if (BuildConfig.DEBUG) Log.d(LoggerTAG, Exception().summary(message))
}

internal fun elog(message: String?) {
    Log.e(LoggerTAG, Exception().summary(message))
}

internal fun elog(exception: Exception) {
    Log.e(LoggerTAG, exception.summary())
}
