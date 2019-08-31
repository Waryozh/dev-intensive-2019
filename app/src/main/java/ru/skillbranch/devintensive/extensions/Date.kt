package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

enum class TimeUnits {
    SECOND {
        override val ONE = "секунду"
        override val FEW = "секунды"
        override val MANY = "секунд"
    },
    MINUTE {
        override val ONE = "минуту"
        override val FEW = "минуты"
        override val MANY = "минут"
    },
    HOUR {
        override val ONE = "час"
        override val FEW = "часа"
        override val MANY = "часов"
    },
    DAY {
        override val ONE = "день"
        override val FEW = "дня"
        override val MANY = "дней"
    };

    abstract val ONE: String
    abstract val FEW: String
    abstract val MANY: String

    fun plural(value: Long): String = when {
        (value % 10 == 1L) && (value % 100 != 11L) -> "$value ${this.ONE}"
        (value % 10 in 2L..4L) && (value % 100 !in 12L..14L) -> "$value ${this.FEW}"
        else -> "$value ${this.MANY}"
    }
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
            if (past) "${TimeUnits.MINUTE.plural(minutes)} назад"
            else "через ${TimeUnits.MINUTE.plural(minutes)}"
        }
        in 45 * MINUTE..75 * MINUTE -> {
            if (past) "час назад"
            else "через час"
        }
        in 75 * MINUTE..22 * HOUR -> {
            val hours = diff / HOUR
            if (past) "${TimeUnits.HOUR.plural(hours)} назад"
            else "через ${TimeUnits.HOUR.plural(hours)}"
        }
        in 22 * HOUR..26 * HOUR -> {
            if (past) "день назад"
            else "через день"
        }
        in 26 * HOUR..360 * DAY -> {
            val days = diff / DAY
            if (past) "${TimeUnits.DAY.plural(days)} назад"
            else "через ${TimeUnits.DAY.plural(days)}"
        }
        else -> {
            if (past) "более года назад"
            else "более чем через год"
        }
    }
}

fun Date.shortFormat(): String {
    val pattern = if (this.isSameDay(Date())) "HH:mm" else "dd.MM.yy"
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.isSameDay(date: Date): Boolean = (this.time / DAY) == (date.time / DAY)
