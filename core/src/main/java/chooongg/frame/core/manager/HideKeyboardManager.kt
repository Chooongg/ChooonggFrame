package chooongg.frame.core.manager

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import chooongg.frame.utils.contentView

@SuppressLint("ClickableViewAccessibility")
class HideKeyboardManager private constructor(activity: Activity, content: ViewGroup?) {

    companion object {
        fun init(activity: Activity) = HideKeyboardManager(activity, null)
        fun init(activity: Activity, content: ViewGroup?) = HideKeyboardManager(activity, content)
    }

    init {
        var contentTemp: ViewGroup? = content
        if (contentTemp == null) {
            contentTemp = activity.contentView
        }
        getScrollView(contentTemp, activity)
        contentTemp.setOnTouchListener { _, motionEvent ->
            dispatchTouchEvent(activity, motionEvent)
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun getScrollView(viewGroup: ViewGroup?, activity: Activity) {
        if (null == viewGroup) {
            return
        }
        val count = viewGroup.childCount
        for (i in 0 until count) {
            val view = viewGroup.getChildAt(i)
            when (view) {
                is ScrollView -> {
                    view.setOnTouchListener { _, motionEvent ->
                        dispatchTouchEvent(activity, motionEvent)
                        false
                    }
                }
                is AbsListView -> {
                    view.setOnTouchListener { _, motionEvent ->
                        dispatchTouchEvent(activity, motionEvent)
                        false
                    }
                }
                is RecyclerView -> {
                    val newDtv: RecyclerView = view
                    newDtv.setOnTouchListener { _, motionEvent ->
                        dispatchTouchEvent(activity, motionEvent)
                        false
                    }
                }
                is ViewGroup -> {
                    getScrollView(view, activity)
                }
            }
            if (view.isClickable && view is TextView && view !is EditText) {
                view.setOnTouchListener { _, motionEvent ->
                    dispatchTouchEvent(activity, motionEvent)
                    false
                }
            }
        }
    }

    private fun dispatchTouchEvent(activity: Activity, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val view = activity.currentFocus
            if (null != view && isShouldHideInput(view, event)) {
                hideSoftInput(activity, view.windowToken)
                view.clearFocus()
            }
        }
        return false
    }

    private fun isShouldHideInput(view: View, event: MotionEvent): Boolean {
        return if (view is EditText) {
            val rect = Rect()
            view.getHitRect(rect)
            !rect.contains(event.x.toInt(), event.y.toInt())
        } else true
    }

    private fun hideSoftInput(activity: Activity, token: IBinder?) {
        if (token != null) {
            val im = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            im?.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}