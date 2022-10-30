package io.chaldeaprjkt.yumetsuki.util

import io.chaldeaprjkt.yumetsuki.constant.Source
import java.math.BigInteger
import java.security.MessageDigest
import java.util.Calendar
import java.util.Locale
import java.util.Random
import java.util.TimeZone
import kotlin.streams.asSequence

object CommonFunction {

    fun getGenshinDS(): String {
        val source = "abcdefghijklmnopqrstuvwxyz"

        val t = System.currentTimeMillis() / 1000L
        val r = Random().ints(6, 0, source.length)
            .asSequence()
            .map(source::get)
            .joinToString("")

        val hash = encodeToMD5("salt=${Source.Salt}&t=$t&r=$r")
        return "${t},${r},${hash}"
    }

    private fun encodeToMD5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
            .lowercase(Locale.getDefault())
    }

    fun getTimeLeftUntilChinaTime(isAM: Boolean, hour: Int, startCalendar: Calendar): Long {
        val targetCalendar = Calendar.getInstance()
        targetCalendar.timeZone = TimeZone.getTimeZone("Asia/Shanghai")
        targetCalendar.set(Calendar.MINUTE, 1)
        targetCalendar.set(Calendar.HOUR, hour)
        targetCalendar.set(Calendar.AM_PM, if (isAM) Calendar.AM else Calendar.PM)

        if (startCalendar.get(Calendar.HOUR_OF_DAY) >= 1) targetCalendar.add(
            Calendar.DAY_OF_YEAR,
            1
        )

        val delay = (targetCalendar.time.time - startCalendar.time.time) / 60000
        return if (delay < 0) {
            targetCalendar.add(Calendar.DAY_OF_YEAR, 1)
            (targetCalendar.time.time - startCalendar.time.time) / 60000
        } else {
            delay
        }
    }
}
