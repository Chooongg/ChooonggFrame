package chooongg.frame.core.utils

import android.animation.ValueAnimator
import android.graphics.RectF
import androidx.annotation.FloatRange
import androidx.annotation.Px
import androidx.core.animation.doOnEnd
import chooongg.frame.utils.resInt
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.RelativeCornerSize
import com.google.android.material.shape.ShapeAppearanceModel

val ShapeableImageView.topLeftCornerSize: Float
    get() = shapeAppearanceModel.topLeftCornerSize.getCornerSize(getImageBound())

val ShapeableImageView.topRightCornerSize: Float
    get() = shapeAppearanceModel.topRightCornerSize.getCornerSize(getImageBound())

val ShapeableImageView.bottomLeftCornerSize: Float
    get() = shapeAppearanceModel.bottomLeftCornerSize.getCornerSize(getImageBound())

val ShapeableImageView.bottomRightCornerSize: Float
    get() = shapeAppearanceModel.bottomRightCornerSize.getCornerSize(getImageBound())

fun ShapeableImageView.setCornerSize(@Px cornerSize: Int) {
    shapeAppearanceModel =
        shapeAppearanceModel.toBuilder().setAllCornerSizes(cornerSize.toFloat()).build()
}

fun ShapeableImageView.setTopLeftCornerSize(@Px cornerSize: Int) {
    shapeAppearanceModel =
        shapeAppearanceModel.toBuilder().setTopLeftCornerSize(cornerSize.toFloat()).build()
}

fun ShapeableImageView.setTopRightCornerSize(@Px cornerSize: Int) {
    shapeAppearanceModel =
        shapeAppearanceModel.toBuilder().setTopRightCornerSize(cornerSize.toFloat()).build()
}

fun ShapeableImageView.setBottomLeftCornerSize(@Px cornerSize: Int) {
    shapeAppearanceModel =
        shapeAppearanceModel.toBuilder().setBottomLeftCornerSize(cornerSize.toFloat()).build()
}

fun ShapeableImageView.setBottomRightCornerSize(@Px cornerSize: Int) {
    shapeAppearanceModel =
        shapeAppearanceModel.toBuilder().setBottomRightCornerSize(cornerSize.toFloat()).build()
}


fun ShapeableImageView.setCornerSize(@FloatRange(from = 0.0, to = 1.0) percent: Float) {
    shapeAppearanceModel =
        shapeAppearanceModel.toBuilder().setAllCornerSizes(RelativeCornerSize(percent)).build()
}

fun ShapeableImageView.setTopLeftCornerSize(@FloatRange(from = 0.0, to = 1.0) percent: Float) {
    shapeAppearanceModel =
        shapeAppearanceModel.toBuilder().setTopLeftCornerSize(RelativeCornerSize(percent)).build()
}

fun ShapeableImageView.setTopRightCornerSize(@FloatRange(from = 0.0, to = 1.0) percent: Float) {
    shapeAppearanceModel =
        shapeAppearanceModel.toBuilder().setTopRightCornerSize(RelativeCornerSize(percent)).build()
}

fun ShapeableImageView.setBottomLeftCornerSize(@FloatRange(from = 0.0, to = 1.0) percent: Float) {
    shapeAppearanceModel =
        shapeAppearanceModel.toBuilder().setBottomLeftCornerSize(RelativeCornerSize(percent))
            .build()
}

fun ShapeableImageView.setBottomRightCornerSize(@FloatRange(from = 0.0, to = 1.0) percent: Float) {
    shapeAppearanceModel =
        shapeAppearanceModel.toBuilder().setBottomRightCornerSize(RelativeCornerSize(percent))
            .build()
}


fun ShapeableImageView.animatorCornerSize(
    newModel: ShapeAppearanceModel,
    duration: Long = resInt(android.R.integer.config_mediumAnimTime).toLong()
) {
    postOnAnimation {
        val animator = ValueAnimator.ofFloat(0f, 1f)
            .setDuration(duration)
        val bound = getImageBound()
        animator.addUpdateListener {
            val value = it.animatedValue as Float
            val modelTempBuilder = shapeAppearanceModel.toBuilder()

            if (shapeAppearanceModel.topLeftCornerSize.getCornerSize(bound)
                != newModel.topLeftCornerSize.getCornerSize(bound)
            ) {
                val oldSize = shapeAppearanceModel.topLeftCornerSize.getCornerSize(bound)
                val newSize = newModel.topLeftCornerSize.getCornerSize(bound)
                modelTempBuilder.setTopLeftCornerSize(oldSize + (newSize - oldSize) * value)
            }
            if (shapeAppearanceModel.topRightCornerSize.getCornerSize(bound)
                != newModel.topRightCornerSize.getCornerSize(bound)
            ) {
                val oldSize = shapeAppearanceModel.topRightCornerSize.getCornerSize(bound)
                val newSize = newModel.topRightCornerSize.getCornerSize(bound)
                modelTempBuilder.setTopRightCornerSize(oldSize + (newSize - oldSize) * value)
            }
            if (shapeAppearanceModel.bottomLeftCornerSize.getCornerSize(bound)
                != newModel.bottomLeftCornerSize.getCornerSize(bound)
            ) {
                val oldSize = shapeAppearanceModel.bottomLeftCornerSize.getCornerSize(bound)
                val newSize = newModel.bottomLeftCornerSize.getCornerSize(bound)
                modelTempBuilder.setBottomLeftCornerSize(oldSize + (newSize - oldSize) * value)
            }
            if (shapeAppearanceModel.bottomRightCornerSize.getCornerSize(bound)
                != newModel.bottomRightCornerSize.getCornerSize(bound)
            ) {
                val oldSize = shapeAppearanceModel.bottomRightCornerSize.getCornerSize(bound)
                val newSize = newModel.bottomRightCornerSize.getCornerSize(bound)
                modelTempBuilder.setBottomRightCornerSize(oldSize + (newSize - oldSize) * value)
            }

            shapeAppearanceModel = modelTempBuilder.build()
        }
        animator.doOnEnd {
            shapeAppearanceModel = newModel
        }
        animator.start()
    }
}

fun ShapeableImageView.animatorCornerSize(
    @Px cornerSize: Float,
    duration: Long = resInt(android.R.integer.config_mediumAnimTime).toLong()
) {
    postOnAnimation {
        animatorCornerSize(
            shapeAppearanceModel.toBuilder().setAllCornerSizes(cornerSize).build(),
            duration
        )
    }
}


private fun ShapeableImageView.getImageBound() = RectF(
    0 + paddingLeft + strokeWidth / 2,
    0 + paddingTop + strokeWidth / 2,
    width + paddingRight - strokeWidth / 2,
    height + paddingBottom - strokeWidth / 2
)