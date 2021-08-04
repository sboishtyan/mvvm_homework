package ru.awac.mvvm_homework.utils

import android.view.View
import androidx.core.view.isVisible
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import java.text.SimpleDateFormat
import java.util.*

fun View.fadeTo(visible: Boolean, duration: Long = 750, startDelay: Long = 0, toAlpha: Float = 1f) {
        // Make this idempotent.
        val tagKey = "fadeTo".hashCode()
        if (visible == isVisible && animation == null && getTag(tagKey) == null) return
        if (getTag(tagKey) == visible) return

        setTag(tagKey, visible)
        setTag("fadeToAlpha".hashCode(), toAlpha)

        if (visible && alpha == 1f) alpha = 0f
        animate()
            .alpha(if (visible) toAlpha else 0f)
            .withStartAction {
                if (visible) isVisible = true
            }
            .withEndAction {
                setTag(tagKey, null)
                if (isAttachedToWindow && !visible) isVisible = false
            }
            .setInterpolator(FastOutSlowInInterpolator())
            .setDuration(duration)
            .setStartDelay(startDelay)
            .start()
    }

fun String?.checkFieldForNull(): String {
    return if (this == null || this.isEmpty()) "Not specified"
    else this
}

fun Long?.formatDateAndTime(): String {
    return if (this == null || this == 0L)
        "Not specified"
    else
        return SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.getDefault())
            .format((this.times(1000L)))
}