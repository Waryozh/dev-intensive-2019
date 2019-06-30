package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        if (fullName.isNullOrBlank()) {
            return null to null
        }
        val parts: List<String>? = fullName.split(" ")
        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)
        return firstName to lastName
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        if (firstName.isNullOrBlank() && lastName.isNullOrBlank()) {
            return null
        }
        var res = ""
        if (!firstName.isNullOrBlank()) {
            res += firstName[0].toUpperCase().toString()
        }
        if (!lastName.isNullOrBlank()) {
            res += lastName[0].toUpperCase().toString()
        }
        return res
    }
}