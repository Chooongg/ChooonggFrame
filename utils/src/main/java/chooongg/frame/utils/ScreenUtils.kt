package chooongg.frame.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

fun Context.getScreenWidth() = resources.displayMetrics.widthPixels

fun Context.getScreenHeight() = resources.displayMetrics.heightPixels

fun getScreenDensity() = Resources.getSystem().displayMetrics.density

fun getScreenDensityDpi() = Resources.getSystem().displayMetrics.densityDpi

fun getScreenScaledDensity() = Resources.getSystem().displayMetrics.scaledDensity

fun Context.isLandscape() =
    resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

fun Context.isPortrait() =
    resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

/**
 * View截图返回Bitmap
 */
fun View?.toBitmap(): Bitmap? {
    if (this == null) return null
    val screenshot = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    val canvas = Canvas(screenshot)
    canvas.translate(-scrollX.toFloat(), -scrollY.toFloat())
    draw(canvas)
    return screenshot
}