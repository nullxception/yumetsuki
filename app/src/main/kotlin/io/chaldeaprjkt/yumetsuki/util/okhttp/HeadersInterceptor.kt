package io.chaldeaprjkt.yumetsuki.util.okhttp

import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response

class HeadersInterceptor : Interceptor {

    private fun buildUA(): String {
        val os = Build.VERSION.RELEASE
        val model = Build.MODEL
        val webkit = "537.36"
        val chrome = "107.0.0.0"
        return "Mozilla/5.0 (Linux; Android $os; $model) " +
                "AppleWebKit/$webkit (KHTML, like Gecko) " +
                "Chrome/$chrome Mobile Safari/$webkit"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder()
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .addHeader("User-Agent", buildUA())
                .addHeader("Origin", "https://act.hoyolab.com")
                .addHeader("Referer", "https://act.hoyolab.com/")
                .addHeader("x-rpc-app_version", "1.50.0")
                .addHeader("x-rpc-client_type", "4")
                .addHeader("x-rpc-language", "en-us")
                .build()
        )
    }
}
