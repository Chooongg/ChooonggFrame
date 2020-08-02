package chooongg.frame.core.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import chooongg.frame.core.R
import chooongg.frame.core.annotation.AutoHideKeyboard
import chooongg.frame.core.annotation.TitleBar
import chooongg.frame.core.annotation.TitleBarElevation
import chooongg.frame.core.annotation.TranslucentStatusBar
import chooongg.frame.core.interfaces.Init
import chooongg.frame.core.manager.HideKeyboardManager
import chooongg.frame.core.widget.ChooonggToolBar
import chooongg.frame.log.L
import chooongg.frame.utils.contentView
import chooongg.frame.utils.dp2px
import chooongg.frame.utils.resAttrDimenOffset

@AutoHideKeyboard
@TitleBar(true, true, TitleBar.SURFACE)
abstract class ChooonggActivity : AppCompatActivity(), Init {

    var isCreated = false
        private set

    inline val context: Context get() = this

    inline val activity: AppCompatActivity get() = this

    var chooonggToolbar: ChooonggToolBar? = null

    open fun configToolBar(toolBar: ChooonggToolBar) = Unit

    @Deprecated("使用使用init方法初始化")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configTranslucentStatusBar4Annotation()
        try {
            configShowToolBar4Annotation()
            if (chooonggToolbar != null) configToolBar(chooonggToolbar!!)
        } catch (e: Exception) {
            L.e("${javaClass.simpleName} configToolBar operation there is an exception", e)
        }
        configAutoHideKeyboard()
        try {
            setContentView(getContentLayout())
        } catch (e: Exception) {
            L.e("${javaClass.simpleName} setContentLayout operation there is an exception", e)
            return
        }
        getWindowBackgroundRes().apply {
            if (this == null) window.setBackgroundDrawable(null)
            else window.setBackgroundDrawableResource(this)
        }
        try {
            initConfig(savedInstanceState)
            isCreated = true
        } catch (e: Exception) {
            L.e("${javaClass.simpleName} initConfig() there is an exception", e)
            return
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (isCreated.not()) return
        try {
            initContent(savedInstanceState)
        } catch (e: Exception) {
            L.e("${javaClass.simpleName} initContent() there is an exception", e)
            return
        }
    }

    @Suppress("DEPRECATION")
    private fun configTranslucentStatusBar4Annotation() {
        if (javaClass.isAnnotationPresent(TranslucentStatusBar::class.java) && supportActionBar == null) {
            val annotation = javaClass.getAnnotation(TranslucentStatusBar::class.java)!!
            if (annotation.isEnable) {
                if (annotation.isHideMask) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = Color.TRANSPARENT
                } else {
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun configShowToolBar4Annotation() {
        if (javaClass.isAnnotationPresent(TitleBar::class.java) && supportActionBar == null) {
            val annotation = javaClass.getAnnotation(TitleBar::class.java)!!
            if (annotation.isShow) {
                try {
                    val layout = when (annotation.style) {
                        TitleBar.PRIMARY_SURFACE -> R.layout.chooongg_title_bar_primary_surface
                        TitleBar.SURFACE -> R.layout.chooongg_title_bar_surface
                        else -> R.layout.chooongg_title_bar_primary
                    }
                    val toolbar = LayoutInflater.from(context).inflate(layout, null)
                            as ChooonggToolBar
                    toolbar.setTitleStyle(annotation.isCenter)
                    if ((javaClass.getAnnotation(TitleBarElevation::class.java)?.value
                            ?: TitleBarElevation.ELEVATION) == TitleBarElevation.ELEVATION
                    ) {
                        toolbar.elevation = dp2px(4f).toFloat()
                    }
                    val parentLayout = contentView.parent as LinearLayout
                    parentLayout.addView(
                        toolbar, 0,
                        LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            resAttrDimenOffset(R.attr.actionBarSize)
                        )
                    )
                    setSupportActionBar(toolbar)
                    chooonggToolbar = toolbar
                } catch (e: Exception) {
                    L.e(
                        "${javaClass.simpleName} configShowToolBar4Annotation() there is an exception",
                        e
                    )
                }
            }
        }
    }

    private fun configAutoHideKeyboard() {
        if (javaClass.isAnnotationPresent(AutoHideKeyboard::class.java)) {
            val annotation = javaClass.getAnnotation(AutoHideKeyboard::class.java)!!
            if (annotation.value) {
                HideKeyboardManager.init(activity)
            }
        }
    }
}