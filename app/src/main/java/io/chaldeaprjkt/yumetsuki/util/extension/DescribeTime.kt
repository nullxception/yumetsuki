package io.chaldeaprjkt.yumetsuki.util.extension

import android.content.Context
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.ParaTransformerStatus
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

enum class FullTimeType {
    Max,
    Done,
}

fun ParaTransformerStatus.describeTime(context: Context): String {
    return if (recoveryTime.days > 0) {
        context.getString(R.string.widget_ui_remain_days, recoveryTime.days)
    } else if (recoveryTime.days == 0) {
        context.describeTimeSecs(recoveryTime.totalInSecs, FullTimeType.Done)
    } else {
        context.getString(R.string.widget_ui_unknown)
    }
}

fun Context.describeTimeSecs(timeSecs: Int, type: FullTimeType): String {
    val completed = when (type) {
        FullTimeType.Max -> getString(R.string.widget_ui_parameter_max)
        FullTimeType.Done -> getString(R.string.widget_ui_parameter_done)
    }

    val hour = timeSecs / 3600
    val minute = (timeSecs - hour * 3600) / 60
    return if (timeSecs > 0) {
        when (type) {
            FullTimeType.Max -> getString(R.string.widget_ui_full_time, hour, minute)
            FullTimeType.Done -> getString(R.string.widget_ui_remain_time, hour, minute)
        }
    } else {
        completed
    }
}

fun Long.describeDateTime(
    formatter: () -> DateTimeFormatter = {
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
    },
): String {
    val instant = Instant.ofEpochMilli(this)
    val zoned = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
    return zoned.format(formatter())
}
