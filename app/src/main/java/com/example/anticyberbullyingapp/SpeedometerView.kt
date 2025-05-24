package com.example.anticyberbullyingapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.anticyberbullyingapp.R

class SpeedometerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Значення за замовчуванням
    private var minValue = MIN_VALUE
    private var maxValue = MAX_VALUE
    private var currentValue = MIN_VALUE

    // Фарби для малювання
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        color = ContextCompat.getColor(context, R.color.speedometer_background)
        strokeCap = Paint.Cap.ROUND
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textSize = 40f
        color = ContextCompat.getColor(context, R.color.speedometer_text)
        textAlign = Paint.Align.CENTER
    }

    private val riskTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textSize = 24f
        color = ContextCompat.getColor(context, R.color.speedometer_text)
        textAlign = Paint.Align.CENTER
    }

    // Області для малювання
    private val rectF = RectF()
    private val textBounds = Rect()

    init {
        // Отримання атрибутів з XML
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SpeedometerView,
            0, 0
        ).apply {
            try {
                minValue = getInt(R.styleable.SpeedometerView_minValue, MIN_VALUE)
                maxValue = getInt(R.styleable.SpeedometerView_maxValue, MAX_VALUE)
                currentValue = getInt(R.styleable.SpeedometerView_currentValue, MIN_VALUE)
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val centerX = width / 2
        val centerY = height / 2
        val radius = (minOf(width, height) * 0.4f)

        // Малюємо фон спідометра
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        canvas.drawArc(rectF, START_ANGLE, SWEEP_ANGLE, false, backgroundPaint)

        // Малюємо прогрес з градієнтом
        val sweepAngle = SWEEP_ANGLE * (currentValue - minValue) / (maxValue - minValue)
        progressPaint.color = getColorForValue(currentValue)
        canvas.drawArc(rectF, START_ANGLE, sweepAngle, false, progressPaint)

        // Малюємо текстові значення
        drawTextCentered(canvas, centerX, centerY, "$currentValue%", textPaint)
        drawRiskLevelText(canvas, centerX, centerY + 50, currentValue)
    }

    private fun drawTextCentered(canvas: Canvas, x: Float, y: Float, text: String, paint: Paint) {
        paint.getTextBounds(text, 0, text.length, textBounds)
        canvas.drawText(text, x, y - (paint.descent() + paint.ascent()) / 2, paint)
    }

    private fun drawRiskLevelText(canvas: Canvas, x: Float, y: Float, value: Int) {
        val riskLevel = when {
            value < 30 -> context.getString(R.string.low_risk)
            value < 70 -> context.getString(R.string.medium_risk)
            else -> context.getString(R.string.high_risk)
        }
        drawTextCentered(canvas, x, y, riskLevel, riskTextPaint)
    }

    private fun getColorForValue(value: Int): Int {
        return ContextCompat.getColor(context, when {
            value < 30 -> R.color.speedometer_green
            value < 70 -> R.color.speedometer_yellow
            else -> R.color.speedometer_red
        })
    }

    fun updateValue(value: Int) {
        currentValue = value.coerceIn(minValue, maxValue)
        invalidate()
    }

    companion object {
        const val MIN_VALUE = 0
        const val MAX_VALUE = 100
        private const val START_ANGLE = 135f
        private const val SWEEP_ANGLE = 270f
    }
}