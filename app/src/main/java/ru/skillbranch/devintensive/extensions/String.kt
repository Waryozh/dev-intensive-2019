package ru.skillbranch.devintensive.extensions

fun String.truncate(size: Int = 16): String {
    var res = this.trim()
    if (size < res.length) {
        res = res.substring(0 until size).trim() + "..."
    }
    return res
}

fun String.stripHtmlTags(): String {
    return this.replace("<[^>]*>".toRegex(), "")
}

fun String.stripHtmlEscapeSequences(): String {
    return this.replace("&amp;|&lt;|&gt;|&#39;|&quot;".toRegex(), "")
}

fun String.stripInternalWhitespaces(): String {
    return this.replace(" {2,}".toRegex(), " ")
}

fun String.stripHtml(): String {
    return this
        .stripHtmlTags()
        .stripHtmlEscapeSequences()
        .stripInternalWhitespaces()
}