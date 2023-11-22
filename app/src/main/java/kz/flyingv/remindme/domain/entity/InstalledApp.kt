package kz.flyingv.remindme.domain.entity

import android.graphics.drawable.Drawable

data class InstalledApp(
    val name: String,
    val icon: Drawable?,
    val launchActivity: String
)