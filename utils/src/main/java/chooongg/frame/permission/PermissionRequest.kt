package chooongg.frame.permission

import android.app.Activity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

/**
 * Represents permission request to retry after rationale message is shown.
 */
class PermissionRequest internal constructor(
    activity: Activity?,
    fragment: Fragment?,
    val permissions: List<String>,
    private val requestCode: Int
) {

    private val weakActivity: WeakReference<Activity?> = WeakReference(activity)
    private val weakFragment: WeakReference<Fragment?> = WeakReference(fragment)

    /**
     * Invoke this after rationale message is shown.
     */
    fun retry() {
        weakFragment.get()?.apply {
            requestPermissions(permissions.toTypedArray(), requestCode)
            return
        }
        weakActivity.get()?.apply {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), requestCode)
        }
    }
}