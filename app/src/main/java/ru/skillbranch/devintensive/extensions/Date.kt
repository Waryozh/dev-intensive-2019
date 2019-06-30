package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }

    this.time = time

    return this
}

private fun minutesAsPlurals(minutes: Long): String = when (minutes) {
    1L -> "$minutes минуту"
    in 2L..4L -> "$minutes минуты"
    else -> "$minutes минут"
}

private fun hoursAsPlurals(hours: Long): String = when (hours) {
    1L -> "$hours час"
    in 2L..4L -> "$hours часа"
    else -> "$hours часов"
}

private fun daysAsPlurals(days: Long): String = when (days) {
    1L -> "$days день"
    in 2L..4L -> "$days дня"
    else -> "$days дней"
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val diff = abs(this.time - date.time)
    val past = this.time < date.time

    return when (diff) {
        in 0..1500 -> {
            if (past) "только что"
            else "через несколько секунд"
        }
        in 1500..45 * SECOND -> {
            if (past) "несколько секунд назад"
            else "через несколько секунд"
        }
        in 45 * SECOND..75 * SECOND -> {
            if (past) "минуту назад"
            else "через минуту"
        }
        in 75 * SECOND..45 * MINUTE -> {
            val minutes = diff / MINUTE
            if (past) "${minutesAsPlurals(minutes)} назад"
            else "через ${minutesAsPlurals(minutes)}"
        }
        in 45 * MINUTE..75 * MINUTE -> {
            if (past) "час назад"
            else "через час"
        }
        in 75 * MINUTE..22 * HOUR -> {
            val hours = diff / HOUR
            if (past) "${hoursAsPlurals(hours)} назад"
            else "через ${hoursAsPlurals(hours)}"
        }
        in 22 * HOUR..26 * HOUR -> {
            if (past) "день назад"
            else "через день"
        }
        in 26 * HOUR..360 * DAY -> {
            val days = diff / DAY
            if (past) "${daysAsPlurals(days)} назад"
            else "через ${daysAsPlurals(days)}"
        }
        else -> {
            if (past) "более года назад"
            else "более чем через год"
        }
    }
}