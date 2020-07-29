package chooongg.frame.core.widget.titleBar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import chooongg.frame.utils.getActivity
import chooongg.frame.utils.setStatusBarLightMode
import com.google.android.material.appbar.MaterialToolbar

@SuppressLint("ResourceType")
class ChooonggToolBar @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialToolbar(context, attrs, defStyleAttr) {

    init {
        if (attrs != null) {

        } else {

        }
    }

    override fun setBackground(background: Drawable?) {
        super.setBackground(background)
        if (background is ColorDrawable) {
            val activity = context.getActivity()
            if (background.color == Color.WHITE) {
                activity?.setStatusBarLightMode(true)
                activity?.window?.statusBarColor = Color.WHITE
            } else {
                activity?.setStatusBarLightMode(false)
            }
        }
    }
}