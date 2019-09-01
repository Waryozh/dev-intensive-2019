package ru.skillbranch.devintensive.extensions

import android.content.res.Resources
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.utils.Utils

fun Snackbar.styleDayNight(theme: Resources.Theme): Snackbar {
    view.setBackgroundColor(Utils.getColorFromTheme(R.attr.colorSnackbar, theme))
    view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        .setTextColor(Utils.getColorFromTheme(R.attr.colorSnackbarText, theme))
    setActionTextColor(Utils.getColorFromTheme(R.attr.colorSnackbarActionText, theme))
    return this
}
