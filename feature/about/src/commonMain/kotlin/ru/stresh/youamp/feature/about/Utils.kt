package ru.stresh.youamp.feature.about

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri

internal fun Context.launchSafeAnyUrl(url: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
        return
    } catch (ignored: ActivityNotFoundException) {
    }
    Toast
        .makeText(this, "No app found for handle this action", Toast.LENGTH_SHORT)
        .show()
}