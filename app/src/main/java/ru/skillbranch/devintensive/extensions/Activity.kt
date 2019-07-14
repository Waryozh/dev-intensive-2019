package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    this.currentFocus?.let { view ->
        val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }
}

fun Activity.isKeyboardOpen(): Boolean {
    val rootView = this.findViewById<View>(android.R.id.content)
    val visibleBounds = Rect()
    rootView.getWindowVisibleDisplayFrame(visibleBounds)
    return rootView.height - visibleBounds.height() > 50
}

fun Activity.isKeyboardClosed(): Boolean {
    return !this.isKeyboardOpen()
}