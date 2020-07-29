package chooongg.frame.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

fun Context.getActivity(): Activity? {
    var contextTemp = this
    while (contextTemp is ContextWrapper) {
        if (contextTemp is Activity) return contextTemp
        contextTemp = contextTemp.baseContext
    }
    return null
}