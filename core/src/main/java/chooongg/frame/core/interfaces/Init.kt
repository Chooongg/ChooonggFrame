package chooongg.frame.core.interfaces

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import chooongg.frame.core.R
import chooongg.frame.core.annotation.ContentLayout
import chooongg.frame.core.annotation.WindowBackground

interface Init {

    @LayoutRes
    fun initLayoutRes() = R.layout.none

    @DrawableRes
    fun initWindowBackgroundRes(): Int? = null

    /**
     * 初始化配置
     */
    fun initConfig(savedInstanceState: Bundle?)

    /**
     * 初始化内容
     */
    fun initContent(savedInstanceState: Bundle?)

    /**
     * 获取布局资源
     */
    fun getContentLayout(): Int {
        var layoutResId = R.layout.none
        if (initLayoutRes() != layoutResId) layoutResId = initLayoutRes()
        if (javaClass.isAnnotationPresent(ContentLayout::class.java)) {
            val value = javaClass.getAnnotation(ContentLayout::class.java)?.value
            if (value != null) layoutResId = value
        }
        return layoutResId
    }

    /**
     * 获取背景资源
     */
    fun getWindowBackgroundRes() = when {
        initWindowBackgroundRes() != null -> initWindowBackgroundRes()
        javaClass.isAnnotationPresent(WindowBackground::class.java) ->
            javaClass.getAnnotation(WindowBackground::class.java)?.value
        else -> null
    }
}