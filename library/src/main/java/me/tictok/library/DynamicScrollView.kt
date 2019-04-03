package me.tictok.library

import android.content.Context
import android.support.animation.DynamicAnimation
import android.support.animation.FlingAnimation
import android.support.animation.SpringAnimation
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.util.Log
import android.view.InputDevice
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View

class DynamicScrollView : NestedScrollView {

    private val TAG = "DynamicScrollView"
    private var child: View? = null
    private var springAnimation: SpringAnimation? = null
    private var overScrollAnimation: SpringAnimation? = null
    private var flingAnimation: FlingAnimation? = null
    private var velocityTracker: VelocityTracker? = null
    private var lastY = 0f
    private var dragY = 0f

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context, attributeSet, defStyle)

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 0) {
            child = getChildAt(0)
            overScrollAnimation = SpringAnimation(child, DynamicAnimation.TRANSLATION_Y, 0f)
            overScrollAnimation!!.spring.apply {
                stiffness = 120f
                dampingRatio = 1.5f
            }
            flingAnimation = FlingAnimation(this, DynamicAnimation.SCROLL_Y).apply {
                friction = 0.3f
                addUpdateListener { _, _, velocity ->
                    if (this@DynamicScrollView.scrollY == 0 || this@DynamicScrollView.scrollY == child!!.measuredHeight - this@DynamicScrollView.height) {
                        flingAnimation!!.cancel()
                        overScrollAnimation!!.setStartVelocity(-velocity).start()
                    }
                }
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev!!.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                flingAnimation?.cancel()
                springAnimation?.cancel()
                overScrollAnimation?.cancel()
                dragY = 0f
            }
        }
        lastY = ev.y
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (child == null)
            return super.onTouchEvent(ev)
        if (velocityTracker == null)
            velocityTracker = VelocityTracker.obtain()
        velocityTracker!!.addMovement(ev)
        val y = ev!!.y
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                flingAnimation?.cancel()
                springAnimation?.cancel()
                overScrollAnimation?.cancel()
                dragY = 0f
            }
            MotionEvent.ACTION_MOVE -> {
                val dy = y - lastY
                if (dy > 0) { // swipe down
                    if (dragY == 0f) dragY = y
                    if (scrollY <= 0) { // already to the edge
                        child!!.translationY = (y - dragY) / 3f
                    } else {
                        scrollBy(0, (-dy).toInt())
                    }
                } else { // swipe up
                    if (dragY == 0f) dragY = y
                    if (scrollY + height >= child!!.measuredHeight) { // already to the edge
                        child!!.translationY = (y - dragY) / 3f
                    } else {
                        scrollBy(0, (-dy).toInt())
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (scrollY <= 0 || scrollY + height >= child!!.measuredHeight) { // already to the edge
                    springAnimation = SpringAnimation(child, DynamicAnimation.TRANSLATION_Y, 0f)
                    springAnimation!!.spring.apply {
                        stiffness = 120f
                        dampingRatio = 1.5f
                    }
                    springAnimation!!.start()
                } else {
                    val tracker = this.velocityTracker
                    tracker!!.computeCurrentVelocity(800)
                    val velocityY = tracker.yVelocity
                    flingAnimation?.setStartVelocity(-velocityY)?.start()
                }
                dragY = 0f
            }
        }
        lastY = y
        return true
    }
}