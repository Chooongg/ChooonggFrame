package chooongg.frame.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * 资源获取
 */

fun Context.resAnimation(@AnimRes id: Int) = AnimationUtils.loadAnimation(this, id)

fun Context.resColor(@ColorRes id: Int) = ContextCompat.getColor(this, id)

fun Context.resColorStateList(@ColorRes id: Int) = ContextCompat.getColorStateList(this, id)

fun Context.resDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

fun Context.resDimen(@DimenRes id: Int) = resources.getDimension(id)

fun Context.resDimenOffset(@DimenRes id: Int) = resources.getDimensionPixelOffset(id)

fun Context.resDimenSize(@DimenRes id: Int) = resources.getDimensionPixelSize(id)

fun Context.resInt(@IntegerRes id: Int) = resources.getInteger(id)

fun Context.resString(@StringRes id: Int) = resources.getString(id)

fun Context.resString(@StringRes id: Int, vararg format: Any?) = resources.getString(id, *format)


fun View.resAnimation(@AnimRes id: Int) = AnimationUtils.loadAnimation(context, id)

fun View.resColor(@ColorRes id: Int) = ContextCompat.getColor(context, id)

fun View.resColorStateList(@ColorRes id: Int) = ContextCompat.getColorStateList(context, id)

fun View.resDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(context, id)

fun View.resDimen(@DimenRes id: Int) = resources.getDimension(id)

fun View.resDimenOffset(@DimenRes id: Int) = resources.getDimensionPixelOffset(id)

fun View.resDimenSize(@DimenRes id: Int) = resources.getDimensionPixelSize(id)

fun View.resInt(@IntegerRes id: Int) = resources.getInteger(id)

fun View.resString(@StringRes id: Int) = resources.getString(id)

fun View.resString(@StringRes id: Int, vararg format: Any?) = resources.getString(id, *format)


fun Fragment.resAnimation(@AnimRes id: Int) = AnimationUtils.loadAnimation(context, id)

fun Fragment.resColor(@ColorRes id: Int) = ContextCompat.getColor(requireContext(), id)

fun Fragment.resColorStateList(@ColorRes id: Int) =
    ContextCompat.getColorStateList(requireContext(), id)

fun Fragment.resDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(requireContext(), id)

fun Fragment.resDimen(@DimenRes id: Int) = resources.getDimension(id)

fun Fragment.resDimenOffset(@DimenRes id: Int) = resources.getDimensionPixelOffset(id)

fun Fragment.resDimenSize(@DimenRes id: Int) = resources.getDimensionPixelSize(id)

fun Fragment.resInt(@IntegerRes id: Int) = resources.getInteger(id)

fun Fragment.resString(@StringRes id: Int) = resources.getString(id)

fun Fragment.resString(@StringRes id: Int, vararg format: Any?) = resources.getString(id, *format)

fun Context.attrColor(@ColorRes id: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(id, typedValue, true)
    return typedValue.data
}