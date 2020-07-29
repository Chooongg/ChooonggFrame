package chooongg.frame.utils

import android.app.Activity
import android.content.ComponentName

fun Activity.loadLabel(): CharSequence? {
    val activityInfo = packageManager.getActivityInfo(ComponentName(this, javaClass), 0)
    return activityInfo.loadLabel(packageManager)
}
