package chooongg.frame.utils

import android.app.Activity
import android.content.res.Resources
import android.os.Build
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment

private const val TAG_STATUS_BAR = "TAG_STATUS_BAR"
private const val TAG_OFFSET = "TAG_OFFSET"
private const val KEY_OFFSET = -123

/**
 * 获取状态栏高度
 */
fun getStatusBarHeight(): Int {
    val resources = Resources.getSystem()
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return resources.getDimensionPixelSize(resourceId)
}

/**
 * 设置状态栏可见
 */
fun Activity.setStatusBarVisibility(isVisible: Boolean) {
    window.setStatusBarVisibility(isVisible)
}

/**
 * 设置状态栏可见
 */
fun Window.setStatusBarVisibility(isVisible: Boolean) {
    if (isVisible) {
        clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        addMarginTopEqualStatusBarHeight(this)
    } else {
        addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        subtractMarginTopEqualStatusBarHeight(this)
    }
}

/**
 * 状态栏是否可见
 */
fun Activity.isStatusBarVisible(): Boolean {
    return window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN == 0
}

/**
 * 设置状态栏深色模式
 */
fun Activity.setStatusBarLightMode(isLightMode: Boolean) {
    window.setStatusBarLightMode(isLightMode)
}

fun Fragment.setStatusBarLightModel(isLightMode: Boolean) {
    activity?.setStatusBarLightMode(isLightMode)
}

/**
 * 设置状态栏深色模式
 */
fun Window.setStatusBarLightMode(isLightMode: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val decorView = decorView
        var vis = decorView.systemUiVisibility
        vis = if (isLightMode) {
            vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        decorView.systemUiVisibility = vis
    }
}

/**
 * 状态栏是否为深色模式
 */
fun Activity.isStatusBarLightMode() = window.isStatusBarLightMode()

/**
 * 状态栏是否为深色模式
 */
fun Window.isStatusBarLightMode(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val decorView = decorView
        val vis = decorView.systemUiVisibility
        return vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR != 0
    }
    return false
}

/**
 * 增加状态栏高度的MarginTop
 */
fun View.addMarginTopEqualStatusBarHeight() {
    tag = TAG_OFFSET
    val haveSetOffset = getTag(KEY_OFFSET)
    if (haveSetOffset != null && haveSetOffset as Boolean) return
    val layoutParams = layoutParams as MarginLayoutParams
    layoutParams.setMargins(
        layoutParams.leftMargin,
        layoutParams.topMargin + getStatusBarHeight(),
        layoutParams.rightMargin,
        layoutParams.bottomMargin
    )
    setTag(KEY_OFFSET, true)
}

/**
 * 减去状态栏高度的MarginTop
 */
fun View.subtractMarginTopEqualStatusBarHeight() {
    val haveSetOffset = getTag(KEY_OFFSET)
    if (haveSetOffset == null || !(haveSetOffset as Boolean)) return
    val layoutParams = layoutParams as MarginLayoutParams
    layoutParams.setMargins(
        layoutParams.leftMargin,
        layoutParams.topMargin - getStatusBarHeight(),
        layoutParams.rightMargin,
        layoutParams.bottomMargin
    )
    setTag(KEY_OFFSET, false)
}

private fun addMarginTopEqualStatusBarHeight(window: Window) {
    window.decorView.findViewWithTag<View>(TAG_OFFSET)
        ?.addMarginTopEqualStatusBarHeight() ?: return
}

private fun subtractMarginTopEqualStatusBarHeight(window: Window) {
    window.decorView.findViewWithTag<View>(TAG_OFFSET)
        ?.subtractMarginTopEqualStatusBarHeight() ?: return
}