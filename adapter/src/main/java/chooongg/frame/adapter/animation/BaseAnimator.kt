package chooongg.frame.adapter.animation

import android.animation.Animator
import android.view.View

interface BaseAnimator {
    fun animators(view: View): Array<Animator>
}