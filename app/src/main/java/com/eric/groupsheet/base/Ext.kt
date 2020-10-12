package com.eric.groupsheet.base

import android.app.Activity
import android.os.Handler
import android.util.DisplayMetrics
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.jakewharton.rxbinding3.view.clicks
import java.util.concurrent.TimeUnit

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, block: (T) -> Unit) {
    liveData.observe(this, Observer {
        block(it)
    })
}

fun Activity.getScreen(block: (width: Int, height: Int) -> Unit) {
    val displayMetrics = DisplayMetrics()
    windowManager?.defaultDisplay?.getMetrics(displayMetrics)
    val height = displayMetrics.heightPixels
    val width = displayMetrics.widthPixels
    block(width, height)
}

fun View.hide() {
    val view = this
    if (!isVisible) {
        return
    }
    visibility = View.GONE
}

fun View.show() {
    if (isVisible) {
        return
    }

    val view = this
    view.visibility = View.VISIBLE
//    view.alpha = 0f
    animate().alpha(1f).setDuration(250).start()
}
fun View.show(show: Boolean) {
    if (show) show() else hide()
}

fun Fragment.toast(message: Any?) {
    Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT).show()
}
fun View.listenClick(throttleTimeMilliSec: Long = 500, onClick: (View) -> Unit) {
    clicks()
        .throttleFirst(throttleTimeMilliSec, TimeUnit.MILLISECONDS)
        .subscribe({
            performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            onClick(this)
        }, {
            it.stackTrace
        })
}