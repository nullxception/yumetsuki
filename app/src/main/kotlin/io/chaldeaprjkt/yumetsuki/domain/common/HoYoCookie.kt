package io.chaldeaprjkt.yumetsuki.domain.common

import io.chaldeaprjkt.yumetsuki.util.extension.trimQuotes

data class HoYoCookie(val text: String) {
    private val map = toString().split(";")
        .map { it.trim() }
        .filter { it.matches("..+=..+".toRegex()) }
        .associate { it.substringBefore("=") to it.substringAfter("=") }

    val lang get() = map[Lang] ?: DefaultLang

    val uid get() = map[UID]?.toIntOrNull() ?: 0

    override fun toString() = text.split("\n")
        .joinToString("") { it.trim() }
        .trimQuotes()

    fun isValid() =
        map.containsKey(Lang) && map.containsKey(Token) && map.containsKey(UID)

    companion object {
        const val Lang = "mi18nLang"
        const val DefaultLang = "en-us"
        const val Token = "ltoken"
        const val UID = "ltuid"
    }
}
