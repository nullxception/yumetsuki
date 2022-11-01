package io.chaldeaprjkt.yumetsuki.util.extension

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable

@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Bundle?.parcelOf(key: String, default: T) =
    if (SDK_INT >= 33) {
        this?.getParcelable(key, T::class.java) ?: default
    } else {
        (this?.getParcelable(key) as? T) ?: default
    }

inline fun <reified T : Parcelable> Intent.putBundledParcel(key: String, data: T) =
    putExtra("bundleOf${key}", Bundle().apply { putParcelable(key, data) })

inline fun <reified T : Parcelable> Intent.getBundledParcel(key: String, default: T) =
    getBundleExtra("bundleOf${key}").parcelOf(key, default)
