package com.example.photovideoclockframe

import android.content.Context
import android.util.AttributeSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import kotlinx.android.synthetic.main.front_analog_clock.view.*
import java.util.*

class FrontAnalogClock : CoordinatorLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(context)
        attrs?.extractAttributes(context, R.styleable.FrontAnalogClock) { typedArray ->
            rotateHourHand(typedArray.getFloat(R.styleable.FrontAnalogClock_hourRotation, 0f))
            rotateMinuteHand(typedArray.getFloat(R.styleable.FrontAnalogClock_minuteRotation, 0f))
            rotateSecondHand(typedArray.getFloat(R.styleable.FrontAnalogClock_secondRotation, 0f))
        }
    }

    private fun initialize(context: Context) {
        inflate(context, R.layout.front_analog_clock, this)
    }

    private fun rotateHourHand(angleDegrees: Float) {
        clockHourHandView.rotation = angleDegrees
    }

    private fun rotateMinuteHand(angleDegrees: Float) {
        clockMinuteHandView.rotation = angleDegrees
    }

    private fun rotateSecondHand(angleDegrees: Float) {
        clockSecondHandView.rotation = angleDegrees
    }

    fun setCurrentTime() {
        val calendar = Calendar.getInstance()
        val second = calendar.get(Calendar.SECOND).toFloat()
        val minute = calendar.get(Calendar.MINUTE).toFloat()
        val hour = calendar.get(Calendar.HOUR).toFloat()
        rotateSecondHand(second * SECONDS_MINUTES_TO_DEGREES)
        rotateMinuteHand(minute * SECONDS_MINUTES_TO_DEGREES)
        rotateHourHand((60 * hour + minute) / 2)
        invalidate()
    }

    companion object {
        private const val SECONDS_MINUTES_TO_DEGREES = 6
    }
}