package io.chaldeaprjkt.yumetsuki.domain.common

import io.chaldeaprjkt.yumetsuki.util.extension.trimQuotes

data class HoYoCookie(val text: String) {
    private val map =
        toString()
            .split(";")
            .map { it.trim() }
            .filter { it.matches("..+=..+".toRegex()) }
            .associate { it.substringBefore("=") to it.substringAfter("=") }

    val lang
        get() = map[Lang] ?: DefaultLang

    val uid
        get() = (map[UIDV2] ?: map[UID])?.toIntOrNull() ?: 0

    override fun toString() = text.split("\n").joinToString("") { it.trim() }.trimQuotes()

    private fun hasToken() = map.containsKey(Token) || map.containsKey(TokenV2)
    private fun hasUID() = map.containsKey(UIDV2) || map.containsKey(UIDV2)
    fun isValid() = map.containsKey(Lang) && hasToken() && hasUID()

    companion object {
        const val Lang = "mi18nLang"
        const val DefaultLang = "en-us"
        const val Token = "ltoken"
        const val UID = "ltuid"
        const val TokenV2 = "ltoken_v2"
        const val UIDV2 = "ltuid_v2"
    }
}
