package io.chaldeaprjkt.yumetsuki.util

import io.chaldeaprjkt.yumetsuki.constant.Source
import java.math.BigInteger
import java.security.MessageDigest
import java.util.Locale
import java.util.Random
import kotlin.streams.asSequence

object CommonFunction {

    fun genDS(salt: String = Source.Salt): String {
        val letters = "abcdefghijklmnopqrstuvwxyz"
        val source = letters + letters.uppercase()

        val t = System.currentTimeMillis() / 1000L
        val r = Random().ints(6, 0, source.length).asSequence().map(source::get).joinToString("")

        val hash = encodeToMD5("salt=${salt}&t=$t&r=$r")
        return "${t},${r},${hash}"
    }

    private fun encodeToMD5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray()))
            .toString(16)
            .padStart(32, '0')
            .lowercase(Locale.getDefault())
    }
}
