package com.mellivora.base.api.textview

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan

class CenterImageSpan(drawable: Drawable, val width: Int, val height: Int) : ImageSpan(drawable) {

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        val fm = paint.fontMetricsInt
        val drawable = drawable
        drawable.setBounds(0, 0, width, height);
        val transY = ((y + fm.descent + y + fm.ascent) / 2 - drawable.bounds.bottom / 2)
        canvas.save()
        canvas.translate(x, transY.toFloat())
        drawable.draw(canvas)
        canvas.restore()
    }
}