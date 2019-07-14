package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    this.currentFocus?.let { view ->
        val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }
}