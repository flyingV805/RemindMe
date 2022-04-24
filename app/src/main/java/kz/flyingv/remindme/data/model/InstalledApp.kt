package kz.flyingv.remindme.data.model

import android.graphics.drawable.Drawable

data class InstalledApp(
    val name: String,
    val icon: Drawable,
    val launchActivity: String
)