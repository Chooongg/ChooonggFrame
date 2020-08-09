package chooongg.frame.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * Request for given permissions and invoke related callback methods.
 *
 * @param permissions manifest permission list (e.g [android.Manifest.permission.CAMERA])
 * @param callbacks permission callback DSL
 */
fun Activity.runtimePermissions(
    vararg permissions: String,
    callbacks: PermissionCallbacksDSL.() -> Unit
) {

    val permissionCallbacks: PermissionCallbacks = PermissionCallbacksDSL().apply { callbacks() }
    val permissionsNeeded = permissions.filter { !isPermissionGranted(it) }
    if (permissionsNeeded.isNotEmpty()) {

        val shouldShowRationalePermissions = mutableListOf<String>()
        val shouldNotShowRationalePermissions = mutableListOf<String>()

        permissionsNeeded.forEach {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, it)) {
                shouldShowRationalePermissions.add(it)
            } else {
                shouldNotShowRationalePermissions.add(it)
            }
        }

        val requestCode = PermissionsMap.put(permissionCallbacks)

        if (shouldShowRationalePermissions.isNotEmpty()) {
            permissionCallbacks.onShowRationale(
                PermissionRequest(
                    this,
                    null,
                    shouldShowRationalePermissions,
                    requestCode
                )
            )
            return
        }

        if (shouldNotShowRationalePermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                shouldNotShowRationalePermissions.toTypedArray(), requestCode
            )
        }
    } else permissionCallbacks.onGranted()
}

/**
 * Request for given permissions and invoke related callback methods.
 *
 * @param permissions manifest permission list (e.g [android.Manifest.permission.CAMERA])
 * @param callbacks permission callback DSL
 */
fun Fragment.runtimePermissions(
    vararg permissions: String,
    callbacks: PermissionCallbacksDSL.() -> Unit
) {

    val permissionCallbacks: PermissionCallbacks = PermissionCallbacksDSL().apply { callbacks() }
    val permissionsNeeded = permissions.filter { !requireContext().isPermissionGranted(it) }
    if (permissionsNeeded.isNotEmpty()) {

        val shouldShowRationalePermissions = mutableListOf<String>()
        val shouldNotShowRationalePermissions = mutableListOf<String>()

        permissionsNeeded.forEach {
            if (shouldShowRequestPermissionRationale(it)) {
                shouldShowRationalePermissions.add(it)
            } else {
                shouldNotShowRationalePermissions.add(it)
            }
        }

        val requestCode = PermissionsMap.put(permissionCallbacks)

        if (shouldShowRationalePermissions.isNotEmpty()) {
            permissionCallbacks.onShowRationale(
                PermissionRequest(
                    null,
                    this,
                    shouldShowRationalePermissions,
                    requestCode
                )
            )
            return
        }

        if (shouldNotShowRationalePermissions.isNotEmpty()) {
            requestPermissions(shouldNotShowRationalePermissions.toTypedArray(), requestCode)
        }
    } else permissionCallbacks.onGranted()
}

/**
 * Delegate function for handling permissions result.
 *
 * Invoke this from [Activity.onRequestPermissionsResult]
 */
fun Activity.handlePermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
) {
    val callbacks = PermissionsMap.get(requestCode) ?: return

    var allGranted = true
    val neverAskAgainPermissions = mutableListOf<String>()
    val deniedPermissions = arrayListOf<String>()

    permissions.forEachIndexed { index, permission ->
        if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
            allGranted = false
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                neverAskAgainPermissions.add(permission)
            } else {
                deniedPermissions.add(permission)
            }
        }
    }

    if (allGranted) {
        callbacks.onGranted()
    } else {
        if (deniedPermissions.isNotEmpty()) {
            callbacks.onDenied(deniedPermissions)
        }
        if (neverAskAgainPermissions.isNotEmpty()) {
            callbacks.onNeverAskAgain(neverAskAgainPermissions)
        }
    }
}

/**
 * Delegate function for handling permissions result.
 *
 * Invoke this from [Activity.onRequestPermissionsResult]
 */
fun Fragment.handlePermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
) {
    val callbacks = PermissionsMap.get(requestCode) ?: return

    var allGranted = true
    val neverAskAgainPermissions = mutableListOf<String>()
    val deniedPermissions = arrayListOf<String>()

    permissions.forEachIndexed { index, permission ->
        if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
            allGranted = false
            if (!shouldShowRequestPermissionRationale(permission)) {
                neverAskAgainPermissions.add(permission)
            } else {
                deniedPermissions.add(permission)
            }
        }
    }

    if (allGranted) {
        callbacks.onGranted()
    } else {
        if (deniedPermissions.isNotEmpty()) {
            callbacks.onDenied(deniedPermissions)
        }
        if (neverAskAgainPermissions.isNotEmpty()) {
            callbacks.onNeverAskAgain(neverAskAgainPermissions)
        }
    }
}

/**
 * Check if permission is granted.
 *
 * @param permission Manifest permission (e.g [android.Manifest.permission.CAMERA])
 * @return true if it is granted
 */
fun Context.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}