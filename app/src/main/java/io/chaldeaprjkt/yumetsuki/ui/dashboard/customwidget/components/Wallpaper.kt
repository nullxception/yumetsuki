package io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.components

import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmapOrNull

fun Context.tryGetWallpaper(): Drawable? {
    return try {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            WallpaperManager.getInstance(this).drawable
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun rememberWallpaperBitmap(): ImageBitmap {
    LocalConfiguration.current
    val context = LocalContext.current
    val value = remember { context.tryGetWallpaper() }
    return remember(value, context.theme) {
        value?.toBitmapOrNull()?.asImageBitmap() ?: ImageBitmap(1, 1)
    }
}
