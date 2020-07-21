package chooongg.frame.utils

import android.content.Context
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

fun Context.resDimenOffset(@DimenRes resId: Int) = resources.getDimensionPixelOffset(resId)

fun Context.resDimenSize(@DimenRes resId: Int) = resources.getDimensionPixelSize(resId)

fun Context.resInt(@IntegerRes resId: Int) = resources.getInteger(resId)

fun Context.resString(@StringRes resId: Int) = resources.getString(resId)

fun Context.resString(@StringRes resId: Int, vararg format: Any?) =
    resources.getString(resId, *format)


fun View.resAnimation(@AnimRes id: Int) = AnimationUtils.loadAnimation(context, id)

fun View.resColor(@ColorRes id: Int) = ContextCompat.getColor(context, id)

fun View.resColorStateList(@ColorRes id: Int) = ContextCompat.getColorStateList(context, id)

fun View.resDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(context, id)

fun View.resDimen(@DimenRes id: Int) = resources.getDimension(id)

fun View.resDimenOffset(@DimenRes resId: Int) = resources.getDimensionPixelOffset(resId)

fun View.resDimenSize(@DimenRes resId: Int) = resources.getDimensionPixelSize(resId)

fun View.resInt(@IntegerRes resId: Int) = resources.getInteger(resId)

fun View.resString(@StringRes resId: Int) = resources.getString(resId)

fun View.resString(@StringRes resId: Int, vararg format: Any?) =
    resources.getString(resId, *format)


fun Fragment.resAnimation(@AnimRes id: Int) = AnimationUtils.loadAnimation(context, id)

fun Fragment.resColor(@ColorRes id: Int) = ContextCompat.getColor(requireContext(), id)

fun Fragment.resColorStateList(@ColorRes id: Int) =
    ContextCompat.getColorStateList(requireContext(), id)

fun Fragment.resDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(requireContext(), id)

fun Fragment.resDimen(@DimenRes id: Int) = resources.getDimension(id)

fun Fragment.resDimenOffset(@DimenRes resId: Int) = resources.getDimensionPixelOffset(resId)

fun Fragment.resDimenSize(@DimenRes resId: Int) = resources.getDimensionPixelSize(resId)

fun Fragment.resInt(@IntegerRes resId: Int) = resources.getInteger(resId)

fun Fragment.resString(@StringRes resId: Int) = resources.getString(resId)

fun Fragment.resString(@StringRes resId: Int, vararg format: Any?) =
    resources.getString(resId, *format)
