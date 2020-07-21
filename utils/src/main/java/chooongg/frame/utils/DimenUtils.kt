package chooongg.frame.utils

fun dp2px(dp: Float) = (dp * getScreenDensity() + 0.5f).toInt()

fun px2dp(px: Int) = (px / getScreenDensity()).toInt() + 0.5f

fun sp2px(sp: Float) = (sp * getScreenScaledDensity() + 0.5f).toInt()

fun px2sp(px: Int) = (px / getScreenScaledDensity()).toInt() + 0.5f