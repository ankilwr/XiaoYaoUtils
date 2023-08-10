package com.mellivora.base.expansion


import android.graphics.Color
import android.graphics.Paint
import android.text.InputFilter
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.mellivora.base.api.textview.CenterImageSpan
import com.mellivora.base.api.textview.ClickMovementMethod

@Suppress("NOTHING_TO_INLINE")
inline fun TextView.isEmpty(): Boolean = text.isNullOrEmpty()

fun TextView.textEquals(tv: TextView): Boolean {
    return this.text.toString() == tv.text.toString()
}

fun TextView.setBold(enable: Boolean): TextView? {
    this.paint.isFakeBoldText = enable
    //typeface = Typeface.defaultFromStyle(if (enable) Typeface.BOLD else Typeface.NORMAL)
    invalidate()
    return this
}

fun TextView.strikeLine(): TextView? {
    this.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
    this.paint.isAntiAlias = true
    return this
}

fun TextView.underLine(): TextView? {
    this.paint.flags = Paint.UNDERLINE_TEXT_FLAG
    this.paint.isAntiAlias = true
    return this
}

fun TextView.maxLength(): Int{
    for (filter in filters) {
        if (filter is InputFilter.LengthFilter) {
            return filter.max
        }
    }
    return -1
}


fun TextView.matchingColorText(text: String?, @ColorInt matchingColor: Int, vararg matchingStr: String?): TextView {
    setText(text)
    if (text.isNullOrEmpty() || matchingStr.isEmpty()) {
        return this
    }
    val builder = SpannableStringBuilder(text)
    matchingStr.forEach {
        if(it.isNullOrEmpty()) return@forEach
        var indexStart = text.indexOf(it)
        while (indexStart != -1) {
            val indexEnd = indexStart + it.length
            val colorSpan = ForegroundColorSpan(matchingColor)
            builder.setSpan(colorSpan, indexStart, indexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            indexStart = text.indexOf(it, indexEnd)
        }
    }
    this.text = builder
    return this
}

fun TextView.setMatchingColorText(text: String?, matchingStr: String?, @ColorInt matchingColor: Int): TextView {
    setText(text)
    this.matchingColor(matchingStr, matchingColor)
    return this
}

fun TextView.setMatchingColorText(@StringRes text: Int, matchingStr: String?, @ColorInt matchingColor: Int): TextView {
    setText(context.getString(text, matchingStr ?: ""))
    this.matchingColor(matchingStr, matchingColor)
    return this
}

fun TextView.matchingSize(matchingStr: String?, @DimenRes size: Int): TextView {
    if (text.isEmpty() || matchingStr.isNullOrEmpty()) {
        return this
    }
    val builder = SpannableStringBuilder(text)
    val textSpan = AbsoluteSizeSpan(this.textSize.toInt(), false)
    builder.setSpan(textSpan, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    var indexStart = text.indexOf(matchingStr)
    while (indexStart != -1) {
        val indexEnd = indexStart + matchingStr.length
        val smallSpan = AbsoluteSizeSpan(context.resources.getDimensionPixelSize(size), false)
        builder.setSpan(smallSpan, indexStart, indexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        indexStart = text.indexOf(matchingStr, indexEnd)
    }
    this.text = builder
    return this
}

fun TextView.matchingColor(matchingStr: String?, @ColorInt matchingColor: Int): TextView {
    if (text.isEmpty() || matchingStr.isNullOrEmpty()) {
        return this
    }
    val builder = SpannableStringBuilder(text)
    var indexStart = text.indexOf(matchingStr)
    while (indexStart != -1) {
        val indexEnd = indexStart + matchingStr.length
        val colorSpan = ForegroundColorSpan(matchingColor)
        builder.setSpan(colorSpan, indexStart, indexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        indexStart = text.indexOf(matchingStr, indexEnd)
    }
    this.text = builder
    return this
}

fun TextView.matchingColorAndSize(text: String?, matchingStr: String?, @ColorInt matchingColor: Int, @DimenRes size: Int, reversed: Boolean = false) {
    if (text.isNullOrEmpty() || matchingStr.isNullOrEmpty()) {
        return
    }
    val builder = SpannableStringBuilder(text)

    if (reversed) {
        val colorSpan = ForegroundColorSpan(matchingColor)
        val textSpan = AbsoluteSizeSpan(context.resources.getDimensionPixelSize(size), false)
        builder.setSpan(colorSpan, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setSpan(textSpan, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    } else {
        val textSpan = AbsoluteSizeSpan(textSize.toInt(), false)
        builder.setSpan(textSpan, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    var indexStart = text.indexOf(matchingStr)
    while (indexStart != -1) {
        val indexEnd = indexStart + matchingStr.length

        val colorSpan = ForegroundColorSpan(if (reversed) textColors.defaultColor else matchingColor)
        val sizeSpan = AbsoluteSizeSpan(if (reversed) textSize.toInt() else context.resources.getDimensionPixelSize(size), false)
        builder.setSpan(colorSpan, indexStart, indexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setSpan(sizeSpan, indexStart, indexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        indexStart = text.indexOf(matchingStr, indexEnd)
    }
    this.text = builder
}




fun TextView.setClickText(@ColorInt matchingColor: Int, content: String?, vararg clickText: String, clickListener: (String) -> Unit) {
    this.text = ""
    if (content == null) {
        return
    }
    appendMatchingClickText(content, matchingColor, *clickText){
        clickListener.invoke(it)
    }
}

fun TextView.setSizeText(text: CharSequence?, matchingStr: String?, @DimenRes size: Int) {
    this.text = text
    if (text.isNullOrEmpty() || matchingStr.isNullOrEmpty()) return
    var indexStart = text.indexOf(matchingStr)
    val builder = SpannableStringBuilder(text)
    while (indexStart != -1) {
        val indexEnd = indexStart + matchingStr.length
        val sizeSpan = AbsoluteSizeSpan(context.resources.getDimensionPixelSize(size), false)
        builder.setSpan(sizeSpan, indexStart, indexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        indexStart = text.indexOf(matchingStr, indexEnd)
    }
    this.text = builder
}

fun TextView.setColorText(start: Int, end: Int, @ColorRes color: Int) {
    val builder = SpannableStringBuilder(this.text)
    val oneSpan = ForegroundColorSpan(ContextCompat.getColor(this.context, color))
    builder.setSpan(oneSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.text = builder
}

fun TextView.setColorText(str: CharSequence, start: Int, end: Int, @ColorInt color: Int) {
    val builder = SpannableStringBuilder(str.toString())
    val oneSpan = ForegroundColorSpan(color)
    builder.setSpan(oneSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.text = builder
}

fun TextView.setColorText(text: CharSequence?, matchingStr: String?, @ColorInt matchingColor: Int): TextView {
    if (text.isNullOrEmpty() || matchingStr.isNullOrEmpty()) {
        this.text = text
        return this
    }
    val builder = SpannableStringBuilder(text)
    var indexStart = text.indexOf(matchingStr)
    while (indexStart != -1) {
        val indexEnd = indexStart + matchingStr.length
        val colorSpan = ForegroundColorSpan(matchingColor)
        builder.setSpan(colorSpan, indexStart, indexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        indexStart = text.indexOf(matchingStr, indexEnd)
    }
    this.text = builder
    return this
}



fun TextView.appendSizeTextForResource(appendContent: CharSequence?, @DimenRes size: Int): TextView {
    return appendSizeText(appendContent, context.resources.getDimensionPixelSize(size))
}

fun TextView.appendSizeText(appendContent: CharSequence?, pxSize: Int): TextView {
    if (appendContent.isNullOrEmpty()) return this
    append(getSizeText(appendContent, pxSize))
    return this
}


fun TextView.appendColorText(appendContent: CharSequence?, color: Int): TextView {
    if (appendContent.isNullOrEmpty()) return this
    append(getColorText(appendContent, color))
    return this
}

fun TextView.appendClickText(@ColorInt matchingColor: Int, content: String, clickListener: () -> Unit): TextView {
    this.highlightColor = Color.TRANSPARENT
    this.movementMethod = ClickMovementMethod()
    val clickBuilder = SpannableStringBuilder(content)
    val click = object : ClickableSpan() {
        override fun onClick(widget: View) {
            clickListener()
        }
        override fun updateDrawState(ds: TextPaint) {
            ds.color = matchingColor
            ds.isUnderlineText = false
        }
    }
    clickBuilder.setSpan(click, 0, content.length-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    append(clickBuilder)
    return this
}

fun TextView.appendImageSpan(@DrawableRes drawable: Int, width: Int, height: Int): TextView {
    if (drawable == 0) return this
    ContextCompat.getDrawable(context, drawable)?.let {
        val builder = SpannableStringBuilder(" ")
        builder.setSpan(CenterImageSpan(it, width, height), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        append(builder)
    }
    return this
}

fun TextView.appendColorAndSize(appendContent: CharSequence?, @ColorInt color: Int, @DimenRes size: Int): TextView {
    if (appendContent.isNullOrEmpty()) return this
    append(getColorAndSizeForResource(appendContent, color, size))
    return this
}

fun TextView.appendMatchingClickText(appendText: CharSequence?, @ColorInt matchingColor: Int, vararg clickText: String, clickListener: (String) -> Unit){
    if(appendText == null){
        return
    }
    this.highlightColor = Color.TRANSPARENT
    this.movementMethod = ClickMovementMethod()
    val builder = SpannableStringBuilder(appendText)
    for (text in clickText) {
        var indexStart = appendText.indexOf(text)
        while (indexStart != -1) {
            val indexEnd = indexStart + text.length
            val click = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    clickListener.invoke(text)
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = matchingColor
                    ds.isUnderlineText = false
                }
            }
            builder.setSpan(click, indexStart, indexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            indexStart = appendText.indexOf(text, indexEnd)
        }
    }
    this.append(builder)
}


fun TextView.setMoneyStr(money: String?, @DimenRes priceSize: Int, reverse: Boolean = false, startStr: String = "¥", endStr: String = ".") {
    this.text = money
    if (money.isNullOrEmpty()) return
    var indexStart = text.indexOf(startStr)
    var indexEnd = text.indexOf(endStr)
    indexStart = if (indexStart == -1) 0 else indexStart + 1
    if (indexEnd == -1) indexEnd = money.length

    val builder = SpannableStringBuilder(text)
    val sizeSpan = AbsoluteSizeSpan(this.context.resources.getDimensionPixelSize(priceSize), false)
    val textSpan = AbsoluteSizeSpan(this.textSize.toInt(), false)
    if (reverse) {
        builder.setSpan(sizeSpan, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setSpan(textSpan, indexStart, indexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    } else {
        builder.setSpan(textSpan, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setSpan(sizeSpan, indexStart, indexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    this.text = builder
}

fun TextView.setDrawableLeft(@DrawableRes resId: Int){
    setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0)
}

fun TextView.setDrawableRight(@DrawableRes resId: Int){
    setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0)
}