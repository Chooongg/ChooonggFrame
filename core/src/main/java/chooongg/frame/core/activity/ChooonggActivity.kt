package chooongg.frame.core.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import chooongg.frame.core.annotation.TranslucentStatusBar
import chooongg.frame.core.interfaces.Init
import chooongg.frame.log.L

abstract class ChooonggActivity : AppCompatActivity(), Init {

    var isCreated = false
        private set

    @Deprecated("使用使用init方法初始化")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configTranslucentStatusBar4Annotation()
        try {
            setContentView(getContentLayout())
        } catch (e: Exception) {
            L.e("${javaClass.simpleName} setContentLayout operation there is an exception", e)
            return
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
}