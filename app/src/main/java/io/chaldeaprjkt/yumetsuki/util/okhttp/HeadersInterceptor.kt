package io.chaldeaprjkt.yumetsuki.util.okhttp

import android.os.Build
import io.chaldeaprjkt.yumetsuki.domain.common.HoYoCookie
import okhttp3.Interceptor
import okhttp3.Response

class HeadersInterceptor : Interceptor {

    private fun buildUA(): String {
        val os = "Android ${Build.VERSION.RELEASE}"
        val device = "${Build.MODEL} Build/${Build.ID}"
        val webkitVersion = "537.36"
        val chromeVersion = "107.0.0.0"
        return "Mozilla/5.0 (Linux; $os; $device; wv) " +
            "AppleWebKit/$webkitVersion (KHTML, like Gecko) Version/4.0 " +
            "Chrome/$chromeVersion Mobile Safari/$webkitVersion"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val newRequest =
            req.newBuilder()
                .addHeader("User-Agent", buildUA())
                .addHeader("Origin", "https://act.hoyolab.com")
                .addHeader("Referer", "https://act.hoyolab.com/")

        val signGame = req.header("X-Rpc-signgame")
        if (signGame.isNullOrEmpty()){
            newRequest.removeHeader("X-Rpc-signgame")
        }

        val ds = req.header("DS")
        if (!ds.isNullOrEmpty()) {
            val cookie = req.header("Cookie")
                ?.takeIf { it.contains("mi18nLang") }
                ?.let { HoYoCookie(it) }

            newRequest
                .addHeader("x-rpc-app_version", "1.5.0")
                .addHeader("x-rpc-client_type", "5")
                .addHeader("x-rpc-language", cookie?.lang ?: "en-us")
        }

        return chain.proceed(newRequest.build())
    }
}
