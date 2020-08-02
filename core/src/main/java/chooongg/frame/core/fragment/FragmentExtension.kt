package chooongg.frame.core.fragment

import androidx.fragment.app.Fragment
import chooongg.frame.core.annotation.FragmentTitle
import kotlin.reflect.KClass

fun KClass<out Fragment>.getTitle() =
    if (java.isAnnotationPresent(FragmentTitle::class.java)) {
        java.getAnnotation(FragmentTitle::class.java)!!.value
    } else null