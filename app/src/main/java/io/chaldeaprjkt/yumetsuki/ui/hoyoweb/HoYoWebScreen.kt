package io.chaldeaprjkt.yumetsuki.ui.hoyoweb

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Cookie
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.constant.Source
import io.chaldeaprjkt.yumetsuki.ui.login.LoginDestination

@Preview(showBackground = true)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun HoYoWebScreen(onPopBack: (Pair<String, String>?) -> Unit = {}) {
    val webTitle = remember { mutableStateOf("") }
    val webOrigin = remember { mutableStateOf(Source.HoYoLAB) }
    val webIsSecure = remember { mutableStateOf(false) }
    val cookie = remember { mutableStateOf("") }
    val webProgress = remember { mutableStateOf(0f) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        WebTitle(
                            title = webTitle.value,
                            origin = webOrigin.value,
                            isSecure = webIsSecure.value,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { onPopBack(null) }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(id = android.R.string.cancel)
                            )
                        }
                    },
                    actions = { GrabCookieButton(cookie = cookie.value, onPopBack = onPopBack) },
                )
                val progressMod = Modifier.fillMaxWidth()
                if (webProgress.value > 0f) {
                    LinearProgressIndicator(
                        progress = webProgress.value,
                        modifier =
                            if (webProgress.value == 1f) {
                                progressMod.alpha(0f)
                            } else {
                                progressMod
                            },
                    )
                } else {
                    LinearProgressIndicator(modifier = progressMod)
                }
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { insets ->
        WebviewComposer(
            modifier = Modifier.padding(insets).consumeWindowInsets(insets),
            url = Source.HoYoLAB,
            options = {
                webViewClient =
                    object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            val uri = url?.toUri() ?: return

                            webIsSecure.value = uri.scheme == "https"
                            webOrigin.value = uri.authority ?: return
                        }

                        override fun onLoadResource(view: WebView?, url: String?) {
                            super.onLoadResource(view, url)
                            view
                                ?.progress
                                ?.takeIf { it > 0 }
                                ?.let { webProgress.value = it.div(100f) }
                            webTitle.value = view?.title ?: return
                            if (cookie.value.isEmpty()) {
                                CookieManager.getInstance()
                                    ?.getCookie(Source.HoYoLAB)
                                    ?.trim()
                                    ?.takeIf { it.contains("ltuid") && it.contains("ltoken") }
                                    ?.let { cookie.value = it }
                            }
                        }
                    }

                webChromeClient = WebChromeClient()
                settings.apply {
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    builtInZoomControls = false
                    javaScriptEnabled = true
                    javaScriptCanOpenWindowsAutomatically = true
                    setSupportMultipleWindows(true)
                    domStorageEnabled = true
                    cacheMode = WebSettings.LOAD_NO_CACHE
                }
            },
        )
    }
}

@Composable
fun GrabCookieButton(
    onPopBack: (Pair<String, String>?) -> Unit = {},
    cookie: String = "",
) {
    TextButton(
        enabled = cookie.isNotBlank(),
        onClick = {
            if (cookie.contains("ltuid") && cookie.contains("ltoken")) {
                onPopBack(LoginDestination.cookieArg to cookie)
            }
        },
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Outlined.Cookie,
                contentDescription = stringResource(id = R.string.get_cookie),
            )
            Text(text = stringResource(id = R.string.get_cookie))
        }
    }
}

@Composable
fun WebTitle(title: String, origin: String, isSecure: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = if (isSecure) Icons.Filled.Lock else Icons.Outlined.Info,
            contentDescription = null,
            modifier = Modifier.padding(8.dp).width(16.dp),
        )
        Column {
            if (title.isNotBlank()) {
                Text(text = title, maxLines = 1, style = MaterialTheme.typography.titleSmall)
            }
            Text(text = origin, maxLines = 1, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
fun WebviewComposer(
    modifier: Modifier = Modifier,
    url: String = "",
    options: WebView.() -> Unit = {},
) {
    val isInPreview = LocalView.current.isInEditMode
    AndroidView(
        modifier = modifier.fillMaxWidth().fillMaxHeight(),
        factory = { context ->
            WebView(context).apply {
                layoutParams =
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                if (!isInPreview) {
                    options(this)
                }
            }
        },
        update = { if (url.isNotBlank()) it.loadUrl(url) },
    )
}
