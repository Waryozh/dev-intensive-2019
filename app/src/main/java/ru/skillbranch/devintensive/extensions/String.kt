package ru.skillbranch.devintensive.extensions

fun String.truncate(size: Int = 16): String {
    var res = this.trim()
    if (size < res.length) {
        res = res.substring(0 until size).trim() + "..."
    }
    return res
}