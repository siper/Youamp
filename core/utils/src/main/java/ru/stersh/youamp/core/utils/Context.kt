package ru.stersh.youamp.core.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

fun Context.launchSafeAnyUri(vararg uri: Uri) {
    for (u in uri) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, u))
            return
        } catch (ignored: ActivityNotFoundException) {
        }
    }
    Toast
        .makeText(this, "No app found for handle this action", Toast.LENGTH_SHORT)
        .show()
}