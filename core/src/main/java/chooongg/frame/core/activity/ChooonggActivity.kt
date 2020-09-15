package chooongg.frame.core.activity

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toolbar
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import chooongg.frame.core.R
import chooongg.frame.core.annotation.*
import chooongg.frame.core.interfaces.Init
import chooongg.frame.core.manager.HideKeyboardManager
import chooongg.frame.core.widget.ChooonggToolBar
import chooongg.frame.permission.handlePermissionsResult
import chooongg.frame.utils.*
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AutoHideKeyboard
@TitleBar(true, true, TitleBar.SURFACE)
abstract class ChooonggActivity : AppCompatActivity(), Init, Toolbar.OnMenuItemClickListener {

    var isCreated = false
        private set

    inline val context: Context get() = this

    inline val activity: AppCompatActivity get() = this

    var chooonggToolbar: ChooonggToolBar? = null

    open fun configToolBar(toolBar: ChooonggToolBar) = Unit

    override fun onMenuItemClick(item: MenuItem?) = true

    @Deprecated("使用使用init方法初始化")
    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configTranslucentStatusBar4Annotation()
        try {
            configShowToolBar4Annotation()
            if (chooonggToolbar != null) {
                configToolBar(chooonggToolbar!!)
                chooonggToolbar!!.setOnMenuItemClickListener { onMenuItemClick(it) }
            }
        } catch (e: Exception) {
            Logger.e(e, "${javaClass.simpleName} configToolBar()")
        }
        configAutoHideKeyboard()
        try {
            setContentView(getContentLayout())
        } catch (e: Exception) {
            Logger.e(e, "${javaClass.simpleName} setContentView()")
            return
        }
        getWindowBackgroundRes().apply {
            window.setBackgroundDrawable(null)
            if (this != null) contentView.setBackgroundResource(this)
        }
        try {
            initConfig(savedInstanceState)
            isCreated = true
        } catch (e: Exception) {
            Logger.e(e, "${javaClass.simpleName} initConfig()")
            return
        }
    }

    final override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (isCreated.not()) return
        try {
            initContent(savedInstanceState)
        } catch (e: Exception) {
            Logger.e(e, "${javaClass.simpleName} initContent()")
            return
        }
    }

    private var loadingTip: View? = null

    @SuppressLint("InflateParams")
    fun showLoading(message: CharSequence? = null, isClickable: Boolean = false) {
        lifecycleScope.launch(Dispatchers.Main) {
            if (loadingTip != null) {
                loadingTip!!.findViewById<TextView>(R.id.tv_message).apply {
                    if (message.isNullOrEmpty()) {
                        gone()
                    } else {
                        text = message
                        visible()
                    }
                }
                return@launch
            }
            loadingTip = layoutInflater.inflate(R.layout.view_loading, null)
            loadingTip!!.alpha = 0f
            loadingTip!!.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            loadingTip!!.findViewById<TextView>(R.id.tv_message).apply {
                if (message.isNullOrEmpty()) {
                    gone()
                } else {
                    text = message
                    visible()
                }
            }
            if (!isClickable) loadingTip!!.setOnClickListener { }
            decorView.addView(loadingTip)
            loadingTip!!.animate().alpha(1f)
        }
    }

    fun showLoading(@StringRes resId: Int) {
        showLoading(getString(resId))
    }

    fun hideLoading() {
        if (!isDestroyed) {
            lifecycleScope.launch(Dispatchers.Main) {
                if (loadingTip == null) return@launch
                loadingTip!!.animate().alpha(0f).setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) = Unit
                    override fun onAnimationRepeat(animation: Animator?) = Unit
                    override fun onAnimationCancel(animation: Animator?) = Unit

                    override fun onAnimationEnd(animation: Animator?) {
                        decorView.removeView(loadingTip)
                        loadingTip = null
                    }
                })
            }
        }
    }

    final override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        handlePermissionsResult(requestCode, permissions, grantResults)
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
                    if (!javaClass.isAnnotationPresent(KeepNavigationButton::class.java)) {
                        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
                    }
                    chooonggToolbar = toolbar
                } catch (e: Exception) {
                    Logger.e(e, "configShowToolBar4Annotation()")
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