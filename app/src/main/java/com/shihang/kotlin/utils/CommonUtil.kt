package com.shihang.kotlin.utils

import android.content.Context
import android.graphics.Paint
import android.text.*
import android.text.method.DigitsKeyListener
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.shihang.kotlin.R
import java.text.SimpleDateFormat
import java.util.*

object CommonUtil {


    fun getEmptyView(context: Context, hintMsg: String): View {
        val hintView = LayoutInflater.from(context).inflate(R.layout.layout_hint, null)
        val hintText = hintView.findViewById<View>(R.id.hintText) as TextView
        hintText.text = hintMsg
        return hintView
    }

    fun getColorText(content: String, colorStr: String, color: Int): SpannableStringBuilder {
        val text = content + colorStr
        val myStart = text.indexOf(colorStr)
        val builder = SpannableStringBuilder(text)
        val oneSpan = ForegroundColorSpan(color)
        builder.setSpan(oneSpan, myStart, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return builder
    }

    fun getColorText(colorStr1: String, colorStr2: String, color1: Int, color2: Int): SpannableStringBuilder {
        val text = colorStr1 + colorStr2
        val builder = SpannableStringBuilder(text)
        val oneSpan = ForegroundColorSpan(color1)
        val twoSpan = ForegroundColorSpan(color2)
        val myStart = text.indexOf(colorStr2)
        builder.setSpan(oneSpan, 0, colorStr1.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setSpan(twoSpan, myStart, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return builder
    }

    fun getColorText(text: String, start: Int, end: Int, color: Int): SpannableStringBuilder {
        val builder = SpannableStringBuilder(text)
        val span = ForegroundColorSpan(color)
        builder.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return builder
    }



    fun time(timestamp: Long): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date(timestamp))
    }

    fun time(timestamp: String): String? {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date(java.lang.Long.parseLong(timestamp)))
    }

    fun time(timestamp: Long, format: String): String {
        return SimpleDateFormat(format, Locale.CHINA).format(Date(timestamp))
    }



    fun textLineStrike(textView: TextView) {
        textView.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        textView.paint.isAntiAlias = true
    }

    fun textLineUnder(textView: TextView) {
        textView.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        textView.paint.isAntiAlias = true
    }



    fun editSetLastLength(edit: EditText) {
        edit.postInvalidate()
        val charSequence = edit.text
        if (charSequence is Spannable) {
            val spanText = charSequence as Spannable
            Selection.setSelection(spanText, charSequence.length)
        }
    }

    fun setMoneyEditStyle(edit: EditText) {
        edit.keyListener = DigitsKeyListener.getInstance("0123456789.")
        edit.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                var s = s
                if (s.toString().contains(".")) {
                    val flag = s.toString().substring(0, s.toString().lastIndexOf("."))
                    if (flag.contains(".")) {
                        s = s.toString().subSequence(0, s.toString().length - 1)
                        edit.setText(s)
                        edit.setSelection(s.length)
                    } else if (s.length - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3)
                        edit.setText(s)
                        edit.setSelection(s.length)
                    }
                }
                if (s.toString().trim { it <= ' ' }.substring(0) == ".") {
                    s = "0" + s
                    edit.setText(s)
                    edit.setSelection(2)
                }
                if (s.toString().startsWith("0") && s.toString().trim { it <= ' ' }.length > 1) {
                    if (s.toString().substring(1, 2) != ".") {
                        edit.setText(s.subSequence(0, 1))
                        edit.setSelection(1)
                        return
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        editSetLastLength(edit)
    }

}
