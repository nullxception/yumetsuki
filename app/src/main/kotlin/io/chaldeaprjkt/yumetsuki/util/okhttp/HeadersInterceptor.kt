package io.chaldeaprjkt.yumetsuki.util.okhttp

import android.os.Build
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
        return chain.proceed(
            chain.request().newBuilder()
                .addHeader("User-Agent", buildUA())
                .addHeader("Origin", "https://act.hoyolab.com")
                .addHeader("Referer", "https://act.hoyolab.com/")
                .addHeader("x-rpc-app_version", "1.5.0")
                .addHeader("x-rpc-client_type", "4")
                .addHeader("x-rpc-language", "en")
                .build()
        )
    }
}
