package chooongg.frame.utils

import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.appcompat.widget.AppCompatImageView
import chooongg.frame.manager.APP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ToastUtils private constructor(
    private var message: CharSequence?,
    @DrawableRes private var icon: Int? = null,
    @Type private var type: Int = TYPE_UNIVERSAL,
    private var duration: Int = Toast.LENGTH_SHORT
) {

    @IntDef(TYPE_UNIVERSAL, TYPE_EMPHASIZE)
    annotation class Type

    companion object {
        const val TYPE_UNIVERSAL = 0
        const val TYPE_EMPHASIZE = 1

        fun make(
            message: CharSequence?,
            @DrawableRes icon: Int?,
            @Type type: Int,
            duration: Int
        ) = ToastUtils(message, icon, type, duration)
    }

    private var gravity = Gravity.CENTER
    private var xOffset = 0
    private var yOffset = 0
    private var backgroundColor = -1
    private var backgroundImage = -1
    private var contentColor = -1

    fun setIcon(@DrawableRes icon: Int) = apply {
        this.icon = icon
    }

    fun setType(@Type type: Int) = apply {
        this.type = type
    }

    fun setDuration(duration: Int) = apply {
        this.duration = duration
    }

    fun setGravity(gravity: Int, xOffset: Int = 0, yOffset: Int = 0) = apply {
        this.gravity = gravity
        this.xOffset = xOffset
        this.yOffset = yOffset
    }

    fun setBackgroundColor(@ColorInt color: Int) = apply {
        this.backgroundColor = color
    }

    fun setBackgroundImage(@DrawableRes resId: Int) = apply {
        this.backgroundImage = resId
    }

    fun setContentColor(@ColorInt color: Int) = apply {
        this.contentColor = color
    }

    fun show() {
        GlobalScope.launch(Dispatchers.Main) {
            toastCancel()
            val layoutResId = when (type) {
                TYPE_EMPHASIZE -> R.layout.toast_emphasize
                else -> R.layout.toast_universal
            }
            if (message.isNullOrEmpty() && icon == -1) return@launch
            toast = Toast.makeText(APP, message, duration).apply {
                val contentView = LayoutInflater.from(APP).inflate(layoutResId, null)
                contentView.findViewById<TextView>(R.id.tv_text).apply {
                    if (message.isNullOrEmpty()) {
                        gone()
                    } else {
                        this.text = message
                        if (contentColor != -1) setTextColor(contentColor)
                        visible()
                    }
                }
                contentView.findViewById<AppCompatImageView>(R.id.iv_image).apply {
                    if (icon != null) {
                        setImageResource(icon!!)
                        if (contentColor != -1) setColorFilter(contentColor)
                        visible()
                    } else {
                        gone()
                    }
                }
                if (backgroundImage != -1) {
                    contentView.setBackgroundResource(backgroundImage)
                }
                if (backgroundColor != -1 && contentView.background is GradientDrawable) {
                    (contentView.background as GradientDrawable).setColor(backgroundColor)
                }
                view = contentView

            }
            if (gravity != Gravity.NO_GRAVITY) toast!!.setGravity(gravity, xOffset, yOffset)
            toast!!.show()
        }
    }
}

private var toast: Toast? = null

fun toastCancel() {
    toast?.cancel()
    toast = null
}

fun toastSuccess(
    message: CharSequence?,
    duration: Int = -1,
    @ToastUtils.Type type: Int = ToastUtils.TYPE_EMPHASIZE
) {
    ToastUtils.make(
        message,
        if (type == ToastUtils.TYPE_EMPHASIZE) {
            R.drawable.ic_toast_success_emphasize
        } else {
            R.drawable.ic_toast_success_universal
        },
        type,
        if (duration == -1) {
            if ((message?.length ?: 0) > 10) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        } else duration
    ).show()
}

fun toastWarn(
    message: CharSequence?,
    duration: Int = -1,
    @ToastUtils.Type type: Int = ToastUtils.TYPE_EMPHASIZE
) {
    ToastUtils.make(
        message,
        if (type == ToastUtils.TYPE_EMPHASIZE) {
            R.drawable.ic_toast_warn_emphasize
        } else {
            R.drawable.ic_toast_warn_universal
        },
        type,
        if (duration == -1) {
            if ((message?.length ?: 0) > 10) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        } else duration
    ).show()
}

fun toastError(
    message: CharSequence?,
    duration: Int = -1,
    @ToastUtils.Type type: Int = ToastUtils.TYPE_EMPHASIZE
) {
    ToastUtils.make(
        message,
        if (type == ToastUtils.TYPE_EMPHASIZE) {
            R.drawable.ic_toast_error_emphasize
        } else {
            R.drawable.ic_toast_error_universal
        },
        type,
        if (duration == -1) {
            if ((message?.length ?: 0) > 10) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        } else duration
    ).show()
}

fun toast(
    message: CharSequence?,
    @DrawableRes icon: Int? = null
) {
    ToastUtils.make(message, icon, ToastUtils.TYPE_UNIVERSAL, Toast.LENGTH_SHORT).show()
}

fun toastEmphasize(
    message: CharSequence?,
    @DrawableRes icon: Int? = null
) {
    ToastUtils.make(message, icon, ToastUtils.TYPE_EMPHASIZE, Toast.LENGTH_LONG).show()
}