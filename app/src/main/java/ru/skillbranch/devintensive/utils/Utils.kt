package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        if (fullName.isNullOrBlank()) {
            return null to null
        }
        val parts: List<String>? = fullName.trim().split(" ")
        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)
        return firstName to lastName
    }

    private val rusEngLettersMap = mapOf(
        "а" to "a",
        "б" to "b",
        "в" to "v",
        "г" to "g",
        "д" to "d",
        "е" to "e",
        "ё" to "e",
        "ж" to "zh",
        "з" to "z",
        "и" to "i",
        "й" to "i",
        "к" to "k",
        "л" to "l",
        "м" to "m",
        "н" to "n",
        "о" to "o",
        "п" to "p",
        "р" to "r",
        "с" to "s",
        "т" to "t",
        "у" to "u",
        "ф" to "f",
        "х" to "h",
        "ц" to "c",
        "ч" to "ch",
        "ш" to "sh",
        "щ" to "sh'",
        "ъ" to "",
        "ы" to "i",
        "ь" to "",
        "э" to "e",
        "ю" to "yu",
        "я" to "ya"
    )

    fun transliteration(payload: String, divider: String = " "): String {
        var res = ""
        for (c in payload) {
            val cIsUpper = c.isUpperCase()
            var transliteratedCharSeq = rusEngLettersMap[c.toLowerCase().toString()] ?: c.toString()
            if (cIsUpper) {
                transliteratedCharSeq = transliteratedCharSeq.capitalize()
            }
            if (transliteratedCharSeq == " ") {
                transliteratedCharSeq = divider
            }
            res += transliteratedCharSeq
        }
        return res
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        if (firstName.isNullOrBlank() && lastName.isNullOrBlank()) {
            return null
        }
        var res = ""
        if (!firstName.isNullOrBlank()) {
            res += firstName.trim()[0].toUpperCase().toString()
        }
        if (!lastName.isNullOrBlank()) {
            res += lastName.trim()[0].toUpperCase().toString()
        }
        return res
    }
}