package chooongg.frame.utils

import android.os.Looper

fun isMainThread() = Looper.myLooper() == Looper.getMainLooper()